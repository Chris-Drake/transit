package nz.co.chrisdrake.transit.domain.static_

import nz.co.chrisdrake.transit.domain.common.Location

interface Shape {
    val shapeId: ShapeId
    val location: Location
    val sequence: Int
}
