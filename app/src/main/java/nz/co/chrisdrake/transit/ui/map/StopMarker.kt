package nz.co.chrisdrake.transit.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import nz.co.chrisdrake.transit.domain.common.Location

data class StopMarker(
    val title: String,
    private val location: Location,
    @DrawableRes private val iconResource: Int,
    val zIndex: Float,
) {
    val position = LatLng(location.latitude, location.longitude)

    fun icon(context: Context): BitmapDescriptor? {
        val vectorDrawable =
            ResourcesCompat.getDrawable(context.resources, iconResource, null) ?: return null

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}