package com.hubelias.parkingmeter.domain.occupation


class ParkingAlreadyStartedException(vehicleId: VehicleId) :
        IllegalStateException("There is already a parking occupation created for vehicle $vehicleId.")
