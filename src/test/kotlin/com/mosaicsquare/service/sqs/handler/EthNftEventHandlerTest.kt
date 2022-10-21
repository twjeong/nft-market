package com.mosaicsquare.service.sqs.handler

import aws.sdk.kotlin.services.sqs.model.Message
import com.mosaicsquare.service.domain.nft.NftRepository
import com.mosaicsquare.service.nft.NftCommandService
import com.mosaicsquare.service.sqs.SqsConfig
import com.mosaicsquare.service.sqs.event.SqsEvent
import com.mosaicsquare.service.sqs.event.SqsEventType
import com.mosaicsquare.service.sqs.event.message.eth.arguments.nft.Minted
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher


@DisplayName("NFT Mint event 관련 기능")
@ExtendWith(MockKExtension::class)
class EthNftEventHandlerTest {
    @InjectMockKs
    private lateinit var ethNftEventHandler: EthNftEventHandler

    @MockK(relaxed = true)
    private lateinit var nftCommandService: NftCommandService

    @MockK(relaxed = true)
    private lateinit var publisher: ApplicationEventPublisher

    @MockK
    private lateinit var nftRepository: NftRepository

    private val sqsConfig = SqsConfig(
        url = "sqs_url",
        sqsEventType = SqsEventType.MOS_ETH_NFT,
    )

    @Test
    fun `Event를 받으면 body에서 transactionHash 키값을 가지고 와서 nft mint 함수를 호출하고, sqs 삭제 이벤트를 발행한다`() {
        // given
        val message = Message {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"Minted\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"tokenId\": \"1\", " +
                    " \"nftContract\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"creator\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"ipfsPath\": \"ipfs://url\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethNftEventHandler.onMintedEvent(sqsEvent)
        // then
        assertAll({
            verify(exactly = 1) { nftCommandService.mintNft(sqsEvent.getBodyMessage()) }
            verify(exactly = 1) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

    @Test
    fun `Event body에서 transactionHash 키가 존재하지 않으면 exception처리된다`() {
        // given
        val message = Message.invoke {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"Minted\", " +
                    " \"transactionHash\" : \"0xcb59d98ac475d0e0a4ae6238839857de5b91c902fda5f95ba918ee3f1e228e5d\", " +
                    " \"blockNumber\": \"7309046\", " +
                    " \"timestamp\" : \"2022-07-28T15:36:56\", " +
                    " \"from\" :  \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"contractAddress\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\" , " +
                    " \"arguments\" :	{ " +
                    " \"creator\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"ipfsPath\": \"ipfs://url\" } " +
                    "}"
        }
        val sqsEvent = SqsEvent(sqsConfig, message)
        // when
        ethNftEventHandler.onMintedEvent(sqsEvent)
        // then
        assertThrows<Exception> { sqsEvent.getBodyMessage<Minted>() }
        assertAll({
            verify(exactly = 0) { nftCommandService.mintNft(any()) }
            verify(exactly = 0) { publisher.publishEvent(sqsEvent.changeToDeleteEvent()) }
        })
    }

}
