package com.hubelias.parkingmeter.acceptance.tests

import com.hubelias.parkingmeter.application.MoneyDto
import com.hubelias.parkingmeter.application.ParkingMeterFacade
import com.hubelias.parkingmeter.application.ParkingReceiptDto
import com.hubelias.parkingmeter.domain.driver.Driver
import com.hubelias.parkingmeter.domain.driver.DriverId
import com.hubelias.parkingmeter.domain.driver.DriverProvider
import com.hubelias.parkingmeter.domain.occupation.DateTimeProvider
import com.hubelias.parkingmeter.domain.receipt.ParkingReceiptRepository
import com.hubelias.parkingmeter.utils.randomUsername
import com.hubelias.parkingmeter.utils.randomVehicleId
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
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


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ParkingFeesAcceptanceTests {
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
    }

    @Test
    fun noReceiptsAtTheBeginning() {
        // given
        val username = randomUsername()

        // then
        assertThat(parkingMeterFacade.findDriverReceipts(username)).isEmpty()
    }

    @Test
    fun noReceiptsWhenParkingStarted() {
        // given
        val username = existingRegularDriverUsername(randomUsername())

        // when
        parkingMeterFacade.startParking(username, randomVehicleId())

        // then
        assertThat(parkingMeterFacade.findDriverReceipts(username)).isEmpty()
    }

    @Test
    fun createReceiptForCorrectDriverWhenParkingEnded() {
        // given
        val username = existingRegularDriverUsername("some.driver")
        val anotherUsername = existingRegularDriverUsername("another.driver")

        // when
        parkingStartedAndEnded(username)

        // then
        assertThat(parkingMeterFacade.findDriverReceipts(username)).hasSize(1)
        assertThat(parkingMeterFacade.findDriverReceipts(anotherUsername)).isEmpty()
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
            testDisabledFeeCalculation(Duration.ofMinutes(25), 0.0)

    @Test
    fun correctFee_disabledOneHour() =
            testDisabledFeeCalculation(Duration.ofHours(1), 0.0)

    @Test
    fun correctFee_disabledOneAndHalfHour() =
            testDisabledFeeCalculation(Duration.ofMinutes(75), 2.0)

    @Test
    fun correctFee_disabledTwoHours() =
            testDisabledFeeCalculation(Duration.ofHours(2), 2.0)

    @Test
    fun correctFee_disabledTwoAndAHalfHour() =
            testDisabledFeeCalculation(Duration.ofMinutes(150), 2.0 + 2.4)

    @Test
    fun correctFee_disabledThreeHours() =
            testDisabledFeeCalculation(Duration.ofHours(3), 2.0 + 2.4)

    @Test
    fun correctFee_disabledFourHours() =
            testDisabledFeeCalculation(Duration.ofHours(4), 2.0 + 2.4 + 2.88)

    @After
    fun tearDown() {
        parkingReceiptRepository.removeAll()
    }

    private fun parkingStartedAndEnded(
            username: String,
            parkingDuration: Duration = Duration.ofMinutes(25)
    ) {
        val vehicleId = randomVehicleId()
        val startTime = LocalDateTime.now()

        whenever(dateTimeProvider.currentDateTime()).doReturn(startTime)
        parkingMeterFacade.startParking(username, vehicleId)

        whenever(dateTimeProvider.currentDateTime()).doReturn(startTime.plus(parkingDuration))
        parkingMeterFacade.endParking(vehicleId)
    }

    private fun testRegularFeeCalculation(parkingDuration: Duration, expectedCostInPLN: Double) {
        // given
        val username = existingRegularDriverUsername(randomUsername())

        // when & then
        testFeeCalculation(username, parkingDuration, expectedCostInPLN)
    }

    private fun existingRegularDriverUsername(username: String): String {
        val driverId = DriverId(username)
        whenever(driverProvider.getDriver(driverId)) doReturn Driver(driverId, Driver.Type.REGULAR)
        return username
    }

    private fun testDisabledFeeCalculation(parkingDuration: Duration, expectedCostInPLN: Double) {
        // given
        val username = existingDisabledDriverUsername(randomUsername())

        // when & then
        testFeeCalculation(username, parkingDuration, expectedCostInPLN)
    }

    private fun existingDisabledDriverUsername(username: String): String {
        val driverId = DriverId(username)
        whenever(driverProvider.getDriver(driverId)) doReturn Driver(driverId, Driver.Type.DISABLED)
        return username
    }

    private fun testFeeCalculation(username: String, parkingDuration: Duration, expectedCostInPLN: Double) {
        // when
        parkingStartedAndEnded(username, parkingDuration)

        // then
        assertThat(parkingMeterFacade.findDriverReceipts(username))
                .containsOnly(ParkingReceiptDto(MoneyDto(expectedCostInPLN, MoneyDto.Currency.PLN)))
    }
}