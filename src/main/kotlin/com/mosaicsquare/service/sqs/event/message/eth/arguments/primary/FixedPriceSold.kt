package com.mosaicsquare.service.sqs.event.message.eth.arguments.primary

@kotlinx.serialization.Serializable
data class FixedPriceSold(
    val tokenId: String,
    val nftContract: String,
    val seller: String,
    val buyer: String,
    val value: String,
    val mssFee: String,
    val creatorFee: String
)
