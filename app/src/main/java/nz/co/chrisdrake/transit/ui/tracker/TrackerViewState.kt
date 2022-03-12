package nz.co.chrisdrake.transit.ui.tracker

import nz.co.chrisdrake.transit.ui.map.MapViewState

data class TrackerViewState(val map: MapViewState, val error: String? = null)