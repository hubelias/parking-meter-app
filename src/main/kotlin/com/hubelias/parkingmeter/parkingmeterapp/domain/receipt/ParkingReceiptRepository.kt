package com.hubelias.parkingmeter.parkingmeterapp.domain.receipt

import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.UserId


interface ParkingReceiptRepository {
    fun add(parkingReceipt: ParkingReceipt)
    fun findByDriver(userId: UserId): List<ParkingReceipt>
    fun removeAll()
}
