package com.mosaicsquare.service.sqs.event.message.eth.arguments.factory

@kotlinx.serialization.Serializable
data class UpdatedMoneypipe(
    val implementation: String,
    val version: String
)
