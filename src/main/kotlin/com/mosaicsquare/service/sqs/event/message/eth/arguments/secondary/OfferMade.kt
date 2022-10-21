package com.mosaicsquare.service.sqs.event.message.eth.arguments.secondary

@kotlinx.serialization.Serializable
data class OfferMade(
    val tokenId: String,
    val nftContract: String,
    val buyer: String,
    val amount: String,
    val originalBuyer: String,
    val originalAmount: String
)