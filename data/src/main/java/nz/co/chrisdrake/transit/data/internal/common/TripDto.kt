package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.static_.*

@Entity(
    tableName = "trips",
    indices = [Index(value = ["route_id"])]
)
internal data class TripDto(
    @PrimaryKey @ColumnInfo(name = "id") @Json(name = "trip_id") override val tripId: TripId,
    @ColumnInfo(name = "route_id") @Json(name = "route_id") override val routeId: RouteId,
    @ColumnInfo(name = "trip_service_id") @Json(name = "service_id") override val serviceId: ServiceId,
    @ColumnInfo(name = "shape_id") @Json(name = "shape_id") override val shapeId: ShapeId,
    @ColumnInfo(name = "trip_headsign") @Json(name = "trip_headsign") override val tripHeadsign: String?,
) : Trip