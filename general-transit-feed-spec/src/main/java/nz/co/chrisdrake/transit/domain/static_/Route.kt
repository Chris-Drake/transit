package nz.co.chrisdrake.transit.domain.static_

interface Route {
    val id: RouteId
    val shortName: String
    val longName: String
}