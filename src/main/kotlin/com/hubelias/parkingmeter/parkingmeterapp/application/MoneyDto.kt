package com.hubelias.parkingmeter.parkingmeterapp.application


data class MoneyDto(val amount: Double, val currency: Currency) {

    enum class Currency {
        PLN
    }
}
