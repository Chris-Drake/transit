package nz.co.chrisdrake.transit.domain.realtime

import nz.co.chrisdrake.transit.domain.common.Location

interface Position {
    val location: Location
    val bearing: Float?
}