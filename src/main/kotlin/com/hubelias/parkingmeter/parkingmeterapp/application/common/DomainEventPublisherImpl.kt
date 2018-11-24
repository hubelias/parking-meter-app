package com.hubelias.parkingmeter.parkingmeterapp.application.common

import com.hubelias.parkingmeter.parkingmeterapp.domain.common.DomainEvent
import org.springframework.stereotype.Service

@Service
class DomainEventPublisherImpl : DomainEventPublisher {
    override fun publish(event: DomainEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
