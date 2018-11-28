package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.db

import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.UserId
import com.hubelias.parkingmeter.parkingmeterapp.domain.receipt.PLN
import com.hubelias.parkingmeter.parkingmeterapp.domain.receipt.ParkingReceipt
import org.assertj.core.api.Assertions.assertThat
import org.joda.money.Money
import org.junit.Test
import java.time.LocalDateTime

class InMemoryParkingReceiptRepositoryTest {

    private val parkingReceiptRepository = InMemoryParkingReceiptRepository()

    @Test
    fun testFindByDriver() {
        // given
        val driverId = UserId("good.driver")
        val aReceipt = ParkingReceipt(driverId, LocalDateTime.now(), Money.of(PLN, 3.0))
        val anotherDriverId = UserId("better.driver")

        // then
        assertThat(parkingReceiptRepository.findByDriver(driverId)).isEmpty()
        assertThat(parkingReceiptRepository.findByDriver(anotherDriverId)).isEmpty()

        // when
        parkingReceiptRepository.add(aReceipt)

        // then
        assertThat(parkingReceiptRepository.findByDriver(driverId)).containsOnly(aReceipt)
        assertThat(parkingReceiptRepository.findByDriver(anotherDriverId)).isEmpty()
    }
}