package com.mosaicsquare.service.sqs.event

import aws.sdk.kotlin.services.sqs.model.Message
import com.mosaicsquare.service.sqs.SqsConfig
import com.mosaicsquare.service.sqs.event.message.eth.arguments.nft.Minted
import com.mosaicsquare.service.sqs.event.message.notification.MessageBody
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
@DisplayName("SQS Message 관련")
class SqsEventTest {

    @Test
    fun `message body의 키 name이 Minted이면  SqsEventType이 MINTED인 SqsEvent객체를 생성한다`() {
        // given
        val sqsConfig = SqsConfig(
            url = "sqs_url",
            sqsEventType = SqsEventType.MOS_ETH_NFT,
        ).apply {
            workers = 20
            waitTime = 20
            maxNumberMessage = 10
        }
        val tokenId = "1"
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
                    " \"tokenId\": \"" + tokenId + "\", " +
                    " \"nftContract\" : \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"creator\": \"0xf76c9b7012c0a3870801eaaddb93b6352c8893db\", " +
                    " \"ipfsPath\": \"ipfs://url\" } " +
                    "}"
        }
        // when
        val sqsEvent = SqsEvent(sqsConfig, message)
        val body = sqsEvent.getBodyMessage<Minted>()

        // then
        assertThat(sqsEvent.name).isEqualTo(SqsEventType.MosEthNftType.MOS_ETH_NFT_Minted.name)
        assertThat(body.arguments.tokenId).isEqualTo(tokenId)
    }

    @Test
    fun `json에 MessageBody객체로 변환하면 salesInfoIds 리스트 필드 길이는 2이다`() {
        // given
        val sqsConfig = SqsConfig(
            url = "sqs_url",
            sqsEventType = SqsEventType.MOS_NOTIFICATION,
        )

        val message = Message.invoke {
            receiptHandle = "receiptHandle"
            body = "{ " +
                    " \"name\" : \"Auction1stStart\", " +
                    " \"salesInfoIds\": [1, 2] " +
                    "}"
        }

        // when
        val sqsEvent = SqsEvent(sqsConfig, message)
        val body = sqsEvent.getBodyMessage(MessageBody.serializer())

        // then
        assertThat(body.salesInfoIds).hasSize(2)
        assertThat(body.salesInfoIds[0]).isEqualTo(1L)
    }
}
