package com.hubelias.parkingmeter.parkingmeterapp.domain.common

import java.time.LocalDateTime


abstract class DomainEvent {
    abstract val timestamp: LocalDateTime
}