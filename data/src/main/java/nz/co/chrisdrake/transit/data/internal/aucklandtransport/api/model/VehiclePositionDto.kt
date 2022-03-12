package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model

import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.data.internal.UnixTimestamp
import nz.co.chrisdrake.transit.domain.realtime.OccupancyStatus
import nz.co.chrisdrake.transit.domain.realtime.VehiclePosition
import java.time.ZonedDateTime

internal data class VehiclePositionDto(
    @Json(name = "trip") override val trip: TripDescriptorDto?,
    @Json(name = "position") override val position: PositionDto,
    @Json(name = "occupancy_status") override val occupancyStatus: OccupancyStatus?,
    @Json(name = "timestamp") @UnixTimestamp override val timestamp: ZonedDateTime
) : VehiclePosition