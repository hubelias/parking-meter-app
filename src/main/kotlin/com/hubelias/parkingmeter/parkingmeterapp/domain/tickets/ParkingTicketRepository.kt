package com.hubelias.parkingmeter.parkingmeterapp.domain.tickets

interface ParkingTicketRepository {
    fun add(parkingTicket: ParkingTicket)
    fun findStartedTicket(vehicleId: VehicleId): ParkingTicket?
    fun doesStartedTicketExist(vehicleId: VehicleId): Boolean
    fun removeAll()
}
