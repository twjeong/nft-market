package com.mosaicsquare.service.nft

import com.mosaicsquare.service.common.S3Service
import com.mosaicsquare.service.domain.account.AccountRepository
import com.mosaicsquare.service.domain.member.MemberRepository
import com.mosaicsquare.service.domain.nft.Nft
import com.mosaicsquare.service.domain.nft.NftRepository
import com.mosaicsquare.service.domain.sales.info.SalesInfoRepository
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistory
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistoryRepository
import com.mosaicsquare.service.domain.tag.TagRepository
import com.mosaicsquare.service.nft.dto.NftMapper
import com.mosaicsquare.service.nft.dto.SalesInfoMapper
import com.mosaicsquare.service.notification.NotificationService
import com.mosaicsquare.service.sqs.event.message.eth.MessageBody
import com.mosaicsquare.service.sqs.event.message.eth.arguments.nft.Minted
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
@DisplayName("NftCommandService 유닛 테스트")
class NftCommandServiceTest {
    @InjectMockKs
    private lateinit var nftCommandService: NftCommandService

    @MockK
    private lateinit var nftRepository: NftRepository

    @MockK
    private lateinit var nftMapper: NftMapper

    @MockK
    private lateinit var tagRepository: TagRepository

    @MockK
    private lateinit var salesInfoMapper: SalesInfoMapper

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

    @MockK
    private lateinit var notificationService: NotificationService

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 nft mint 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        every { nftRepository.findTopByTokenIdAndAddress(any(), any()) } returns Nft()
        every { transactionHistoryRepository.save(any()) } returns mockk()
        val body = MessageBody<Minted>(
            name = "Minted",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = Minted(
                tokenId = "1",
                creator = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                ipfsPath = "ipfs://url"
            )
        )
        // when
        nftCommandService.mintNft(body)
        // then
        verify { transactionHistoryRepository.save(withArg { TransactionHistory() }) }
    }
}
