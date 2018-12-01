package com.hubelias.parkingmeter.port.adapter.clock

import com.hubelias.parkingmeter.domain.occupation.DateTimeProvider
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DefaultDateTimeProvider : DateTimeProvider {
    override fun currentDateTime(): LocalDateTime = LocalDateTime.now()
}
