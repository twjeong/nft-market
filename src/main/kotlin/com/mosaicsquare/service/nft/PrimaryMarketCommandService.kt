package com.mosaicsquare.service.nft

import com.mosaicsquare.service.baseException.BaseResponseCode
import com.mosaicsquare.service.domain.account.AccountRepository
import com.mosaicsquare.service.domain.member.MemberRepository
import com.mosaicsquare.service.domain.nft.NftRepository
import com.mosaicsquare.service.domain.nft.type.NftStatus
import com.mosaicsquare.service.domain.sales.info.SalesInfoRepository
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistory
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistoryRepository
import com.mosaicsquare.service.notification.NotificationService
import com.mosaicsquare.service.sqs.event.message.eth.MessageBody
import com.mosaicsquare.service.sqs.event.message.eth.arguments.primary.*
import org.springframework.stereotype.Service
import org.web3j.utils.Convert
import javax.transaction.Transactional

@Service
@Transactional
class PrimaryMarketCommandService(
    private val nftRepository: NftRepository,
    private val salesInfoRepository: SalesInfoRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val memberRepository: MemberRepository,
    private val accountRepository: AccountRepository,
    private val notificationService: NotificationService
) {

    fun createAuction(body: MessageBody<AuctionCreated>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract)?: throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)

        nft.changeToMarketListed()

        sales.startDate?.let {
            sales.changeEndDate(it, args.duration.toLong())
        }

        transactionHistoryRepository.save(
            TransactionHistory(
                nft = nft,
                salesInfo = sales,
                transactionHash = body.transactionHash,
                eventName = body.name,
                price = Convert.fromWei(args.reservePrice, Convert.Unit.ETHER),
                address = body.contractAddress,
                senderAddress = body.from
            )
        )
    }

    fun placeBid(body: MessageBody<AuctionBidPlaced>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract)?: throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)
        val ethValue = Convert.fromWei(args.amount, Convert.Unit.ETHER)

        sales.changeEndDate(args.endTime.toLong())

        transactionHistoryRepository.save(
            TransactionHistory(
                nft = nft,
                salesInfo = sales,
                transactionHash = body.transactionHash,
                eventName = body.name,
                price = ethValue,
                address = body.contractAddress,
                senderAddress = body.from
            )
        )

        notificationService.auction1stBid(
            salesInfoId = sales.id,
            bidderAddres = args.bidder,
            outBidderAddress = args.originalBidder,
            value = ethValue.toString()
        )
    }

    fun adminCancelAuction(body: MessageBody<AuctionCanceled>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract)?:throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)

        nft.changeStatus(NftStatus.MINTED)

        transactionHistoryRepository.save(
            TransactionHistory(
                nft = nft,
                salesInfo = sales,
                transactionHash = body.transactionHash,
                eventName = body.name,
                price = null,
                address = body.contractAddress,
                senderAddress = body.from
            )
        )

        notificationService.auction1stCancel(salesInfoId = sales.id)
    }

    fun finalizeAuction(body: MessageBody<AuctionFinalized>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract)?: throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)
        val ethValue = Convert.fromWei(args.value, Convert.Unit.ETHER)

        if (accountRepository.findByWalletAddress(args.bidder) != null) {
            nft.changeToOwned(
                ownerId = memberRepository.findByWalletAddress(args.bidder)?.id
            )
        } else {
            nft.changeToLost()
        }

        sales.changeOnSale(false, ethValue)

        transactionHistoryRepository.save(
            TransactionHistory(
                nft = nft,
                salesInfo = sales,
                transactionHash = body.transactionHash,
                eventName = body.name,
                price = ethValue,
                address = body.contractAddress,
                senderAddress = body.from
            )
        )

        notificationService.auction1stTranslate(
            salesInfoId = sales.id,
            bidSuccesserAddress = args.bidder,
            value = ethValue.toString()
        )
    }

    fun createFixedPrice(body: MessageBody<FixedPriceSet>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract)?: throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)

        nft.changeStatus(NftStatus.MARKET_LISTED)

        transactionHistoryRepository.save(
            TransactionHistory(
                nft = nft,
                salesInfo = sales,
                transactionHash = body.transactionHash,
                eventName = body.name,
                price = Convert.fromWei(args.price, Convert.Unit.ETHER),
                address = body.contractAddress,
                senderAddress = body.from
            )
        )
    }

    fun adminCancelFixedPrice(body: MessageBody<FixedPriceCanceled>) {
        val nft = nftRepository.findTopByTokenIdAndAddress(body.arguments.tokenId.toLong(), body.arguments.nftContract)?: throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)

        nft.changeStatus(NftStatus.MINTED)

        transactionHistoryRepository.save(
            TransactionHistory(
                nft = nft,
                salesInfo = sales,
                transactionHash = body.transactionHash,
                eventName = body.name,
                price = null,
                address = body.contractAddress,
                senderAddress = body.from
            )
        )

        notificationService.fixedPrice1stCancel(salesInfoId = sales.id)
    }

    fun fixedPriceBuy(body: MessageBody<FixedPriceSold>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(body.arguments.tokenId.toLong(), args.nftContract)?: throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)
        val ethValue = Convert.fromWei(args.value, Convert.Unit.ETHER)

        if (accountRepository.findByWalletAddress(args.buyer) != null) {
            nft.changeToOwned(
                ownerId = memberRepository.findByWalletAddress(args.buyer)?.id
            )
        } else {
            nft.changeToLost()
        }

        sales.changeOnSale(false, ethValue)

        transactionHistoryRepository.save(
            TransactionHistory(
                nft = nft,
                salesInfo = sales,
                transactionHash = body.transactionHash,
                eventName = body.name,
                price = ethValue,
                address = body.contractAddress,
                senderAddress = body.from
            )
        )

        notificationService.fixedPrice1stSold(
            salesInfoId = sales.id,
            buyerAddress = args.buyer,
            value = ethValue.toString()
        )
    }
}
