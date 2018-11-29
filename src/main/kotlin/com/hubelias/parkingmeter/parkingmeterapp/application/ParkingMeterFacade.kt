package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.DriverProvider
import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.UserId
import com.hubelias.parkingmeter.parkingmeterapp.domain.occupation.DateTimeProvider
import com.hubelias.parkingmeter.parkingmeterapp.domain.occupation.ParkingOccupation
import com.hubelias.parkingmeter.parkingmeterapp.domain.occupation.ParkingOccupationRepository
import com.hubelias.parkingmeter.parkingmeterapp.domain.occupation.VehicleId
import com.hubelias.parkingmeter.parkingmeterapp.domain.receipt.*
import org.joda.money.Money
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
        return parkingOccupationRepository.isParkingRegistered(VehicleId(vehicleId))
    }

    fun startParking(driverId: String, vehicleId: String) {
        val driver = driverProvider.getDriver(UserId(driverId))
                ?: throw UnknownDriverException(driverId)

        if (isParkingRegistered(vehicleId)) {
            throw ParkingAlreadyStartedException(vehicleId)
        } //TODO - can be solved by distinct on DB

        val ticket = ParkingOccupation.start(driver, VehicleId(vehicleId), dateTimeProvider)

        parkingOccupationRepository.add(ticket)
    }

    fun endParking(vehicleId: String) {
        val parkingOccupation = parkingOccupationRepository.findOne(VehicleId(vehicleId))
                ?: throw IllegalArgumentException("There is no started ticket for vehicle $vehicleId")

        val parkingReceipt = parkingOccupation.end(parkingFeeCalculator, dateTimeProvider)

        parkingOccupationRepository.remove(parkingOccupation)
        parkingReceiptRepository.add(parkingReceipt)
    }

    fun findDriverReceipts(driverId: String) =
            parkingReceiptRepository.findByDriver(UserId(driverId)).map(ParkingReceipt::dto)

    fun getDailyEarnings(dayOfYear: LocalDate) = parkingReceiptRepository.calculateDailyEarnings(dayOfYear)
}
