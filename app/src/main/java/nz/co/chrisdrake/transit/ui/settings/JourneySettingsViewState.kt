package nz.co.chrisdrake.transit.ui.settings

data class JourneySettingsViewState(
    val outboundJourneys: List<JourneySetting> = emptyList(),
    val inboundJourneys: List<JourneySetting> = emptyList(),
)

data class JourneySetting(
    val title: String,
    val checked: Boolean,
    val onClick: () -> Unit,
)