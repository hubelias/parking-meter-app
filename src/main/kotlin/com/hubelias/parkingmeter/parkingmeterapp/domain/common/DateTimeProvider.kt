package com.hubelias.parkingmeter.parkingmeterapp.domain.common

import java.time.LocalDateTime


interface DateTimeProvider {
    fun currentDateTime(): LocalDateTime
}