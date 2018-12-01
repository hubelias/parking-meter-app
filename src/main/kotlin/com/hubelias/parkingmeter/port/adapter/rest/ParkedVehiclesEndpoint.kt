package com.hubelias.parkingmeter.port.adapter.rest

import com.hubelias.parkingmeter.application.ParkingMeterFacade
import com.hubelias.parkingmeter.port.adapter.users.UserRole
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.annotation.security.RolesAllowed


@Controller
@RequestMapping("api/parkedVehicles")
class ParkedVehiclesEndpoint(
        private val parkingMeterFacade: ParkingMeterFacade
) {
    data class StartParkingCmd(val vehicleId: String)

    @PostMapping
    @RolesAllowed(UserRole.DRIVER)
    fun startParking(@RequestBody cmd: StartParkingCmd, principal: Principal): ResponseEntity<Void> {
        parkingMeterFacade.startParking(principal.name, cmd.vehicleId)

        val headers = HttpHeaders().apply {
            location = linkTo(ParkedVehiclesEndpoint::class.java).slash(cmd.vehicleId).toUri()
        }

        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @GetMapping("{vehicleId}")
    @RolesAllowed(UserRole.DRIVER, UserRole.OPERATOR, UserRole.OWNER)
    fun isParkingRegistered(@PathVariable vehicleId: String): ResponseEntity<Unit> {
        return if (parkingMeterFacade.isParkingRegistered(vehicleId)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("{vehicleId}")
    @RolesAllowed(UserRole.DRIVER)
    fun endParking(@PathVariable vehicleId: String): ResponseEntity<Unit> {
        parkingMeterFacade.endParking(vehicleId)
        return ResponseEntity.noContent().build()
    }
}
