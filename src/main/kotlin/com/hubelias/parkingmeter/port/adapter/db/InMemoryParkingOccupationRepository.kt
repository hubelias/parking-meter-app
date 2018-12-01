package com.hubelias.parkingmeter.port.adapter.db

import com.hubelias.parkingmeter.domain.occupation.ParkingOccupation
import com.hubelias.parkingmeter.domain.occupation.ParkingOccupationRepository
import com.hubelias.parkingmeter.domain.occupation.VehicleId
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryParkingOccupationRepository : ParkingOccupationRepository {
    private val tickets = ConcurrentHashMap<String, ParkingOccupation>()

    override fun add(parkingOccupation: ParkingOccupation) {
        tickets[parkingOccupation.id] = parkingOccupation
    }

    override fun findOne(vehicleId: VehicleId): ParkingOccupation? {
        return tickets.values.find { it.vehicleId == vehicleId }
    }

    override fun remove(parkingOccupation: ParkingOccupation) {
        tickets.remove(parkingOccupation.id)
    }

    override fun isParkingRegistered(vehicleId: VehicleId): Boolean {
        return tickets.values.any { it.vehicleId == vehicleId }
    }

    override fun removeAll() {
        tickets.clear()
    }
}