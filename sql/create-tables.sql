CREATE TABLE `testprojekt`.`cities` (
  `id` INT NOT NULL COMMENT '',
  `name` VARCHAR(255) NULL COMMENT '',
  `latitude` DECIMAL NULL COMMENT '',
  `longitude` DECIMAL NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '');

CREATE TABLE `testprojekt`.`books` (
  `id` VARCHAR(36) NOT NULL COMMENT '',
  `name` VARCHAR(255) NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '');

CREATE TABLE `testprojekt`.`authors` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `name` VARCHAR(255) NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '');


CREATE TABLE `testprojekt`.`book_author` (
  `bookId` VARCHAR(36) NOT NULL COMMENT '',
  `authorId` INT NOT NULL COMMENT '',
  PRIMARY KEY (`bookId`, `authorId`)  COMMENT '',
  INDEX `author_idx` (`authorId` ASC)  COMMENT '',
  CONSTRAINT `author`
    FOREIGN KEY (`authorId`)
    REFERENCES `testprojekt`.`authors` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book`
    FOREIGN KEY (`bookId`)
    REFERENCES `testprojekt`.`books` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `testprojekt`.`book_city` (
  `bookId` VARCHAR(36) NOT NULL COMMENT '',
  `cityId` INT NOT NULL COMMENT '',
  PRIMARY KEY (`bookId`, `cityId`)  COMMENT '',
  INDEX `city_idx` (`cityId` ASC)  COMMENT '',
  CONSTRAINT `book_c`
    FOREIGN KEY (`bookId`)
    REFERENCES `testprojekt`.`books` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `city`
    FOREIGN KEY (`cityId`)
    REFERENCES `testprojekt`.`cities` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `testprojekt`.`cities` 
DROP COLUMN `longitude`,
DROP COLUMN `latitude`,
ADD COLUMN `location` POINT NULL COMMENT '' AFTER `name`;
