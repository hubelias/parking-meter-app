package com.hubelias.parkingmeter.parkingmeterapp.domain.parking.receipt

import com.hubelias.parkingmeter.parkingmeterapp.domain.parking.driver.UserId


interface ParkingReceiptRepository {
    fun add(parkingReceipt: ParkingReceipt)
    fun findByDriver(userId: UserId): List<ParkingReceipt>
    fun removeAll()
}
