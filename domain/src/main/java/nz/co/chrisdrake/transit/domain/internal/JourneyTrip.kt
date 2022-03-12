package nz.co.chrisdrake.transit.domain.internal

import nz.co.chrisdrake.transit.domain.static_.Route
import nz.co.chrisdrake.transit.domain.static_.Stop
import nz.co.chrisdrake.transit.domain.static_.TripId

internal data class JourneyTrip(
    val id: TripId,
    val route: Route,
    val stops: List<Stop>,
)