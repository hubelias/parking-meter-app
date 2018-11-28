package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.db

import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.driver.Driver
import com.hubelias.parkingmeter.parkingmeterapp.domain.user.UserId
import com.hubelias.parkingmeter.parkingmeterapp.domain.user.UserRepository
import org.springframework.stereotype.Repository


@Repository
class FakeUserRepository : UserRepository {
    override fun getDriver(userId: UserId): Driver? {
        return Driver(userId, Driver.Type.REGULAR)
    }
}
