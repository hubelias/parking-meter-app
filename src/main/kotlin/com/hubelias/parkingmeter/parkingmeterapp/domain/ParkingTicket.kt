package com.hubelias.parkingmeter.parkingmeterapp.domain

import java.util.*

class ParkingTicket(
        val id: String,
        val vehicleId: String
) {
    companion object {
        fun startParking(vehicleId: String) = ParkingTicket(
                UUID.randomUUID().toString(),
                vehicleId
        )
    }

    private var status = TicketStatus.STARTED

    val hasNotEnded: Boolean
        get() = (status == TicketStatus.STARTED)

    fun endParking() {
        status = TicketStatus.ENDED
    }

    private enum class TicketStatus {
        STARTED, ENDED
    }
}
