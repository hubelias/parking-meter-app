package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.domain.user.UserId
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.driver.DriverProvider
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.occupation.DateTimeProvider
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.occupation.ParkingOccupation
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.occupation.ParkingOccupationRepository
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.occupation.VehicleId
import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.receipt.ParkingReceiptRepository
import org.springframework.stereotype.Service

@Service
class ParkingOccupationApplicationService(
        private val driverProvider: DriverProvider,
        private val parkingOccupationRepository: ParkingOccupationRepository,
        private val parkingReceiptRepository: ParkingReceiptRepository,
        private val dateTimeProvider: DateTimeProvider
) {
    fun isParkingRegistered(vehicleId: String): Boolean {
        return parkingOccupationRepository.doesStartedTicketExist(VehicleId(vehicleId))
    }

    fun startParking(driverId: String, vehicleId: String) {
        val driver = driverProvider.getDriver(UserId(driverId))
                ?: throw Exception()

        if (isParkingRegistered(vehicleId)) {
            throw IllegalStateException("Parking meter was already started for vehicle $vehicleId")
        } //TODO - can be solved by distinct on DB

        val ticket = ParkingOccupation.start(driver, VehicleId(vehicleId), dateTimeProvider)

        parkingOccupationRepository.add(ticket)
    }

    fun endParking(vehicleId: String) {
        val parkingOccupation = parkingOccupationRepository.findOne(VehicleId(vehicleId))
                ?: throw IllegalArgumentException("There is no started ticket for vehicle $vehicleId")

        val parkingReceipt = parkingOccupation.end(dateTimeProvider)

        parkingOccupationRepository.remove(parkingOccupation)
        parkingReceiptRepository.add(parkingReceipt)
    }
}

