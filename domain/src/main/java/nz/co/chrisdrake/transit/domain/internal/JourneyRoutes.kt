package nz.co.chrisdrake.transit.domain.internal

import nz.co.chrisdrake.transit.domain.static_.*

internal class JourneyRoutes(private val routes: List<JourneyRoute>): List<JourneyRoute> by routes {

    val trips: List<JourneyTrip> = flatMap { route ->
        route.tripIds.map { JourneyTrip(it, route, route.stops) }
    }

    val routeShortNames: List<String> = map(JourneyRoute::shortName).distinct()

    val shapeIds: List<ShapeId> = map(JourneyRoute::shapeIds).flatten().distinct()

    val commonStops: List<Stop> = map(JourneyRoute::stops).filterCommon()

    val terminalStops: List<Stop> = flatMap { listOf(it.stops.first(), it.stops.last()) }.distinct()
}