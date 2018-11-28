package com.hubelias.parkingmeter.parkingmeterapp.domain.parking.driver


class Driver(
        val id: UserId,
        val type: Type
) {
    enum class Type {
        REGULAR, DISABLED
    }
}
