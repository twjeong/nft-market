package com.mosaicsquare.service.sqs.event.message.eth.arguments.primary

@kotlinx.serialization.Serializable
data class AuctionBidPlaced(
    val tokenId: String,
    val nftContract: String,
    val bidder: String,
    val amount: String,
    val originalBidder: String,
    val originalAmount: String,
    val endTime: String,
)
