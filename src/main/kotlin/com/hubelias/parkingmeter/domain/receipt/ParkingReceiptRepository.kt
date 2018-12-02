package com.hubelias.parkingmeter.domain.receipt

import com.hubelias.parkingmeter.domain.driver.DriverId
import org.joda.money.Money
import java.time.LocalDate


interface ParkingReceiptRepository {
    fun add(parkingReceipt: ParkingReceipt)
    fun findByDriver(driverId: DriverId): List<ParkingReceipt>
    fun calculateDailyEarnings(dayOfYear: LocalDate) : Money
    fun removeAll()
}
