CREATE TABLE `sss`.`recommuserrealms` (
  `userId` VARCHAR(255) NOT NULL,
  `realm` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`userId`, `realm`),
  CONSTRAINT `userIdFKrecommuserrealms`
    FOREIGN KEY (`userId`)
    REFERENCES `sss`.`user` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
