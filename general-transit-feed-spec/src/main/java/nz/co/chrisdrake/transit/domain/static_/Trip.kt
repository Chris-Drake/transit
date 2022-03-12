package nz.co.chrisdrake.transit.domain.static_

interface Trip {
    val tripId: TripId
    val routeId: RouteId
    val serviceId: ServiceId
    val shapeId: ShapeId
    val tripHeadsign: String?
}
