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