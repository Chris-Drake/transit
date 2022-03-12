package nz.co.chrisdrake.transit.ui.tracker

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nz.co.chrisdrake.transit.domain.GetTripUpdate
import nz.co.chrisdrake.transit.domain.JourneyPath
import nz.co.chrisdrake.transit.domain.common.Location
import nz.co.chrisdrake.transit.domain.realtime.VehiclePosition
import nz.co.chrisdrake.transit.domain.static_.RouteId
import nz.co.chrisdrake.transit.domain.static_.TripId
import nz.co.chrisdrake.transit.ui.map.*

class TrackerViewModel(
    args: TrackerActivityArgs,
    private val getTripUpdate: GetTripUpdate,
) : ViewModel(), DefaultLifecycleObserver {

    private val tripId: TripId = args.tripId
    private val routeId: RouteId = args.routeId
    private var pollUpdatesJob: Job? = null

    private val _viewState = MutableStateFlow(TrackerViewState(
        map = MapViewState(
            initialCameraPosition = CameraPosition.fromLatLngZoom(args.initialPosition, 11f)
        )
    ))

    val viewState: StateFlow<TrackerViewState> = _viewState

    override fun onStart(owner: LifecycleOwner) {
        pollUpdatesJob = viewModelScope.launch { pollTripUpdates() }
    }

    override fun onStop(owner: LifecycleOwner) {
        pollUpdatesJob?.cancel()
    }

    private suspend fun pollTripUpdates() {
        while (true) {
            try {
                getTripUpdate(tripId, routeId, ::displayPath, ::displayVehicle)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                Firebase.crashlytics.recordException(error)

                _viewState.update { it.copy(error = error.message ?: error.toString()) }
            }

            delay(5000)
        }
    }

    private fun displayPath(path: JourneyPath) {
        updateMapViewState { copy(routeMarker = path.toRouteMarker()) }
    }

    private fun displayVehicle(vehicle: VehiclePosition?) {
        val vehicleMarker = vehicle?.toMarker()

        updateMapViewState {
            copy(
                vehicleMarkers = listOfNotNull(vehicleMarker),
                animateCameraUpdate = vehicleMarker?.position?.let {
                    AnimateCameraUpdate(CameraUpdateFactory.newLatLngZoom(it, 14.3f), ::onCameraUpdate)
                }
            )
        }
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

    private fun Location.toLatLng() = LatLng(latitude, longitude)

    private fun VehiclePosition.toMarker(): VehicleMarker? {
        val vehiclePosition = position ?: return null
        val vehicleTrip = trip ?: return null

        return VehicleMarker(
            title = null,
            snippet = null,
            position = vehiclePosition.location.toLatLng(),
            rotation = vehiclePosition.bearing ?: 0f,
            tripDescriptor = vehicleTrip,
        )
    }
}