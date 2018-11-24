package com.hubelias.parkingmeter.parkingmeterapp.application.tickets

import com.hubelias.parkingmeter.parkingmeterapp.application.common.DomainEventPublisher
import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId
import com.hubelias.parkingmeter.parkingmeterapp.domain.common.DateTimeProvider
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.ParkingEnded
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.ParkingTicket
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.ParkingTicketRepository
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.VehicleId
import org.springframework.stereotype.Service

@Service
class ParkingTicketApplicationService(
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
        val ticket = parkingTicketRepository.findStartedTicket(VehicleId(vehicleId))
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