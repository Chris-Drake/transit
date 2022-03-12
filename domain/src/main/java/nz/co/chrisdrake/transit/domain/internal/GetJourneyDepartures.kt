package nz.co.chrisdrake.transit.domain.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.repository.RealtimeFeedsRepository
import nz.co.chrisdrake.transit.data.repository.StopsRepository
import nz.co.chrisdrake.transit.domain.Departure
import nz.co.chrisdrake.transit.domain.DepartureStatus.COMPLETED_ROUTE
import nz.co.chrisdrake.transit.domain.JourneyDepartures
import nz.co.chrisdrake.transit.domain.realtime.FeedEntity
import nz.co.chrisdrake.transit.domain.realtime.VehiclePosition
import nz.co.chrisdrake.transit.domain.static_.Route
import nz.co.chrisdrake.transit.domain.static_.StopId
import nz.co.chrisdrake.transit.domain.static_.StopTime
import java.time.ZonedDateTime

internal class GetJourneyDepartures(
    private val realtimeFeedsRepository: RealtimeFeedsRepository,
    private val stopsRepository: StopsRepository,
    private val filterActiveTrips: FilterActiveTrips,
    private val getDeparture: GetDeparture,
) {

    suspend operator fun invoke(
        date: ZonedDateTime,
        stopId: StopId,
        routes: JourneyRoutes,
    ): JourneyDepartures = withContext(Dispatchers.IO) {
        val activeTrips = filterActiveTrips(routes, date)

        val stopTimesAsync = async { stopsRepository.getStopTimes(stopId) }
        val tripUpdatesAsync = async { realtimeFeedsRepository.getFeed(activeTrips) }

        val tripUpdates = tripUpdatesAsync.await()
        val stopTimes = stopTimesAsync.await()

        val departures = getDepartures(routes, tripUpdates, stopTimes)

        val vehicles = tripUpdates.mapNotNull {
            it.vehiclePosition.takeIf { vehicle ->
                val tripId = vehicle?.trip?.tripId
                val departure = departures.firstOrNull { it.tripId == tripId }
                departure?.status != COMPLETED_ROUTE && activeTrips.contains(tripId)
            }
        }

        val now = ZonedDateTime.now()

        val pendingDepartures = departures
            .filter { now.minusMinutes(1).isBefore(it.expectedDepartureTime) }
            .sortedBy { it.expectedDepartureTime }
            .take(4)

        fun findVehicle(departure: Departure): VehiclePosition? =
            vehicles.firstOrNull { it.trip?.tripId == departure.tripId }

        fun findRoute(vehicle: VehiclePosition): Route =
            checkNotNull(routes.find { it.id == vehicle.trip?.routeId })

        JourneyDepartures(
            departures = pendingDepartures.associateWith(::findVehicle),
            vehiclePositions = vehicles.associateWith(::findRoute),
            hasMultipleRoutes = routes.routeShortNames.size > 1
        )
    }

    private fun getDepartures(
        routes: JourneyRoutes,
        tripUpdates: List<FeedEntity>,
        stopTimes: List<StopTime>,
    ): List<Departure> {
        return routes.trips.map { trip ->
            val tripId = trip.id

            val tripUpdate = tripUpdates
                .firstOrNull { it.tripUpdate?.trip?.tripId == tripId }
                ?.tripUpdate

            val stopTime = stopTimes.first { it.tripId == tripId }

            getDeparture(trip, tripUpdate, stopTime)
        }
    }
}