package com.hubelias.parkingmeter.parkingmeterapp.domain.parking.occupation

import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.driver.Driver
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.receipt.ParkingDuration
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.receipt.ParkingFeeCalculator
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.receipt.ParkingReceipt
import java.time.LocalDateTime


class ParkingOccupation(
        private val driver: Driver,
        val vehicleId: VehicleId, //TODO: distinct
        private val startedAt: LocalDateTime,
        val id: String
) {
    companion object {
        fun start(driver: Driver, vehicleId: VehicleId, dateTimeProvider: DateTimeProvider) =
                ParkingOccupation(driver, vehicleId, dateTimeProvider.currentDateTime(), vehicleId.id) //TODO vehicleId as Id?
    }

    fun end(parkingFeeCalculator: ParkingFeeCalculator, dateTimeProvider: DateTimeProvider): ParkingReceipt {
        val parkingDuration = ParkingDuration(startedAt, dateTimeProvider.currentDateTime())
        return ParkingReceipt(
                driver.id,
                parkingDuration.endedAt,
                parkingFeeCalculator.calculateFee(driver, parkingDuration))
    }
}
