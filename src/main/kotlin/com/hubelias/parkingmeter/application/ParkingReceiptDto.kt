package com.hubelias.parkingmeter.application

import com.hubelias.parkingmeter.domain.receipt.ParkingReceipt


data class ParkingReceiptDto(
        val cost: MoneyDto
)

fun ParkingReceipt.dto() = ParkingReceiptDto(cost.dto())