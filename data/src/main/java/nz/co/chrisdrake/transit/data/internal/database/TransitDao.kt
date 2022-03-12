package nz.co.chrisdrake.transit.data.internal.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nz.co.chrisdrake.transit.data.internal.common.CalendarDto
import nz.co.chrisdrake.transit.data.internal.common.*
import nz.co.chrisdrake.transit.data.Version
import nz.co.chrisdrake.transit.data.Journey
import nz.co.chrisdrake.transit.domain.static_.*

@Dao
internal interface TransitDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertVersions(versions: List<VersionDto>)

    @Insert(onConflict = REPLACE)
    suspend fun insertTrips(trips: List<TripDto>)

    @Insert(onConflict = REPLACE)
    suspend fun insertRoutes(routes: List<RouteDto>)

    @Insert(onConflict = REPLACE)
    suspend fun insertRouteStops(routeStops: List<RouteStopDto>)

    @Insert(onConflict = REPLACE)
    suspend fun insertServiceCalendars(calendars: List<CalendarDto>)

    @Insert(onConflict = REPLACE)
    suspend fun insertStops(routes: List<StopDto>)

    @Insert(onConflict = REPLACE)
    suspend fun insertStopTimes(stopTimes: List<StopTimeDto>)

    @Insert(onConflict = REPLACE)
    suspend fun insertStopRoutes(stopRoutes: List<StopRouteDto>)

    @Insert(onConflict = REPLACE)
    suspend fun insertShapes(shapes: List<ShapeDto>)

    @Insert
    suspend fun insertUserJourney(journey: Journey)

    @Insert
    suspend fun insertUserJourneys(journeys: List<Journey>)

    @Update
    suspend fun updateUserJourney(journey: Journey)

    @Query("SELECT * FROM versions ORDER BY id DESC")
    suspend fun versions(): List<VersionDto>

    @Query("SELECT stops.* FROM route_stops INNER JOIN stops ON stop_id = stops.id WHERE route_id = :routeId ORDER BY stop_sequence ASC")
    suspend fun stopsForRoute(routeId: RouteId): List<StopDto>

    @Query("SELECT routes.* FROM stop_routes INNER JOIN routes ON route_id = routes.id WHERE stop_id = :stopId")
    suspend fun routesForStop(stopId: StopId): List<RouteDto>

    @Query("SELECT * FROM stop_times WHERE stop_id = :stopId")
    suspend fun stopTimes(stopId: StopId): List<StopTimeDto>

    @Query("SELECT trips.*, calendar.* FROM trips INNER JOIN calendar ON trips.trip_service_id = calendar.service_id WHERE route_id = :routeId")
    suspend fun tripsForRoute(routeId: RouteId): List<TripWithCalendarDto>

    @Query("SELECT shape_id FROM trips WHERE id = :tripId")
    suspend fun shapeIdForTrip(tripId: TripId): ShapeId

    @Query("SELECT * FROM shapes WHERE shape_id = :shapeId ORDER BY shape_pt_sequence ASC")
    suspend fun shapes(shapeId: ShapeId): List<ShapeDto>

    @Query("SELECT (EXISTS(SELECT * FROM routes WHERE id LIKE '%' || :version) AND EXISTS(SELECT * FROM stops WHERE id LIKE '%' || :version) AND EXISTS(SELECT * FROM trips WHERE id LIKE '%' || :version) AND EXISTS(SELECT * FROM calendar WHERE service_id LIKE '%' || :version))")
    suspend fun populated(version: Version): Boolean

    @Query("DELETE FROM trips")
    suspend fun deleteTrips()

    @Query("DELETE FROM routes")
    suspend fun deleteRoutes()

    @Query("DELETE FROM route_stops")
    suspend fun deleteRouteStops()

    @Query("DELETE FROM stops")
    suspend fun deleteStops()

    @Query("DELETE FROM stop_times")
    suspend fun deleteStopTimes()

    @Query("DELETE FROM stop_routes")
    suspend fun deleteStopRoutes()

    @Query("DELETE FROM shapes")
    suspend fun deleteShapes()

    @Query("SELECT * FROM user_journey ORDER BY id ASC")
    fun userJourneys(): Flow<List<Journey>>
}