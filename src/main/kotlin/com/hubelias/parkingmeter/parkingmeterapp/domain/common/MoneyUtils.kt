package com.hubelias.parkingmeter.parkingmeterapp.domain.common

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.math.RoundingMode


val PLN by lazy { CurrencyUnit.of("PLN") }

operator fun Money.times(valueToMultiplyBy: Double): Money = multipliedBy(valueToMultiplyBy, RoundingMode.HALF_EVEN)
