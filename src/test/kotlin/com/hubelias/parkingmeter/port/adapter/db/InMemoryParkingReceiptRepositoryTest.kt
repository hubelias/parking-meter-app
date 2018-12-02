package com.hubelias.parkingmeter.port.adapter.db

import com.hubelias.parkingmeter.domain.driver.DriverId
import com.hubelias.parkingmeter.domain.receipt.PLN
import com.hubelias.parkingmeter.domain.receipt.ParkingReceipt
import com.hubelias.parkingmeter.utils.PLN
import com.hubelias.parkingmeter.utils.randomUsername
import org.assertj.core.api.Assertions.assertThat
import org.joda.money.Money
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar.NOVEMBER

class InMemoryParkingReceiptRepositoryTest {

    private val parkingReceiptRepository = InMemoryParkingReceiptRepository()

    @Test
    fun testFindByDriver() {
        // given
        val driverId = DriverId("good.driver")
        val aReceipt = ParkingReceipt(driverId, LocalDateTime.now(), 3.0.PLN())
        val anotherDriverId = DriverId("better.driver")

        // then
        assertThat(parkingReceiptRepository.findByDriver(driverId)).isEmpty()
        assertThat(parkingReceiptRepository.findByDriver(anotherDriverId)).isEmpty()

        // when
        parkingReceiptRepository.add(aReceipt)

        // then
        assertThat(parkingReceiptRepository.findByDriver(driverId)).containsOnly(aReceipt)
        assertThat(parkingReceiptRepository.findByDriver(anotherDriverId)).isEmpty()
    }

    @Test
    fun calculateDailyEarnings_noEarnings() {
        assertEquals(0.0.PLN(), parkingReceiptRepository.calculateDailyEarnings(LocalDate.now()))
    }

    @Test
    fun calculateDailyEarnings() {
        val dayOfInterest = LocalDate.of(2018, NOVEMBER, 30)
        addReceipt(dayOfInterest.atTime(7, 30), 2.30)
        addReceipt(dayOfInterest.atTime(9, 55), 1.50)
        addReceipt(dayOfInterest.atTime(20, 30), 6.50)
        assertEquals((2.30 + 1.50 + 6.50).PLN(), parkingReceiptRepository.calculateDailyEarnings(dayOfInterest))
    }

    @Test
    fun calculateDailyEarnings_excludeYesterdayAndTommorow() {
        val dayOfInterest = LocalDate.of(2018, NOVEMBER, 30)
        addReceipt(dayOfInterest.minusDays(1).atTime(7, 30), 2.30)
        addReceipt(dayOfInterest.atTime(9, 55), 1.50)
        addReceipt(dayOfInterest.plusDays(1).atTime(20, 30), 6.50)
        assertEquals(1.50.PLN(), parkingReceiptRepository.calculateDailyEarnings(dayOfInterest))
    }

    @Test
    fun calculateDailyEarnings_midnightIsNextDay() {
        val dayOfInterest = LocalDate.of(2018, NOVEMBER, 30)
        addReceipt(dayOfInterest.atStartOfDay(), 23.0)
        assertEquals(23.0.PLN(), parkingReceiptRepository.calculateDailyEarnings(dayOfInterest))
        assertEquals(0.0.PLN(), parkingReceiptRepository.calculateDailyEarnings(dayOfInterest.minusDays(1)))
    }

    private fun addReceipt(issuedAt: LocalDateTime, priceInPLN: Double) {
        parkingReceiptRepository.add(ParkingReceipt(DriverId(randomUsername()), issuedAt, Money.of(PLN, priceInPLN)))
    }
}