package com.hubelias.parkingmeter.parkingmeterapp.fixtures

import org.apache.commons.lang3.RandomStringUtils


fun randomDriverId(): String = RandomStringUtils.randomAlphanumeric(5, 15)
fun randomVehicleId(): String = RandomStringUtils.randomAlphanumeric(6, 8)
