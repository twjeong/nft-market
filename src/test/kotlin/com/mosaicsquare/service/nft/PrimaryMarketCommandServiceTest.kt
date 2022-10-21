package com.mosaicsquare.service.nft

import com.mosaicsquare.service.domain.account.Account
import com.mosaicsquare.service.domain.account.AccountRepository
import com.mosaicsquare.service.domain.member.Member
import com.mosaicsquare.service.domain.member.MemberRepository
import com.mosaicsquare.service.domain.nft.Nft
import com.mosaicsquare.service.domain.nft.NftRepository
import com.mosaicsquare.service.domain.sales.info.SalesInfo
import com.mosaicsquare.service.domain.sales.info.SalesInfoRepository
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistory
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistoryRepository
import com.mosaicsquare.service.notification.NotificationService
import com.mosaicsquare.service.sqs.event.message.eth.MessageBody
import com.mosaicsquare.service.sqs.event.message.eth.arguments.primary.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.anyOrNull
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import org.junit.jupiter.api.Assertions

@ExtendWith(MockKExtension::class)
@DisplayName("PrimaryMarketCommandService 유닛 테스트")
class PrimaryMarketCommandServiceTest {
    @InjectMockKs
    private lateinit var primaryMarketCommandService: PrimaryMarketCommandService

    @MockK
    private lateinit var nftRepository: NftRepository

    @MockK
    private lateinit var salesInfoRepository: SalesInfoRepository

    @MockK
    private lateinit var transactionHistoryRepository: TransactionHistoryRepository

    @MockK
    private lateinit var memberRepository: MemberRepository

    @MockK
    private lateinit var accountRepository: AccountRepository

    @MockK(relaxed = true)
    private lateinit var notificationService: NotificationService

    @Test
    fun `1차마켓 AuctionCreated event를 받으면 createAuction 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody(
            name = "AuctionCreated",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = AuctionCreated(
                tokenId = "1",
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                seller = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                duration = "10000",
                extensionDuration = "20000",
                reservePrice = "2"
            )
        )
        // when
        primaryMarketCommandService.createAuction(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
    }

    @Test
    fun `1차마켓 AuctionBidPlaced event를 받으면 placeBid 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody(
            name = "AuctionBidPlaced",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = AuctionBidPlaced(
                tokenId = "1",
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                bidder = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                amount = "2",
                endTime = "10000",
                originalBidder = "20000",
                originalAmount = "1"
            )
        )
        // when
        primaryMarketCommandService.placeBid(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
        verify(exactly = 1) { notificationService.auction1stBid(any(), any(), any(), any()) }
    }

    @Test
    fun `1차마켓 AuctionCanceled event를 받으면 adminCancelAuction 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody(
            name = "AuctionCanceled",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = AuctionCanceled(
                tokenId = "1",
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                reason = "blabla..."
            )
        )
        // when
        primaryMarketCommandService.adminCancelAuction(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
        verify(exactly = 1) { notificationService.auction1stCancel(any()) }
    }

    @Test
    fun `1차마켓 AuctionFinalized event를 받으면 finalizeAuction 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        val account = Account("0xf76c9b7012c0a3870801eaaddb93b6352c8893db")
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { accountRepository.findByWalletAddress(any()) } returns account
        every { memberRepository.findByWalletAddress(any()) } returns Member()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody(
            name = "AuctionFinalized",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = AuctionFinalized(
                tokenId = "1",
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                seller = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                bidder = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                value = "2",
                mssFee = "10000000000000",
                creatorFee = "10000000000000"
            )
        )
        // when
        primaryMarketCommandService.finalizeAuction(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
        verify(exactly = 1) { notificationService.auction1stTranslate(any(), any(), any()) }
    }

    @Test
    fun `1차마켓 FixedPriceSet event를 받으면 createFixedPrice 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody(
            name = "FixedPriceSet",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = FixedPriceSet(
                tokenId = "1",
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                seller = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                price = "2"
            )
        )
        // when
        primaryMarketCommandService.createFixedPrice(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
    }

    @Test
    fun `1차마켓 FixedPriceCanceled event를 받으면 adminCancelFixedPrice 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody(
            name = "FixedPriceCanceled",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = FixedPriceCanceled(
                tokenId = "1",
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                reason = "blabla..."
            )
        )
        // when
        primaryMarketCommandService.adminCancelFixedPrice(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
        verify(exactly = 1) { notificationService.fixedPrice1stCancel(any()) }
    }

    @Test
    fun `1차마켓 FixedPriceSold event를 받으면 fixedPriceBuy 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        val account = Account("0xf76c9b7012c0a3870801eaaddb93b6352c8893db")
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { accountRepository.findByWalletAddress(any()) } returns account
        every { memberRepository.findByWalletAddress(any()) } returns Member()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody(
            name = "FixedPriceSold",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = FixedPriceSold(
                tokenId = "1",
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                seller = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                buyer = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                value = "2",
                mssFee = "10000000000000",
                creatorFee = "10000000000000"
            )
        )
        // when
        primaryMarketCommandService.fixedPriceBuy(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
        verify(exactly = 1) { notificationService.fixedPrice1stSold(any(), any(), any()) }
    }

    @Test
    fun `SalesInfo changeEndDate 테스트`() {

        // given
        val salesInfo = SalesInfo()
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val duration = "86400".toLong()

        // when
        salesInfo.changeEndDate(now, duration)

        // then
        Assertions.assertTrue(now.plusDays(1).equals(salesInfo.endDate))
    }
}
