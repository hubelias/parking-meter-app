package com.hubelias.parkingmeter.domain.occupation

import com.hubelias.parkingmeter.domain.driver.Driver
import com.hubelias.parkingmeter.domain.receipt.ParkingDuration
import com.hubelias.parkingmeter.domain.receipt.ParkingFeeCalculator
import com.hubelias.parkingmeter.domain.receipt.ParkingReceipt
import java.time.LocalDateTime


class ParkingOccupation(
        private val driver: Driver,
        val vehicleId: VehicleId,
        private val startedAt: LocalDateTime,
        val id: String
) {
    companion object {
        fun start(driver: Driver, vehicleId: VehicleId, dateTimeProvider: DateTimeProvider) =
                ParkingOccupation(driver, vehicleId, dateTimeProvider.currentDateTime(), vehicleId.registrationNumber)
    }

    fun end(parkingFeeCalculator: ParkingFeeCalculator, dateTimeProvider: DateTimeProvider): ParkingReceipt {
        val parkingDuration = ParkingDuration(startedAt, dateTimeProvider.currentDateTime())
        return ParkingReceipt(
                driver.id,
                parkingDuration.endedAt,
                parkingFeeCalculator.calculateFee(driver, parkingDuration))
    }
}
