package com.hubelias.parkingmeter.fixtures

import com.hubelias.parkingmeter.domain.receipt.PLN
import org.apache.commons.lang3.RandomStringUtils
import org.joda.money.Money
import java.math.RoundingMode


fun randomDriverId(): String = RandomStringUtils.randomAlphanumeric(5, 15)
fun randomVehicleId(): String = RandomStringUtils.randomAlphanumeric(6, 8)

fun Double.PLN() = Money.of(PLN, this, RoundingMode.HALF_EVEN)
