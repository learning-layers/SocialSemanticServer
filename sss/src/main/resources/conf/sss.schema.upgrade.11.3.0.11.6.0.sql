CREATE TABLE `sss`.`mail` (
  `mailId` VARCHAR(255) NOT NULL COMMENT '',
  `receiverEmail` VARCHAR(255) NOT NULL COMMENT '',
  PRIMARY KEY (`mailId`)  COMMENT '',
  CONSTRAINT `mailIdFKmail`
    FOREIGN KEY (`mailId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

ALTER TABLE `sss`.`mail` 
ADD COLUMN `hash` VARCHAR(255) NOT NULL COMMENT '' AFTER `receiverEmail`;