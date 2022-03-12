package nz.co.chrisdrake.transit.domain

import nz.co.chrisdrake.transit.domain.common.Location
import nz.co.chrisdrake.transit.domain.static_.Stop

data class JourneyPath(
    val stops: List<JourneyStop>,
    val shapeLocations: List<Location>,
)

data class JourneyStop(val stop: Stop, val isTerminal: Boolean)
