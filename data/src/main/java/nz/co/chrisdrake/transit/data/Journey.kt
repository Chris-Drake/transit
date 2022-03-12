package nz.co.chrisdrake.transit.data

import androidx.room.*
import nz.co.chrisdrake.transit.domain.common.Location
import nz.co.chrisdrake.transit.domain.static_.StopCode

@Entity(tableName = "user_journey")
data class Journey(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") internal val id: Long? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "from_stop_code") val fromStopCode: StopCode,
    @ColumnInfo(name = "to_stop_code") val toStopCode: StopCode,
    @ColumnInfo(name = "stop_lat") internal val stopLat: Double,
    @ColumnInfo(name = "stop_lng") internal val stopLng: Double,
    @ColumnInfo(name = "direction") val direction: Direction,
    @ColumnInfo(name = "enabled") val enabled: Boolean,
) {
    @Ignore
    val stopLocation = Location(stopLat, stopLng)
}