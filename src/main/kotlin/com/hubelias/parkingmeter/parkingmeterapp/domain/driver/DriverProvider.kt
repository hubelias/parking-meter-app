package com.hubelias.parkingmeter.parkingmeterapp.domain.driver


interface DriverProvider {
    fun getDriver(userId: UserId): Driver?
}
