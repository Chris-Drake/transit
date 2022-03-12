package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model

import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.common.Location
import nz.co.chrisdrake.transit.domain.realtime.Position

internal data class PositionDto(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "bearing") override val bearing: Float?
) : Position {
    override val location: Location = Location(latitude, longitude)
}