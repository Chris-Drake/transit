package nz.co.chrisdrake.transit.ui.map

import com.google.android.gms.maps.model.CameraPosition

data class MapViewState(
    val initialCameraPosition: CameraPosition? = null,
    val animateCameraUpdate: AnimateCameraUpdate? = null,
    val routeMarker: RouteMarker? = null,
    val vehicleMarkers: List<VehicleMarker> = emptyList(),
)
