package nz.co.chrisdrake.transit.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.Journey
import nz.co.chrisdrake.transit.data.Version
import nz.co.chrisdrake.transit.data.repository.VersionsRepository
import nz.co.chrisdrake.transit.domain.internal.*
import nz.co.chrisdrake.transit.domain.internal.GetJourneyPath
import nz.co.chrisdrake.transit.domain.internal.GetRoutesBetweenStops
import nz.co.chrisdrake.transit.domain.static_.StopCode
import nz.co.chrisdrake.transit.domain.static_.StopId
import java.time.ZonedDateTime

class GetJourneyUpdate internal constructor(
    private val versionsRepository: VersionsRepository,
    private val getRoutesBetweenStops: GetRoutesBetweenStops,
    private val getJourneyPath: GetJourneyPath,
    private val getJourneyDepartures: GetJourneyDepartures,
) {

    suspend operator fun invoke(
        journey: Journey,
        onPathFetched: (JourneyPath) -> Unit,
        onDeparturesFetched: (JourneyDepartures) -> Unit,
    ) = withContext(Dispatchers.IO) {
        val version = versionsRepository.currentVersion()

        val fromStopId = journey.fromStopCode.toStopId(version)
        val toStopId = journey.toStopCode.toStopId(version)

        val date = ZonedDateTime.now()

        val routes = getRoutesBetweenStops(fromStopId = fromStopId, toStopId = toStopId, date = date)

        val pathAsync = async { getJourneyPath(journey, routes) }
        val departuresAsync = async { getJourneyDepartures(date, fromStopId, routes) }

        onPathFetched(pathAsync.await())
        onDeparturesFetched(departuresAsync.await())
    }

    private fun StopCode.toStopId(version: Version) =
        StopId("${this.value.toString().padStart(4, '0')}-${version.value}")
}