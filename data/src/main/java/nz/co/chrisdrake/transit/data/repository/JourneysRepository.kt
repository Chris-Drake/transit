package nz.co.chrisdrake.transit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.Journey
import nz.co.chrisdrake.transit.data.internal.database.TransitDao

class JourneysRepository internal constructor(
    private val dao: TransitDao,
) {

    fun getEnabledJourneys(): Flow<List<Journey>> {
        return getJourneys().map { journeys -> journeys.filter(Journey::enabled) }
    }

    fun getJourneys(): Flow<List<Journey>> = dao.userJourneys()

    suspend fun updateJourney(journey: Journey) = withContext(Dispatchers.IO) {
        dao.updateUserJourney(journey)
    }
}