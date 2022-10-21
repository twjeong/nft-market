package com.mosaicsquare.service.sqs.event.message.eth.arguments.secondary

@kotlinx.serialization.Serializable
data class OfferAccepted(
    val tokenId: String,
    val nftContract: String,
    val seller: String,
    val buyer: String,
    val value: String,
    val mssFee: String,
    val creatorFee: String,
    val ownerRev: String
)