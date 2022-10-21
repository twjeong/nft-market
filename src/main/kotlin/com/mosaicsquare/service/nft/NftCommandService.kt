package com.mosaicsquare.service.nft

import com.mosaicsquare.service.baseException.BaseException
import com.mosaicsquare.service.baseException.BaseResponseCode
import com.mosaicsquare.service.common.S3Service
import com.mosaicsquare.service.domain.nft.NftRepository
import com.mosaicsquare.service.domain.sales.info.SalesInfoRepository
import com.mosaicsquare.service.domain.sales.transaction_history.TransactionHistoryRepository
import com.mosaicsquare.service.domain.tag.Tag
import com.mosaicsquare.service.domain.tag.TagRepository
import com.mosaicsquare.service.nft.dto.*
import com.mosaicsquare.service.sqs.event.message.eth.MessageBody
import com.mosaicsquare.service.sqs.event.message.eth.arguments.nft.Minted
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import javax.transaction.Transactional

@Service
@Transactional
class NftCommandService(
    private val nftRepository: NftRepository,
    private val nftMapper: NftMapper,
    private val tagRepository: TagRepository,
    private val salesInfoMapper: SalesInfoMapper,
    private val salesInfoRepository: SalesInfoRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val s3Service: S3Service
) {
    @Value("\${cloud.aws.s3.image-dir}")
    lateinit var imageDir: String

    fun createNft(addNftRequest: AddNftRequest): NftResponse {
        if (addNftRequest.checkMarketFee()) {
            throw BaseException(BaseResponseCode.NFT_MARKET_TOTAL_FEE_OVER_100)
        }

        addNftRequest.generateTokenId(nftRepository.findTopByAddressOrderByTokenIdDesc(addNftRequest.address)?.tokenId)

        val nft = nftMapper.toEntity(addNftRequest)
        addNftRequest.tagIdList?.let { getTags(it) }?.let { nft.addTags(it) }
        nftRepository.save(nft)

        val salesInfoEntity = salesInfoMapper.toEntity(nftMapper.toSalesInfoRequestFromAddNftRequest(addNftRequest))
        salesInfoEntity.bindNft(nft)
        salesInfoEntity.changeOnSale(true)
        salesInfoRepository.save(salesInfoEntity)

        return nftMapper.toDto(nft)
    }

    fun updateNft(id: Long, updateNftRequest: UpdateNftRequest): NftResponse {
        if (updateNftRequest.checkMarketFee()) {
            throw BaseException(BaseResponseCode.NFT_MARKET_TOTAL_FEE_OVER_100)
        }

        val nft = nftRepository.findByIdOrNull(id) ?: throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        if (nft.checkNftUpdateAvailableByStatus(updateNftRequest)) {
            throw BaseException(BaseResponseCode.NFT_EDIT_NOT_POSSIBLE_BY_STATUS)
        }

        nftMapper.toEntity(updateNftRequest, nft)
        updateSalesInfo(updateNftRequest, nft.id)
        updateNftRequest.tagIdList?.let { getTags(it) }?.let { nft.addTags(it) }

        return nftMapper.toDto(nft)
    }

    fun deleteNft(id: Long) {
        if (nftRepository.existsById(id)) {
            nftRepository.findByIdOrNull(id)?.run {
                this.salesInfos?.forEach { salesInfo ->
                    salesInfoRepository.findByIdOrNull(salesInfo.id)?.let {
                        salesInfoRepository.delete(it)
                    }
                }

                nftRepository.delete(this)
            }
        } else {
            throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        }
    }

    fun saveNftImage(nftId: Long, imageFile: MultipartFile): ResponseEntity<NftResponse> {
        if (!imageFile.contentType!!.startsWith("image/") && !imageFile.contentType!!.startsWith("application/octet-stream")) throw ResponseStatusException(
            HttpStatus.BAD_REQUEST
        )

        return if (nftRepository.existsById(nftId)) {
            ResponseEntity.ok(nftRepository.findByIdOrNull(nftId)?.apply {
                val oldImageUrl = this.imageUrl

                this.changeImageUrl(
                    imageUrl = s3Service.upload(imageFile, imageDir)
                )
                nftRepository.save(this)

                oldImageUrl?.let { s3Service.delete(it) }
            }?.let { nftMapper.toDto(it) })
        } else {
            throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        }
    }

    fun deleteNftImage(nftId: Long) {
        if (!nftRepository.existsById(nftId)) {
            throw Exception(BaseResponseCode.NFT_NOT_FOUND.name)
        }

        nftRepository.findByIdOrNull(nftId)?.apply {
            val imageUrl = this.imageUrl

            if (!imageUrl.isNullOrEmpty()) {
                s3Service.delete(imageUrl)
            }

            this.removeImageUrl()
            nftRepository.save(this)
        }
    }

    fun mintNft(body: MessageBody<Minted>) {
        val args = body.arguments
        val nft = nftRepository.findTopByTokenIdAndAddress(args.getTokenId(), body.contractAddress)
        nft?.let {
            it.changeToMinted(args)
            transactionHistoryRepository.save(body.getTransactionHistory(it))
        }
    }

    private fun updateSalesInfo(updateNftRequest: UpdateNftRequest, nftId: Long) {
        val salesInfo = salesInfoRepository.findSalesInfoByNftIdAndOnSaleIsTrue(nftId) ?: throw BaseException(
            BaseResponseCode.SALESINFO_NOT_FOUND
        )

        salesInfo.changeSalesInfoField(nftMapper.toSalesInfoRequestFromUpdateNftRequest(updateNftRequest))
    }

    private fun getTags(tagIdList: List<Long>): MutableList<Tag> {
        val list: MutableList<Tag> = mutableListOf()
        tagIdList.forEach { tagId ->
            tagRepository.findByIdOrNull(tagId)?.let {
                list.add(it)
            }
        }
        return list
    }
}
