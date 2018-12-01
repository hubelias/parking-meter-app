package com.hubelias.parkingmeter.domain.occupation

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class VehicleIdTest {

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()

    @Test
    fun invalid_whitespace() {
        // expect
        expectedException.expect(InvalidVehicleId::class.java)
        expectedException.expectMessage("contain whitespace")

        // when
        VehicleId("NO 12345")
    }

    @Test
    fun invalid_specialCharacters() {
        // expect
        expectedException.expect(InvalidVehicleId::class.java)
        expectedException.expectMessage("special characters")

        // when
        VehicleId("NO#12*45")
    }

    @Test
    fun valid() {
        VehicleId("NO12345")
    }
}