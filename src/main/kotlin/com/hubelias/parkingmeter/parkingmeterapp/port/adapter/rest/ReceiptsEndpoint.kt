package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.rest

import com.hubelias.parkingmeter.parkingmeterapp.application.ParkingMeterFacade
import com.hubelias.parkingmeter.parkingmeterapp.application.ParkingReceiptDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api/user/receipts")
class ReceiptsEndpoint(
        private val parkingMeterFacade: ParkingMeterFacade
) {
    @GetMapping
    fun getReceipts(): List<ParkingReceiptDto> {
        val driverId = "s.hawking" //TODO - as argument
        return parkingMeterFacade.findDriverReceipts(driverId)
    }
}
