package com.mosaicsquare.service.nft

import com.mosaicsquare.service.domain.sales.royalty.Royalty
import com.mosaicsquare.service.domain.sales.royalty.RoyaltyRepository
import com.mosaicsquare.service.sqs.event.message.eth.MessageBody
import com.mosaicsquare.service.sqs.event.message.eth.arguments.factory.MoneypipeCreated
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull


@ExtendWith(MockKExtension::class)
@DisplayName("FactoryCommandService 유닛 테스트")
class FactoryCommandServiceTest {
    @InjectMockKs
    private lateinit var factoryCommandService: FactoryCommandService

    @MockK
    private lateinit var royaltyRepository: RoyaltyRepository

    @MockK(relaxed = true)
    private lateinit var royalty: Royalty

    @Test
    fun `MoneypipeCreated event를 받으면 contractMoneypipe 함수를 호출하고, royalty 상태를 변경한다`() {
        // given
        every { royaltyRepository.existsById(any()) } returns true
        every { royaltyRepository.findByIdOrNull(any()) } returns royalty

        val body = MessageBody(
            name = "MoneypipeCreated",
            transactionHash = "0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d",
            blockNumber = "7309046",
            timestamp = "2022-07-28T15:36:56",
            from = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
            arguments = MoneypipeCreated(
                id = "1",
                contractAddress = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                creator = "0xf76c9b7012c0a3870801eaaddb93b6352c8893db",
                version = "0.1.0"
            )
        )
        // when
        factoryCommandService.contractMoneypipe(body)

        // then
        verify(exactly = 1) { royalty.royaltyListed(any()) }
    }
}
