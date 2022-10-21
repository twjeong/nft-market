package com.mosaicsquare.service.sqs.event.message.eth

import com.mosaicsquare.service.domain.nft.Nft
import com.mosaicsquare.service.domain.sales.info.SalesInfo
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistory
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@kotlinx.serialization.Serializable
data class MessageBody<T>(
    val name: String,
    val transactionHash: String,
    val blockNumber: String,
    val timestamp: String,
    val from: String,
    val contractAddress: String,
    val arguments: T
) {
    fun getTransactionHistory(nft: Nft): TransactionHistory {
        return TransactionHistory(
            nft = nft,
            transactionHash = this.transactionHash,
            senderAddress = this.from,
            eventName = this.name,
            address = this.contractAddress,
            createdDate = LocalDateTime.parse(this.timestamp, DateTimeFormatter.ISO_DATE_TIME)
        )
    }
}
