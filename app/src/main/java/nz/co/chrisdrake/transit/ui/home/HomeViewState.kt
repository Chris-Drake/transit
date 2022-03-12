package nz.co.chrisdrake.transit.ui.home

import nz.co.chrisdrake.transit.ui.departures.DepartureListItem
import nz.co.chrisdrake.transit.ui.map.MapViewState

data class HomeViewState(
    val journeyId: Any? = null,
    val title: String = "",
    val progressVisible: Boolean = true,
    val nextJourneyEnabled: Boolean = false,
    val toggleDirectionEnabled: Boolean = false,
    val map: MapViewState,
    val departures: List<DepartureListItem> = emptyList(),
    val error: String? = null,
    val onClickNextJourney: () -> Unit = {},
    val onClickToggleDirection: () -> Unit,
)