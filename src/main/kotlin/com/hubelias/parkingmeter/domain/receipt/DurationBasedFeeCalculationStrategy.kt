package com.hubelias.parkingmeter.domain.receipt

import org.joda.money.Money
import java.time.Duration


class DurationBasedFeeCalculationStrategy(
        private val firstHourPrice: Money,
        private val secondHourPrice: Money,
        private val nextHoursMultiplier: Double
) {
    fun calculateParkingFee(parkingDuration: ParkingDuration): Money {
        val hoursToPayFor = countHoursToPayFor(parkingDuration.duration)
        return calculateTotalPrice(hoursToPayFor)
    }

    private fun countHoursToPayFor(parkingDuration: Duration): Int {
        val fullHours = parkingDuration.toHours()
        val startedNextHour = parkingDuration.minusHours(fullHours) > Duration.ZERO
        return (if (startedNextHour) fullHours.inc() else fullHours).toInt()
    }

    private fun calculateTotalPrice(hoursToPayFor: Int): Money {
        var totalPrice = firstHourPrice
        var priceOfNextHour = secondHourPrice
        repeat(hoursToPayFor - 1) {
            totalPrice += priceOfNextHour
            priceOfNextHour *= nextHoursMultiplier
        }

        return totalPrice
    }
}
