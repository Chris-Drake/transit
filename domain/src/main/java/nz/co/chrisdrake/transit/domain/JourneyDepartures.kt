package nz.co.chrisdrake.transit.domain

import nz.co.chrisdrake.transit.domain.realtime.VehiclePosition
import nz.co.chrisdrake.transit.domain.static_.Route

data class JourneyDepartures(
    val departures: Map<Departure, VehiclePosition?>,
    val vehiclePositions: Map<VehiclePosition, Route>,
    val hasMultipleRoutes: Boolean,
)
