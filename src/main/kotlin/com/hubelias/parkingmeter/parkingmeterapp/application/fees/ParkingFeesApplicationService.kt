package com.hubelias.parkingmeter.parkingmeterapp.application.fees

import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId
import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverType
import com.hubelias.parkingmeter.parkingmeterapp.domain.fees.ParkingFeeCalculator
import com.hubelias.parkingmeter.parkingmeterapp.domain.fees.ParkingReceipt
import com.hubelias.parkingmeter.parkingmeterapp.domain.fees.ParkingReceiptRepository
import com.hubelias.parkingmeter.parkingmeterapp.domain.tickets.ParkingEnded
import org.joda.money.Money
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class ParkingFeesApplicationService(
        private val parkingReceiptRepository: ParkingReceiptRepository
) {
    private val parkingFeeCalculator = ParkingFeeCalculator()

    fun onParkingEnded(parkingEndedEvent: ParkingEnded) {
        val parkingReceipt = ParkingReceipt(
                parkingEndedEvent.driverId,
                parkingFeeCalculator.calculateFee(parkingEndedEvent.parkingDuration, DriverType.REGULAR)
        )
        parkingReceiptRepository.add(parkingReceipt)
    }

    fun getDriverReceipts(driverId: String): List<ParkingReceiptDto> {
        return parkingReceiptRepository
                .findByDriver(DriverId(driverId))
                .map { it.dto() }
    }

    fun getDailyEarnings(date: LocalDate): MoneyDto {
        TODO()
    }

    private fun ParkingReceipt.dto() = ParkingReceiptDto(cost.dto())
    private fun Money.dto() = MoneyDto(amount.toDouble(), MoneyDto.Currency.valueOf(currencyUnit.code))
}
