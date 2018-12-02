package com.hubelias.parkingmeter.domain.driver


class Driver(
        val id: DriverId,
        val type: Type
) {
    enum class Type {
        REGULAR, DISABLED
    }
}
