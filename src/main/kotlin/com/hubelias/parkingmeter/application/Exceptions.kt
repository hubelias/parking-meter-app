package com.hubelias.parkingmeter.application


class UnknownDriverException(driverId: String) :
        IllegalArgumentException("Driver with id=$driverId was not found.")

class ParkingAlreadyStartedException(vehicleId: String) :
        IllegalStateException("There is already a parking occupation created for vehicle $vehicleId.")
