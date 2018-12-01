package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.rest

import com.hubelias.parkingmeter.parkingmeterapp.application.MoneyDto
import com.hubelias.parkingmeter.parkingmeterapp.application.ParkingMeterFacade
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.Month


@WebMvcTest(EarningsEndpoint::class)
@RunWith(SpringRunner::class)
class EarningsEndpointTest {
    companion object {
        private const val EARNINGS_BASE_PATH = "http://localhost:8090/api/earnings"
    }

    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockBean
    private lateinit var parkingMeterFacade: ParkingMeterFacade

    @Test
    fun getEarnings() {
        whenever(parkingMeterFacade.getDailyEarnings(LocalDate.of(2018, Month.OCTOBER, 23)))
                .doReturn(MoneyDto(76.0, MoneyDto.Currency.PLN))

        mockMvc.perform(get("$EARNINGS_BASE_PATH?date=2018-10-23"))
                .andExpect(status().isOk)
                .andExpect(content().json("""{"amount": 76.0, "currency": "PLN"}"""))
    }
}
