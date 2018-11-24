package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.db

import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId
import com.hubelias.parkingmeter.parkingmeterapp.domain.fees.ParkingReceipt
import com.hubelias.parkingmeter.parkingmeterapp.domain.fees.ParkingReceiptRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryParkingReceiptRepository : ParkingReceiptRepository {
    private val receipts = ConcurrentHashMap.newKeySet<ParkingReceipt>()

    override fun add(parkingReceipt: ParkingReceipt) {
        receipts.add(parkingReceipt)
    }

    override fun findByDriver(driverId: DriverId): List<ParkingReceipt> {
        return receipts.filter { it.driverId == driverId }
    }

    override fun removeAll() {
        receipts.clear()
    }
}
