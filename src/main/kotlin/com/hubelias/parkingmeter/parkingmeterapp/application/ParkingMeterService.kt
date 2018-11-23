package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.domain.*
import com.hubelias.parkingmeter.parkingmeterapp.domain.common.DateTimeProvider
import org.springframework.stereotype.Service

@Service
class ParkingMeterService(
        private val parkingTicketRepository: ParkingTicketRepository,
        private val domainEventPublisher: DomainEventPublisher,
        private val dateTimeProvider: DateTimeProvider
) {
    fun isMeterStarted(vehicleId: String): Boolean {
        return parkingTicketRepository.doesStartedTicketExist(VehicleId(vehicleId))
    }

    fun startMeter(driverId: String, vehicleId: String) {
        if (isMeterStarted(vehicleId)) {
            throw IllegalStateException("Parking meter was already started for vehicle $vehicleId")
        }

        val ticket = ParkingTicket.startParking(DriverId(driverId), VehicleId(vehicleId), dateTimeProvider)

        parkingTicketRepository.add(ticket)
    }

    fun stopMeter(vehicleId: String) {
        val ticket = parkingTicketRepository.getStartedTicket(VehicleId(vehicleId))
                ?: throw IllegalArgumentException("There is no started ticket for vehicle $vehicleId")

        ticket.endParking(dateTimeProvider)

        domainEventPublisher.publish(
                ParkingEnded(
                        ticket.driverId,
                        ticket.parkingDuration,
                        dateTimeProvider.currentDateTime()
                ))
    }
}