package com.hubelias.parkingmeter.application


data class MoneyDto(val amount: Double, val currency: Currency) {

    enum class Currency {
        PLN
    }
}
