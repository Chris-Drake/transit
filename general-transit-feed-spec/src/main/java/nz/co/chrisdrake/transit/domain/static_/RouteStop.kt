package nz.co.chrisdrake.transit.domain.static_

interface RouteStop {
    val routeId: RouteId?
    val stopId: StopId
    val stopCode: StopCode
    val stopSequence: Int
}