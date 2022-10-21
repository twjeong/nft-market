package com.mosaicsquare.service.sqs.event.message.eth.arguments.factory

@kotlinx.serialization.Serializable
data class MoneypipeCreated(
    val id: String,
    val contractAddress: String,
    val creator: String,
    val version: String
)
