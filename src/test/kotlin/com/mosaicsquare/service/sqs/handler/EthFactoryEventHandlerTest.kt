package com.mosaicsquare.service.sqs.handler

import aws.sdk.kotlin.services.sqs.model.Message
import com.mosaicsquare.service.nft.FactoryCommandService
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


@DisplayName("Factory moneypipe event 관련 기능")
@ExtendWith(MockKExtension::class)
class EthFactoryEventHandlerTest {
    @InjectMockKs
    private lateinit var ethFactoryEventHandler: EthFactoryEventHandler

    @MockK(relaxed = true)
    private lateinit var factoryCommandService: FactoryCommandService

    @MockK(relaxed = true)
    private lateinit var publisher: ApplicationEventPublisher

    private val sqsConfig = SqsConfig(
        url = "sqs_url",
        sqsEventType = SqsEventType.MOS_ETH_FACTORY,
    )

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 factory contractMoneypipe 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"Minted\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"id\": \"1\", " +
                    " \"creator\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"version\": \"1\", " +
                    " \"contractAddress\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethFactoryEventHandler.onMoneypipeCreatedEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { factoryCommandService.contractMoneypipe(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }
}
