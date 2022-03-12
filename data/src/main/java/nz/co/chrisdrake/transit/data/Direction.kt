package nz.co.chrisdrake.transit.data

import java.time.LocalDateTime

enum class Direction {
    OUTBOUND, INBOUND;

    companion object {
        fun default(): Direction = when {
            LocalDateTime.now().hour < 12 -> OUTBOUND
            else -> INBOUND
        }
    }
}