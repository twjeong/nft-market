package com.mosaicsquare.service.nft

import com.mosaicsquare.service.baseException.BaseResponseCode
import com.mosaicsquare.service.domain.member.MemberRepository
import com.mosaicsquare.service.domain.nft.NftRepository
import com.mosaicsquare.service.domain.nft.type.NftStatus
import com.mosaicsquare.service.domain.sales.info.SalesInfo
import com.mosaicsquare.service.domain.sales.info.SalesInfoRepository
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistory
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistoryRepository
import com.mosaicsquare.service.domain.sales.type.CurrencyType
import com.mosaicsquare.service.domain.sales.type.SalesType
import com.mosaicsquare.service.notification.NotificationService
import com.mosaicsquare.service.sqs.event.message.eth.MessageBody
import com.mosaicsquare.service.sqs.event.message.eth.arguments.secondary.*
import org.springframework.stereotype.Service
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
@Transactional
class SecondaryMarketCommandService(
    private val nftRepository: NftRepository,
    private val salesInfoRepository: SalesInfoRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val memberRepository: MemberRepository,
    private val notificationService: NotificationService,
) {

    fun createFixedPrice(body: MessageBody<FixedPriceSet>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract)
            ?: throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)

        val sales = salesInfoRepository.save(
            SalesInfo(
                nft = nft,
                sellerMemberId = nft.ownerId,
                currencyType = CurrencyType.ETH,
                startPrice = Convert.fromWei(args.price, Convert.Unit.ETHER),
                floorPrice = when (args.offerPrice.toBigDecimal()) {
                    BigDecimal.ZERO -> null
                    else -> Convert.fromWei(args.offerPrice, Convert.Unit.ETHER)
                },
                salesType = when (args.offerPrice.toBigDecimal()) {
                    BigDecimal.ZERO -> SalesType.ON_SALE
                    else -> SalesType.ON_OFFER
                },
                startDate = LocalDateTime.now(),
                duration = args.duration.toInt(),
                onSale = true,
            )
        )
        sales.changeEndDate(args.endTime.toLong())

        nft.changeStatus(NftStatus.MEMBER_LISTED)

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

    fun fixedPriceBuy(body: MessageBody<FixedPriceSold>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract) ?: throw Exception(
            BaseResponseCode.NFT_NOT_FOUND.name
        )
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)
            ?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)
        val ethValue = Convert.fromWei(args.value, Convert.Unit.ETHER)

        nft.changeToOwned(
            ownerId = memberRepository.findByWalletAddress(args.buyer)?.id
        )

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

        notificationService.offer2ndSold(
            salesInfoId = sales.id,
            buyerAddress = args.buyer,
            value = Convert.fromWei(args.value, Convert.Unit.ETHER).toString()
        )
    }

    fun makeOffer(body: MessageBody<OfferMade>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract) ?: throw Exception(
            BaseResponseCode.NFT_NOT_FOUND.name
        )
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)
            ?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)

        transactionHistoryRepository.save(
            TransactionHistory(
                nft = nft,
                salesInfo = sales,
                transactionHash = body.transactionHash,
                eventName = body.name,
                price = Convert.fromWei(args.amount, Convert.Unit.ETHER),
                address = body.contractAddress,
                senderAddress = body.from
            )
        )

        notificationService.offer2ndOfferMade(
            salesInfoId = sales.id,
            offerAddress = body.from,
            value = Convert.fromWei(args.amount, Convert.Unit.ETHER).toString()
        )
    }

    fun acceptOffer(body: MessageBody<OfferAccepted>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract) ?: throw Exception(
            BaseResponseCode.NFT_NOT_FOUND.name
        )
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)
            ?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)
        val ethValue = Convert.fromWei(args.value, Convert.Unit.ETHER)

        nft.changeToOwned(
            ownerId = memberRepository.findByWalletAddress(args.buyer)?.id
        )

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

        notificationService.offer2ndOfferAccepted(
            salesInfoId = sales.id,
            acceptedOfferAddress = args.buyer,
            value = ethValue.toString()
        )
    }

    fun cancelFixedPrice(body: MessageBody<FixedPriceCanceled>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.tokenId.toLong(), args.nftContract) ?: throw Exception(
            BaseResponseCode.NFT_NOT_FOUND.name
        )
        val sales = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nft.id)
            ?: throw Exception(BaseResponseCode.SALESINFO_NOT_FOUND.name)

        salesInfoRepository.delete(sales)

        nft.changeToOwned()

        transactionHistoryRepository.save(
            TransactionHistory(
                nft = nft,
                salesInfo = null,
                transactionHash = body.transactionHash,
                eventName = body.name,
                price = null,
                address = body.contractAddress,
                senderAddress = body.from
            )
        )

        notificationService.offer2ndCancel(salesInfoId = sales.id)
    }
}