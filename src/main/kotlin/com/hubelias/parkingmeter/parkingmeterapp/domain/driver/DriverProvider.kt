package com.hubelias.parkingmeter.parkingmeterapp.domain.parking.driver


interface DriverProvider {
    fun getDriver(userId: UserId): Driver?
}
