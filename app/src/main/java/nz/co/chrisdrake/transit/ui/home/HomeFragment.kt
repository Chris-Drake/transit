package nz.co.chrisdrake.transit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import nz.co.chrisdrake.transit.R
import nz.co.chrisdrake.transit.domain.realtime.TripDescriptor
import nz.co.chrisdrake.transit.ui.common.MainTheme
import nz.co.chrisdrake.transit.ui.departures.DeparturesView
import nz.co.chrisdrake.transit.ui.map.Map
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.home_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ComposeView>(R.id.departures_board_container).apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                val viewState = viewModel.viewState.collectAsState().value

                DeparturesView(
                    title = viewState.title,
                    progressVisible = viewState.progressVisible,
                    nextJourneyEnabled = viewState.nextJourneyEnabled,
                    departures = viewState.departures,
                    onClickNextJourney = viewState.onClickNextJourney
                )
            }
        }

        view.findViewById<ComposeView>(R.id.map_container).apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setNavigationBarColor(color = Color(0xB3FFFFFF))
                    systemUiController.setStatusBarColor(color = Color(0x43000000))
                }

                val state = viewModel.viewState.collectAsState().value

                MainTheme {
                    ProvideWindowInsets {
                        HomeScreen(
                            viewState = state,
                            onClickSettings = ::onClickSettings,
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.viewState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { showError(it.error) }
        }
    }

    private fun onInfoWindowClick(trip: TripDescriptor?, position: LatLng) {
        trip ?: return

        findNavController().navigate(
            HomeFragmentDirections.actionMapToTracker(
                tripId = trip.tripId,
                routeId = trip.routeId,
                initialPosition = position
            )
        )
    }

    private fun showError(error: String?) {
        error ?: return

        Snackbar
            .make(requireView(), error, Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.departures_board_container)
            .show()

        viewModel.onErrorShown()
    }

    private fun onClickSettings() {
        findNavController().navigate(HomeFragmentDirections.actionMapToSettings())
    }

    @Composable
    private fun HomeScreen(
        viewState: HomeViewState,
        onClickSettings: () -> Unit,
    ) {
        Box(
            modifier = Modifier
                .systemBarsPadding(start = false, top = false, end = false, bottom = true)
        ) {

            Map(
                modifier = Modifier.fillMaxSize(),
                viewState = viewState.map,
                onInfoWindowClick = { marker, position ->
                    onInfoWindowClick(marker.tripDescriptor, position)
                }
            )

            FloatingActions(
                onClickToggleDirection = {
                    if (viewState.toggleDirectionEnabled) viewState.onClickToggleDirection()
                },
                onClickSettings = onClickSettings,
            )
        }
    }

    @Composable
    private fun BoxScope.FloatingActions(
        onClickToggleDirection: () -> Unit,
        onClickSettings: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 16.dp, end = 16.dp)
        ) {
            FloatingActionButton(
                modifier = Modifier.size(40.dp),
                contentColor = Color.White,
                onClick = onClickToggleDirection
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_direction_toggle),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FloatingActionButton(
                modifier = Modifier.size(40.dp),
                backgroundColor = Color.White,
                onClick = onClickSettings,
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null
                )
            }
        }
    }

    @Preview
    @Composable
    private fun FloatingActionsPreview() {
        MainTheme {
            Box {
                FloatingActions({}, {})
            }
        }
    }
}