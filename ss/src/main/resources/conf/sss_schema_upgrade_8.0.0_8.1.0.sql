ALTER TABLE `sss`.`activity` 
ADD COLUMN `entityId` VARCHAR(255) NOT NULL AFTER `textComment`;

ALTER TABLE `sss`.`message` 
CHANGE COLUMN `messageContent` `messageContent` VARCHAR(10000) NOT NULL ;
