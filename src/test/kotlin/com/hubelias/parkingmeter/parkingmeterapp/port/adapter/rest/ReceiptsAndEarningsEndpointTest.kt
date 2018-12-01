package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.rest

import com.hubelias.parkingmeter.parkingmeterapp.application.MoneyDto
import com.hubelias.parkingmeter.parkingmeterapp.application.ParkingMeterFacade
import com.hubelias.parkingmeter.parkingmeterapp.application.ParkingReceiptDto
import com.hubelias.parkingmeter.parkingmeterapp.fixtures.randomDriverId
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.time.Month


class ReceiptsAndEarningsEndpointTest {
    companion object {
        private const val RECEIPTS_BASE_PATH = "http://localhost:8090/api/user/receipts"
    }

    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockBean
    private lateinit var parkingMeterFacade: ParkingMeterFacade

    @Test
    fun getReceipts() {
        val driverId = randomDriverId()
        whenever(parkingMeterFacade.findDriverReceipts(any())) doReturn listOf( //TODO: use driverId
                ParkingReceiptDto(MoneyDto(2.0, MoneyDto.Currency.PLN)),
                ParkingReceiptDto(MoneyDto(3.0, MoneyDto.Currency.PLN))
        )

        mockMvc
                .perform(get(RECEIPTS_BASE_PATH))
                .andExpect(status().isOk)
                .andExpect(content().json("""[
                            {"cost" : { "amount" : 2.0, "currency" : "PLN" }},
                            {"cost" : { "amount" : 3.0, "currency" : "PLN" }}
                        ]""".trimMargin())
                )
    }


}