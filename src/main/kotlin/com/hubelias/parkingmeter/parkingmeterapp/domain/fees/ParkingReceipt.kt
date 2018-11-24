package com.hubelias.parkingmeter.parkingmeterapp.domain.fees

import com.hubelias.parkingmeter.parkingmeterapp.domain.DriverId
import org.joda.money.Money


//TODO: equals and hash code, id
class ParkingReceipt(
        val driverId: DriverId,
        val cost: Money
) {

}
