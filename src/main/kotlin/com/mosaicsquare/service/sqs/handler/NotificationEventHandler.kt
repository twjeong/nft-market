package com.mosaicsquare.service.sqs.handler

import com.mosaicsquare.service.notification.NotificationService
import com.mosaicsquare.service.sqs.event.SqsEvent
import com.mosaicsquare.service.sqs.event.message.notification.MessageBody
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class NotificationEventHandler(
    private val publisher: ApplicationEventPublisher,
    private val notificationService: NotificationService,
) {
    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosNotificationType).MOS_NOTIFICATION_Auction1stStart.name")
    fun onAuction1stStartEvent(sqsEvent: SqsEvent) {
        try {
            val body = sqsEvent.getBodyMessage(MessageBody.serializer())
            body.salesInfoIds.forEach { notificationService.auction1stStart(it) }
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosNotificationType).MOS_NOTIFICATION_Auction1stRemind.name")
    fun onAuction1stRemindEvent(sqsEvent: SqsEvent) {
        try {
            val body = sqsEvent.getBodyMessage(MessageBody.serializer())
            body.salesInfoIds.forEach { notificationService.auction1stRemind(it) }
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosNotificationType).MOS_NOTIFICATION_Auction1stEnd.name")
    fun onAuction1stEndEvent(sqsEvent: SqsEvent) {
        try {
            val body = sqsEvent.getBodyMessage(MessageBody.serializer())
            body.salesInfoIds.forEach { notificationService.auction1stEnd(it) }
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosNotificationType).MOS_NOTIFICATION_FixedPrice1stStart.name")
    fun onFixedPrice1stStartEvent(sqsEvent: SqsEvent) {
        try {
            val body = sqsEvent.getBodyMessage(MessageBody.serializer())
            body.salesInfoIds.forEach { notificationService.fixedPrice1stStart(it) }
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosNotificationType).MOS_NOTIFICATION_Offer2ndRemind.name")
    fun onOffer2ndRemindEvent(sqsEvent: SqsEvent) {
        try {
            val body = sqsEvent.getBodyMessage(MessageBody.serializer())
            body.salesInfoIds.forEach { notificationService.offer2ndRemind(it) }
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosNotificationType).MOS_NOTIFICATION_Offer2ndEnd.name")
    fun onOffer2ndEndEvent(sqsEvent: SqsEvent) {
        try {
            val body = sqsEvent.getBodyMessage(MessageBody.serializer())
            body.salesInfoIds.forEach { notificationService.offer2ndEnd(it) }
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosNotificationType).MOS_NOTIFICATION_Offer2ndEndRemind.name")
    fun onOffer2ndEndRemindEvent(sqsEvent: SqsEvent) {
        try {
            val body = sqsEvent.getBodyMessage(MessageBody.serializer())
            body.salesInfoIds.forEach { notificationService.offer2ndEndRemind(it) }
            publisher.publishEvent(sqsEvent.changeToDeleteEvent())
        } catch (ex: Exception) {
            SqsEventException(ex, sqsEvent, publisher)
        }
    }
}
