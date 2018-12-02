package com.hubelias.parkingmeter.acceptance.tests

import com.hubelias.parkingmeter.application.ParkingMeterFacade
import com.hubelias.parkingmeter.domain.driver.Driver
import com.hubelias.parkingmeter.domain.driver.DriverId
import com.hubelias.parkingmeter.domain.driver.DriverProvider
import com.hubelias.parkingmeter.domain.driver.UnknownDriverException
import com.hubelias.parkingmeter.domain.occupation.ParkingAlreadyStartedException
import com.hubelias.parkingmeter.domain.occupation.ParkingOccupationRepository
import com.hubelias.parkingmeter.utils.randomUsername
import com.hubelias.parkingmeter.utils.randomVehicleId
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ParkingOccupationAcceptanceTests {

    @Autowired
    private lateinit var parkingOccupationRepository: ParkingOccupationRepository
    @Autowired
    private lateinit var parkingMeterFacade: ParkingMeterFacade
    @MockBean
    private lateinit var driverProvider: DriverProvider

    @Before
    fun setUp() {
        parkingOccupationRepository.removeAll()
    }

    @Test
    fun parkingIsNotStartedAtBeginning() {
        assertFalse(parkingIsRegistered(randomVehicleId()))
    }

    @Test(expected = UnknownDriverException::class)
    fun startParking_unknownDriver() {
        // given
        val username = randomUsername()
        whenever(driverProvider.getDriver(DriverId(username))) doThrow UnknownDriverException(DriverId(username))

        // when
        parkingMeterFacade.startParking(username, randomVehicleId())
    }

    @Test
    fun startParking_parkingIsRegistered() {
        // given
        val vehicleId = randomVehicleId()

        // when
        parkingIsStartedByExistingUser(vehicleId)

        // then
        assertTrue(parkingIsRegistered(vehicleId))
    }

    @Test(expected = ParkingAlreadyStartedException::class)
    fun startParking_alreadyStarted() {
        // given
        val vehicleId = randomVehicleId()
        parkingIsStartedByExistingUser(vehicleId)

        // when
        parkingIsStartedByExistingUser(vehicleId)
    }

    @Test
    fun endParking_parkingIsUnregistered() {
        // given
        val vehicleId = randomVehicleId()
        parkingIsStartedByExistingUser(vehicleId)

        //when
        parkingIsEnded(vehicleId)

        //then
        assertFalse(parkingIsRegistered(vehicleId))
    }

    @Test(expected = IllegalArgumentException::class)
    fun endParking_notStarted() {
        // given
        val vehicleId = randomVehicleId()

        //when
        parkingIsEnded(vehicleId)
    }

    @After
    fun tearDown() {
        parkingOccupationRepository.removeAll()
    }

    private fun parkingIsStartedByExistingUser(vehicleId: String) {
        val username = existingUsername()
        parkingMeterFacade.startParking(username, vehicleId)
    }

    private fun existingUsername() = randomUsername().also { username ->
        val driverId = DriverId(username)
        whenever(driverProvider.getDriver(driverId)) doReturn Driver(driverId, Driver.Type.DISABLED)
    }

    private fun parkingIsEnded(vehicleId: String) {
        parkingMeterFacade.endParking(vehicleId)
    }

    private fun parkingIsRegistered(vehicleId: String) =
            parkingMeterFacade.isParkingRegistered(vehicleId)
}