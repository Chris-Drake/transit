package nz.co.chrisdrake.transit.domain.static_

import java.time.LocalTime

interface StopTime {
    val stopId: StopId
    val tripId: TripId
    val departureTime: LocalTime
    val arrivalTime: LocalTime
}