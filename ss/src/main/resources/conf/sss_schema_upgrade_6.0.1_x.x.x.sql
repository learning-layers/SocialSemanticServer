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

CREATE TABLE `sss`.`images` (
  `imageId` VARCHAR(255) NOT NULL,
  `entityId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`imageId`, `entityId`),
  INDEX `entityIdFKimages_idx` (`entityId` ASC),
  CONSTRAINT `entityIdFKimages`
    FOREIGN KEY (`entityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `imageIdFKimages`
    FOREIGN KEY (`imageId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE `sss`.`screenshots` (
  `entityId` VARCHAR(255) NOT NULL,
  `screenShotId` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`entityId`, `screenShotId`),
  INDEX `screenShotIdFKscreenhots_idx` (`screenShotId` ASC),
  CONSTRAINT `entityIdFKscreenshots`
    FOREIGN KEY (`entityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `screenShotIdFKscreenhots`
    FOREIGN KEY (`screenShotId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE `sss`.`videos` (
  `videoId` VARCHAR(200) NOT NULL,
  `entityId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`videoId`, `entityId`),
  INDEX `entityIdFKvideos_idx` (`entityId` ASC),
  CONSTRAINT `videoIdFKvideos`
    FOREIGN KEY (`videoId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `entityIdFKvideos`
    FOREIGN KEY (`entityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE `sss`.`downloads` (
  `entityId` VARCHAR(255) NOT NULL,
  `downloadId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`entityId`, `downloadId`),
  CONSTRAINT `entityIdFKdownloads`
    FOREIGN KEY (`entityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE `sss`.`app` (
  `appId` VARCHAR(200) NOT NULL,
  `descriptionShort` VARCHAR(5000) NOT NULL,
  `descriptionFunctional` VARCHAR(5000) NOT NULL,
  `descriptionTechnical` VARCHAR(5000) NOT NULL,
  `descriptionInstall` VARCHAR(5000) NOT NULL,
  `downloadIOS` VARCHAR(255) NOT NULL,
  `downloadAndroid` VARCHAR(255) NOT NULL,
  `fork` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`appId`),
  CONSTRAINT `appIdFKapp`
    FOREIGN KEY (`appId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE `sss`.`friends` (
  `userId` VARCHAR(200) NOT NULL,
  `friendId` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`userId`, `friendId`),
  INDEX `friendIdFKfriends_idx` (`friendId` ASC),
  CONSTRAINT `userIdFKfriends`
    FOREIGN KEY (`userId`)
    REFERENCES `sss`.`user` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `friendIdFKfriends`
    FOREIGN KEY (`friendId`)
    REFERENCES `sss`.`user` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);