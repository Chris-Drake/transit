package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import nz.co.chrisdrake.transit.data.internal.BooleanInt
import nz.co.chrisdrake.transit.domain.static_.Calendar
import nz.co.chrisdrake.transit.domain.static_.ServiceId
import java.time.DayOfWeek

@Entity(
    tableName = "calendar",
)
internal data class CalendarDto(
    @PrimaryKey @ColumnInfo(name = "service_id") @Json(name = "service_id") override val serviceId: ServiceId,
    @ColumnInfo(name = "monday") @Json(name = "monday") @BooleanInt val monday: Boolean,
    @ColumnInfo(name = "tuesday") @Json(name = "tuesday") @BooleanInt val tuesday: Boolean,
    @ColumnInfo(name = "wednesday") @Json(name = "wednesday") @BooleanInt val wednesday: Boolean,
    @ColumnInfo(name = "thursday") @Json(name = "thursday") @BooleanInt val thursday: Boolean,
    @ColumnInfo(name = "friday") @Json(name = "friday") @BooleanInt val friday: Boolean,
    @ColumnInfo(name = "saturday") @Json(name = "saturday") @BooleanInt val saturday: Boolean,
    @ColumnInfo(name = "sunday") @Json(name = "sunday") @BooleanInt val sunday: Boolean,
) : Calendar {
    override fun isOperating(dayOfWeek: DayOfWeek) = when (dayOfWeek) {
        DayOfWeek.MONDAY -> monday
        DayOfWeek.TUESDAY -> tuesday
        DayOfWeek.WEDNESDAY -> wednesday
        DayOfWeek.THURSDAY -> thursday
        DayOfWeek.FRIDAY -> friday
        DayOfWeek.SATURDAY -> saturday
        DayOfWeek.SUNDAY -> sunday
    }
}