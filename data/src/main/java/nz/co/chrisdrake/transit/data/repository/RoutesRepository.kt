package nz.co.chrisdrake.transit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.ApiService
import nz.co.chrisdrake.transit.data.internal.common.StopRouteDto
import nz.co.chrisdrake.transit.data.internal.database.TransitDao
import nz.co.chrisdrake.transit.domain.static_.Calendar
import nz.co.chrisdrake.transit.domain.static_.Route
import nz.co.chrisdrake.transit.domain.static_.StopId
import nz.co.chrisdrake.transit.domain.static_.Trip

class RoutesRepository internal constructor(
    private val apiService: ApiService,
    private val dao: TransitDao
) {

    suspend fun getRoutes(stopId: StopId): List<Route> = withContext(Dispatchers.IO) {
        dao.routesForStop(stopId).ifEmpty {
            val apiData = apiService.routes(stopId.value).data
                .map { StopRouteDto(stopId = stopId, routeId = it.id) }

            dao.insertStopRoutes(apiData)
            dao.routesForStop(stopId)
        }
    }

    suspend fun getRouteTrips(route: Route): List<Pair<Trip, Calendar>> =
        withContext(Dispatchers.IO) {
            dao.tripsForRoute(route.id).map { it.trip to it.calendar }
        }
}