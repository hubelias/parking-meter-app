package com.hubelias.parkingmeter.domain.occupation


data class VehicleId(val registrationNumber: String) {
    init {
        if (registrationNumber.all(Character::isLetterOrDigit).not()) {
            throw InvalidVehicleId("Vehicle id $registrationNumber is invalid - it should not contain whitespace nor special characters")
        }
    }
}
