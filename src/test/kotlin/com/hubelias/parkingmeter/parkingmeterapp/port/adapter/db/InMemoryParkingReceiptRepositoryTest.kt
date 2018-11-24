package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.db

import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId
import com.hubelias.parkingmeter.parkingmeterapp.domain.common.PLN
import com.hubelias.parkingmeter.parkingmeterapp.domain.fees.ParkingReceipt
import org.assertj.core.api.Assertions.assertThat
import org.joda.money.Money
import org.junit.Test

class InMemoryParkingReceiptRepositoryTest {

    private val parkingReceiptRepository = InMemoryParkingReceiptRepository()

    @Test
    fun testFindByDriver() {
        // given
        val driverId = DriverId("good.driver")
        val aReceipt = ParkingReceipt(driverId, Money.of(PLN, 3.0))
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
}