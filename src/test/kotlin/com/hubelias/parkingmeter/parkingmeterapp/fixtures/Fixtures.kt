package com.hubelias.parkingmeter.parkingmeterapp.application

import org.apache.commons.lang3.RandomStringUtils


fun randomValidDriverId() = RandomStringUtils.randomAlphanumeric(5, 15)
fun randomValidVehicleId() = RandomStringUtils.randomAlphanumeric(6, 8)
