package com.hubelias.parkingmeter.parkingmeterapp.domain.receipt

import org.junit.Test
import java.lang.IllegalArgumentException
import java.time.Duration


class DurationBasedFeeCalculationStrategySpec {
    @Test(expected = IllegalArgumentException::class)
    fun exceptionOnNegativeDuration() {
        val strategy = DurationBasedFeeCalculationStrategy().calculateParkingFee(Duration.ofHours(-1))
    }
}