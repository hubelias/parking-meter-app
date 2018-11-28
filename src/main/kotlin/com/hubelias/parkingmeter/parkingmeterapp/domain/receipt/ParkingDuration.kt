package com.hubelias.parkingmeter.parkingmeterapp.domain.parking.receipt

import java.time.Duration
import java.time.LocalDateTime

data class ParkingDuration(
        val startedAt: LocalDateTime,
        val endedAt: LocalDateTime
) {
    val duration : Duration get() = Duration.between(startedAt, endedAt)
}