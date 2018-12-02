package com.hubelias.parkingmeter.port.adapter.rest

import com.hubelias.parkingmeter.application.MoneyDto
import com.hubelias.parkingmeter.application.ParkingMeterFacade
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("api/earnings")
class EarningsEndpoint(
        private val parkingMeterFacade: ParkingMeterFacade
) {
    @GetMapping
    @RolesAllowed(UserRole.OWNER)
    fun getEarnings(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: LocalDate) : MoneyDto {
        return parkingMeterFacade.getDailyEarnings(date)
    }
}
