ALTER TABLE `sss`.`user` 
ADD COLUMN `oidcSub` VARCHAR(200) NULL COMMENT '' AFTER `email`;

CREATE TABLE `sss`.`entityauthors` (
  `userId` VARCHAR(255) NOT NULL COMMENT '',
  `entityId` VARCHAR(255) NOT NULL COMMENT '',
  `creationTime` BIGINT(13) NOT NULL COMMENT '',
  PRIMARY KEY (`userId`, `entityId`)  COMMENT '');

ALTER TABLE `sss`.`entityauthors` 
ADD INDEX `entityIdFKentityauthors_idx` (`entityId` ASC)  COMMENT '';
ALTER TABLE `sss`.`entityauthors` 
ADD CONSTRAINT `userIdFKentityauthors`
  FOREIGN KEY (`userId`)
  REFERENCES `sss`.`user` (`userId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `entityIdFKentityauthors`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;