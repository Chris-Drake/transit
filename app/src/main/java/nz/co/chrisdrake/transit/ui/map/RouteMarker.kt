package nz.co.chrisdrake.transit.ui.map

import com.google.android.gms.maps.model.LatLng
import nz.co.chrisdrake.transit.R
import nz.co.chrisdrake.transit.domain.JourneyPath
import nz.co.chrisdrake.transit.domain.common.Location

data class RouteMarker(
    val stopMarkers: List<StopMarker>,
    val points: List<LatLng>,
)

fun JourneyPath.toRouteMarker(): RouteMarker {
    val points = shapeLocations.map { it.toLatLng() }

    val stopMarkers = stops.mapIndexed { index, (stop, isTerminal) ->
        StopMarker(
            title = stop.name,
            location = stop.location,
            iconResource = R.drawable.ic_map_marker_stop,
            zIndex = .01f * index + if (isTerminal) 0.5f else 0f
        )
    }

    return RouteMarker(stopMarkers = stopMarkers, points = points)
}

private fun Location.toLatLng() = LatLng(latitude, longitude)