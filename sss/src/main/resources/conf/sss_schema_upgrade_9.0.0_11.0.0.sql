ALTER TABLE `sss`.`tagass` 
ADD CONSTRAINT `tagIdFKtagass`
  FOREIGN KEY (`tagId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `sss`.`tagass` 
ADD COLUMN `circleId` VARCHAR(150) NULL AFTER `creationTime`,
ADD INDEX `circleIdFKtagass_idx` (`circleId` ASC);
ALTER TABLE `sss`.`tagass` 
ADD CONSTRAINT `circleIdFKtagass`
  FOREIGN KEY (`circleId`)
  REFERENCES `sss`.`circle` (`circleId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `sss`.`categoryass` 
ADD COLUMN `circleId` VARCHAR(150) NULL AFTER `creationTime`,
ADD INDEX `circleIdFKcategoryass_idx` (`circleId` ASC);
ALTER TABLE `sss`.`categoryass` 
ADD CONSTRAINT `circleIdFKcategoryass`
  FOREIGN KEY (`circleId`)
  REFERENCES `sss`.`circle` (`circleId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
INSERT INTO `sss`.`space` (`id`) VALUES ('circleSpace');

ALTER TABLE `sss`.`tagass` 
DROP FOREIGN KEY `circleIdFKtagass`;
ALTER TABLE `sss`.`tagass` 
CHANGE COLUMN `circleId` `circleId` VARCHAR(150) NULL DEFAULT '' ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`userId`, `entityId`, `tagId`, `tagSpace`, `circleId`);
ALTER TABLE `sss`.`tagass` 
ADD CONSTRAINT `circleIdFKtagass`
  FOREIGN KEY (`circleId`)
  REFERENCES `sss`.`circle` (`circleId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `sss`.`categoryass` 
DROP FOREIGN KEY `circleIdFKcategoryass`;
ALTER TABLE `sss`.`categoryass` 
CHANGE COLUMN `circleId` `circleId` VARCHAR(150) NULL ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`categoryId`, `entityId`, `userId`, `categorySpace`, `circleId`);
ALTER TABLE `sss`.`categoryass` 
ADD CONSTRAINT `circleIdFKcategoryass`
  FOREIGN KEY (`circleId`)
  REFERENCES `sss`.`circle` (`circleId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
