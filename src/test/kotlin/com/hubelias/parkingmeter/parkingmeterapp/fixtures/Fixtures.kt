package com.hubelias.parkingmeter.parkingmeterapp.fixtures

import org.apache.commons.lang3.RandomStringUtils
import org.joda.money.Money
import java.math.RoundingMode
import com.hubelias.parkingmeter.parkingmeterapp.domain.receipt.PLN


fun randomDriverId(): String = RandomStringUtils.randomAlphanumeric(5, 15)
fun randomVehicleId(): String = RandomStringUtils.randomAlphanumeric(6, 8)

fun Double.PLN() = Money.of(PLN, this, RoundingMode.HALF_EVEN)
