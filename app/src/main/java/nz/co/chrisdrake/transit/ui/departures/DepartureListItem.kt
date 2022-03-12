package nz.co.chrisdrake.transit.ui.departures

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import nz.co.chrisdrake.transit.domain.realtime.Position

data class DepartureListItem(
    val id: Any,
    val expectedDepartureTime: String,
    @StringRes val status: Int,
    val position: Position?,
    @DrawableRes val icon: Int,
    @DrawableRes val occupancyIcon: Int?,
    val onClick: (() -> Unit)? = null
)