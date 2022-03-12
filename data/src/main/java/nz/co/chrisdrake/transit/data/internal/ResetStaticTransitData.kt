package nz.co.chrisdrake.transit.data.internal

import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.ApiService
import nz.co.chrisdrake.transit.data.internal.database.TransitDao
import nz.co.chrisdrake.transit.data.internal.database.TransitDatabase

internal class ResetStaticTransitData(
    private val apiService: ApiService,
    private val database: TransitDatabase,
    private val dao: TransitDao,
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val routesAsync = async { apiService.routes().data }
        val stopsAsync = async { apiService.stops().data }
        val tripsAsync = async { apiService.trips().data }
        val calendarAsync = async { apiService.calendar().data }

        val routes = routesAsync.await()
        val stops = stopsAsync.await()
        val trips = tripsAsync.await()
        val calendar = calendarAsync.await()

        database.withTransaction {
            with(dao) {
                deleteRouteStops()
                deleteStopRoutes()
                deleteTrips()
                deleteRoutes()
                deleteStops()
                deleteStopTimes()
                deleteShapes()

                insertRoutes(routes)
                insertStops(stops)
                insertTrips(trips)
                insertServiceCalendars(calendar)
            }
        }
    }
}