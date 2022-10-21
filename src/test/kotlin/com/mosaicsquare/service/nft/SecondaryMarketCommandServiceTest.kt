package com.mosaicsquare.service.nft

import com.mosaicsquare.service.common.S3Service
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
import com.mosaicsquare.service.sqs.event.message.eth.arguments.secondary.*
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doNothing


@ExtendWith(MockKExtension::class)
@DisplayName("SecondaryMarketCommandService 유닛 테스트")
class SecondaryMarketCommandServiceTest {
    @InjectMockKs
    private lateinit var secondaryMarketCommandService: SecondaryMarketCommandService

    @MockK
    private lateinit var nftRepository: NftRepository

    @MockK
    private lateinit var salesInfoRepository: SalesInfoRepository

    @MockK
    private lateinit var transactionHistoryRepository: TransactionHistoryRepository

    @MockK
    private lateinit var s3Service: S3Service

    @MockK
    private lateinit var memberRepository: MemberRepository

    @MockK
    private lateinit var accountRepository: AccountRepository

    @MockK(relaxed = true)
    private lateinit var notificationService: NotificationService

    @Test
    fun `2차마켓 FixedPriceSet event를 받으면 createFixedPrice 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.save(any()) } returns SalesInfo()
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
                price = "2",
                duration = "100000",
                endTime = "100000",
                offerPrice = "2",
                seller = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db"
            )
        )
        // when
        secondaryMarketCommandService.createFixedPrice(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
    }

    @Test
    fun `2차마켓 FixedPriceSold event를 받으면 fixedPriceBuy 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
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
                mssFee = "20000000000",
                creatorFee = "2000000000",
                ownerRev = "1000000"
            )
        )
        // when
        secondaryMarketCommandService.fixedPriceBuy(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
        verify(exactly = 1) { notificationService.offer2ndSold(any(), any(), any()) }
    }

    @Test
    fun `2차마켓 OfferMade event를 받으면 makeOffer 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { memberRepository.findByWalletAddress(any()) } returns Member()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody(
            name = "OfferMade",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = OfferMade(
                tokenId = "1",
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                amount = "3",
                originalBuyer = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                originalAmount = "2",
                buyer = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db"
            )
        )
        // when
        secondaryMarketCommandService.makeOffer(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
        verify(exactly = 1) { notificationService.offer2ndOfferMade(any(), any(), any()) }
    }

    @Test
    fun `2차마켓 OfferAccepted event를 받으면 acceptOffer 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { memberRepository.findByWalletAddress(any()) } returns Member()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody(
            name = "OfferAccepted",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = OfferAccepted(
                tokenId = "1",
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                seller = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                buyer = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                value = "2",
                mssFee = "20000000000",
                creatorFee = "2000000000",
                ownerRev = "1000000"
            )
        )
        // when
        secondaryMarketCommandService.acceptOffer(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
        verify(exactly = 1) { notificationService.offer2ndOfferAccepted(any(), any(), any()) }
    }

    @Test
    fun `2차마켓 FixedPriceCanceled event를 받으면 cancelFixedPrice 함수를 호출하고, TransactionHistory를 저장한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(anyOrNull()) } returns SalesInfo()
        every { salesInfoRepository.delete(any()) } returns Unit
        every { memberRepository.findByWalletAddress(any()) } returns Member()
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
                nftContract = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db"
            )
        )
        // when
        secondaryMarketCommandService.cancelFixedPrice(body)

        // then
        verify(exactly = 1) { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
        verify(exactly = 1) { notificationService.offer2ndCancel(any()) }
    }
}
