package com.mosaicsquare.service.sqs.event

enum class SqsEventType {
    MOS_ETH_FACTORY {
        override fun getEvent(type: String): String {
            return MosEthFactoryType.valueOf(type).name
        }
    },
    MOS_ETH_NFT {
        override fun getEvent(type: String): String {
            return MosEthNftType.valueOf(type).name
        }
    },

    MOS_ETH_PRIMARY_MARKET {
        override fun getEvent(type: String): String {
            return MosEthPrimaryMarketType.valueOf(type).name
        }
    },

    MOS_ETH_SECONDARY_MARKET {
        override fun getEvent(type: String): String {
            return MosEthSecondaryMarketType.valueOf(type).name
        }
    },

    MOS_NOTIFICATION {
        override fun getEvent(type: String): String {
            return MosNotificationType.valueOf(type).name
        }
    },

    MOS_ETH_DEAD_LETTER {
        override fun getEvent(type: String): String {
            return MosEthDeadLetter.DeadLetter.name
        }
    },

    EVENT_HANDLER {
        override fun getEvent(type: String): String {
            return EventHandlerType.valueOf(type).name
        }
    };

    enum class MosEthFactoryType {
        MOS_ETH_FACTORY_MoneypipeCreated,
        MOS_ETH_FACTORY_UpdatedMoneypipe,
    }
    enum class MosEthNftType {
        MOS_ETH_NFT_Minted,
        MOS_ETH_NFT_Burned,
        MOS_ETH_NFT_Transfered,
    }

    enum class MosEthPrimaryMarketType {
        MOS_ETH_PRIMARY_MARKET_AuctionCreated,
        MOS_ETH_PRIMARY_MARKET_AuctionCanceled,
        MOS_ETH_PRIMARY_MARKET_AuctionBidPlaced,
        MOS_ETH_PRIMARY_MARKET_AuctionFinalized,
        MOS_ETH_PRIMARY_MARKET_FixedPriceSet,
        MOS_ETH_PRIMARY_MARKET_FixedPriceCanceled,
        MOS_ETH_PRIMARY_MARKET_FixedPriceSold,
    }

    enum class MosEthSecondaryMarketType {
        MOS_ETH_SECONDARY_MARKET_FixedPriceSet,
        MOS_ETH_SECONDARY_MARKET_FixedPriceSold,
        MOS_ETH_SECONDARY_MARKET_OfferMade,
        MOS_ETH_SECONDARY_MARKET_OfferAccepted,
        MOS_ETH_SECONDARY_MARKET_FixedPriceCanceled
    }

    enum class MosEthDeadLetter {
        DeadLetter,
    }

    enum class MosNotificationType {
        MOS_NOTIFICATION_Auction1stStart,
        MOS_NOTIFICATION_Auction1stRemind,
        MOS_NOTIFICATION_Auction1stEnd,
        MOS_NOTIFICATION_FixedPrice1stStart,
        MOS_NOTIFICATION_Offer2ndRemind,
        MOS_NOTIFICATION_Offer2ndEnd,
        MOS_NOTIFICATION_Offer2ndEndRemind
    }

    enum class EventHandlerType {
        DeleteMessage,
        ChangeVisibilityMessage,
    }

    abstract fun getEvent(type: String): String
}
