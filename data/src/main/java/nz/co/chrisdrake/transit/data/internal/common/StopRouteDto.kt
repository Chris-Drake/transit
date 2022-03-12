package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.*
import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.static_.RouteId
import nz.co.chrisdrake.transit.domain.static_.StopId
import nz.co.chrisdrake.transit.domain.static_.StopRoute

@Entity(
    tableName = "stop_routes",
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
        Index(value = ["stop_id"]),
        Index(value = ["route_id"]),
    ]
)
internal data class StopRouteDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "stop_id") @Json(name = "stop_id") override val stopId: StopId,
    @ColumnInfo(name = "route_id") @Json(name = "route_id") override val routeId: RouteId,
) : StopRoute