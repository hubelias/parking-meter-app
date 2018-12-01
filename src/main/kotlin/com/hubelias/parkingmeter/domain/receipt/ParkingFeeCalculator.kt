package com.hubelias.parkingmeter.domain.receipt

import com.hubelias.parkingmeter.domain.driver.Driver
import org.joda.money.Money
import org.springframework.stereotype.Service


@Service
class ParkingFeeCalculator {
    private val regularFeeCalculationStrategy = DurationBasedFeeCalculationStrategy(
            Money.of(PLN, 1.0),
            Money.of(PLN, 2.0),
            1.5)
    private val disabledFeeCalculationStrategy = DurationBasedFeeCalculationStrategy(
            Money.of(PLN, 0.0),
            Money.of(PLN, 2.0),
            1.2)

    fun calculateFee(driver: Driver, parkingDuration: ParkingDuration): Money {
        return selectFeeCalculationStrategy(driver.type).calculateParkingFee(parkingDuration)
    }

    private fun selectFeeCalculationStrategy(driverType: Driver.Type) = when (driverType) {
        Driver.Type.REGULAR -> regularFeeCalculationStrategy
        Driver.Type.DISABLED -> disabledFeeCalculationStrategy
    }
}
