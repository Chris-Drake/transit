package nz.co.chrisdrake.transit.domain

import nz.co.chrisdrake.transit.domain.internal.*
import org.koin.dsl.module

val domainModule = module {

    factory { GetDeparture() }

    factory { GetJourneyUpdate(get(), get(), get(), get()) }

    factory { GetJourneyDepartures(get(), get(), get(), get()) }

    factory { GetJourneyPath(get()) }

    single { GetRoutesBetweenStops(get(), get()) }

    factory { GetTripUpdate(get(), get(), get()) }

    factory { FilterActiveTrips(get()) }
}