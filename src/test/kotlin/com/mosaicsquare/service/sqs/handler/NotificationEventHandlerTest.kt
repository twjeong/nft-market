package com.mosaicsquare.service.sqs.handler

//import org.mockito.kotlin.verify
import aws.sdk.kotlin.services.sqs.model.Message
import com.mosaicsquare.service.notification.NotificationService
import com.mosaicsquare.service.sqs.SqsConfig
import com.mosaicsquare.service.sqs.event.SqsEvent
import com.mosaicsquare.service.sqs.event.SqsEventType
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher

@DisplayName("Notification event 관련 기능")
@ExtendWith(MockKExtension::class)
class NotificationEventHandlerTest {
    @InjectMockKs
    private lateinit var notificationEventHandler: NotificationEventHandler

    @MockK(relaxed = true)
    private lateinit var notificationService: NotificationService

    @MockK(relaxed = true)
    private lateinit var publisher: ApplicationEventPublisher

    @Test
    fun `salesInfoIds list 사이즈가 2개이면 auction1stStart 메소드를 두번 호출 한다`() {
        // given
        val sqsConfig = SqsConfig(
            url = "sqs_url",
            sqsEventType = SqsEventType.MOS_NOTIFICATION,
        )
        val message = Message.invoke {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"Auction1stStart\", " +
                    " \"salesInfoIds\": [1, 2] " +
                    "}"
        }

        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        notificationEventHandler.onAuction1stStartEvent(sqsEvent)
        // then
        Assertions.assertAll({
            verify(exactly = 1) { notificationService.auction1stStart(1L) }
            verify(exactly = 1) { notificationService.auction1stStart(2L) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }
}
