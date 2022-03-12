package nz.co.chrisdrake.transit.domain.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.repository.RoutesRepository
import nz.co.chrisdrake.transit.data.repository.StopsRepository
import nz.co.chrisdrake.transit.domain.static_.Route
import nz.co.chrisdrake.transit.domain.static_.Stop
import nz.co.chrisdrake.transit.domain.static_.StopId
import nz.co.chrisdrake.transit.domain.static_.Trip
import java.time.DayOfWeek
import java.time.ZonedDateTime

internal class GetRoutesBetweenStops(
    private val routesRepository: RoutesRepository,
    private val stopsRepository: StopsRepository,
) {
    private val cache = mutableMapOf<CacheKey, JourneyRoutes>()

    suspend operator fun invoke(
        fromStopId: StopId,
        toStopId: StopId,
        date: ZonedDateTime
    ): JourneyRoutes = withContext(Dispatchers.IO) {
        val cacheKey = CacheKey(fromStopId, toStopId, checkNotNull(date.dayOfWeek))

        cache.getOrPut(cacheKey) {
            val routes = awaitAll(
                async { routesRepository.getRoutes(fromStopId) },
                async { routesRepository.getRoutes(toStopId) },
            )

            JourneyRoutes(
                routes = routes.filterCommon()
                    .associateWith { async { stopsRepository.getStops(it.id) } }
                    .mapValues { (_, value) -> value.await() }
                    .map { (route, stops) -> getOperatingRoute(route, stops, date.dayOfWeek) }
                    .filter { it.isValid(fromStopId, toStopId) }
            )
        }
    }

    private suspend fun getOperatingRoute(
        route: Route,
        stops: List<Stop>,
        dayOfWeek: DayOfWeek
    ): JourneyRoute {
        val trips = routesRepository.getRouteTrips(route)
            // TODO: Check public holiday calendar
            .filter { (trip, calendar) -> calendar.isOperating(dayOfWeek) && !trip.isSchoolBus }

        val tripIds = trips.map { (trip, _) -> trip.tripId }
        val shapeIds = trips.map { (trip, _) -> trip.shapeId }

        return JourneyRoute(route, stops, tripIds, shapeIds)
    }

    private fun JourneyRoute.isValid(fromStopId: StopId, toStopId: StopId) =
        tripIds.isNotEmpty() && hasStopsInSequence(fromStopId, toStopId)

    private fun JourneyRoute.hasStopsInSequence(fromStopId: StopId, toStopId: StopId) =
        stops.indexOfFirst { it.id == toStopId } > stops.indexOfFirst { it.id == fromStopId }

    private val Trip.isSchoolBus
        get() = tripHeadsign.equals("Schools", ignoreCase = true)

    private data class CacheKey(
        val fromStopId: StopId,
        val toStopId: StopId,
        val dayOfWeek: DayOfWeek
    )
}