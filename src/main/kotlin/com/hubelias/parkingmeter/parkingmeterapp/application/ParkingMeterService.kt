package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.domain.ParkingTicket
import com.hubelias.parkingmeter.parkingmeterapp.domain.ParkingTicketRepository
import org.springframework.stereotype.Service

@Service
class ParkingMeterService(
        private val parkingTicketRepository: ParkingTicketRepository
) {
    fun isMeterStarted(vehicleId: String): Boolean {
        return parkingTicketRepository.doesStartedTicketExist(vehicleId)
    }

    fun startMeter(driverId: String, vehicleId: String) {
        if (isMeterStarted(vehicleId)) {
            throw IllegalStateException("Parking meter was already started for vehicle $vehicleId")
        }

        val ticket = ParkingTicket.startParking(vehicleId)

        parkingTicketRepository.add(ticket)
    }

    fun stopMeter(vehicleId: String) {
        val ticket = parkingTicketRepository.getStartedByVehicleId(vehicleId)
                ?: throw IllegalArgumentException("There is no started ticket for vehicle $vehicleId")

        ticket.endParking()
    }
}