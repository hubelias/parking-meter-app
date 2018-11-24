package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.application.fees.MoneyDto
import com.hubelias.parkingmeter.parkingmeterapp.application.fees.ParkingFeesApplicationService
import com.hubelias.parkingmeter.parkingmeterapp.application.fees.ParkingReceiptDto
import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId
import com.hubelias.parkingmeter.parkingmeterapp.domain.fees.ParkingReceiptRepository
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.ParkingEnded
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.Duration
import java.time.LocalDateTime

/**
 * no receipts at the beginning
 * parking ended create receipt - disabled, regular, <1h, 2h, 3h etc
 * daily earnings calculated by repository - so that's an easy shot
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ParkingFeesApplicationServiceSpec {
    @Autowired
    private lateinit var parkingReceiptRepository: ParkingReceiptRepository
    @Autowired
    private lateinit var parkingFeesApplicationService: ParkingFeesApplicationService

    @Before
    fun setUp() {
        parkingReceiptRepository.removeAll()
    }

    @Test
    fun noReceiptsAtTheBeginning() {
        // given
        val driverId = "good.driver"

        // when
        assertThat(parkingFeesApplicationService.getDriverReceipts(driverId)).isEmpty()
    }

    @Test
    fun whenParkingEndedCreateNewReceipt() {
        // given
        val driverId = "good.driver"
        val anotherDriverId = "bad.driver"

        // when
        parkingFeesApplicationService.onParkingEnded(ParkingEnded(DriverId(driverId), Duration.ofHours(2), LocalDateTime.now()))

        // then
        assertThat(parkingFeesApplicationService.getDriverReceipts(driverId)).hasSize(1)
        assertThat(parkingFeesApplicationService.getDriverReceipts(anotherDriverId)).hasSize(0)

        // when
        parkingFeesApplicationService.onParkingEnded(ParkingEnded(DriverId(driverId), Duration.ofMinutes(50), LocalDateTime.now()))
        parkingFeesApplicationService.onParkingEnded(ParkingEnded(DriverId(anotherDriverId), Duration.ofMinutes(27), LocalDateTime.now()))

        // then
        assertThat(parkingFeesApplicationService.getDriverReceipts(driverId)).hasSize(2)
        assertThat(parkingFeesApplicationService.getDriverReceipts(anotherDriverId)).hasSize(1)
    }

    @Test
    fun calculateParkingFee_regularDriverLessThanHour() {
        // given
        val driverId = "regular.driver"

        // when
        parkingFeesApplicationService.onParkingEnded(ParkingEnded(DriverId(driverId), Duration.ofMinutes(45), LocalDateTime.now()))

        // then
        assertThat(parkingFeesApplicationService.getDriverReceipts(driverId))
                .containsOnly(ParkingReceiptDto(MoneyDto(1.0, MoneyDto.Currency.PLN)))
    }

    @After
    fun tearDown() {
        parkingReceiptRepository.removeAll()
    }
}