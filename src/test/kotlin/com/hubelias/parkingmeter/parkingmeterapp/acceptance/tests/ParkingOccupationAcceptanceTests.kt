package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.occupation.ParkingOccupationRepository
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ParkingOccupationAcceptanceTests {

    @Autowired
    private lateinit var parkingOccupationRepository: ParkingOccupationRepository
    @Autowired
    private lateinit var parkingMeterFacade: ParkingMeterFacade

    @Before
    fun setUp() {
        parkingOccupationRepository.removeAll()
    }

    @Test
    fun parkingIsNotStartedAtBeginning() {
        // given
        val vehicleId = randomValidVehicleId()

        // then
        assertFalse(parkingMeterFacade.isParkingRegistered(vehicleId))
    }

    @Test
    fun startParking_parkingIsRegistered() {
        // given
        val vehicleId = randomValidVehicleId()

        // when
        parkingIsStarted(vehicleId)

        // then
        assertTrue(parkingMeterFacade.isParkingRegistered(vehicleId))
    }

    @Test(expected = IllegalStateException::class)
    fun startParking_alreadyStarted() {
        // given
        val vehicleId = randomValidVehicleId()
        parkingIsStarted(vehicleId)

        // when
        parkingIsStarted(vehicleId)
    }

    @Test
    fun endParking_parkingIsUnregistered() {
        // given
        val vehicleId = randomValidVehicleId()
        parkingIsStarted(vehicleId)

        //when
        parkingIsEnded(vehicleId)

        //then
        assertFalse(parkingMeterFacade.isParkingRegistered(vehicleId))
    }

    @Test(expected = IllegalArgumentException::class)
    fun endParking_notStarted() {
        // given
        val vehicleId = randomValidVehicleId()

        //when
        parkingMeterFacade.endParking(vehicleId)
    }

    @After
    fun tearDown() {
        parkingOccupationRepository.removeAll()
    }

    private fun parkingIsStarted(vehicleId: String) {
        parkingMeterFacade.startParking(randomValidDriverId(), vehicleId)
    }

    private fun parkingIsEnded(vehicleId: String = randomValidVehicleId()) {
        parkingMeterFacade.endParking(vehicleId)
    }
}