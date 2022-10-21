package com.mosaicsquare.service.sqs.handler

import aws.sdk.kotlin.services.sqs.model.Message
import com.mosaicsquare.service.nft.PrimaryMarketCommandService
import com.mosaicsquare.service.sqs.SqsConfig
import com.mosaicsquare.service.sqs.event.SqsEvent
import com.mosaicsquare.service.sqs.event.SqsEventType
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher


@DisplayName("Primary market event 관련 기능")
@ExtendWith(MockKExtension::class)
class EthPrimaryMarketEventHandlerTest {
    @InjectMockKs
    private lateinit var ethPrimaryMarketEventHandler: EthPrimaryMarketEventHandler

    @MockK(relaxed = true)
    private lateinit var primaryMarketCommandService: PrimaryMarketCommandService

    @MockK(relaxed = true)
    private lateinit var publisher: ApplicationEventPublisher

    private val sqsConfig = SqsConfig(
        url = "sqs_url",
        sqsEventType = SqsEventType.MOS_ETH_PRIMARY_MARKET,
    )

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 createAuction 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"AuctionCreated\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"tokenId\": \"1\", " +
                    " \"nftContract\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"seller\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"extensionDuration\": \"10000\", " +
                    " \"reservePrice\": \"2\", " +
                    " \"duration\": \"100000\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethPrimaryMarketEventHandler.onAuctionCreatedEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { primaryMarketCommandService.createAuction(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 placeBid 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"AuctionBidPlaced\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"tokenId\": \"1\", " +
                    " \"nftContract\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"amount\": \"2\", " +
                    " \"endTime\": \"10000\", " +
                    " \"bidder\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"originalAmount\": \"1\", " +
                    " \"originalBidder\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethPrimaryMarketEventHandler.onAuctionBidPlacedEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { primaryMarketCommandService.placeBid(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 finalizeAuction 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"AuctionFinalized\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"tokenId\": \"1\", " +
                    " \"nftContract\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"seller\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"bidder\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"mssFee\" : \"2000000000\", " +
                    " \"creatorFee\" : \"2000000000\", " +
                    " \"value\": \"2\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethPrimaryMarketEventHandler.onAuctionFinalizedEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { primaryMarketCommandService.finalizeAuction(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 createFixedPrice 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"FixedPriceSet\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"tokenId\": \"1\", " +
                    " \"nftContract\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"seller\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"price\": \"2\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethPrimaryMarketEventHandler.onFixedPriceSetEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { primaryMarketCommandService.createFixedPrice(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 adminCancelFixedPrice 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"FixedPriceCanceled\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"tokenId\": \"1\", " +
                    " \"nftContract\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"reason\": \"blabla...\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethPrimaryMarketEventHandler.onFixedPriceCanceledEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { primaryMarketCommandService.adminCancelFixedPrice(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 fixedPriceBuy 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"FixedPriceSold\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"tokenId\": \"1\", " +
                    " \"nftContract\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"seller\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"buyer\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"mssFee\": \"2000000000\" , " +
                    " \"creatorFee\": \"2000000000\" , " +
                    " \"value\": \"2\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethPrimaryMarketEventHandler.onFixedPriceSoldEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { primaryMarketCommandService.fixedPriceBuy(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }
}
