package com.mosaicsquare.service.sqs.event

import com.mosaicsquare.service.sqs.SqsConfig
import com.mosaicsquare.service.sqs.SqsConsumer
import com.mosaicsquare.service.sqs.event.message.eth.MessageBody
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.apache.logging.log4j.LogManager
import org.json.JSONObject

data class SqsEvent(private val sqsConfig: SqsConfig) {

    companion object {
        const val KEY = "name"
    }

    private val logger = LogManager.getLogger(SqsConsumer::class.java)

    var name: String = ""
        private set
    var url: String = ""
        private set
    var receiptHandle: String = ""
        private set
    var body: String = ""
        private set

    constructor(sqsConfig: SqsConfig, message: aws.sdk.kotlin.services.sqs.model.Message) : this(sqsConfig) {
        this.url = sqsConfig.url
        this.receiptHandle = message.receiptHandle!!
        try {
            this.body = message.body ?: throw IllegalAccessException("Body message is null")
            this.name = getEventName(message)
        } catch (e: Exception) {
            this.url = sqsConfig.url
            this.name = SqsEventType.MosEthDeadLetter.DeadLetter.name
            logger.error("Event message format error:: ${e}, ${message}")
        }
    }

    fun changeToDeleteEvent() {
        this.name = SqsEventType.EventHandlerType.DeleteMessage.name
    }

    fun changeToVisibilityEvent() {
        this.name = SqsEventType.EventHandlerType.ChangeVisibilityMessage.name
    }

    @OptIn(InternalSerializationApi::class)
    inline fun <reified T : Any> getBodyMessage(): MessageBody<T> {
        return Json {
            isLenient = true
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
        }.decodeFromString(MessageBody.serializer(T::class.serializer()), this.body)
    }

    inline fun <reified T : Any> getBodyMessage(dataSerializer: KSerializer<T>): T {
        return Json {
            isLenient = true
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
        }.decodeFromString(dataSerializer, this.body)
    }

    @Throws(Exception::class)
    private fun getEventName(message: aws.sdk.kotlin.services.sqs.model.Message): String {
        return this.sqsConfig.sqsEventType.getEvent(
            sqsConfig.sqsEventType.name + "_" + JSONObject(message.body).get(KEY).toString())
    }
}
