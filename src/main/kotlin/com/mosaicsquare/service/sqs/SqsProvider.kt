package com.mosaicsquare.service.sqs

import aws.sdk.kotlin.services.sqs.SqsClient
import com.mosaicsquare.service.sqs.event.SqsEventType
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SqsProvider(
    @Value("\${cloud.aws.sqs.mos-eth-factory}")
    private val mosEthFactoryQueueUrl: String,
    @Value("\${cloud.aws.sqs.mos-eth-nft}")
    private val mosEthNftQueueUrl: String,
    @Value("\${cloud.aws.sqs.mos-eth-primary-market}")
    private val mosEthPrimaryMarketQueueUrl: String,
    @Value("\${cloud.aws.sqs.mos-eth-secondary-market}")
    private val mosEthSecondaryMarketQueueUrl: String,
    @Value("\${cloud.aws.sqs.mos-eth-dead-letter}")
    private val ethDeadLetterQueueUrl: String,
    @Value("\${cloud.aws.sqs.mos-notification}")
    private val mosNotificationUrl: String,
    private val sqs: SqsClient,
    private val publisher: ApplicationEventPublisher
) {
    private val logger = LogManager.getLogger(SqsProvider::class.java)

    @Bean
    fun initMosEthFactoryQueueUrl() {
        val config = SqsConfig(mosEthFactoryQueueUrl, SqsEventType.MOS_ETH_FACTORY)
        SqsConsumer(sqs, config, publisher).start()
    }
    @Bean
    fun initMosEthNftQueueUrl() {
        val config = SqsConfig(mosEthNftQueueUrl, SqsEventType.MOS_ETH_NFT)
        SqsConsumer(sqs, config, publisher).start()
    }

    @Bean
    fun initPrimaryMarketQueue() {
        val config = SqsConfig(mosEthPrimaryMarketQueueUrl, SqsEventType.MOS_ETH_PRIMARY_MARKET)
        SqsConsumer(sqs, config, publisher).start()
    }

    @Bean
    fun initSecondaryMarketQueue() {
        val config = SqsConfig(mosEthSecondaryMarketQueueUrl, SqsEventType.MOS_ETH_SECONDARY_MARKET)
        SqsConsumer(sqs, config, publisher).start()
    }

    @Bean
    fun initEthDeadLetterQueue() {
        val config = SqsConfig(ethDeadLetterQueueUrl, SqsEventType.MOS_ETH_DEAD_LETTER)
        SqsConsumer(sqs, config, publisher).start()
    }

    @Bean
    fun initMosNotification() {
        val config = SqsConfig(mosNotificationUrl, SqsEventType.MOS_NOTIFICATION)
        SqsConsumer(sqs, config, publisher).start()
    }
}
