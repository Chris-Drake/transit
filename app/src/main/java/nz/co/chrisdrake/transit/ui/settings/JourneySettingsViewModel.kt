package nz.co.chrisdrake.transit.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nz.co.chrisdrake.transit.data.Direction
import nz.co.chrisdrake.transit.data.Journey
import nz.co.chrisdrake.transit.data.repository.JourneysRepository

class JourneySettingsViewModel(
    private val journeysRepository: JourneysRepository
) : ViewModel() {

    val viewState: StateFlow<JourneySettingsViewState> = journeysRepository.getJourneys()
        .map(::createViewState)
        .stateIn(viewModelScope, Eagerly, JourneySettingsViewState())

    private fun createViewState(journeys: List<Journey>): JourneySettingsViewState {
        val outboundJourneys = journeys
            .filter { it.direction == Direction.OUTBOUND }
            .map { it.toSetting() }
        val inboundJourneys = journeys
            .filter { it.direction == Direction.INBOUND }
            .map { it.toSetting() }

        return JourneySettingsViewState(
            outboundJourneys = outboundJourneys,
            inboundJourneys = inboundJourneys,
        )
    }

    private fun Journey.toSetting() = JourneySetting(
        title = title,
        checked = enabled,
        onClick = { toggleJourneyEnabledState(this) }
    )

    private fun toggleJourneyEnabledState(journey: Journey) {
        viewModelScope.launch {
            journeysRepository.updateJourney(journey.copy(enabled = !journey.enabled))
        }
    }
}