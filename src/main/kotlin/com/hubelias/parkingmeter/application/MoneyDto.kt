package com.hubelias.parkingmeter.application

import org.joda.money.Money

data class MoneyDto(val amount: Double, val currency: Currency) {

    enum class Currency {
        PLN
    }
}

fun Money.dto() = MoneyDto(amount.toDouble(), MoneyDto.Currency.PLN)
