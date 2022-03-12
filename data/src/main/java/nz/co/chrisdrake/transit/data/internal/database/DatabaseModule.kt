package nz.co.chrisdrake.transit.data.internal.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val databaseModule = module {

    single {
        Room.databaseBuilder(androidContext(), TransitDatabase::class.java, "transit.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<TransitDatabase>().dao()
    }
}