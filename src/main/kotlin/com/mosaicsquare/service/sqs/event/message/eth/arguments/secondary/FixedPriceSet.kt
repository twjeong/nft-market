package com.mosaicsquare.service.sqs.event.message.eth.arguments.secondary

@kotlinx.serialization.Serializable
data class FixedPriceSet(
    val tokenId: String,
    val nftContract: String,
    val seller: String,
    val price: String,
    val duration: String,
    val endTime: String,
    val offerPrice: String
)