package nz.co.chrisdrake.transit.ui

import nz.co.chrisdrake.transit.ui.home.HomeViewModel
import nz.co.chrisdrake.transit.ui.settings.JourneySettingsViewModel
import nz.co.chrisdrake.transit.ui.tracker.TrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    viewModel { HomeViewModel(get(), get()) }

    viewModel { parameters -> TrackerViewModel(args = parameters.get(), get()) }

    viewModel { JourneySettingsViewModel(get()) }
}