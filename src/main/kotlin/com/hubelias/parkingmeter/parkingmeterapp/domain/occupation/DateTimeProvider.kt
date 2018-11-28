package com.hubelias.parkingmeter.parkingmeterapp.domain.parking.occupation

import java.time.LocalDateTime


interface DateTimeProvider {
    fun currentDateTime(): LocalDateTime
}
