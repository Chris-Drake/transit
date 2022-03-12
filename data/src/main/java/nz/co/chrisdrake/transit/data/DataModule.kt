package nz.co.chrisdrake.transit.data

import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.apiModule
import nz.co.chrisdrake.transit.data.internal.database.databaseModule
import nz.co.chrisdrake.transit.data.internal.ResetStaticTransitData
import nz.co.chrisdrake.transit.data.repository.JourneysRepository
import nz.co.chrisdrake.transit.data.repository.*
import org.koin.dsl.module

val dataModules = databaseModule + apiModule + module {

    factory { JourneysRepository(get()) }

    single { VersionsRepository(get(), get(), get()) }

    factory { RealtimeFeedsRepository(get()) }

    factory { ResetStaticTransitData(get(), get(), get()) }

    factory { RoutesRepository(get(), get()) }

    factory { ShapesRepository(get(), get()) }

    single { StopsRepository(get(), get()) }
}