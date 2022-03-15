package nz.co.chrisdrake.transit.ui.map

import androidx.compose.animation.core.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import nz.co.chrisdrake.transit.R
import nz.co.chrisdrake.transit.domain.static_.TripId

class AnimateCameraUpdate(val update: CameraUpdate, val fulfill: () -> Unit)

@Composable
fun Map(
    modifier: Modifier,
    viewState: MapViewState,
    onInfoWindowClick: (VehicleMarker, LatLng) -> Unit = { _, _ -> },
) {
    val coroutineScope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        viewState.initialCameraPosition?.let { position = it }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            indoorLevelPickerEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false,
            zoomControlsEnabled = false,
            rotationGesturesEnabled = false,
        ),
        properties = MapProperties(isTrafficEnabled = true)
    ) {

        viewState.animateCameraUpdate?.let {
            coroutineScope.launch { cameraPositionState.animate(it.update) }
            it.fulfill()
        }

        viewState.routeMarker?.let { RouteMarker(it) }

        VehicleMarkers(viewState.routeMarker, viewState.vehicleMarkers, onInfoWindowClick)
    }
}

@Composable
private fun RouteMarker(route: RouteMarker) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(route) {
        alpha.snapTo(0f)
        alpha.animateTo(1f, tween(durationMillis = 600))
    }

    route.stopMarkers.forEach {
        key(it.position) {
            Marker(
                position = it.position,
                title = it.title,
                icon = it.icon(LocalContext.current),
                alpha = alpha.value,
                zIndex = it.zIndex
            )
        }
    }

    Polyline(
        points = route.points,
        color = MaterialTheme.colors.secondary.copy(alpha = alpha.value / 3),
    )
}

@Composable
private fun VehicleMarkers(
    route: RouteMarker?,
    vehicleMarkers: List<VehicleMarker>,
    onInfoWindowClick: (VehicleMarker, LatLng) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val vehiclePositionAnimatables = remember(route) {
        mutableMapOf<TripId, Animatable<LatLng, AnimationVector2D>>()
    }

    val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_vehicle)

    vehicleMarkers.forEachIndexed { index, marker ->
        val targetPosition = marker.position
        val tripId = marker.tripDescriptor.tripId

        val animatable = vehiclePositionAnimatables.getOrPut(tripId) {
            Animatable(targetPosition, latLngToVector)
        }

        val shouldAnimate = animatable.value != targetPosition
                && (!animatable.isRunning || animatable.targetValue != targetPosition)

        if (shouldAnimate) {
            coroutineScope.launch {
                animatable.animateTo(
                    targetValue = targetPosition,
                    animationSpec = tween(durationMillis = 600, easing = LinearEasing)
                )
            }
        }

        key(tripId) {

            Marker(
                position = animatable.value,
                rotation = marker.rotation,
                anchor = Offset(0.5f, 0.5f),
                infoWindowAnchor = Offset(0.5f, 0.5f),
                icon = icon,
                title = marker.title,
                snippet = marker.snippet?.let { stringResource(id = it) },
                zIndex = 1f + index,
                onInfoWindowClick = {
                    onInfoWindowClick(marker, it.position)
                    it.hideInfoWindow()
                }
            )
        }
    }
}

private val latLngToVector = TwoWayConverter<LatLng, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.latitude.toFloat(), it.longitude.toFloat()) },
    convertFromVector = { LatLng(it.v1.toDouble(), it.v2.toDouble()) }
)