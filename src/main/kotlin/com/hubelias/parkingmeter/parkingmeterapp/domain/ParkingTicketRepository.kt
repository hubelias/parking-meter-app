package com.hubelias.parkingmeter.parkingmeterapp.domain

interface ParkingTicketRepository {
    fun add(parkingTicket: ParkingTicket)
    fun getStartedByVehicleId(vehicleId: String): ParkingTicket?
    fun doesStartedTicketExist(vehicleId: String): Boolean
    fun removeAll()
}
