package com.mosaicsquare.service.sqs.event.message.eth.arguments.nft

@kotlinx.serialization.Serializable
data class Minted(
    val tokenId: String,
    val creator: String,
    val ipfsPath: String,
) {
    fun getTokenId(): Long {
        return this.tokenId.toLong()
    }
}
