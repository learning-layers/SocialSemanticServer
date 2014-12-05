ALTER TABLE `sss`.`entity` 
CHANGE COLUMN `description` `description` VARCHAR(10000) NOT NULL ;

ALTER TABLE `achso`.`video` 
ADD COLUMN `link` VARCHAR(255) NOT NULL AFTER `genre`;