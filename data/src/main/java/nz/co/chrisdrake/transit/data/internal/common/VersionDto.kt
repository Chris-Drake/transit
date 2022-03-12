package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.data.Version
import java.time.ZonedDateTime

@Entity(tableName = "versions")
internal data class VersionDto(
    @PrimaryKey @ColumnInfo(name = "id") @Json(name = "version") val version: Version,
    @ColumnInfo(name = "start_date") @Json(name = "startdate") val startDate: ZonedDateTime,
    @ColumnInfo(name = "end_date") @Json(name = "enddate") val endDate: ZonedDateTime
) {
    fun contains(date: ZonedDateTime) =
        date.isAfter(startDate) && date.isBefore(endDate.withHour(23).withMinute(59))
}