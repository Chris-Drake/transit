package nz.co.chrisdrake.transit.domain.realtime

import java.time.ZonedDateTime
import kotlin.time.Duration

interface StopTimeEvent {
    val delay: Duration?
    val timestamp: ZonedDateTime?
    val uncertainty: Int?
}