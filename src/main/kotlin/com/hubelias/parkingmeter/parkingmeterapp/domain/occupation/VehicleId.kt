package com.hubelias.parkingmeter.parkingmeterapp.domain.occupation


data class VehicleId(val id: String) {
    init {
        if(id.all(Character::isLetterOrDigit).not()) {
            throw InvalidVehicleId("Vehicle id $id is invalid - it should not contain whitespace nor special characters")
        }
    }
}
