package nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model

import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.realtime.ScheduleRelationship
import nz.co.chrisdrake.transit.domain.realtime.TripDescriptor
import nz.co.chrisdrake.transit.domain.static_.RouteId
import nz.co.chrisdrake.transit.domain.static_.TripId
import java.time.LocalTime

internal data class TripDescriptorDto(
    @Json(name = "trip_id") override val tripId: TripId,
    @Json(name = "route_id") override val routeId: RouteId,
    @Json(name = "start_time") override val startTime: LocalTime,
    @Json(name = "schedule_relationship") override val scheduleRelationship: ScheduleRelationship,
) : TripDescriptor
