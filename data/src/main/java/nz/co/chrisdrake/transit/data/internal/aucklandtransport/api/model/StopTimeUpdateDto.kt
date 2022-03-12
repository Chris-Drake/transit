package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model

import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.realtime.StopTimeUpdate
import nz.co.chrisdrake.transit.domain.static_.StopId

internal data class StopTimeUpdateDto(
    @Json(name = "stop_sequence") override val stopSequence: Int?,
    @Json(name = "stop_id") override val stopId: StopId?,
    @Json(name = "arrival") override val arrival: StopTimeEventDto?,
    @Json(name = "departure") override val departure: StopTimeEventDto?,
) : StopTimeUpdate {
    init {
        check(stopId != null || stopSequence != null)
    }
}