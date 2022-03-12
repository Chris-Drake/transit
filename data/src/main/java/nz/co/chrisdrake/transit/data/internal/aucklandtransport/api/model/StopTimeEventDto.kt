package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model

import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.data.internal.UnixTimestamp
import nz.co.chrisdrake.transit.domain.realtime.StopTimeEvent
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal data class StopTimeEventDto(
    @Json(name = "delay") val delayInSeconds: Long?,
    @Json(name = "time") @UnixTimestamp override val timestamp: ZonedDateTime?,
    @Json(name = "uncertainty") override val uncertainty: Int?,
) : StopTimeEvent {

    override val delay: Duration? = delayInSeconds?.seconds

    init {
        check(delayInSeconds != null || timestamp != null)
    }
}