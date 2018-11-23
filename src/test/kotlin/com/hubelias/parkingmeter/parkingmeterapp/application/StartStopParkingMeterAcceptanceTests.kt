package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.domain.ParkingTicketRepository
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
class StartStopParkingMeterAcceptanceTests {

    @Autowired
    private lateinit var parkingTicketRepository: ParkingTicketRepository
    @Autowired
    private lateinit var parkingMeterService: ParkingMeterService

    @Before
    fun setUp() {
        parkingTicketRepository.removeAll()
    }

    @Test
    fun meterIsNotStartedAtBeginning() {
        // given
        val vehicleId = "WW 123456"

        // then
        assertFalse(parkingMeterService.isMeterStarted(vehicleId))
    }

    @Test
    fun startMeter() {
        // given
        val driverId = "good.driver"
        val vehicleId = "WW 123456"

        // when
        parkingMeterService.startMeter(driverId, vehicleId)

        // then
        assertTrue(parkingMeterService.isMeterStarted(vehicleId))
    }

    @Test(expected = IllegalStateException::class)
    fun startMeter_alreadyStarted() {
        // given
        val driverId = "good.driver"
        val vehicleId = "WW 123456"
        parkingMeterService.startMeter(driverId, vehicleId)

        // when
        parkingMeterService.startMeter(driverId, vehicleId)
    }

    @Test
    fun stopMeter() {
        // given
        val driverId = "good.driver"
        val vehicleId = "WW 123456"
        parkingMeterService.startMeter(driverId, vehicleId)

        //when
        parkingMeterService.stopMeter(vehicleId)

        //then
        assertFalse(parkingMeterService.isMeterStarted(vehicleId))
    }

    @Test(expected = IllegalArgumentException::class)
    fun stopMeter_notStarted() {
        // given
        val vehicleId = "WW 123456"

        //when
        parkingMeterService.stopMeter(vehicleId)
    }

    @After
    fun tearDown() {
        parkingTicketRepository.removeAll()
    }
}