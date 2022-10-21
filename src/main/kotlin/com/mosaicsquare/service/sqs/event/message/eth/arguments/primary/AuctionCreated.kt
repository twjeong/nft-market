package com.mosaicsquare.service.sqs.event.message.eth.arguments.primary

@kotlinx.serialization.Serializable
data class AuctionCreated(
    val tokenId: String,
    val nftContract: String,
    val seller: String,
    val duration: String,
    val extensionDuration: String,
    val reservePrice: String,
)
