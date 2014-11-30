CREATE TABLE `sss`.`likes` (
  `userId` VARCHAR(200) NOT NULL,
  `entityId` VARCHAR(255) NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userId`, `entityId`),
  INDEX `entityIdFKlikes_idx` (`entityId` ASC),
  CONSTRAINT `userIdFKlikes`
    FOREIGN KEY (`userId`)
    REFERENCES `sss`.`user` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `entityIdFKlikes`
    FOREIGN KEY (`entityId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);