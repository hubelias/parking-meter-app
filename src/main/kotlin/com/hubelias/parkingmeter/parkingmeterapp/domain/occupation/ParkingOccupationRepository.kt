package com.hubelias.parkingmeter.parkingmeterapp.domain.occupation

interface ParkingOccupationRepository {
    fun add(parkingOccupation: ParkingOccupation)
    fun findOne(vehicleId: VehicleId): ParkingOccupation?
    fun remove(parkingOccupation: ParkingOccupation)
    fun isParkingRegistered(vehicleId: VehicleId): Boolean
    fun removeAll()
}
