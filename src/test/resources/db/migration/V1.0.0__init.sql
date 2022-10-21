# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: db-mosaic-square-dev-instance-1.cj6lgpsklgfx.ap-northeast-2.rds.amazonaws.com (MySQL 5.7.12)
# Database: mosaic_square
# Generation Time: 2022-08-23 09:09:29 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table account
# ------------------------------------------------------------

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `created_date` datetime(6) DEFAULT NULL,
                           `updated_date` datetime(6) DEFAULT NULL,
                           `block_chain_type` varchar(16) NOT NULL,
                           `incorrect_password_count` smallint(6) DEFAULT NULL,
                           `last_login_date` datetime(6) DEFAULT NULL,
                           `nonce` varchar(255) DEFAULT NULL,
                           `password` varchar(512) DEFAULT NULL,
                           `password_changed` bit(1) NOT NULL,
                           `refresh_token` varchar(512) DEFAULT NULL,
                           `role` varchar(255) DEFAULT NULL,
                           `user_name` varchar(64) DEFAULT NULL,
                           `wallet_address` varchar(64) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_account_wallet_address` (`wallet_address`),
                           UNIQUE KEY `UK_account_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table admin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
                         `id` bigint(20) NOT NULL,
                         `created_date` datetime(6) DEFAULT NULL,
                         `updated_date` datetime(6) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         CONSTRAINT `FK_admin_account_id` FOREIGN KEY (`id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table collection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `collection`;

CREATE TABLE `collection` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `created_date` datetime(6) DEFAULT NULL,
                              `updated_date` datetime(6) DEFAULT NULL,
                              `description` text,
                              `nft_open_date` datetime(6) DEFAULT NULL,
                              `status` varchar(255) NOT NULL,
                              `title` varchar(128) NOT NULL,
                              `type` varchar(255) DEFAULT NULL,
                              `uuid` varchar(40) NOT NULL,
                              `template_id` bigint(20) DEFAULT NULL,
                              `image_url` varchar(2048) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `UK_collection_title` (`title`),
                              UNIQUE KEY `UK_collection_uuid` (`uuid`),
                              KEY `FK_collection_template_id` (`template_id`),
                              CONSTRAINT `FK_collection_template_id` FOREIGN KEY (`template_id`) REFERENCES `template` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table collection_creator
# ------------------------------------------------------------

DROP TABLE IF EXISTS `collection_creator`;

CREATE TABLE `collection_creator` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `created_date` datetime(6) DEFAULT NULL,
                                      `updated_date` datetime(6) DEFAULT NULL,
                                      `member_id` bigint(20) DEFAULT NULL,
                                      `collection_id` bigint(20) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `FK_collection_creator_collection_id` (`collection_id`),
                                      CONSTRAINT `FK_collection_creator_collection_id` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table content
# ------------------------------------------------------------

DROP TABLE IF EXISTS `content`;

CREATE TABLE `content` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `created_date` datetime(6) DEFAULT NULL,
                           `updated_date` datetime(6) DEFAULT NULL,
                           `enabled` bit(1) NOT NULL,
                           `image_url` varchar(255) DEFAULT NULL,
                           `key_name` varchar(255) DEFAULT NULL,
                           `link` varchar(255) DEFAULT NULL,
                           `template_id` bigint(20) DEFAULT NULL,
                           `text` longtext,
                           `type` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_content_template_id_key_name` (`template_id`,`key_name`),
                           CONSTRAINT `FK_template_content_id` FOREIGN KEY (`template_id`) REFERENCES `template` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table creator_royalty
# ------------------------------------------------------------

DROP TABLE IF EXISTS `creator_royalty`;

CREATE TABLE `creator_royalty` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `created_date` datetime(6) DEFAULT NULL,
                                   `updated_date` datetime(6) DEFAULT NULL,
                                   `member_id` bigint(20) DEFAULT NULL,
                                   `ratio` double DEFAULT NULL,
                                   `royalty_id` bigint(20) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `FK_creator_royalty_royalty_id` (`royalty_id`),
                                   CONSTRAINT `FK_creator_royalty_royalty_id` FOREIGN KEY (`royalty_id`) REFERENCES `royalty` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table creator_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `creator_type`;

CREATE TABLE `creator_type` (
                                `member_id` bigint(20) NOT NULL,
                                `type` varchar(255) DEFAULT NULL,
                                KEY `FK_creator_type_member_id` (`member_id`),
                                CONSTRAINT `FK_creator_type_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table editorial
# ------------------------------------------------------------

DROP TABLE IF EXISTS `editorial`;

CREATE TABLE `editorial` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `created_date` datetime(6) DEFAULT NULL,
                             `updated_date` datetime(6) DEFAULT NULL,
                             `description` text,
                             `image_url` varchar(2048) DEFAULT NULL,
                             `status` varchar(255) NOT NULL,
                             `title` varchar(128) NOT NULL,
                             `uuid` varchar(40) NOT NULL,
                             `template_id` bigint(20) DEFAULT NULL,
                             `editor_id` bigint(20) NOT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `UK_editorial_uuid` (`uuid`),
                             KEY `FK_editorial_template_id` (`template_id`),
                             CONSTRAINT `FK_editorial_template_id` FOREIGN KEY (`template_id`) REFERENCES `template` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table email_verification
# ------------------------------------------------------------

DROP TABLE IF EXISTS `email_verification`;

CREATE TABLE `email_verification` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `created_date` datetime(6) DEFAULT NULL,
                                      `updated_date` datetime(6) DEFAULT NULL,
                                      `code` varchar(64) DEFAULT NULL,
                                      `code_last_send_date` datetime(6) DEFAULT NULL,
                                      `code_send_count` int(11) DEFAULT NULL,
                                      `code_valid_date` datetime(6) DEFAULT NULL,
                                      `email_address` varchar(320) DEFAULT NULL,
                                      `verified` bit(1) DEFAULT NULL,
                                      `member_id` bigint(20) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `FK_email_verification_member_id` (`member_id`),
                                      CONSTRAINT `FK_email_verification_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table follow
# ------------------------------------------------------------

DROP TABLE IF EXISTS `follow`;

CREATE TABLE `follow` (
                          `from_member_id` bigint(20) NOT NULL,
                          `to_member_id` bigint(20) NOT NULL,
                          PRIMARY KEY (`from_member_id`,`to_member_id`),
                          KEY `FK_follow_to_member_id` (`to_member_id`),
                          CONSTRAINT `FK_follow_from_member_id` FOREIGN KEY (`from_member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE,
                          CONSTRAINT `FK_follow_to_member_id` FOREIGN KEY (`to_member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table member
# ------------------------------------------------------------

DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
                          `id` bigint(20) NOT NULL,
                          `created_date` datetime(6) DEFAULT NULL,
                          `updated_date` datetime(6) DEFAULT NULL,
                          `creator_state` varchar(255) DEFAULT NULL,
                          `country_code` varchar(255) DEFAULT NULL,
                          `mobile_number` varchar(255) DEFAULT NULL,
                          `description` longtext,
                          `email` varchar(255) DEFAULT NULL,
                          `instagram` varchar(255) DEFAULT NULL,
                          `open_created_nft_list` bit(1) DEFAULT NULL,
                          `open_owned_nft_list` bit(1) DEFAULT NULL,
                          `twitter` varchar(255) DEFAULT NULL,
                          `user_name` varchar(255) DEFAULT NULL,
                          `web_site` varchar(255) DEFAULT NULL,
                          `profile_image_url` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `UK_member_user_name` (`user_name`),
                          CONSTRAINT `FK_member_account_id` FOREIGN KEY (`id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table nft
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nft`;

CREATE TABLE `nft` (
                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                       `created_date` datetime(6) DEFAULT NULL,
                       `updated_date` datetime(6) DEFAULT NULL,
                       `address` varchar(64) NOT NULL,
                       `blockchain_type` varchar(16) NOT NULL,
                       `collection_id` bigint(20) DEFAULT NULL,
                       `creator_id` bigint(20) NOT NULL,
                       `description` text,
                       `file_ipfs_hash` varchar(64) DEFAULT NULL,
                       `image_url` varchar(2048) DEFAULT NULL,
                       `market_fee_1st` double DEFAULT NULL,
                       `metadata_ipfs_hash` varchar(64) DEFAULT NULL,
                       `minted_date` datetime(6) DEFAULT NULL,
                       `owner_id` bigint(20) DEFAULT NULL,
                       `property` json DEFAULT NULL,
                       `royalty_1st` double DEFAULT NULL,
                       `royalty_2nd` double DEFAULT NULL,
                       `royalty_address` varchar(64) DEFAULT NULL,
                       `royalty_type` varchar(16) DEFAULT NULL,
                       `status` varchar(16) NOT NULL,
                       `title` varchar(128) NOT NULL,
                       `token_id` bigint(20) DEFAULT NULL,
                       PRIMARY KEY (`id`),
                       UNIQUE KEY `UK_nft_address_token_id` (`address`,`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table nft_like
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nft_like`;

CREATE TABLE `nft_like` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `created_date` datetime(6) DEFAULT NULL,
                            `updated_date` datetime(6) DEFAULT NULL,
                            `member_id` bigint(20) DEFAULT NULL,
                            `nft_id` bigint(20) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `UK_nft_like_nft_id_member_id` (`nft_id`,`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table nft_tag
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nft_tag`;

CREATE TABLE `nft_tag` (
                           `nft_id` bigint(20) NOT NULL,
                           `tag_id` bigint(20) NOT NULL,
                           KEY `FK_nft_tag_tag_id` (`tag_id`),
                           KEY `FK_nft_tag_nft_id` (`nft_id`),
                           CONSTRAINT `FK_nft_tag_nft_id` FOREIGN KEY (`nft_id`) REFERENCES `nft` (`id`),
                           CONSTRAINT `FK_nft_tag_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table notification_message
# ------------------------------------------------------------

DROP TABLE IF EXISTS `notification_message`;

CREATE TABLE `notification_message` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `created_date` datetime(6) DEFAULT NULL,
                                        `updated_date` datetime(6) DEFAULT NULL,
                                        `event_message` longtext,
                                        `is_read` bit(1) NOT NULL,
                                        `member_id` bigint(20) NOT NULL,
                                        PRIMARY KEY (`id`),
                                        KEY `FK_notification_message_member_id` (`member_id`),
                                        CONSTRAINT `FK_notification_message_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table royalty
# ------------------------------------------------------------

DROP TABLE IF EXISTS `royalty`;

CREATE TABLE `royalty` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `created_date` datetime(6) DEFAULT NULL,
                           `updated_date` datetime(6) DEFAULT NULL,
                           `address` varchar(64) DEFAULT NULL,
                           `description` text,
                           `name` varchar(128) NOT NULL,
                           `status` varchar(16) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_royalty_address` (`address`),
                           UNIQUE KEY `UK_royalty_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sales_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sales_info`;

CREATE TABLE `sales_info` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `created_date` datetime(6) DEFAULT NULL,
                              `updated_date` datetime(6) DEFAULT NULL,
                              `currency_type` varchar(255) DEFAULT NULL,
                              `duration` int(11) NOT NULL,
                              `end_date` datetime(6) DEFAULT NULL,
                              `last_price` decimal(19,3) DEFAULT NULL,
                              `min_price` decimal(19,3) DEFAULT NULL,
                              `on_sale` bit(1) DEFAULT NULL,
                              `sales_type` varchar(255) DEFAULT NULL,
                              `seller_member_id` bigint(20) DEFAULT NULL,
                              `start_date` datetime(6) NOT NULL,
                              `start_price` decimal(19,3) NOT NULL,
                              `nft_id` bigint(20) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `FK_sales_info_nft_id` (`nft_id`),
                              CONSTRAINT `FK_sales_info_nft_id` FOREIGN KEY (`nft_id`) REFERENCES `nft` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tag
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                       `created_date` datetime(6) DEFAULT NULL,
                       `updated_date` datetime(6) DEFAULT NULL,
                       `name` varchar(255) DEFAULT NULL,
                       PRIMARY KEY (`id`),
                       UNIQUE KEY `UK_tag_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table template
# ------------------------------------------------------------

DROP TABLE IF EXISTS `template`;

CREATE TABLE `template` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `created_date` datetime(6) DEFAULT NULL,
                            `updated_date` datetime(6) DEFAULT NULL,
                            `type` varchar(255) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table transaction_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `transaction_history`;

CREATE TABLE `transaction_history` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `address` varchar(255) DEFAULT NULL,
                                       `created_date` datetime(6) DEFAULT NULL,
                                       `event_name` varchar(255) DEFAULT NULL,
                                       `price` decimal(19,3) DEFAULT NULL,
                                       `sender_address` varchar(255) DEFAULT NULL,
                                       `transaction_hash` varchar(255) DEFAULT NULL,
                                       `nft_id` bigint(20) DEFAULT NULL,
                                       `sales_info_id` bigint(20) DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       KEY `FK_transaction_history_nft_id` (`nft_id`),
                                       KEY `FK_transaction_history_sales_info_id` (`sales_info_id`),
                                       CONSTRAINT `FK_transaction_history_nft_id` FOREIGN KEY (`nft_id`) REFERENCES `nft` (`id`),
                                       CONSTRAINT `FK_transaction_history_sales_info_id` FOREIGN KEY (`sales_info_id`) REFERENCES `sales_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table z_flyway_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `z_flyway_history`;

CREATE TABLE `z_flyway_history` (
                                    `installed_rank` int(11) NOT NULL,
                                    `version` varchar(50) DEFAULT NULL,
                                    `description` varchar(200) NOT NULL,
                                    `type` varchar(20) NOT NULL,
                                    `script` varchar(1000) NOT NULL,
                                    `checksum` int(11) DEFAULT NULL,
                                    `installed_by` varchar(100) NOT NULL,
                                    `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    `execution_time` int(11) NOT NULL,
                                    `success` tinyint(1) NOT NULL,
                                    PRIMARY KEY (`installed_rank`),
                                    KEY `z_flyway_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




--
-- Dumping routines (PROCEDURE) for database 'mosaic_square'
--
DELIMITER ;;

# Dump of PROCEDURE explain_statement
# ------------------------------------------------------------

/*!50003 DROP PROCEDURE IF EXISTS `explain_statement` */;;
/*!50003 SET SESSION SQL_MODE=""*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`dev_admin`@`%`*/ /*!50003 PROCEDURE `explain_statement`(IN query TEXT)
BEGIN
    SET @explain := CONCAT('EXPLAIN FORMAT=json ', query);
    PREPARE stmt FROM @explain;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END */;;

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
DELIMITER ;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
