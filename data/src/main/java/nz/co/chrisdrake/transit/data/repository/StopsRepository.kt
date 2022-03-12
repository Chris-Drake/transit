package nz.co.chrisdrake.transit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.ApiService
import nz.co.chrisdrake.transit.data.internal.common.StopTimeDto
import nz.co.chrisdrake.transit.data.internal.database.TransitDao
import nz.co.chrisdrake.transit.domain.static_.RouteId
import nz.co.chrisdrake.transit.domain.static_.Stop
import nz.co.chrisdrake.transit.domain.static_.StopId
import nz.co.chrisdrake.transit.domain.static_.StopTime

class StopsRepository internal constructor(
    private val apiService: ApiService,
    private val dao: TransitDao
) {
    private val stopTimesCache = mutableMapOf<StopId, List<StopTimeDto>>()

    suspend fun getStops(routeId: RouteId): List<Stop> = withContext(Dispatchers.IO) {
        dao.stopsForRoute(routeId).ifEmpty {
            val apiData = apiService.routeStops(routeId.value).data

            dao.insertRouteStops(apiData.stops.map { it.copy(routeId = routeId) })
            dao.stopsForRoute(routeId)
        }
    }

    suspend fun getStopTimes(stopId: StopId): List<StopTime> = withContext(Dispatchers.IO) {
        stopTimesCache
            .getOrPut(stopId) { dao.stopTimes(stopId) }
            .ifEmpty {
                apiService.stopTimes(stopId.value).data.also {
                    dao.insertStopTimes(it)
                    stopTimesCache[stopId] = it
                }
            }
    }
}