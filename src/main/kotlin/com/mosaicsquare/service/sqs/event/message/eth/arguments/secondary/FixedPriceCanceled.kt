package com.mosaicsquare.service.sqs.event.message.eth.arguments.secondary

@kotlinx.serialization.Serializable
data class FixedPriceCanceled(
    val tokenId: String,
    val nftContract: String
)
