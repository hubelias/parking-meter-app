package com.hubelias.parkingmeter.parkingmeterapp.domain

interface ParkingTicketRepository {
    fun add(parkingTicket: ParkingTicket)
    fun getStartedTicket(vehicleId: VehicleId): ParkingTicket?
    fun doesStartedTicketExist(vehicleId: VehicleId): Boolean
    fun removeAll()
}
