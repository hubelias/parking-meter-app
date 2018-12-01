package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.rest

import com.hubelias.parkingmeter.parkingmeterapp.application.MoneyDto
import com.hubelias.parkingmeter.parkingmeterapp.application.ParkingMeterFacade
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime


@RestController
@RequestMapping("api/earnings")
class EarningsEndpoint(
        private val parkingMeterFacade: ParkingMeterFacade
) {
    @GetMapping
    fun getEarnings(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: LocalDate) : MoneyDto {
        return parkingMeterFacade.getDailyEarnings(date)
    }
}
