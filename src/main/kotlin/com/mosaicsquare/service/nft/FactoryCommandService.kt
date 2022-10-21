package com.mosaicsquare.service.nft

import com.mosaicsquare.service.baseException.BaseException
import com.mosaicsquare.service.baseException.BaseResponseCode
import com.mosaicsquare.service.domain.sales.royalty.RoyaltyRepository
import com.mosaicsquare.service.sqs.event.message.eth.MessageBody
import com.mosaicsquare.service.sqs.event.message.eth.arguments.factory.MoneypipeCreated
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class FactoryCommandService(
    private val royaltyRepository: RoyaltyRepository,
) {
    fun contractMoneypipe(body: MessageBody<MoneypipeCreated>) {

        if (!royaltyRepository.existsById(body.arguments.id.toLong())) {
            throw BaseException(BaseResponseCode.ROYALTY_NOT_FOUND)
        }

        royaltyRepository.findByIdOrNull(body.arguments.id.toLong())?.royaltyListed(body.arguments.contractAddress)
    }
}
