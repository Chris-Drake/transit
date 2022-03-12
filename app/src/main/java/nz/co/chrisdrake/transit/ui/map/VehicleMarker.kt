package nz.co.chrisdrake.transit.ui.map

import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import nz.co.chrisdrake.transit.domain.realtime.TripDescriptor

data class VehicleMarker(
    val title: String?,
    @StringRes val snippet: Int?,
    val position: LatLng,
    val rotation: Float,
    val tripDescriptor: TripDescriptor,
)