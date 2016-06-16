CREATE TABLE `sss`.`circleinvitees` (
  `circleId` VARCHAR(255) NOT NULL,
  `inviteeId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`circleId`, `inviteeId`),
  CONSTRAINT `circleIdFKcircleinvitees`
    FOREIGN KEY (`circleId`)
    REFERENCES `sss`.`circle` (`circleId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

DROP TABLE `sss`.`entities`;

CREATE TABLE `sss`.`entityimages` (
  `entityId` VARCHAR(255) NOT NULL,
  `imageId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`entityId`, `imageId`),
  INDEX `imageId_idx` (`imageId` ASC),
  CONSTRAINT `entityIdFKentityimages`
    FOREIGN KEY (`entityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `imageId`
    FOREIGN KEY (`imageId`)
    REFERENCES `sss`.`image` (`imageId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

ALTER TABLE `sss`.`files` 
ADD PRIMARY KEY (`fileId`), RENAME TO  `sss`.`file` ;

DROP TABLE `sss`.`thumbnails`;

ALTER TABLE `sss`.`uservideos` 
RENAME TO  `sss`.`videousers` ;

ALTER TABLE `sss`.`tagass` 
RENAME TO  `sss`.`tags` ;

ALTER TABLE `sss`.`ratingass` 
RENAME TO  `sss`.`ratings` ;

ALTER TABLE `sss`.`downloads` 
RENAME TO  `sss`.`entitydownloads` ;

ALTER TABLE `sss`.`categoryass` 
RENAME TO  `sss`.`categories` ;

CREATE TABLE `sss`.`entityvideos` (
  `entityId` VARCHAR(255) NOT NULL,
  `videoId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`entityId`, `videoId`),
  INDEX `videoIdFKentityvideos_idx` (`videoId` ASC),
  CONSTRAINT `entityIdFKentityvideos`
    FOREIGN KEY (`entityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `videoIdFKentityvideos`
    FOREIGN KEY (`videoId`)
    REFERENCES `sss`.`video` (`videoId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

ALTER TABLE `sss`.`image` 
ADD COLUMN `link` VARCHAR(255) NOT NULL AFTER `type`;

CREATE TABLE `sss`.`entityfiles` (
  `entityId` VARCHAR(255) NOT NULL,
  `fileId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`entityId`, `fileId`),
  INDEX `fileIdFKentityfiles_idx` (`fileId` ASC),
  CONSTRAINT `entityIdFKentityfiles`
    FOREIGN KEY (`entityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fileIdFKentityfiles`
    FOREIGN KEY (`fileId`)
    REFERENCES `sss`.`file` (`fileId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

ALTER TABLE `sss`.`appstacklayout` 
DROP FOREIGN KEY `appFKappstacklayout`;
ALTER TABLE `sss`.`appstacklayout` 
CHANGE COLUMN `app` `app` VARCHAR(255) NOT NULL ;

ALTER TABLE `sss`.`user` 
ADD COLUMN `profilePictureId` VARCHAR(255) NOT NULL AFTER `email`,
ADD INDEX `profilePictureId_idx` (`profilePictureId` ASC);
ALTER TABLE `sss`.`user` 
ADD CONSTRAINT `profilePictureId`
  FOREIGN KEY (`profilePictureId`)
  REFERENCES `sss`.`image` (`imageId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

CREATE TABLE `sss`.`entityattachedentities` (
  `entityId` VARCHAR(255) NOT NULL,
  `attachedEntityId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`entityId`, `attachedEntityId`),
  INDEX `attachedEntityIdFKentityattachedentities_idx` (`attachedEntityId` ASC),
  CONSTRAINT `entityIdFKentityattachedentities`
    FOREIGN KEY (`entityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `attachedEntityIdFKentityattachedentities`
    FOREIGN KEY (`attachedEntityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

ALTER TABLE `sss`.`user` 
DROP FOREIGN KEY `profilePictureId`;
ALTER TABLE `sss`.`user` 
DROP COLUMN `profilePictureId`,
DROP INDEX `profilePictureId_idx` ;

DROP TABLE IF EXISTS `entityprofilepictures`;
CREATE TABLE `entityprofilepictures` (
  `entityId` varchar(255) NOT NULL,
  `imageId` varchar(255) NOT NULL,
  PRIMARY KEY (`entityId`,`imageId`),
  KEY `imageId_idx` (`imageId`),
  CONSTRAINT `imageIdFKentityprofilepictures` FOREIGN KEY (`imageId`) REFERENCES `image` (`imageId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `entityIdFKentityprofilepictures` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `sss`.`discentry` 
ADD COLUMN `accepted` VARCHAR(200) NOT NULL AFTER `discEntryContent`;

CREATE TABLE `sss`.`livingdoc` (
  `livingDocId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`livingDocId`),
  CONSTRAINT `livingDocIdFKlivingdoc`
    FOREIGN KEY (`livingDocId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE `sss`.`livingdocusers` (
  `livingDocId` VARCHAR(255) NOT NULL,
  `userId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`livingDocId`, `userId`),
  INDEX `userId_idx` (`userId` ASC),
  CONSTRAINT `livingDocIdFKlivingdocusers`
    FOREIGN KEY (`livingDocId`)
    REFERENCES `sss`.`livingdoc` (`livingDocId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKlivingdocusers`
    FOREIGN KEY (`userId`)
    REFERENCES `sss`.`user` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
