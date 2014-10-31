ALTER TABLE `sss`.`categoryass` 
ADD COLUMN `creationTime` VARCHAR(200) NOT NULL AFTER `categorySpace`;

CREATE TABLE `sss`.`user` (
  `userId` VARCHAR(255) NOT NULL,
  `email` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`userId`));

ALTER TABLE `sss`.`user` 
ADD CONSTRAINT `userIdFKuser`
  FOREIGN KEY (`userId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

CREATE TABLE `sss`.`message` (
  `messageId` VARCHAR(200) NOT NULL,
  `userId` VARCHAR(200) NOT NULL,
  `forEntityId` VARCHAR(200) NOT NULL,
  `messageContent` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`messageId`));

ALTER TABLE `sss`.`message` 
ADD INDEX `userIdFKmessage_idx` (`userId` ASC),
ADD INDEX `forEntityIdFKmessage_idx` (`forEntityId` ASC);
ALTER TABLE `sss`.`message` 
ADD CONSTRAINT `messageIdFKmessage`
  FOREIGN KEY (`messageId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `userIdFKmessage`
  FOREIGN KEY (`userId`)
  REFERENCES `sss`.`user` (`userId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `forEntityIdFKmessage`
  FOREIGN KEY (`forEntityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

CREATE TABLE `sss`.`entityreads` (
  `entityId` VARCHAR(255) NOT NULL,
  `userId` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`entityId`, `userId`));

ALTER TABLE `sss`.`entityreads` 
ADD INDEX `userIdFKentityreads_idx` (`userId` ASC);
ALTER TABLE `sss`.`entityreads` 
ADD CONSTRAINT `entityIdFKentityreads`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `userIdFKentityreads`
  FOREIGN KEY (`userId`)
  REFERENCES `sss`.`user` (`userId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;