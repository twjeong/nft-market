package com.mosaicsquare.service.sqs.handler

import com.mosaicsquare.service.nft.FactoryCommandService
import com.mosaicsquare.service.sqs.event.SqsEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class EthFactoryEventHandler(
    private val publisher: ApplicationEventPublisher,
    private val factoryCommandService: FactoryCommandService
) {
    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthFactoryType).MOS_ETH_FACTORY_MoneypipeCreated.name")
    fun onMoneypipeCreatedEvent(sqsEvent: SqsEvent) {
        try {
            factoryCommandService.contractMoneypipe(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }
}
