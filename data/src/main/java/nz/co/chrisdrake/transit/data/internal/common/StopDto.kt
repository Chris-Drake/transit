package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.*
import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.common.Location
import nz.co.chrisdrake.transit.domain.static_.Stop
import nz.co.chrisdrake.transit.domain.static_.StopCode
import nz.co.chrisdrake.transit.domain.static_.StopId

@Entity(
    tableName = "stops",
    indices = [Index(value = ["code"], unique = true)],
)
internal data class StopDto(
    @PrimaryKey @ColumnInfo(name = "id") @Json(name = "stop_id") override val id: StopId,
    @ColumnInfo(name = "name") @Json(name = "stop_name") override val name: String,
    @ColumnInfo(name = "lat") @Json(name = "stop_lat") val lat: Double,
    @ColumnInfo(name = "lng") @Json(name = "stop_lon") val lng: Double,
    @ColumnInfo(name = "code") @Json(name = "stop_code") override val code: StopCode
) : Stop {
    @Ignore
    override val location = Location(lat, lng)
}