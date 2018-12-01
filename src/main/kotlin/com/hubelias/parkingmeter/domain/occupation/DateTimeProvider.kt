package com.hubelias.parkingmeter.domain.occupation

import java.time.LocalDateTime


interface DateTimeProvider {
    fun currentDateTime(): LocalDateTime
}
