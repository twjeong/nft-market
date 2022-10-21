package com.mosaicsquare.service.sqs.handler

import aws.sdk.kotlin.services.sqs.model.Message
import com.mosaicsquare.service.nft.SecondaryMarketCommandService
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


@DisplayName("Secondary market event 관련 기능")
@ExtendWith(MockKExtension::class)
class EthSecondaryMarketEventHandlerTest {
    @InjectMockKs
    private lateinit var ethSecondaryMarketEventHandler: EthSecondaryMarketEventHandler

    @MockK(relaxed = true)
    private lateinit var secondaryMarketCommandService: SecondaryMarketCommandService

    @MockK(relaxed = true)
    private lateinit var publisher: ApplicationEventPublisher

    private val sqsConfig = SqsConfig(
        url = "sqs_url",
        sqsEventType = SqsEventType.MOS_ETH_SECONDARY_MARKET,
    )

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
                    " \"nftContract\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"seller\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"price\": \"1\", " +
                    " \"duration\": \"10000\", " +
                    " \"endTime\": \"200000\", " +
                    " \"offerPrice\": \"1\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethSecondaryMarketEventHandler.onFixedPriceSetEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { secondaryMarketCommandService.createFixedPrice(sqsEvent.getBodyMessage()) }
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
                    " \"nftContract\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"seller\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"buyer\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"mssFee\": \"2000000000\", " +
                    " \"creatorFee\": \"2000000000\", " +
                    " \"ownerRev\": \"2000000000\", " +
                    " \"value\": \"1\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethSecondaryMarketEventHandler.onFixedPriceSoldEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { secondaryMarketCommandService.fixedPriceBuy(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 makeOffer 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"OfferMade\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"tokenId\": \"1\", " +
                    " \"nftContract\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"buyer\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"amount\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"originalBuyer\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"originalAmount\": \"1\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethSecondaryMarketEventHandler.onOfferMadeEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { secondaryMarketCommandService.makeOffer(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 acceptOffer 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"OfferAccepted\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"tokenId\": \"1\", " +
                    " \"nftContract\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"seller\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"buyer\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"mssFee\" : \"2000000000\", " +
                    " \"creatorFee\": \"2000000000\", " +
                    " \"ownerRev\": \"2000000000\", " +
                    " \"value\": \"1\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethSecondaryMarketEventHandler.onOfferAcceptedEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { secondaryMarketCommandService.acceptOffer(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 cancelFixedPrice 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
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
                    " \"nftContract\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethSecondaryMarketEventHandler.onFixedPriceCanceledEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { secondaryMarketCommandService.cancelFixedPrice(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }
}
