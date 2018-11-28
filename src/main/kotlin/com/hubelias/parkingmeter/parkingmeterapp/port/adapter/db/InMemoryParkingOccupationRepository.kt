package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.db

import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.ParkingOccupation
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.ParkingTicketRepository
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.VehicleId
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryParkingTicketRepository : ParkingTicketRepository {
    private val tickets = ConcurrentHashMap<String, ParkingOccupation>()

    override fun add(parkingOccupation: ParkingOccupation) {
        tickets[parkingOccupation.id] = parkingOccupation
    }

    override fun findOne(vehicleId: VehicleId): ParkingOccupation? {
        return tickets.values.find { it.vehicleId == vehicleId }
    }

    override fun doesStartedTicketExist(vehicleId: VehicleId): Boolean {
        return tickets.values.any { it.parkingDuration != null && it.vehicleId == vehicleId }
    }

    override fun removeAll() {
        tickets.clear()
    }
}