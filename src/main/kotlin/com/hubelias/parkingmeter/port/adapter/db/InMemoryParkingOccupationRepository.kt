package com.hubelias.parkingmeter.port.adapter.db

import com.hubelias.parkingmeter.domain.occupation.ParkingAlreadyStartedException
import com.hubelias.parkingmeter.domain.occupation.ParkingOccupation
import com.hubelias.parkingmeter.domain.occupation.ParkingOccupationRepository
import com.hubelias.parkingmeter.domain.occupation.VehicleId
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryParkingOccupationRepository : ParkingOccupationRepository {
    private val parkedVehicles = ConcurrentHashMap<String, ParkingOccupation>()

    override fun add(parkingOccupation: ParkingOccupation) {
        parkedVehicles.putIfAbsent(parkingOccupation.id, parkingOccupation)?.let { alreadyExisting ->
            throw ParkingAlreadyStartedException(alreadyExisting.vehicleId)
        }
    }

    override fun findOne(vehicleId: VehicleId): ParkingOccupation? {
        return parkedVehicles.values.find { it.vehicleId == vehicleId }
    }

    override fun remove(parkingOccupation: ParkingOccupation) {
        parkedVehicles.remove(parkingOccupation.id)
    }

    override fun removeAll() {
        parkedVehicles.clear()
    }
}