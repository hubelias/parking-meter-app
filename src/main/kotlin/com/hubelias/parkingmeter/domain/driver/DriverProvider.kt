package com.hubelias.parkingmeter.domain.driver


interface DriverProvider {
    @Throws(UnknownDriverException::class)
    fun getDriver(driverId: DriverId): Driver
}
