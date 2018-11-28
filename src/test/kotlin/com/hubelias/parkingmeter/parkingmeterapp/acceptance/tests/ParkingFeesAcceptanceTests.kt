package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.driver.Driver
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.driver.DriverProvider
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.occupation.DateTimeProvider
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.receipt.ParkingReceiptRepository
import com.hubelias.parkingmeter.parkingmeterapp.domain.user.UserId
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import junitparams.JUnitParamsRunner
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.exp

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
    private lateinit var parkingMeterFacade: ParkingMeterFacade
    @MockBean
    private lateinit var dateTimeProvider: DateTimeProvider
    @MockBean
    private lateinit var driverProvider: DriverProvider

    @Before
    fun setUp() {
        parkingReceiptRepository.removeAll()
        whenever(dateTimeProvider.currentDateTime()) doReturn LocalDateTime.now()
        whenever(driverProvider.getDriver(any())) doAnswer { Driver(it.getArgument(0), Driver.Type.REGULAR) }
    }

    @Test
    fun noReceiptsAtTheBeginning() {
        // given
        val driverId = randomValidDriverId()

        // then
        assertThat(parkingMeterFacade.findDriverReceipts(driverId)).isEmpty()
    }

    @Test
    fun noReceiptsWhenParkingStarted() {
        // given
        val driverId = "some.driver"
        val vehicleId = "another.driver"

        // when
        parkingMeterFacade.startParking(driverId, vehicleId)

        // then
        assertThat(parkingMeterFacade.findDriverReceipts(driverId)).isEmpty()
    }

    @Test
    fun createReceiptForCorrectDriverWhenParkingEnded() {
        // given
        val driverId = "some.driver"
        val anotherDriverId = "another.driver"

        // when
        parkingStartedAndEnded(driverId)

        // then
        assertThat(parkingMeterFacade.findDriverReceipts(driverId)).hasSize(1)
        assertThat(parkingMeterFacade.findDriverReceipts(anotherDriverId)).isEmpty()
    }

    @Test
    fun correctFee_regularLessThanHour() =
            testRegularFeeCalculation(Duration.ofMinutes(25), 1.0)

    @Test
    fun correctFee_regularOneHour() =
            testRegularFeeCalculation(Duration.ofHours(1), 1.0)

    @Test
    fun correctFee_regularOneAndHalfHour() =
            testRegularFeeCalculation(Duration.ofMinutes(75), 1.0 + 2.0)

    @Test
    fun correctFee_regularTwoHours() =
            testRegularFeeCalculation(Duration.ofHours(2), 1.0 + 2.0)

    @Test
    fun correctFee_regularTwoAndAHalfHour() =
            testRegularFeeCalculation(Duration.ofMinutes(150), 1.0 + 2.0 + 3.0)

    @Test
    fun correctFee_regularThreeHours() =
            testRegularFeeCalculation(Duration.ofHours(3), 1.0 + 2.0 + 3.0)

    @Test
    fun correctFee_regularFourHours() =
            testRegularFeeCalculation(Duration.ofHours(4), 1.0 + 2.0 + 3.0 + 4.5)

    @Test
    fun correctFee_disabledLessThanHour() =
            testRegularFeeCalculation(Duration.ofMinutes(25), 0.0)

    @Test
    fun correctFee_disabledOneHour() =
            testRegularFeeCalculation(Duration.ofHours(1), 0.0)

    @Test
    fun correctFee_disabledOneAndHalfHour() =
            testRegularFeeCalculation(Duration.ofMinutes(75), 2.0)

    @Test
    fun correctFee_disabledTwoHours() =
            testRegularFeeCalculation(Duration.ofHours(2), 2.0)

    @Test
    fun correctFee_disabledTwoAndAHalfHour() =
            testRegularFeeCalculation(Duration.ofMinutes(150), 2.0 + 2.4)

    @Test
    fun correctFee_disabledThreeHours() =
            testRegularFeeCalculation(Duration.ofHours(3), 2.0 + 2.4)

    @Test
    fun correctFee_disabledFourHours() =
            testRegularFeeCalculation(Duration.ofHours(4), 2.0 + 2.4 + 2.88)

    @After
    fun tearDown() {
        parkingReceiptRepository.removeAll()
    }

    private fun parkingStartedAndEnded(
            driverId: String,
            parkingDuration: Duration = Duration.ofMinutes(25)
    ) {
        val startTime = LocalDateTime.now()
        whenever(dateTimeProvider.currentDateTime()).doReturn(startTime, startTime.plus(parkingDuration))
        val vehicleId = randomValidVehicleId()
        parkingMeterFacade.startParking(driverId, vehicleId)
        parkingMeterFacade.endParking(vehicleId)
    }

    private fun testRegularFeeCalculation(parkingDuration: Duration, expectedCostInPLN: Double) {
        // given
        val driverId = randomValidDriverId()
        regularDriverExists(driverId)

        // when & then
        testFeeCalculation(driverId, parkingDuration, expectedCostInPLN)
    }

    private fun regularDriverExists(driverId: String) {
        val userId = UserId(driverId)
        whenever(driverProvider.getDriver(userId)) doReturn Driver(userId, Driver.Type.REGULAR)
    }

    private fun testDisabledFeeCalculation(parkingDuration: Duration, expectedCostInPLN: Double) {
        // given
        val driverId = randomValidDriverId()
        disabledDriverExists(driverId)

        // when & then
        testFeeCalculation(driverId, parkingDuration, expectedCostInPLN)
    }

    private fun disabledDriverExists(driverId: String) {
        val userId = UserId(driverId)
        whenever(driverProvider.getDriver(userId)) doReturn Driver(userId, Driver.Type.DISABLED)
    }

    private fun testFeeCalculation(driverId: String, parkingDuration: Duration, expectedCostInPLN: Double) {
        // when
        parkingStartedAndEnded(driverId, Duration.ofMinutes(25))

        // then
        assertThat(parkingMeterFacade.findDriverReceipts(driverId)).containsOnly(ParkingReceiptDto(MoneyDto(1.0, MoneyDto.Currency.PLN)))

        // when
        parkingStartedAndEnded("s.hawking")
    }
}