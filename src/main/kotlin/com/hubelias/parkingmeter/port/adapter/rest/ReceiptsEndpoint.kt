package com.hubelias.parkingmeter.port.adapter.rest

import com.hubelias.parkingmeter.application.ParkingMeterFacade
import com.hubelias.parkingmeter.application.ParkingReceiptDto
import com.hubelias.parkingmeter.port.adapter.users.UserRole
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("api/user/receipts")
class ReceiptsEndpoint(
        private val parkingMeterFacade: ParkingMeterFacade
) {
    @GetMapping
    @RolesAllowed(UserRole.DRIVER)
    fun getReceipts(principal: Principal): List<ParkingReceiptDto> {
        return parkingMeterFacade.findDriverReceipts(principal.name)
    }
}
