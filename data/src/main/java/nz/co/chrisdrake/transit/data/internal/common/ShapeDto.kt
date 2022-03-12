package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.*
import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.domain.common.Location
import nz.co.chrisdrake.transit.domain.static_.Shape
import nz.co.chrisdrake.transit.domain.static_.ShapeId

@Entity(
    tableName = "shapes",
    indices = [Index(value = ["shape_id"])]
)
internal data class ShapeDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "shape_id") @Json(name = "shape_id") override val shapeId: ShapeId,
    @ColumnInfo(name = "shape_pt_lat") @Json(name = "shape_pt_lat") val lat: Double,
    @ColumnInfo(name = "shape_pt_lon") @Json(name = "shape_pt_lon") val lng: Double,
    @ColumnInfo(name = "shape_pt_sequence") @Json(name = "shape_pt_sequence") override val sequence: Int
) : Shape {
    @Ignore
    override val location = Location(lat, lng)
}