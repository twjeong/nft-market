package com.mosaicsquare.service.sqs.handler

import com.mosaicsquare.service.nft.SecondaryMarketCommandService
import com.mosaicsquare.service.sqs.event.SqsEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class EthSecondaryMarketEventHandler(
    private val publisher: ApplicationEventPublisher,
    private val secondaryMarketCommandService: SecondaryMarketCommandService
) {

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthSecondaryMarketType).MOS_ETH_SECONDARY_MARKET_FixedPriceSet.name")
    fun onFixedPriceSetEvent(sqsEvent: SqsEvent) {
        try {
            secondaryMarketCommandService.createFixedPrice(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())

        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthSecondaryMarketType).MOS_ETH_SECONDARY_MARKET_FixedPriceSold.name")
    fun onFixedPriceSoldEvent(sqsEvent: SqsEvent) {
        try {
            secondaryMarketCommandService.fixedPriceBuy(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())

        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthSecondaryMarketType).MOS_ETH_SECONDARY_MARKET_OfferMade.name")
    fun onOfferMadeEvent(sqsEvent: SqsEvent) {
        try {
            secondaryMarketCommandService.makeOffer(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())

        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthSecondaryMarketType).MOS_ETH_SECONDARY_MARKET_OfferAccepted.name")
    fun onOfferAcceptedEvent(sqsEvent: SqsEvent) {
        try {
            secondaryMarketCommandService.acceptOffer(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())

        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthSecondaryMarketType).MOS_ETH_SECONDARY_MARKET_FixedPriceCanceled.name")
    fun onFixedPriceCanceledEvent(sqsEvent: SqsEvent) {
        try {
            secondaryMarketCommandService.cancelFixedPrice(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())

        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }
}
