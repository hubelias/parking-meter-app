package com.hubelias.parkingmeter.parkingmeterapp.domain.tickets

import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId
import com.hubelias.parkingmeter.parkingmeterapp.domain.common.DomainEvent
import java.time.Duration
import java.time.LocalDateTime


data class ParkingEnded(
        val driverId: DriverId,
        val parkingDuration: Duration,
        override val timestamp: LocalDateTime
) : DomainEvent()