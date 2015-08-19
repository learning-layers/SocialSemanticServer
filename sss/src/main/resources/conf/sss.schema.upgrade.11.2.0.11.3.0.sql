CREATE TABLE `sss`.`circleinvitees` (
  `circleId` VARCHAR(255) NOT NULL,
  `inviteeId` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`circleId`, `inviteeId`),
  CONSTRAINT `circleIdFKcircleinvitees`
    FOREIGN KEY (`circleId`)
    REFERENCES `sss`.`circle` (`circleId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
