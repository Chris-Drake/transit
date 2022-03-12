package nz.co.chrisdrake.transit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.ApiService
import nz.co.chrisdrake.transit.domain.static_.ShapeId
import nz.co.chrisdrake.transit.domain.static_.TripId
import nz.co.chrisdrake.transit.data.internal.database.TransitDao
import nz.co.chrisdrake.transit.domain.static_.Shape

class ShapesRepository internal constructor(
    private val apiService: ApiService,
    private val dao: TransitDao
) {

    suspend fun getShape(tripId: TripId) = withContext(Dispatchers.IO) {
        getShape(dao.shapeIdForTrip(tripId))
    }

    suspend fun getShape(shapeId: ShapeId): List<Shape> = withContext(Dispatchers.IO) {
        dao.shapes(shapeId).ifEmpty {
            apiService.shapes(shapeId.value).data.also { dao.insertShapes(it) }
        }
    }
}