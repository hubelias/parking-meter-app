package com.hubelias.parkingmeter.application

import com.hubelias.parkingmeter.domain.driver.DriverId
import com.hubelias.parkingmeter.domain.driver.DriverProvider
import com.hubelias.parkingmeter.domain.driver.UnknownDriverException
import com.hubelias.parkingmeter.domain.occupation.*
import com.hubelias.parkingmeter.domain.receipt.ParkingFeeCalculator
import com.hubelias.parkingmeter.domain.receipt.ParkingReceipt
import com.hubelias.parkingmeter.domain.receipt.ParkingReceiptRepository
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class ParkingMeterFacade(
        private val driverProvider: DriverProvider,
        private val parkingOccupationRepository: ParkingOccupationRepository,
        private val parkingReceiptRepository: ParkingReceiptRepository,
        private val dateTimeProvider: DateTimeProvider,
        private val parkingFeeCalculator: ParkingFeeCalculator
) {
    fun isParkingRegistered(vehicleId: String): Boolean {
        return parkingOccupationRepository.findOne(VehicleId(vehicleId)) != null
    }

    @Throws(UnknownDriverException::class, ParkingAlreadyStartedException::class)
    fun startParking(username: String, vehicleId: String) {
        val driver = driverProvider.getDriver(DriverId(username))
        val ticket = ParkingOccupation.start(driver, VehicleId(vehicleId), dateTimeProvider)

        parkingOccupationRepository.add(ticket)
    }

    @Throws(IllegalArgumentException::class)
    fun endParking(vehicleId: String) {
        val parkingOccupation = parkingOccupationRepository.findOne(VehicleId(vehicleId))
                ?: throw IllegalArgumentException("Parking has not been started for vehicle $vehicleId")

        val parkingReceipt = parkingOccupation.end(parkingFeeCalculator, dateTimeProvider)

        parkingOccupationRepository.remove(parkingOccupation)
        parkingReceiptRepository.add(parkingReceipt)
    }

    fun findDriverReceipts(username: String) =
            parkingReceiptRepository.findByDriver(DriverId(username)).map(ParkingReceipt::dto)

    fun getDailyEarnings(dayOfYear: LocalDate) = parkingReceiptRepository.calculateDailyEarnings(dayOfYear).dto()
}
