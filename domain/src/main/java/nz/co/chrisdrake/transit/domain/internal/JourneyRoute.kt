package nz.co.chrisdrake.transit.domain.internal

import nz.co.chrisdrake.transit.domain.static_.Route
import nz.co.chrisdrake.transit.domain.static_.ShapeId
import nz.co.chrisdrake.transit.domain.static_.Stop
import nz.co.chrisdrake.transit.domain.static_.TripId

internal data class JourneyRoute(
    private val route: Route,
    val stops: List<Stop>,
    val tripIds: List<TripId>,
    val shapeIds: List<ShapeId>,
) : Route by route