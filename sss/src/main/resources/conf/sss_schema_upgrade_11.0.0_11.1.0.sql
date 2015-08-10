CREATE TABLE `sss`.`disctargets` (
  `discId` VARCHAR(255) NOT NULL,
  `targetId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`discId`, `targetId`),
  INDEX `targetIdFKdisctargets_idx` (`targetId` ASC),
  CONSTRAINT `discIdFKdisctargets`
    FOREIGN KEY (`discId`)
    REFERENCES `sss`.`disc` (`discId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `targetIdFKdisctargets`
    FOREIGN KEY (`targetId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

ALTER TABLE `sss`.`disc` 
DROP FOREIGN KEY `entityIdFKdisc`;
ALTER TABLE `sss`.`disc` 
DROP COLUMN `entityId`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`discId`),
DROP INDEX `targetFKdisc_idx` ;

ALTER TABLE `sss`.`files` 
DROP FOREIGN KEY `entityIdFKfiles`;
ALTER TABLE `sss`.`files` 
DROP COLUMN `entityId`,
DROP PRIMARY KEY;
