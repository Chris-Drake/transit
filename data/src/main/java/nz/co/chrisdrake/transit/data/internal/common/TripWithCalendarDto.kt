package nz.co.chrisdrake.transit.data.internal.common

import androidx.room.Embedded

internal data class TripWithCalendarDto(
    @Embedded val trip: TripDto,
    @Embedded val calendar: CalendarDto,
)