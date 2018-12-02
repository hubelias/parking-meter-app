package com.hubelias.parkingmeter.domain.receipt

import java.time.Duration
import java.time.LocalDateTime


data class ParkingDuration(
        val startedAt: LocalDateTime,
        val endedAt: LocalDateTime
) {
    init {
        if (endedAt < startedAt) {
            throw IllegalArgumentException("Parking duration cannot be negative!")
        }
    }

    val duration: Duration = Duration.between(startedAt, endedAt)
}
