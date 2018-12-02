package com.hubelias.parkingmeter.domain.driver


class UnknownDriverException(driverId: DriverId) :
        IllegalArgumentException("Driver with username=${driverId.username} was not found.")
