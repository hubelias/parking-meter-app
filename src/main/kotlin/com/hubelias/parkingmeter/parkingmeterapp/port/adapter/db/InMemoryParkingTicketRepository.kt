package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.db

import com.hubelias.parkingmeter.parkingmeterapp.domain.ParkingTicket
import com.hubelias.parkingmeter.parkingmeterapp.domain.ParkingTicketRepository
import com.hubelias.parkingmeter.parkingmeterapp.domain.VehicleId
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryParkingTicketRepository : ParkingTicketRepository {
    private val tickets = ConcurrentHashMap<String, ParkingTicket>()

    override fun add(parkingTicket: ParkingTicket) {
        tickets[parkingTicket.id] = parkingTicket
    }

    override fun getStartedTicket(vehicleId: VehicleId): ParkingTicket? {
        return tickets.values.find { it.vehicleId == vehicleId }
    }

    override fun doesStartedTicketExist(vehicleId: VehicleId): Boolean {
        return tickets.values.any { it.hasNotEnded && it.vehicleId == vehicleId }
    }

    override fun removeAll() {
        tickets.clear()
    }
}