package com.mosaicsquare.service.sqs

import aws.sdk.kotlin.services.sqs.SqsClient
import aws.sdk.kotlin.services.sqs.model.Message
import aws.sdk.kotlin.services.sqs.model.ReceiveMessageRequest
import com.mosaicsquare.service.sqs.event.SqsEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.apache.logging.log4j.LogManager
import org.springframework.context.ApplicationEventPublisher
import java.lang.Thread.currentThread

class SqsConsumer(
    private val sqs: SqsClient,
    private val sqsConfig: SqsConfig,
    private val publisher: ApplicationEventPublisher,
) {
    private val channel: Channel<Message> = Channel()
    private val logger = LogManager.getLogger(SqsConsumer::class.java)

    fun start() = GlobalScope.launch {
        repeat(sqsConfig.workers) {
            launchWorker()
        }
        launchMsgReceiver()
    }

    private suspend fun CoroutineScope.repeatUntilCancelled(suspendFunc: suspend () -> Unit) {
        while (isActive) {
            try {
                suspendFunc()
                yield()
            } catch (ex: CancellationException) {
                logger.error("coroutine on ${currentThread().name} cancelled")
            } catch (ex: Exception) {
                logger.error("${currentThread().name} failed with {$ex}. Retrying...")
            }
        }
    }

    private fun CoroutineScope.launchMsgReceiver() = launch {
        repeatUntilCancelled {
            val receiveMessageRequest = ReceiveMessageRequest {
                queueUrl = sqsConfig.url
                maxNumberOfMessages = sqsConfig.maxNumberMessage
                waitTimeSeconds = sqsConfig.waitTime
            }
            sqs.receiveMessage(receiveMessageRequest).messages?.forEach {
                channel.send(it)
            }
        }
    }

    private fun CoroutineScope.launchWorker() = launch {
        repeatUntilCancelled {
            for (message in channel) {
                logger.info("Sqs event:: ${message.body.toString().replace("\n", " ")}")
                publisher.publishEvent(SqsEvent(sqsConfig, message))
            }
        }
    }
}
