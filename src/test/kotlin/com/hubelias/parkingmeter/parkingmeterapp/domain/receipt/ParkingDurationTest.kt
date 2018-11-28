package com.hubelias.parkingmeter.parkingmeterapp.domain.receipt

import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

class ParkingDurationTest {
    @Test(expected = IllegalArgumentException::class)
    fun cannotBeNegative() {
        val startTime = LocalDateTime.now()
        ParkingDuration(startTime, startTime.minusMinutes(1))
    }

    @Test
    fun durationIsCorrect() {
        val startTime = LocalDateTime.now()
        val parkingDuration = ParkingDuration(startTime, startTime.plusMinutes(17))

        assertEquals(17, parkingDuration.duration.toMinutes())
    }
}