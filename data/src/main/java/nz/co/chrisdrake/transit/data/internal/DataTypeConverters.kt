package nz.co.chrisdrake.transit.data.internal

import androidx.room.TypeConverter
import com.squareup.moshi.*
import nz.co.chrisdrake.transit.data.Direction
import nz.co.chrisdrake.transit.data.Version
import nz.co.chrisdrake.transit.domain.realtime.OccupancyStatus
import nz.co.chrisdrake.transit.domain.realtime.ScheduleRelationship
import nz.co.chrisdrake.transit.domain.static_.*
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.ZoneId

@Suppress("unused")
internal object DataTypeConverters {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    private val moshi by lazy { Moshi.Builder().build() }

    @TypeConverter
    @FromJson
    @JvmStatic
    fun toZonedDateTime(value: String?) = value?.let { formatter.parse(value, ZonedDateTime::from) }

    @TypeConverter
    @ToJson
    @JvmStatic
    fun fromZonedDateTime(date: ZonedDateTime?): String? = date?.format(formatter)

    @FromJson
    @JvmStatic
    fun toLocalTime(value: String?) = value?.let {
        DateTimeFormatter.ISO_TIME.parse(value, LocalTime::from)
    }

    @ToJson
    @JvmStatic
    fun fromLocalTime(date: LocalTime?): String? = date?.format(DateTimeFormatter.ISO_TIME)

    @FromJson
    @JvmStatic
    fun toOccupancyStatus(value: Int): OccupancyStatus = OccupancyStatus.values()[value]

    @FromJson
    @JvmStatic
    fun toScheduleRelationship(value: Int?): ScheduleRelationship =
        ScheduleRelationship.values().getOrElse(value ?: Int.MAX_VALUE) {
            ScheduleRelationship.SCHEDULED
        }

    @FromJson
    @BooleanInt
    @JvmStatic
    fun toBoolean(value: Int): Boolean {
        check(value == 0 || value == 1)
        return value == 1
    }

    @ToJson
    @JvmStatic
    fun fromBoolean(@BooleanInt value: Boolean): Int = if (value) 1 else 0

    @FromJson
    @UnixTimestamp
    @JvmStatic
    fun toZonedDateTimeFromEpoch(epochSecond: Long): ZonedDateTime {
        return ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(epochSecond),
            ZoneId.systemDefault()
        )
    }

    @ToJson
    @JvmStatic
    fun fromZonedDateTimeToEpoch(@UnixTimestamp value: ZonedDateTime): Long = value.toEpochSecond()

    @TypeConverter
    @JvmStatic
    fun toDirection(value: Int) = Direction.values()[value]

    @TypeConverter
    @JvmStatic
    fun fromDirection(direction: Direction): Int = direction.ordinal

    @TypeConverter
    @JvmStatic
    fun toList(json: String?): List<String>? = json?.let {
        val adapter = moshi.adapter<List<String>>(
            Types.newParameterizedType(List::class.java, String::class.java)
        )

        adapter.fromJson(it)
    }

    @TypeConverter
    @JvmStatic
    fun fromList(value: List<String>?): String? = value?.let {
        val adapter = moshi.adapter<List<String>>(
            Types.newParameterizedType(List::class.java, String::class.java)
        )

        adapter.toJson(it)
    }

    @TypeConverter
    @FromJson
    @JvmStatic
    fun toVersion(value: String?): Version? = value?.let { Version(it) }

    @TypeConverter
    @ToJson
    @JvmStatic
    fun fromVersion(id: Version?): String? = id?.value

    @TypeConverter
    @FromJson
    @JvmStatic
    fun toShapeId(value: String?): ShapeId? = value?.let { ShapeId(it) }

    @TypeConverter
    @ToJson
    @JvmStatic
    fun fromShapeId(id: ShapeId?): String? = id?.value

    @TypeConverter
    @FromJson
    @JvmStatic
    fun toTripId(value: String?): TripId? = value?.let { TripId(it) }

    @TypeConverter
    @ToJson
    @JvmStatic
    fun fromTripId(id: TripId?): String? = id?.value

    @TypeConverter
    @FromJson
    @JvmStatic
    fun toServiceId(value: String?): ServiceId? = value?.let { ServiceId(it) }

    @TypeConverter
    @ToJson
    @JvmStatic
    fun fromServiceId(id: ServiceId?): String? = id?.value

    @TypeConverter
    @FromJson
    @JvmStatic
    fun toRouteId(value: String?): RouteId? = value?.let { RouteId(it) }

    @TypeConverter
    @ToJson
    @JvmStatic
    fun fromRouteId(id: RouteId?): String? = id?.value

    @TypeConverter
    @FromJson
    @JvmStatic
    fun toStopId(value: String?): StopId? = value?.let { StopId(it) }

    @TypeConverter
    @ToJson
    @JvmStatic
    fun fromStopId(id: StopId?): String? = id?.value

    @TypeConverter
    @FromJson
    @JvmStatic
    fun toStopCode(value: Long): StopCode = StopCode(value)

    @TypeConverter
    @ToJson
    @JvmStatic
    fun fromStopCode(code: StopCode): Long = code.value
}