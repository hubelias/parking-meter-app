package com.hubelias.parkingmeter.parkingmeterapp.domain.fees

import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverType
import com.hubelias.parkingmeter.parkingmeterapp.domain.common.PLN
import org.joda.money.Money
import java.time.Duration


class ParkingFeeCalculator {
    fun calculateFee(parkingDuration: Duration, driverType: DriverType): Money {
        return Money.of(PLN, 1.0)
    }
}