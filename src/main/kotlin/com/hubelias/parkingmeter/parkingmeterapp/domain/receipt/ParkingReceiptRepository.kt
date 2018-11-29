package com.hubelias.parkingmeter.parkingmeterapp.domain.receipt

import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.UserId
import org.joda.money.Money
import java.time.LocalDate


interface ParkingReceiptRepository {
    fun add(parkingReceipt: ParkingReceipt)
    fun findByDriver(userId: UserId): List<ParkingReceipt>
    fun calculateDailyEarnings(dayOfYear: LocalDate) : Money
    fun removeAll()
}
