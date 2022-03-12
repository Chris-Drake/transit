package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api

import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model.ApiResponse
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model.FeedMessageDto
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model.RouteStopsDto
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model.FeedMessageVehicleDto
import nz.co.chrisdrake.transit.data.internal.common.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ApiService {

    @GET("gtfs/versions")
    suspend fun versions(): ApiResponse<List<VersionDto>>

    @GET("gtfs/routes")
    suspend fun routes(): ApiResponse<List<RouteDto>>

    @GET("gtfs/routes/stopid/{stopId}")
    suspend fun routes(@Path("stopId") stopId: String): ApiResponse<List<RouteDto>>

    @GET("gtfs/stops")
    suspend fun stops(): ApiResponse<List<StopDto>>

    @GET("gtfs/stopTimes/stopId/{stopId}")
    suspend fun stopTimes(@Path("stopId") stopId: String): ApiResponse<List<StopTimeDto>>

    @GET("gtfs/btf/stops")
    suspend fun routeStops(@Query("route_ids") routeIds: String): ApiResponse<RouteStopsDto>

    @GET("gtfs/trips")
    suspend fun trips(): ApiResponse<List<TripDto>>

    @GET("gtfs/calendar")
    suspend fun calendar(): ApiResponse<List<CalendarDto>>

    @GET("gtfs/shapes/shapeId/{shapeId}")
    suspend fun shapes(@Path("shapeId") shapeId: String): ApiResponse<List<ShapeDto>>

    @GET("public/realtime")
    suspend fun combinedTransitFeed(
        @Query(value = "tripid", encoded = true) tripId: String
    ): ApiResponse<FeedMessageDto>

    @GET("public/realtime/vehiclelocations")
    suspend fun vehicleLocationsFeed(
        @Query(value = "tripid", encoded = true) tripId: String
    ): ApiResponse<FeedMessageVehicleDto>
}