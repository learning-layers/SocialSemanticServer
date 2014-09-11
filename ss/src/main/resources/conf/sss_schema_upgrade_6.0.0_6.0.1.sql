ALTER TABLE `sss`.`activityentities` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;

ALTER TABLE `sss`.`categoryass` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;

ALTER TABLE `sss`.`circleentities` 
DROP FOREIGN KEY `entityIdFKcircleentities`;
ALTER TABLE `sss`.`circleentities` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`circleentities` 
ADD CONSTRAINT `entityIdFKcircleentities`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `sss`.`collentrypos` 
DROP FOREIGN KEY `collEntryIdFKcollentrypos`;
ALTER TABLE `sss`.`collentrypos` 
CHANGE COLUMN `entryId` `entryId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`collentrypos` 
ADD CONSTRAINT `collEntryIdFKcollentrypos`
  FOREIGN KEY (`entryId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `sss`.`comment` 
CHANGE COLUMN `commentContent` `commentContent` VARCHAR(1000) NOT NULL ;

ALTER TABLE `sss`.`comments` 
DROP FOREIGN KEY `entityIdFKcomments`;
ALTER TABLE `sss`.`comments` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`comments` 
ADD CONSTRAINT `entityIdFKcomments`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `sss`.`disc` 
DROP FOREIGN KEY `entityIdFKdisc`;
ALTER TABLE `sss`.`disc` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`disc` 
ADD CONSTRAINT `entityIdFKdisc`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `sss`.`entities` 
DROP FOREIGN KEY `attachedEntityIdFKentities`,
DROP FOREIGN KEY `entityIdFKentities`;
ALTER TABLE `sss`.`entities` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `attachedEntityId` `attachedEntityId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`entities` 
ADD CONSTRAINT `attachedEntityIdFKentities`
  FOREIGN KEY (`attachedEntityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `entityIdFKentities`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `sss`.`entity` 
CHANGE COLUMN `id` `id` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `label` `label` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `description` `description` VARCHAR(1000) NOT NULL ;

ALTER TABLE `sss`.`evernotenote` 
DROP FOREIGN KEY `notebookIdFKevernotenote`,
DROP FOREIGN KEY `noteIdFKevernotenote`;
ALTER TABLE `sss`.`evernotenote` 
CHANGE COLUMN `noteId` `noteId` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `notebookId` `notebookId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`evernotenote` 
ADD CONSTRAINT `notebookIdFKevernotenote`
  FOREIGN KEY (`notebookId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `noteIdFKevernotenote`
  FOREIGN KEY (`noteId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `sss`.`evernoteresource` 
DROP FOREIGN KEY `entityIdFKevernoteresource`,
DROP FOREIGN KEY `noteIdFKevernoteresource`;
ALTER TABLE `sss`.`evernoteresource` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `noteId` `noteId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`evernoteresource` 
ADD CONSTRAINT `entityIdFKevernoteresource`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `noteIdFKevernoteresource`
  FOREIGN KEY (`noteId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `sss`.`files` 
DROP FOREIGN KEY `entityIdFKfiles`,
DROP FOREIGN KEY `fileIdFKfiles`;
ALTER TABLE `sss`.`files` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `fileId` `fileId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`files` 
ADD CONSTRAINT `entityIdFKfiles`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fileIdFKfiles`
  FOREIGN KEY (`fileId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `sss`.`flags` 
DROP FOREIGN KEY `entityId`;
ALTER TABLE `sss`.`flags` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`flags` 
ADD CONSTRAINT `entityId`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `sss`.`learnepentity` 
DROP FOREIGN KEY `entityIdFKlearnepentity`;
ALTER TABLE `sss`.`learnepentity` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`learnepentity` 
ADD CONSTRAINT `entityIdFKlearnepentity`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `sss`.`locations` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;

ALTER TABLE `sss`.`ratingass` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;

ALTER TABLE `sss`.`tagass` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;

ALTER TABLE `sss`.`thumbnails` 
DROP FOREIGN KEY `entityIdFKthumbnails`;
ALTER TABLE `sss`.`thumbnails` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ;
ALTER TABLE `sss`.`thumbnails` 
ADD CONSTRAINT `entityIdFKthumbnails`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `sss`.`ues` 
CHANGE COLUMN `entityId` `entityId` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `content` `content` VARCHAR(100) NOT NULL ;