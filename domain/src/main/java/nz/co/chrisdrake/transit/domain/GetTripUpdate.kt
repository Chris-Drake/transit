package nz.co.chrisdrake.transit.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.repository.RealtimeFeedsRepository
import nz.co.chrisdrake.transit.data.repository.ShapesRepository
import nz.co.chrisdrake.transit.data.repository.StopsRepository
import nz.co.chrisdrake.transit.domain.realtime.VehiclePosition
import nz.co.chrisdrake.transit.domain.static_.RouteId
import nz.co.chrisdrake.transit.domain.static_.TripId

class GetTripUpdate(
    private val realtimeFeedsRepository: RealtimeFeedsRepository,
    private val shapesRepository: ShapesRepository,
    private val stopsRepository: StopsRepository,
) {

    suspend operator fun invoke(
        tripId: TripId,
        routeId: RouteId,
        onPathFetched: (JourneyPath) -> Unit,
        onVehiclePositionFetched: (VehiclePosition?) -> Unit,
    ) = withContext(Dispatchers.IO) {
        val pathAsync = async { getPath(tripId, routeId) }
        val vehiclePositionAsync = async {
            realtimeFeedsRepository
                .getVehicleLocationsFeed(listOf(tripId))
                .firstOrNull()
        }

        onPathFetched(pathAsync.await())
        onVehiclePositionFetched(vehiclePositionAsync.await())
    }

    private suspend fun getPath(tripId: TripId, routeId: RouteId): JourneyPath {
        val points = shapesRepository.getShape(tripId).map { it.location }
        val stops = stopsRepository.getStops(routeId).map { JourneyStop(it, isTerminal = false) }

        return JourneyPath(stops = stops, shapeLocations = points)
    }
}