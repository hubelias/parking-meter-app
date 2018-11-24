package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.application.common.DomainEventPublisher
import com.hubelias.parkingmeter.parkingmeterapp.application.tickets.ParkingTicketApplicationService
import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId
import com.hubelias.parkingmeter.parkingmeterapp.domain.common.DateTimeProvider
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.ParkingEnded
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.ParkingTicketRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
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
import java.time.Duration
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ParkingTicketApplicationServiceSpec {

    @Autowired
    private lateinit var parkingTicketRepository: ParkingTicketRepository
    @Autowired
    private lateinit var parkingTicketApplicationService: ParkingTicketApplicationService
    @MockBean
    private lateinit var domainEventPublisher: DomainEventPublisher
    @MockBean
    private lateinit var dateTimeProvider: DateTimeProvider

    @Before
    fun setUp() {
        parkingTicketRepository.removeAll()
    }

    @Test
    fun meterIsNotStartedAtBeginning() {
        // given
        val vehicleId = "WW 123456"

        // then
        assertFalse(parkingTicketApplicationService.isMeterStarted(vehicleId))
        verifyZeroInteractions(domainEventPublisher)
    }

    @Test
    fun startMeter() {
        // given
        val vehicleId = "WW 123456"

        // when
        parkingMeterIsStarted(vehicleId = vehicleId)

        // then
        assertTrue(parkingTicketApplicationService.isMeterStarted(vehicleId))
        verifyZeroInteractions(domainEventPublisher)
    }

    @Test(expected = IllegalStateException::class)
    fun startMeter_alreadyStarted() {
        // given
        val vehicleId = "WW 123456"
        parkingMeterIsStarted(vehicleId = vehicleId)

        // when
        parkingMeterIsStarted(vehicleId = vehicleId)
    }

    @Test
    fun stopMeter() {
        // given
        val startTime = LocalDateTime.now()
        val parkingDuration = Duration.ofHours(2).plusMinutes(25)
        val driverId = "good.driver"
        val vehicleId = "WW 123456"
        parkingMeterIsStarted(vehicleId = vehicleId, startTime = startTime)

        //when
        val stopTime = startTime.plus(parkingDuration)
        parkingMeterIsStopped(vehicleId, stopTime)

        //then
        assertFalse(parkingTicketApplicationService.isMeterStarted(vehicleId))
        verify(domainEventPublisher)
                .publish(ParkingEnded(DriverId(driverId), parkingDuration, stopTime))
    }

    @Test(expected = IllegalArgumentException::class)
    fun stopMeter_notStarted() {
        // given
        val vehicleId = "WW 123456"

        //when
        parkingTicketApplicationService.stopMeter(vehicleId)
        verifyZeroInteractions(domainEventPublisher)
    }

    @After
    fun tearDown() {
        parkingTicketRepository.removeAll()
    }

    private fun parkingMeterIsStarted(
            driverId: String = "good.driver",
            vehicleId: String = "WW 12345",
            startTime: LocalDateTime = LocalDateTime.now()
    ) {
        whenever(dateTimeProvider.currentDateTime()).doReturn(startTime)
        parkingTicketApplicationService.startMeter(driverId, vehicleId)
    }

    private fun parkingMeterIsStopped(
            vehicleId: String = "WW 12345",
            stopTime: LocalDateTime = LocalDateTime.now()
    ) {
        whenever(dateTimeProvider.currentDateTime()).doReturn(stopTime)
        parkingTicketApplicationService.stopMeter(vehicleId)
    }
}