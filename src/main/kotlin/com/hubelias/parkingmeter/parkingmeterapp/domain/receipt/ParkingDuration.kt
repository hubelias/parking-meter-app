package com.hubelias.parkingmeter.parkingmeterapp.domain.receipt

import java.lang.IllegalArgumentException
import java.time.Duration
import java.time.LocalDateTime

data class ParkingDuration(
        val startedAt: LocalDateTime,
        val endedAt: LocalDateTime
) {
    init {
        if(endedAt < startedAt) {
            throw IllegalArgumentException("Parking duration cannot be negative!")
        }
    }

    val duration : Duration get() = Duration.between(startedAt, endedAt)
}