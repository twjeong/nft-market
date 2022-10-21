package com.mosaicsquare.service.sqs.handler

import com.mosaicsquare.service.nft.NftCommandService
import com.mosaicsquare.service.sqs.event.SqsEvent
import com.mosaicsquare.service.sqs.event.message.eth.MessageBody
import com.mosaicsquare.service.sqs.event.message.eth.arguments.nft.Burned
import com.mosaicsquare.service.sqs.event.message.eth.arguments.nft.Transfered
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class EthNftEventHandler(
    private val publisher: ApplicationEventPublisher,
    private val nftCommandService: NftCommandService
) {
    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthNftType).MOS_ETH_NFT_Minted.name")
    fun onMintedEvent(sqsEvent: SqsEvent) {
        try {
            nftCommandService.mintNft(sqsEvent.getBodyMessage())
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthNftType).MOS_ETH_NFT_Burned.name")
    fun onBurnedEvent(sqsEvent: SqsEvent) {
        try {
            val body = sqsEvent.getBodyMessage<MessageBody<Burned>>()
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthNftType).MOS_ETH_NFT_Transfered.name")
    fun onTransferedEvent(sqsEvent: SqsEvent) {
        try {
            val body = sqsEvent.getBodyMessage<Transfered>()
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }
}
