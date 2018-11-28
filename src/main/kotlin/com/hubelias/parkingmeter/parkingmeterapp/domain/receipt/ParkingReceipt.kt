package com.hubelias.parkingmeter.parkingmeterapp.domain.receipt

import com.hubelias.parkingmeter.parkingmeterapp.application.ParkingReceiptDto
import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.UserId
import org.joda.money.Money
import java.time.LocalDateTime
import java.util.*


//TODO: equals and hash code, id
class ParkingReceipt(
        val userId: UserId,
        val issuedAt: LocalDateTime,
        val cost: Money
) {
    val id: String = UUID.randomUUID().toString()

    fun dto() = ParkingReceiptDto(cost.dto())
}

