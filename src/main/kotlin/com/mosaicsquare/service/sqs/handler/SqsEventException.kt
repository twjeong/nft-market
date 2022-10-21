package com.mosaicsquare.service.sqs.handler

import com.mosaicsquare.service.sqs.event.SqsEvent
import org.apache.logging.log4j.LogManager
import org.springframework.context.ApplicationEventPublisher

class SqsEventException(
    var exception: Exception,
    val sqsEvent: SqsEvent,
    val publisher: ApplicationEventPublisher,
) {
    private val logger = LogManager.getLogger(SqsEventException::class.java)

    companion object {
        val MESSAGE_FORMAT_ERROR = "MissingFieldException"
    }

    init {
        logger.error("SQS Event error:: ${exception.message}, ${exception} ")

        sqsEvent.changeToVisibilityEvent()
        if (exception.javaClass.name.contains(MESSAGE_FORMAT_ERROR)) {
            sqsEvent.changeToDeleteEvent()
        }
        publisher.publishEvent(sqsEvent)
    }
}
