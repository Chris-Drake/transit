package nz.co.chrisdrake.transit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.ApiService
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.model.FeedEntityVehicleDto
import nz.co.chrisdrake.transit.domain.realtime.FeedEntity
import nz.co.chrisdrake.transit.domain.realtime.VehiclePosition
import nz.co.chrisdrake.transit.domain.static_.TripId

class RealtimeFeedsRepository internal constructor(
    private val apiService: ApiService
) {

    suspend fun getFeed(tripIds: List<TripId>): List<FeedEntity> {
        if (tripIds.isEmpty()) return emptyList()

        return withContext(Dispatchers.IO) {
            apiService.combinedTransitFeed(tripIds.joinToString(",") { it.value }).data.entities
        }
    }

    suspend fun getVehicleLocationsFeed(tripIds: List<TripId>): List<VehiclePosition> {
        return apiService.vehicleLocationsFeed(tripIds.joinToString(",") { it.value })
            .data.entities
            .map(FeedEntityVehicleDto::vehiclePosition)
    }
}