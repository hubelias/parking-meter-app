package com.hubelias.parkingmeter.parkingmeterapp.domain.occupation

import java.time.LocalDateTime


interface DateTimeProvider {
    fun currentDateTime(): LocalDateTime
}
