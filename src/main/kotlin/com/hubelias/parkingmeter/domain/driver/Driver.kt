package com.hubelias.parkingmeter.domain.driver


class Driver(
        val id: UserId,
        val type: Type
) {
    enum class Type {
        REGULAR, DISABLED
    }
}
