package nz.co.chrisdrake.transit.domain.static_

import nz.co.chrisdrake.transit.domain.common.Location

interface Stop {
    val id: StopId
    val name: String
    val code: StopCode
    val location: Location
}