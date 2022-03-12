package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model

import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.realtime.FeedEntity
import nz.co.chrisdrake.transit.domain.realtime.TripUpdate

internal data class FeedMessageDto(
    @Json(name = "entity") val entities: List<FeedEntityDto>
)

internal data class FeedMessageVehicleDto(
    @Json(name = "entity") val entities: List<FeedEntityVehicleDto>
)

internal data class FeedEntityDto(
    @Json(name = "id") val id: String,
    @Json(name = "vehicle") override val vehiclePosition: VehiclePositionDto?,
    @Json(name = "trip_update") override val tripUpdate: TripUpdateDto?,
) : FeedEntity

internal data class FeedEntityVehicleDto(
    @Json(name = "id") val id: String,
    @Json(name = "vehicle") override val vehiclePosition: VehiclePositionDto,
) : FeedEntity {
    override val tripUpdate: TripUpdate? = null
}