package com.mosaicsquare.service.sqs.event.message.notification

@kotlinx.serialization.Serializable
data class MessageBody(
    val name: String,
    val salesInfoIds: List<Long>,
)
