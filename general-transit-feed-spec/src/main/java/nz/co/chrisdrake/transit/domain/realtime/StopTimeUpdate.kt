package nz.co.chrisdrake.transit.domain.realtime

import nz.co.chrisdrake.transit.domain.static_.StopId

interface StopTimeUpdate {
    val stopSequence: Int?
    val stopId: StopId?
    val arrival: StopTimeEvent?
    val departure: StopTimeEvent?
}