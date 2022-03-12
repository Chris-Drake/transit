package nz.co.chrisdrake.transit.domain.realtime

import java.time.ZonedDateTime

interface VehiclePosition {
    val trip: TripDescriptor?
    val position: Position?
    val occupancyStatus: OccupancyStatus?
    val timestamp: ZonedDateTime
}