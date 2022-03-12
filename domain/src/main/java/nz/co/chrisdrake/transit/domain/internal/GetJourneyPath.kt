package nz.co.chrisdrake.transit.domain.internal

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import nz.co.chrisdrake.transit.data.Journey
import nz.co.chrisdrake.transit.data.repository.ShapesRepository
import nz.co.chrisdrake.transit.domain.JourneyPath
import nz.co.chrisdrake.transit.domain.JourneyStop
import nz.co.chrisdrake.transit.domain.static_.Shape
import nz.co.chrisdrake.transit.domain.static_.ShapeId
import nz.co.chrisdrake.transit.domain.static_.Stop

internal class GetJourneyPath(
    private val shapesRepository: ShapesRepository
) {

    suspend operator fun invoke(journey: Journey, routes: JourneyRoutes): JourneyPath {
        val routePoints = getCommonRoute(routes.shapeIds).map(Shape::location)

        val stopMarkers = routes.commonStops.map {
            JourneyStop(stop = it, isTerminal = journey.isTerminalStop(it))
        }

        return JourneyPath(stopMarkers, routePoints)
    }

    private suspend fun getCommonRoute(shapeIds: List<ShapeId>) = coroutineScope {
        shapeIds
            .map { shapeId -> async { shapesRepository.getShape(shapeId) } }
            .awaitAll()
            .filterCommon()
    }

    private fun Journey.isTerminalStop(stop: Stop) =
        stop.code == fromStopCode || stop.code == toStopCode
}