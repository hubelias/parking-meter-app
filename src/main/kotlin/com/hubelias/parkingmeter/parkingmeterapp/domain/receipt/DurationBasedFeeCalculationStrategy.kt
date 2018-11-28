package com.hubelias.parkingmeter.parkingmeterapp.domain.receipt

import org.joda.money.Money
import java.lang.IllegalArgumentException
import java.time.Duration


class DurationBasedFeeCalculationStrategy(
        private val firstHourPrice : Money,
        private val secondHourPrice: Money,
        private val nextHoursMultiplier: Double
) {
    fun calculateParkingFee(parkingDuration: ParkingDuration) : Money {
        val duration = parkingDuration.duration

        val hoursAboveTwo = if(duration.toMinutes() % 60 == 0L) {
            duration.toHours() - 2
        } else {
            duration.toHours() - 1
        }

        return when {
            duration == Duration.ZERO -> Money.of(PLN, 0.0)
            duration <= Duration.ofHours(1) -> firstHourPrice
            duration <= Duration.ofHours(2) -> firstHourPrice + secondHourPrice
            else -> hourPriceAfterThirdHour(secondHourPrice to firstHourPrice + secondHourPrice, hoursAboveTwo).second
        }
    }

    private fun hourPriceAfterThirdHour(stuff: Pair<Money, Money>, remainingHours: Long) : Pair<Money, Money> {
        val (previousHourPrice, accumulatedPrice) = stuff
        return if(remainingHours == 0L) {
            previousHourPrice to accumulatedPrice
        } else {
            val currentHourPrice = previousHourPrice * nextHoursMultiplier
            val currentAccumulatedPrice = accumulatedPrice + currentHourPrice
            hourPriceAfterThirdHour(currentHourPrice to currentAccumulatedPrice, remainingHours - 1)
        }
    }
}
