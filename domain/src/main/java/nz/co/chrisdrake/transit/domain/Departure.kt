package nz.co.chrisdrake.transit.domain

import nz.co.chrisdrake.transit.domain.static_.TripId
import java.time.ZonedDateTime

data class Departure(
    val tripId: TripId,
    val routeShortName: String,
    val status: DepartureStatus,
    val scheduledDepartureTime: ZonedDateTime,
    val expectedDepartureTime: ZonedDateTime,
)

enum class DepartureStatus {
    SCHEDULED, DEPARTED_STOP, CANCELLED, COMPLETED_ROUTE
}