package com.hubelias.parkingmeter.domain.receipt

import com.hubelias.parkingmeter.domain.driver.DriverId
import org.joda.money.Money
import java.time.LocalDateTime
import java.util.*


class ParkingReceipt(
        val driverId: DriverId,
        val issuedAt: LocalDateTime,
        val cost: Money
) {
    val id: String = UUID.randomUUID().toString()

    override fun equals(other: Any?) = other is ParkingReceipt && id == other.id

    override fun hashCode(): Int = id.hashCode()
}
