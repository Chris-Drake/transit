package nz.co.chrisdrake.transit.domain.realtime

import nz.co.chrisdrake.transit.domain.static_.RouteId
import nz.co.chrisdrake.transit.domain.static_.TripId
import java.time.LocalTime

interface TripDescriptor {
    val tripId: TripId
    val routeId: RouteId
    val startTime: LocalTime
    val scheduleRelationship: ScheduleRelationship?
}