package com.hubelias.parkingmeter.parkingmeterapp.port.adapter.clock

import com.hubelias.parkingmeter.parkingmeterapp.domain.occupation.DateTimeProvider
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DefaultDateTimeProvider : DateTimeProvider {
    override fun currentDateTime(): LocalDateTime = LocalDateTime.now()
}
