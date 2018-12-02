package com.hubelias.parkingmeter.domain.receipt

import com.hubelias.parkingmeter.application.ParkingReceiptDto
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

    fun dto() = ParkingReceiptDto(cost.dto())

    override fun equals(other: Any?) = other is ParkingReceipt && id == other.id

    override fun hashCode(): Int = id.hashCode()
}
