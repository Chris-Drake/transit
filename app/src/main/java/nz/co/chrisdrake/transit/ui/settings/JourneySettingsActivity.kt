package nz.co.chrisdrake.transit.ui.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nz.co.chrisdrake.transit.R
import nz.co.chrisdrake.transit.ui.common.MainTheme
import org.koin.android.ext.android.inject

class JourneySettingsActivity : AppCompatActivity() {

    private val viewModel: JourneySettingsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainTheme {
                val systemUiController = rememberSystemUiController()
                val statusBarColor = MaterialTheme.colors.primaryVariant

                SideEffect {
                    systemUiController.setStatusBarColor(color = statusBarColor)
                }

                val state = viewModel.viewState.collectAsState().value
                JourneySettingsScreen(state)
            }
        }
    }

    @Composable
    private fun JourneySettingsScreen(viewState: JourneySettingsViewState) {
        Scaffold(
            topBar = { AppBar() }
        ) {
            JourneyList(viewState)
        }
    }

    @Composable
    private fun AppBar() {
        TopAppBar(
            title = { Text(stringResource(R.string.journey_settings_title)) },
            navigationIcon = {
                IconButton(onClick = ::finish) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.journey_settings_back),
                    )
                }
            }
        )
    }

    @Composable
    private fun JourneyList(viewState: JourneySettingsViewState) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
        ) {
            item { JourneyDirectionHeader(stringResource(R.string.journey_settings_journey_outbound)) }

            items(viewState.outboundJourneys) { journey -> JourneyRow(journey) }

            item { JourneyDirectionHeader(stringResource(R.string.journey_settings_journey_inbound)) }

            items(viewState.inboundJourneys) { journey -> JourneyRow(journey) }
        }
    }

    @Composable
    private fun JourneyDirectionHeader(text: String) {
        Column(
            modifier = Modifier.defaultMinSize(minHeight = 48.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.overline
            )
        }
    }

    @Composable
    private fun JourneyRow(journey: JourneySetting) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .clickable { journey.onClick() },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = journey.checked, onCheckedChange = null)

            Spacer(modifier = Modifier.width(32.dp))

            Text(journey.title, style = MaterialTheme.typography.body1)
        }
    }

    @Preview
    @Composable
    private fun JourneySettingsScreenPreview() {
        val journey = JourneySetting(title = "NX1", checked = true, onClick = {})

        MainTheme {
            JourneySettingsScreen(
                viewState = JourneySettingsViewState(
                    outboundJourneys = listOf(journey),
                    inboundJourneys = listOf(journey),
                )
            )
        }
    }
}