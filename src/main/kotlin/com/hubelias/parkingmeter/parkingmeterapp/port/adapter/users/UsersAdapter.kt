package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.users

import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.Driver
import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.DriverProvider
import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.UserId
import org.springframework.stereotype.Service


@Service
class UsersAdapter : DriverProvider {
    override fun getDriver(userId: UserId): Driver? {
        return Driver(userId, Driver.Type.REGULAR)
    }
}
