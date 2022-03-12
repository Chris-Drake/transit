package nz.co.chrisdrake.transit.ui.tracker

import android.app.PictureInPictureParams
import android.os.Bundle
import android.util.Rational
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.google.accompanist.insets.systemBarsPadding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import kotlinx.coroutines.launch
import nz.co.chrisdrake.transit.R
import nz.co.chrisdrake.transit.ui.common.MainTheme
import nz.co.chrisdrake.transit.ui.map.Map
import nz.co.chrisdrake.transit.ui.map.MapViewState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TrackerActivity : AppCompatActivity() {

    private val navArgs: TrackerActivityArgs by navArgs()
    private val viewModel by viewModel<TrackerViewModel> { parametersOf(navArgs) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MapScreen(viewModel.viewState.collectAsState().value.map)
        }

        lifecycle.addObserver(viewModel)

        lifecycleScope.launch {
            viewModel.viewState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { showError(it.error) }
        }

        enterPictureInPictureMode(
            PictureInPictureParams.Builder()
                .setAspectRatio(Rational(1, 1))
                .build()
        )
    }

    override fun onStop() {
        super.onStop()

        if (!isInPictureInPictureMode) finish()
    }

    private fun showError(error: String?) {
        error ?: return

        Snackbar.make(findViewById(android.R.id.content), error, LENGTH_LONG).show()

        viewModel.onErrorShown()
    }

    @Composable
    private fun MapScreen(mapViewState: MapViewState) {
        MainTheme {
            Map(
                modifier = Modifier.fillMaxSize().systemBarsPadding(false),
                viewState = mapViewState,
            )
        }
    }
}