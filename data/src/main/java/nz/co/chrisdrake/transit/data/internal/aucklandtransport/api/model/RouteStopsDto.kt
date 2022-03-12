package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model

import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.data.internal.common.RouteStopDto

internal data class RouteStopsDto(
    @Json(name = "isline") val isLine: Boolean,
    @Json(name = "stops") val stops: List<RouteStopDto>
)