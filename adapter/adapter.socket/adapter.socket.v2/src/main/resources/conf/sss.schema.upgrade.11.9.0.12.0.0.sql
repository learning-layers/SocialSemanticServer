DROP TABLE `sss`.`learnepversiontimelinestates`;

ALTER TABLE `sss`.`learneptimelinestate` 
ADD COLUMN `userId` VARCHAR(200) NULL COMMENT '' AFTER `endTime`;