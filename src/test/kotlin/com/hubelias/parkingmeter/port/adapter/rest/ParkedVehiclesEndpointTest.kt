package com.hubelias.parkingmeter.port.adapter.rest

import com.hubelias.parkingmeter.application.ParkingMeterFacade
import com.hubelias.parkingmeter.domain.occupation.InvalidVehicleId
import com.hubelias.parkingmeter.port.adapter.rest.ParkedVehiclesEndpointTest.Companion.DRIVER_ID
import com.hubelias.parkingmeter.utils.randomVehicleId
import com.nhaarman.mockitokotlin2.*
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@WebMvcTest(ParkedVehiclesEndpoint::class)
@RunWith(SpringRunner::class)
@WithMockUser(username = DRIVER_ID, roles = [UserRole.DRIVER])
class ParkedVehiclesEndpointTest {
    companion object {
        const val DRIVER_ID = "j.alba"
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

        verify(parkingMeterFacade).startParking(DRIVER_ID, vehicleId)
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
    @WithMockUser(roles = [UserRole.OPERATOR])
    fun startParking_forbiddenForOperator() = startParking_forbidden()

    @Test
    @WithMockUser(roles = [UserRole.OWNER])
    fun startParking_forbiddenForOwner() = startParking_forbidden()

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
    @WithMockUser(roles = [UserRole.OPERATOR])
    fun isParkingRegistered_isNotRegistered_allowedForOperator() = isParkingRegistered_isNotRegistered()

    @Test
    @WithMockUser(roles = [UserRole.OPERATOR])
    fun isParkingRegistered_isRegistered_get_allowedForOperator() = isParkingRegistered_isRegistered_get()

    @Test
    @WithMockUser(roles = [UserRole.OWNER])
    fun isParkingRegistered_allowedForOwner() = isParkingRegistered_isNotRegistered()

    @Test
    @WithMockUser(roles = [UserRole.OWNER])
    fun isParkingRegistered_isRegistered_get_allowedForOwner() = isParkingRegistered_isRegistered_get()

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

        mockMvc.performEndParkingRequest(vehicleId)
                .andExpect(status().isNoContent)

        verify(parkingMeterFacade).endParking(vehicleId)
        verifyNoMoreInteractions(parkingMeterFacade)
    }

    @Test
    fun endParking_failure() {
        val vehicleId = randomVehicleId()
        whenever(parkingMeterFacade.endParking(any())) doThrow IllegalStateException("Ooops!")

        mockMvc.performEndParkingRequest(vehicleId)
                .andExpect(status().isBadRequest)
                .andExpect(content().json("""{
                    "error" : "IllegalStateException",
                    "message" : "Ooops!"
                }""".trimIndent()))
    }

    @Test
    @WithMockUser(roles = [UserRole.OWNER])
    fun endParking_forbiddenForOwner() = endParking_forbidden()

    @Test
    @WithMockUser(roles = [UserRole.OPERATOR])
    fun endParking_forbiddenForOperator()  = endParking_forbidden()

    private fun MockMvc.performStartParkingRequest(vehicleId: String = randomVehicleId()) =
            perform(post("$BASE_PATH/parkedVehicles")
                    .content("""{ "vehicleId" : "$vehicleId" }""")
                    .contentType(MediaType.APPLICATION_JSON_UTF8))

    private fun startParking_forbidden() {
        mockMvc.performStartParkingRequest().andExpect(status().isForbidden)
    }

    private fun MockMvc.performEndParkingRequest(vehicleId: String = randomVehicleId()) =
            perform(delete("$BASE_PATH/parkedVehicles/$vehicleId")
                    .contentType(MediaType.APPLICATION_JSON_UTF8))

    private fun endParking_forbidden() {
        mockMvc.performEndParkingRequest().andExpect(status().isForbidden)
    }
}
