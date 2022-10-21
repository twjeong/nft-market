package com.mosaicsquare.service.sqs.event.message.eth.arguments.primary

@kotlinx.serialization.Serializable
data class FixedPriceCanceled(
    val tokenId: String,
    val nftContract: String,
    val reason: String,
)
