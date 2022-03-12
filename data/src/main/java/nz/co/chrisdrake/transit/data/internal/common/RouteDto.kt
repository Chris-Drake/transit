package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.static_.Route
import nz.co.chrisdrake.transit.domain.static_.RouteId

@Entity(
    tableName = "routes",
    indices = [Index(value = ["short_name"])]
)
internal data class RouteDto(
    @PrimaryKey @ColumnInfo(name = "id") @Json(name = "route_id") override val id: RouteId,
    @ColumnInfo(name = "short_name") @Json(name = "route_short_name") override val shortName: String,
    @ColumnInfo(name = "long_name") @Json(name = "route_long_name") override val longName: String
) : Route