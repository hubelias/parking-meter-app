package com.hubelias.parkingmeter.parkingmeterapp.domain.receipt

import org.joda.money.Money
import java.lang.IllegalArgumentException
import java.time.Duration


class DurationBasedFeeCalculationStrategy(
        private val firstHourPrice : Money,
        private val secondHourPrice: Money
) {
    fun calculateParkingFee(parkingDuration: ParkingDuration) : Money {
        if(parkingDuration.duration == Duration.ZERO) {
            return Money.of(PLN, 0.0)
        }

        return firstHourPrice
    }
}
