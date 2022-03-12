package nz.co.chrisdrake.transit.domain.static_

import java.time.DayOfWeek

interface Calendar {
    val serviceId: ServiceId

    fun isOperating(dayOfWeek: DayOfWeek): Boolean
}