package nz.co.chrisdrake.transit.domain.internal

import androidx.annotation.CheckResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.repository.StopsRepository
import nz.co.chrisdrake.transit.domain.static_.TripId
import java.time.ZonedDateTime

internal class FilterActiveTrips(
    private val stopsRepository: StopsRepository,
) {

    @CheckResult
    suspend operator fun invoke(
        routes: JourneyRoutes,
        date: ZonedDateTime
    ): List<TripId> = withContext(Dispatchers.IO) {
        val routeStopTimes = routes.terminalStops
            .associateWith { async { stopsRepository.getStopTimes(it.id) } }
            .mapValues { it.value.await() }

        return@withContext routes.trips
            .filter { (tripId, _, stops) ->
                val firstStopTimes = routeStopTimes.getValue(stops.first())
                val lastStopTimes = routeStopTimes.getValue(stops.last())

                val departureTime = firstStopTimes.first { it.tripId == tripId }.departureTime
                val arrivalTime = lastStopTimes.first { it.tripId == tripId }.arrivalTime

                date.toLocalTime().plusMinutes(5).isAfter(departureTime) &&
                        date.toLocalTime().minusMinutes(5).isBefore(arrivalTime)
            }
            .map(JourneyTrip::id)
    }
}