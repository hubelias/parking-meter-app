package com.hubelias.parkingmeter.domain.driver


interface DriverProvider {
    fun getDriver(userId: UserId): Driver?
}
