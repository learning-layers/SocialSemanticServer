ALTER TABLE `sss`.`tagass` 
ADD COLUMN `circleId` VARCHAR(200) NULL AFTER `creationTime`;

INSERT INTO `sss`.`space` (`id`) VALUES ('circleSpace');