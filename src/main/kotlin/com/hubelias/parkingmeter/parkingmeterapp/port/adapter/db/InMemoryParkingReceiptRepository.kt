package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.db

import com.hubelias.parkingmeter.parkingmeterapp.domain.driver.UserId
import com.hubelias.parkingmeter.parkingmeterapp.domain.receipt.ParkingReceipt
import com.hubelias.parkingmeter.parkingmeterapp.domain.receipt.ParkingReceiptRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryParkingReceiptRepository : ParkingReceiptRepository {
    private val receipts = ConcurrentHashMap.newKeySet<ParkingReceipt>()

    override fun add(parkingReceipt: ParkingReceipt) {
        receipts.add(parkingReceipt)
    }

    override fun findByDriver(userId: UserId): List<ParkingReceipt> {
        return receipts.filter { it.userId == userId }
    }

    override fun removeAll() {
        receipts.clear()
    }
}
