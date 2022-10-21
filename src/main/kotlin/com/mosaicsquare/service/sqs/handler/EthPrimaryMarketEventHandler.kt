package com.mosaicsquare.service.sqs.handler

import com.mosaicsquare.service.nft.PrimaryMarketCommandService
import com.mosaicsquare.service.sqs.event.SqsEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class EthPrimaryMarketEventHandler(
    private val publisher: ApplicationEventPublisher,
    private val primaryMarketCommandService: PrimaryMarketCommandService
) {
    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthPrimaryMarketType).MOS_ETH_PRIMARY_MARKET_AuctionCreated.name")
    fun onAuctionCreatedEvent(sqsEvent: SqsEvent) {
        try {
            primaryMarketCommandService.createAuction(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())

        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthPrimaryMarketType).MOS_ETH_PRIMARY_MARKET_AuctionCanceled.name")
    fun onAuctionCanceledEvent(sqsEvent: SqsEvent) {
        try {
            primaryMarketCommandService.adminCancelAuction(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthPrimaryMarketType).MOS_ETH_PRIMARY_MARKET_AuctionBidPlaced.name")
    fun onAuctionBidPlacedEvent(sqsEvent: SqsEvent) {
        try {
            primaryMarketCommandService.placeBid(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthPrimaryMarketType).MOS_ETH_PRIMARY_MARKET_AuctionFinalized.name")
    fun onAuctionFinalizedEvent(sqsEvent: SqsEvent) {
        try {
            primaryMarketCommandService.finalizeAuction(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthPrimaryMarketType).MOS_ETH_PRIMARY_MARKET_FixedPriceSet.name")
    fun onFixedPriceSetEvent(sqsEvent: SqsEvent) {
        try {
            primaryMarketCommandService.createFixedPrice(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthPrimaryMarketType).MOS_ETH_PRIMARY_MARKET_FixedPriceCanceled.name")
    fun onFixedPriceCanceledEvent(sqsEvent: SqsEvent) {
        try {
            primaryMarketCommandService.adminCancelFixedPrice(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthPrimaryMarketType).MOS_ETH_PRIMARY_MARKET_FixedPriceSold.name")
    fun onFixedPriceSoldEvent(sqsEvent: SqsEvent) {
        try {
            primaryMarketCommandService.fixedPriceBuy(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }
}
