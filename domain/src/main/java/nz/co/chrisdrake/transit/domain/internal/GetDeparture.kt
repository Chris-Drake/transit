package nz.co.chrisdrake.transit.domain.internal

import nz.co.chrisdrake.transit.domain.Departure
import nz.co.chrisdrake.transit.domain.DepartureStatus
import nz.co.chrisdrake.transit.domain.DepartureStatus.*
import nz.co.chrisdrake.transit.domain.realtime.ScheduleRelationship
import nz.co.chrisdrake.transit.domain.realtime.StopTimeEvent
import nz.co.chrisdrake.transit.domain.realtime.TripUpdate
import nz.co.chrisdrake.transit.domain.static_.Stop
import nz.co.chrisdrake.transit.domain.static_.StopId
import nz.co.chrisdrake.transit.domain.static_.StopTime
import java.time.ZonedDateTime
import kotlin.time.Duration

internal class GetDeparture {

    operator fun invoke(
        trip: JourneyTrip,
        tripUpdate: TripUpdate?,
        stopTime: StopTime,
    ): Departure {
        val (tripId, route, stops) = trip

        val stopId = stopTime.stopId

        val scheduledDepartureTime = ZonedDateTime.now().with(stopTime.departureTime)

        val status = tripUpdate?.getDepartureStatus(stops, stopId)

        val delay = tripUpdate?.stopTimeUpdate
            ?.takeIf { it.stopId == stopId }
            ?.departure?.delay
            ?: tripUpdate?.delay?.takeUnless { it.isPositive() && status == DEPARTED_STOP }
            ?: Duration.ZERO

        val expectedDepartureTime = scheduledDepartureTime.plusSeconds(delay.inWholeSeconds)

        return Departure(
            tripId = tripId,
            routeShortName = route.shortName,
            status = status ?: SCHEDULED,
            scheduledDepartureTime = scheduledDepartureTime,
            expectedDepartureTime = expectedDepartureTime,
        )
    }

    private fun TripUpdate.getDepartureStatus(
        stops: List<Stop>,
        stopId: StopId
    ): DepartureStatus {
        if (trip.scheduleRelationship == ScheduleRelationship.CANCELLED)
            return CANCELLED

        val date = ZonedDateTime.now()

        val stopSequence = stops.indexOfFirst { it.id == stopId } + 1
        val updateStopSequence = stopTimeUpdate?.stopSequence ?: Int.MIN_VALUE

        val completed = updateStopSequence == stops.size
                && stopTimeUpdate?.arrival.isBeforeWithCertainty(date)
        val departed = updateStopSequence > stopSequence || updateStopSequence == stopSequence
                && stopTimeUpdate?.departure.isBeforeWithCertainty(date)

        return when {
            completed -> COMPLETED_ROUTE
            departed -> DEPARTED_STOP
            else -> SCHEDULED
        }
    }

    private fun StopTimeEvent?.isBeforeWithCertainty(date: ZonedDateTime) =
        this?.timestamp?.isBefore(date)?.takeIf { uncertainty == 0 } ?: false
}