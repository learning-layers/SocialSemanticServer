CREATE TABLE `sss`.`video` (
  `videoId` VARCHAR(200) NOT NULL,
  `genre` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`videoId`),
  CONSTRAINT `videoIdFKvideo`
    FOREIGN KEY (`videoId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

ALTER TABLE `sss`.`videos` 
DROP FOREIGN KEY `entityIdFKvideos`,
DROP FOREIGN KEY `videoIdFKvideos`;
ALTER TABLE `sss`.`videos` 
ADD INDEX `videoIdFKentityvideos_idx` (`videoId` ASC),
DROP PRIMARY KEY;
ALTER TABLE `sss`.`videos` 
ADD CONSTRAINT `entityIdFKentityvideos`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `videoIdFKentityvideos`
  FOREIGN KEY (`videoId`)
  REFERENCES `sss`.`video` (`videoId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `sss`.`videos` 
RENAME TO  `sss`.`entityvideos` ;

ALTER TABLE `sss`.`images` 
DROP FOREIGN KEY `entityIdFKimages`,
DROP FOREIGN KEY `imageIdFKimages`;
ALTER TABLE `sss`.`images` 
ADD CONSTRAINT `entityIdFKentityimages`
  FOREIGN KEY (`entityId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `imageIdFKentityimages`
  FOREIGN KEY (`imageId`)
  REFERENCES `sss`.`entity` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `sss`.`images` 
RENAME TO  `sss`.`entityimages` ;

CREATE TABLE `sss`.`videoannotation` (
  `videoAnnotationId` VARCHAR(200) NOT NULL,
  `timePoint` VARCHAR(200) NOT NULL,
  `x` VARCHAR(200) NOT NULL,
  `y` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`videoAnnotationId`),
  CONSTRAINT `videoAnnotationIdvideoannotation`
    FOREIGN KEY (`videoAnnotationId`)
    REFERENCES `sss`.`entity` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE `sss`.`videoannotations` (
  `videoId` VARCHAR(200) NOT NULL,
  `videoAnnotationId` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`videoId`, `videoAnnotationId`),
  INDEX `videoAnnotationIdFKvideoannotations_idx` (`videoAnnotationId` ASC),
  CONSTRAINT `videoIdFKvideoannotations`
    FOREIGN KEY (`videoId`)
    REFERENCES `sss`.`video` (`videoId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `videoAnnotationIdFKvideoannotations`
    FOREIGN KEY (`videoAnnotationId`)
    REFERENCES `sss`.`videoannotation` (`videoAnnotationId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);