package com.mosaicsquare.service.sqs.event.message.eth.arguments.primary

@kotlinx.serialization.Serializable
data class AuctionFinalized(
    val tokenId: String,
    val nftContract: String,
    val seller: String,
    val bidder: String,
    val value: String,
    val mssFee: String,
    val creatorFee: String
)
