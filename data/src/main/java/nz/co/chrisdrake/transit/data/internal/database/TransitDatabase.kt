package nz.co.chrisdrake.transit.data.internal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nz.co.chrisdrake.transit.data.internal.common.CalendarDto
import nz.co.chrisdrake.transit.data.internal.common.*
import nz.co.chrisdrake.transit.data.internal.DataTypeConverters
import nz.co.chrisdrake.transit.data.Journey

@Database(
    entities = [
        VersionDto::class,
        TripDto::class,
        RouteDto::class,
        RouteStopDto::class,
        CalendarDto::class,
        StopDto::class,
        StopTimeDto::class,
        StopRouteDto::class,
        ShapeDto::class,
        Journey::class,
    ],
    version = 1
)
@TypeConverters(DataTypeConverters::class)
internal abstract class TransitDatabase : RoomDatabase() {

    abstract fun dao(): TransitDao
}