package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.rest

import com.hubelias.parkingmeter.parkingmeterapp.application.ParkingMeterFacade
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@RequestMapping("api/parkedVehicles")
class ParkedVehiclesEndpoint(
        private val parkingMeterFacade: ParkingMeterFacade
) {
    data class StartParkingCmd(val vehicleId: String)

    @PostMapping
    fun startParking(@RequestBody cmd: StartParkingCmd): ResponseEntity<Void> {
        val driverId = "s.hawking" //TODO: as argument
        parkingMeterFacade.startParking(driverId, cmd.vehicleId)

        val headers = HttpHeaders().apply {
            location = linkTo(ParkedVehiclesEndpoint::class.java).slash(cmd.vehicleId).toUri()
        }

        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @GetMapping("{vehicleId}")
    fun isParkingRegistered(@PathVariable vehicleId: String): ResponseEntity<Unit> {
        return if (parkingMeterFacade.isParkingRegistered(vehicleId)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("{vehicleId}")
    fun endParking(@PathVariable vehicleId: String): ResponseEntity<Unit> {
        parkingMeterFacade.endParking(vehicleId)
        return ResponseEntity.noContent().build()
    }
}
