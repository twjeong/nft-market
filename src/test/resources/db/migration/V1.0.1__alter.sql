ALTER TABLE `account` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `admin` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `collection` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `collection_creator` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `content` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `creator_royalty` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `editorial` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `email_verification` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `member` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `nft` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `nft_like` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `notification_message` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `royalty` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `sales_info` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `tag` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
ALTER TABLE `template` ADD COLUMN `deleted_date` DATETIME DEFAULT NULL;
