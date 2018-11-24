package com.hubelias.parkingmeter.parkingmeterapp.domain.fees

import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId


interface ParkingReceiptRepository {
    fun add(parkingReceipt: ParkingReceipt)
    fun findByDriver(driverId: DriverId): List<ParkingReceipt>
    fun removeAll()
}