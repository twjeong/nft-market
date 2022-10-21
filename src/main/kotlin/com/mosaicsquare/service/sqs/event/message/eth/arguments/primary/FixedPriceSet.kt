package com.mosaicsquare.service.sqs.event.message.eth.arguments.primary

@kotlinx.serialization.Serializable
data class FixedPriceSet(
    val tokenId: String,
    val nftContract: String,
    val seller: String,
    val price: String,
)
