package com.mosaicsquare.service.sqs.event.message.eth.arguments.nft

@kotlinx.serialization.Serializable
data class Transfered(
    val tokenId: String,
)
