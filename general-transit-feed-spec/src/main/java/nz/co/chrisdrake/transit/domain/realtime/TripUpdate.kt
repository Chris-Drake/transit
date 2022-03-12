package nz.co.chrisdrake.transit.domain.realtime

import kotlin.time.Duration

interface TripUpdate {
    val trip: TripDescriptor
    val stopTimeUpdate: StopTimeUpdate?
    val delay: Duration?
}