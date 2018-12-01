package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.rest

import com.hubelias.parkingmeter.parkingmeterapp.application.ParkingMeterFacade
import com.hubelias.parkingmeter.parkingmeterapp.domain.occupation.InvalidVehicleId
import com.hubelias.parkingmeter.parkingmeterapp.fixtures.randomVehicleId
import com.nhaarman.mockitokotlin2.*
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.lang.IllegalArgumentException


@WebMvcTest(ParkedVehiclesEndpoint::class)
@RunWith(SpringRunner::class)
class ParkedVehiclesEndpointTest {
    companion object {
        private const val BASE_PATH = "http://localhost:8090/api"
    }

    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockBean
    private lateinit var parkingMeterFacade: ParkingMeterFacade

    @Test
    fun startParking_success() {
        val vehicleId = "NO12345"

        mockMvc.performStartParkingRequest(vehicleId)
                .andExpect(status().isCreated)
                .andExpect(header().string("Location", "$BASE_PATH/parkedVehicles/$vehicleId"))

        verify(parkingMeterFacade).startParking("s.hawking", vehicleId) //TODO: use driverId
        verifyNoMoreInteractions(parkingMeterFacade)
    }

    @Test
    fun startParking_badRequest() {
        whenever(parkingMeterFacade.startParking(any(), any()))
                .doThrow(InvalidVehicleId("Some message would be useful"))

        mockMvc.performStartParkingRequest()
                .andExpect(status().isBadRequest)
                .andExpect(content().json("""{
                    "error" : "InvalidVehicleId",
                    "message" : "Some message would be useful"
                }""".trimIndent()))
    }

    @Test
    fun isParkingRegistered_isNotRegistered() {
        mockMvc.perform(get("$BASE_PATH/parkedVehicles/NO12345"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun isParkingRegistered_isRegistered_get() {
        val vehicleId = randomVehicleId()
        whenever(parkingMeterFacade.isParkingRegistered(vehicleId)).doReturn(true)

        mockMvc.perform(get("$BASE_PATH/parkedVehicles/$vehicleId"))
                .andExpect(status().isNoContent)
    }

    @Test
    fun isParkingRegistered_isRegistered_options() {
        val vehicleId = randomVehicleId()
        whenever(parkingMeterFacade.isParkingRegistered(vehicleId)).doReturn(true)

        mockMvc.perform(options("$BASE_PATH/parkedVehicles/$vehicleId"))
                .andExpect(status().isOk)
                .andExpect(header().string("Allow", allOf(
                                containsString("HEAD"),
                                containsString("OPTIONS"),
                                containsString("DELETE"),
                                containsString("GET")
                        )))
    }

    @Test
    fun endParking_success() {
        val vehicleId = randomVehicleId()

        mockMvc.perform(delete("$BASE_PATH/parkedVehicles/$vehicleId"))
                .andExpect(status().isNoContent)

        verify(parkingMeterFacade).endParking(vehicleId)
        verifyNoMoreInteractions(parkingMeterFacade)
    }

    @Test
    fun endParking_failure() {
        val vehicleId = randomVehicleId()
        whenever(parkingMeterFacade.endParking(any())) doThrow IllegalArgumentException("Ooops!")

        mockMvc.perform(delete("$BASE_PATH/parkedVehicles/$vehicleId"))
                .andExpect(status().isBadRequest)
                .andExpect(content().json("""{
                    "error" : "IllegalArgumentException",
                    "message" : "Ooops!"
                }""".trimIndent()))
    }

    private fun MockMvc.performStartParkingRequest(vehicleId: String = randomVehicleId()) =
            perform(post("$BASE_PATH/parkedVehicles")
                    .content("""{ "vehicleId" : "$vehicleId" }""")
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
}
