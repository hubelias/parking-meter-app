package com.hubelias.parkingmeter.port.adapter.db

import com.hubelias.parkingmeter.domain.driver.UserId
import com.hubelias.parkingmeter.domain.receipt.PLN
import com.hubelias.parkingmeter.domain.receipt.ParkingReceipt
import com.hubelias.parkingmeter.domain.receipt.ParkingReceiptRepository
import org.joda.money.Money
import org.springframework.stereotype.Repository
import java.time.LocalDate
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

    override fun calculateDailyEarnings(dayOfYear: LocalDate) = receipts
            .asSequence()
            .filter { it.issuedAt.toLocalDate() == dayOfYear }
            .map { it.cost }
            .sum()

    private fun Sequence<Money>.sum() : Money = fold(Money.of(PLN, 0.0)) { sum, money -> sum + money }

    override fun removeAll() {
        receipts.clear()
    }
}
