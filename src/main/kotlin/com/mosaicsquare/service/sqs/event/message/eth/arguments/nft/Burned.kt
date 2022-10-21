package com.mosaicsquare.service.sqs.event.message.eth.arguments.nft

@kotlinx.serialization.Serializable
data class Burned(
    val tokenId: String,
)
