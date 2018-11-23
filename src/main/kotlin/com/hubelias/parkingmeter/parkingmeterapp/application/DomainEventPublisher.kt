package com.hubelias.parkingmeter.parkingmeterapp.application

import com.hubelias.parkingmeter.parkingmeterapp.domain.common.DomainEvent


interface DomainEventPublisher {
    fun publish(event: DomainEvent)
}
