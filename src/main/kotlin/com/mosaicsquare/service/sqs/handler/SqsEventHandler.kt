package com.mosaicsquare.service.sqs.handler

import aws.sdk.kotlin.services.sqs.SqsClient
import aws.sdk.kotlin.services.sqs.model.ChangeMessageVisibilityRequest
import aws.sdk.kotlin.services.sqs.model.DeleteMessageRequest
import com.mosaicsquare.service.sqs.event.SqsEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SqsEventHandler(private val sqs: SqsClient) {
    private val logger = LogManager.getLogger(SqsEventHandler::class.java)

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.EventHandlerType).DeleteMessage.name")
    private fun deleteMessage(sqsEvent: SqsEvent) = GlobalScope.launch {
        logger.info("SQS Event deleteMessage:: {}", sqsEvent.receiptHandle)
        sqs.deleteMessage(DeleteMessageRequest {
            queueUrl = sqsEvent.url
            receiptHandle = sqsEvent.receiptHandle
        })
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.EventHandlerType).ChangeVisibilityMessage.name")
    private fun changeVisibility(sqsEvent: SqsEvent) = GlobalScope.launch {
        logger.info("SQS Event changeVisibility:: {}", sqsEvent.receiptHandle)
        sqs.changeMessageVisibility(ChangeMessageVisibilityRequest {
            queueUrl = sqsEvent.url
            receiptHandle = sqsEvent.receiptHandle
            visibilityTimeout = 10
        })
    }

    @EventListener(condition = "#sqsEvent.name == T(com.mosaicsquare.service.sqs.event.SqsEventType.MosEthDeadLetter).DeadLetter.name")
    private fun handleDeadLetter(sqsEvent: SqsEvent) = GlobalScope.launch {
        logger.info("SQS Event handleDeadLetter:: {}", sqsEvent.receiptHandle)
        sqs.deleteMessage(DeleteMessageRequest {
            queueUrl = sqsEvent.url
            receiptHandle = sqsEvent.receiptHandle
        })
    }
}
