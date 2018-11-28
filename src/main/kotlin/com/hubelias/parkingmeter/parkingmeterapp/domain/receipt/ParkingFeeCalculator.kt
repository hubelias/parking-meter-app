package com.hubelias.parkingmeter.parkingmeterapp.domain.parking.receipt

import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.driver.Driver
import org.joda.money.Money
import org.springframework.stereotype.Service


@Service
class ParkingFeeCalculator {
    fun calculateFee(driver: Driver, parkingDuration: ParkingDuration): Money {
        return selectFeeCalculationStrategy(driver.type).calculateParkingFee(parkingDuration.duration)
    }

    private fun selectFeeCalculationStrategy(driverType: Driver.Type) = when (driverType) {
        Driver.Type.REGULAR -> RegularFeeCalculationStrategy
        Driver.Type.DISABLED -> DriverDisabledFeeCalculationStrategy
    }
}
