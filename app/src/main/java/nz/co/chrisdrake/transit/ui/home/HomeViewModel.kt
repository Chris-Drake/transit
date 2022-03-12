package nz.co.chrisdrake.transit.ui.home

import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nz.co.chrisdrake.transit.data.Direction
import nz.co.chrisdrake.transit.data.Journey
import nz.co.chrisdrake.transit.data.repository.JourneysRepository
import nz.co.chrisdrake.transit.domain.Departure
import nz.co.chrisdrake.transit.domain.DepartureStatus.*
import nz.co.chrisdrake.transit.domain.GetJourneyUpdate
import nz.co.chrisdrake.transit.domain.JourneyDepartures
import nz.co.chrisdrake.transit.domain.JourneyPath
import nz.co.chrisdrake.transit.domain.common.Location
import nz.co.chrisdrake.transit.domain.realtime.OccupancyStatus
import nz.co.chrisdrake.transit.domain.realtime.Position
import nz.co.chrisdrake.transit.domain.realtime.VehiclePosition
import nz.co.chrisdrake.transit.R
import nz.co.chrisdrake.transit.ui.departures.DepartureListItem
import nz.co.chrisdrake.transit.ui.map.AnimateCameraUpdate
import nz.co.chrisdrake.transit.ui.map.MapViewState
import nz.co.chrisdrake.transit.ui.map.VehicleMarker
import nz.co.chrisdrake.transit.ui.map.toRouteMarker
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeViewModel(
    private val journeysRepository: JourneysRepository,
    private val getJourneyUpdate: GetJourneyUpdate,
) : ViewModel(), DefaultLifecycleObserver {

    private val direction = MutableStateFlow(Direction.default())
    private val selectedJourney = MutableStateFlow<Journey?>(null)
    private var pollUpdatesJob: Job? = null

    private val _viewState = MutableStateFlow(
        HomeViewState(
            map = MapViewState(
                initialCameraPosition = CameraPosition.fromLatLngZoom(AUCKLAND_CBD, 11f),
            ),
            onClickToggleDirection = ::onClickToggleDirection
        )
    )

    val viewState: StateFlow<HomeViewState> = _viewState

    override fun onStart(owner: LifecycleOwner) {
        beginPollingJourneyUpdates()
    }

    override fun onStop(owner: LifecycleOwner) {
        pollUpdatesJob?.cancel()
    }

    private fun beginPollingJourneyUpdates() {
        pollUpdatesJob?.cancel()
        pollUpdatesJob = viewModelScope.launch {

            journeysRepository.getEnabledJourneys()
                .combine(direction) { journeys, direction ->
                    journeys.filter { it.direction == direction }
                }
                .onEmpty { TODO() }
                .combine(selectedJourney) { journeys, selectedJourney ->
                    journeys to (selectedJourney.takeIf(journeys::contains) ?: journeys.first())
                }
                .collectLatest { (journeys, selectedJourney) ->
                    onJourneySelection(selectedJourney, journeys)
                }
        }
    }

    private suspend fun onJourneySelection(journey: Journey, journeys: List<Journey>) {
        val index = journeys.indexOf(journey)
        val nextJourney = journeys.getOrElse(index + 1) { journeys.first() }
        val journeyId = journey.hashCode()

        _viewState.update {
            val mapState = if (it.journeyId == journeyId) {
                it.map.copy(vehicleMarkers = emptyList())
            } else {
                it.map.copy(
                    routeMarker = null,
                    vehicleMarkers = emptyList(),
                    animateCameraUpdate = AnimateCameraUpdate(
                        CameraUpdateFactory.newLatLngZoom(journey.stopLocation.toLatLng(), 14.8f),
                        ::onCameraUpdate
                    )
                )
            }

            it.copy(
                journeyId = journeyId,
                title = journey.title,
                nextJourneyEnabled = nextJourney != journey,
                toggleDirectionEnabled = true,
                departures = emptyList(),
                map = mapState,
                error = null,
                onClickNextJourney = { selectedJourney.update { nextJourney } },
                onClickToggleDirection = ::onClickToggleDirection
            )
        }

        pollVehicleLocations(journey)
    }

    private suspend fun pollVehicleLocations(journey: Journey) {
        while (true) {
            Firebase.crashlytics.setCustomKey("journey_routes", journey.toString())

            _viewState.update { it.copy(progressVisible = true) }

            try {
                getJourneyUpdate(journey, ::displayPath, ::displayDepartures)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                Firebase.crashlytics.recordException(error)

                _viewState.update { it.copy(error = error.message ?: error.toString()) }
            }

            _viewState.update { it.copy(progressVisible = false) }

            delay(6000)
        }
    }

    private fun displayDepartures(journeyDepartures: JourneyDepartures) {
        val departures = journeyDepartures.departures.map { (departure, vehicle) ->
            departure.toListItem(
                includeRouteName = journeyDepartures.hasMultipleRoutes,
                vehicle = vehicle
            )
        }

        val vehicleMarkers = journeyDepartures.vehiclePositions.mapNotNull { (vehicle, route) ->
            val vehiclePosition = vehicle.position ?: return@mapNotNull null
            val vehicleTrip = vehicle.trip ?: return@mapNotNull null

            VehicleMarker(
                title = route.shortName,
                snippet = vehicle.occupancyStatus?.label,
                position = vehiclePosition.location.toLatLng(),
                rotation = vehiclePosition.bearing ?: 0f,
                tripDescriptor = vehicleTrip,
            )
        }

        _viewState.update {
            it.copy(
                departures = departures,
                map = it.map.copy(vehicleMarkers = vehicleMarkers)
            )
        }
    }

    private fun onClickDepartureListener(position: Position): () -> Unit = {
        val cameraUpdate = AnimateCameraUpdate(
            CameraUpdateFactory.newLatLng(position.location.toLatLng()),
            ::onCameraUpdate
        )

        updateMapViewState { copy(animateCameraUpdate = cameraUpdate) }
    }

    private fun onClickToggleDirection() {
        direction.update {
            when (it) {
                Direction.OUTBOUND -> Direction.INBOUND
                Direction.INBOUND -> Direction.OUTBOUND
            }
        }
    }

    private fun displayPath(journeyPath: JourneyPath) {
        updateMapViewState { copy(routeMarker = journeyPath.toRouteMarker()) }
    }

    private fun onCameraUpdate() {
        updateMapViewState { copy(animateCameraUpdate = null) }
    }

    fun onErrorShown() {
        _viewState.update { it.copy(error = null) }
    }

    private fun updateMapViewState(update: MapViewState.() -> MapViewState) {
        _viewState.update { it.copy(map = update(it.map)) }
    }

    private fun Departure.toListItem(
        includeRouteName: Boolean,
        vehicle: VehiclePosition?
    ): DepartureListItem {
        val departureTime = expectedDepartureTime.withZoneSameInstant(ZoneId.systemDefault())

        val inTransit = vehicle != null

        val formattedDeparture: String = if (inTransit) {
            DateUtils.getRelativeTimeSpanString(departureTime.toEpochSecond() * 1000).toString()
        } else {
            departureTime.toLocalTime().format(DEPARTURE_TIME_FORMATTER)
        }

        val icon = when {
            status == CANCELLED -> R.drawable.ic_departure_alert
            inTransit -> R.drawable.ic_departure_in_transit
            else -> R.drawable.ic_departure
        }

        val status = when (status) {
            SCHEDULED -> R.string.home_departure_status_scheduled
            CANCELLED -> R.string.home_departure_status_cancelled
            DEPARTED_STOP, COMPLETED_ROUTE -> R.string.home_departure_status_departed
        }

        return DepartureListItem(
            id = tripId,
            expectedDepartureTime = if (includeRouteName) "$formattedDeparture ($routeShortName)" else formattedDeparture,
            status = status,
            position = vehicle?.position,
            icon = icon,
            occupancyIcon = vehicle?.occupancyStatus?.icon,
            onClick = vehicle?.position?.let(::onClickDepartureListener),
        )
    }

    @get:DrawableRes
    private val OccupancyStatus.icon: Int
        get() = when (this) {
            OccupancyStatus.EMPTY -> R.drawable.ic_occupancy_empty
            OccupancyStatus.MANY_SEATS_AVAILABLE -> R.drawable.ic_occupancy_many_available
            OccupancyStatus.FEW_SEATS_AVAILABLE -> R.drawable.ic_occupancy_few
            OccupancyStatus.FULL,
            OccupancyStatus.STANDING_ROOM_ONLY,
            OccupancyStatus.CRUSHED_STANDING_ROOM_ONLY,
            OccupancyStatus.NOT_ACCEPTING_PASSENGERS -> R.drawable.ic_occupancy_full
        }

    @get:StringRes
    private val OccupancyStatus.label:  Int
        get() = when (this) {
            OccupancyStatus.EMPTY -> R.string.home_occupancy_empty
            OccupancyStatus.MANY_SEATS_AVAILABLE -> R.string.home_occupancy_many_available
            OccupancyStatus.FEW_SEATS_AVAILABLE -> R.string.home_occupancy_few_available
            OccupancyStatus.STANDING_ROOM_ONLY,
            OccupancyStatus.CRUSHED_STANDING_ROOM_ONLY -> R.string.home_occupancy_standing_room
            OccupancyStatus.FULL,
            OccupancyStatus.NOT_ACCEPTING_PASSENGERS -> R.string.home_occupancy_full
        }

    private fun Location.toLatLng() = LatLng(latitude, longitude)

    companion object {
        private val AUCKLAND_CBD = LatLng(-36.8597287,174.5660386)
        private val DEPARTURE_TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a")
    }
}