package nz.co.chrisdrake.transit.domain.realtime

interface FeedEntity {
    val tripUpdate: TripUpdate?
    val vehiclePosition: VehiclePosition?
}