package com.hubelias.parkingmeter.parkingmeterapp.domain.receipt

import com.hubelias.parkingmeter.parkingmeterapp.fixtures.PLN
import org.joda.money.Money
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.time.LocalDateTime
import kotlin.math.pow


class DurationBasedFeeCalculationStrategyTest {
    @Test
    fun freeIfDurationIsZero() {
        assertEquals(0.0.PLN(), createStrategy().calculateParkingFee(minutes(0)))
    }

    @Test
    fun useFirstHourPriceWhenParkingNoMoreThanHour() {
        val firstHourPrice = 1.30.PLN()
        val strategy = createStrategy(firstHourPrice)

        assertEquals(firstHourPrice, strategy.calculateParkingFee(minutes(10)))
        assertEquals(firstHourPrice, strategy.calculateParkingFee(minutes(30)))
        assertEquals(firstHourPrice, strategy.calculateParkingFee(minutes(60)))
    }

    @Test
    fun calculateFee_oneHourToTwoHours() {
        val firstHourPrice = 1.50.PLN()
        val secondHourPrice = 2.20.PLN()
        val expectedTotalPrice = firstHourPrice + secondHourPrice
        val strategy = createStrategy(firstHourPrice, secondHourPrice)

        assertEquals(expectedTotalPrice, strategy.calculateParkingFee(minutes(61)))
        assertEquals(expectedTotalPrice, strategy.calculateParkingFee(minutes(78)))
        assertEquals(expectedTotalPrice, strategy.calculateParkingFee(minutes(120)))
    }

    @Test
    fun calculateFee_twoHoursToThreeHours() {
        val firstHourPrice = 1.50.PLN()
        val secondHourPrice = 2.20.PLN()
        val nextHoursMultiplier = 1.5
        val expectedTotalPrice = (1.50 + 2.20 + 3.30).PLN()
        val strategy = createStrategy(firstHourPrice, secondHourPrice, nextHoursMultiplier)

        assertEquals(expectedTotalPrice, strategy.calculateParkingFee(minutes(121)))
        assertEquals(expectedTotalPrice, strategy.calculateParkingFee(minutes(150)))
        assertEquals(expectedTotalPrice, strategy.calculateParkingFee(minutes(180)))
    }

    @Test
    fun calculateFee_moreThanThreeHours() {
        val firstHourPrice = 1.50.PLN()
        val secondHourPrice = 2.20.PLN()
        val nextHoursMultiplier = 2.0
        val fourHoursPrice = (1.50 + 2.20 + 4.40 + 8.80).PLN()
        val fiveHoursPrice = (1.50 + 2.20 + 4.40 + 8.80 + 17.60).PLN()
        val strategy = createStrategy(firstHourPrice, secondHourPrice, nextHoursMultiplier)

        assertEquals(fourHoursPrice, strategy.calculateParkingFee(hoursAndMinutes(3, 30)))
        assertEquals(fourHoursPrice, strategy.calculateParkingFee(hoursAndMinutes(4, 0)))
        assertEquals(fiveHoursPrice, strategy.calculateParkingFee(hoursAndMinutes(4, 30)))
        assertEquals(fiveHoursPrice, strategy.calculateParkingFee(hoursAndMinutes(5, 0)))
    }

    @Test
    fun calculateFee_moreThanThreeHours_halfEvenRoundingAfterSecondHour() {
        val firstHourPrice = 0.0.PLN()
        val secondHourPrice = 1.00.PLN()

        createStrategy(firstHourPrice, secondHourPrice, 1.113).run {
            assertEquals(2.11.PLN(), calculateParkingFee(hoursAndMinutes(2, 30)))
        }

        createStrategy(firstHourPrice, secondHourPrice, 1.115).run {
            assertEquals(2.12.PLN(), calculateParkingFee(hoursAndMinutes(2, 30)))
        }

        createStrategy(firstHourPrice, secondHourPrice, 1.125).run {
            assertEquals(2.12.PLN(), calculateParkingFee(hoursAndMinutes(2, 30)))
        }

        createStrategy(firstHourPrice, secondHourPrice, 1.135).run {
            assertEquals(2.14.PLN(), calculateParkingFee(hoursAndMinutes(2, 30)))
        }
    }

    /**
     * We cannot simply use Math.pow on multiplier because we wouldn't use
     * previous hour's cost to calculate next hour's cost then.
     *
     * Therefore please hold your eager refactorings ;)
     */
    @Test
    fun calculateFee_previousHoursRoundedPlaceIsUsedForCalculations() {
        val secondHourPrice = 1.00.PLN()
        val nextHoursMultiplier = 1.13
        val thirdHourPrice = secondHourPrice * nextHoursMultiplier
        val forthHourPrice = thirdHourPrice * nextHoursMultiplier
        val fifthHourPrice = forthHourPrice * nextHoursMultiplier

        val strategy = createStrategy(0.0.PLN(), secondHourPrice, nextHoursMultiplier)

        assertNotEquals(secondHourPrice * nextHoursMultiplier.pow(3), fifthHourPrice)
        assertEquals(
                secondHourPrice + thirdHourPrice + forthHourPrice + fifthHourPrice,
                strategy.calculateParkingFee(hoursAndMinutes(5, 0))
        )
    }


    private fun createStrategy(
            firstHourPrice: Money = 3.33.PLN(),
            secondHourPrice: Money = 5.47.PLN(),
            nextHoursMultiplier: Double = 1.1
    ) = DurationBasedFeeCalculationStrategy(firstHourPrice, secondHourPrice, nextHoursMultiplier)

    private fun minutes(minutes: Long) = LocalDateTime.now().let {
        ParkingDuration(it, it.plusMinutes(minutes))
    }

    private fun hoursAndMinutes(hours: Long, minutes: Long) = minutes(hours * 60 + minutes)
}