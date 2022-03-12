package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.*
import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.static_.RouteId
import nz.co.chrisdrake.transit.domain.static_.RouteStop
import nz.co.chrisdrake.transit.domain.static_.StopCode
import nz.co.chrisdrake.transit.domain.static_.StopId

@Entity(
    tableName = "route_stops",
    foreignKeys = [
        ForeignKey(
            entity = RouteDto::class,
            parentColumns = ["id"],
            childColumns = ["route_id"]
        ),
        ForeignKey(
            entity = StopDto::class,
            parentColumns = ["id"],
            childColumns = ["stop_id"]
        )
    ],
    indices = [
        Index(value = ["route_id"]),
        Index(value = ["stop_id"]),
    ],
)
internal data class RouteStopDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "route_id") override val routeId: RouteId? = null,
    @ColumnInfo(name = "stop_id") @Json(name = "stop_id") override val stopId: StopId,
    @ColumnInfo(name = "stop_code") @Json(name = "stop_code") override val stopCode: StopCode,
    @ColumnInfo(name = "stop_sequence") @Json(name = "stop_sequence") override val stopSequence: Int
) : RouteStop