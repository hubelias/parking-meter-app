package com.hubelias.parkingmeter.domain.occupation


interface ParkingOccupationRepository {

    @Throws(ParkingAlreadyStartedException::class)
    fun add(parkingOccupation: ParkingOccupation)

    fun findOne(vehicleId: VehicleId): ParkingOccupation?

    fun remove(parkingOccupation: ParkingOccupation)

    fun removeAll()
}
