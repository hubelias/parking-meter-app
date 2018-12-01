package com.hubelias.parkingmeter.domain.receipt

import com.hubelias.parkingmeter.application.MoneyDto
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.math.RoundingMode


val PLN : CurrencyUnit = CurrencyUnit.of("PLN")

operator fun Money.times(valueToMultiplyBy: Double): Money = multipliedBy(valueToMultiplyBy, RoundingMode.HALF_EVEN)

fun Money.dto() = MoneyDto(amount.toDouble(), MoneyDto.Currency.PLN)
