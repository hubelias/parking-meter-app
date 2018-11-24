package com.hubelias.parkingmeter.parkingmeterapp.domain.tickets

import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId
import com.hubelias.parkingmeter.parkingmeterapp.domain.common.DateTimeProvider
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class ParkingTicket(
        val id: String,
        val driverId: DriverId,
        val vehicleId: VehicleId,
        private val startTime: LocalDateTime
) {
    companion object {
        fun startParking(driverId: DriverId, vehicleId: VehicleId, dateTimeProvider: DateTimeProvider) = ParkingTicket(
                UUID.randomUUID().toString(),
                driverId,
                vehicleId,
                dateTimeProvider.currentDateTime()
        )
    }

    private var status = TicketStatus.STARTED
    private var endTime: LocalDateTime? = null

    val hasNotEnded: Boolean
        get() = (status == TicketStatus.STARTED)

    val parkingDuration: Duration
        get() = endTime?.let { Duration.between(startTime, it) } ?: Duration.ZERO

    fun endParking(dateTimeProvider: DateTimeProvider) {
        endTime = dateTimeProvider.currentDateTime()
        status = TicketStatus.ENDED
    }

    private enum class TicketStatus {
        STARTED, ENDED
    }
}
