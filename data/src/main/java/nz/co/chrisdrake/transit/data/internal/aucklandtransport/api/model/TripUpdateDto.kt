package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model

import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.realtime.TripUpdate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal data class TripUpdateDto(
    @Json(name = "trip") override val trip: TripDescriptorDto,
    @Json(name = "stop_time_update") override val stopTimeUpdate: StopTimeUpdateDto?,
    @Json(name = "delay") val delayInSeconds: Long,
) : TripUpdate {
    override val delay: Duration = delayInSeconds.seconds
}