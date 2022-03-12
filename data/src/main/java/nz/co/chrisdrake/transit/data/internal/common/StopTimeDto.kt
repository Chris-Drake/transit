package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.*
import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.data.internal.DataTypeConverters
import nz.co.chrisdrake.transit.domain.static_.StopId
import nz.co.chrisdrake.transit.domain.static_.StopTime
import nz.co.chrisdrake.transit.domain.static_.TripId
import java.time.LocalTime

@Entity(
    tableName = "stop_times",
    indices = [Index(value = ["stop_id"])]
)
internal data class StopTimeDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "stop_id") @Json(name = "stop_id") override val stopId: StopId,
    @ColumnInfo(name = "trip_id") @Json(name = "trip_id") override val tripId: TripId,
    @ColumnInfo(name = "arrival_time") @Json(name = "arrival_time") val _arrivalTime: String,
    @ColumnInfo(name = "departure_time") @Json(name = "departure_time") val _departureTime: String
) : StopTime {
    @Ignore
    override val departureTime = _departureTime.toLocalTime()

    @Ignore
    override val arrivalTime = _arrivalTime.toLocalTime()

    private fun String.toLocalTime(): LocalTime {
        val hour = substringBefore(":").toInt()
        val correctHour = if (hour >= 24) hour - 24 else hour
        val value = replaceBefore(":", correctHour.toString().padStart(2, '0'))
        return DataTypeConverters.toLocalTime(value)!!
    }
}