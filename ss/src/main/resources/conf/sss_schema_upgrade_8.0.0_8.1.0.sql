ALTER TABLE `sss`.`activity` 
ADD COLUMN `entityId` VARCHAR(255) NOT NULL AFTER `textComment`;

ALTER TABLE `sss`.`message` 
CHANGE COLUMN `messageContent` `messageContent` VARCHAR(10000) NOT NULL ;

ALTER TABLE `sss`.`appstacklayout` 
DROP FOREIGN KEY `appFKappstacklayout`;
ALTER TABLE `sss`.`appstacklayout` 
CHANGE COLUMN `app` `app` VARCHAR(255) NULL ;
ALTER TABLE `sss`.`appstacklayout` 
ADD CONSTRAINT `appFKappstacklayout`
  FOREIGN KEY (`app`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

DROP TABLE `sss`.`appstacklayouttiles`;

DROP TABLE `sss`.`appstacklayouttile`;