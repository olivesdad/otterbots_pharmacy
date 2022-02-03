-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema pharmacy
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema pharmacy
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pharmacy` ;
USE `pharmacy` ;

-- -----------------------------------------------------
-- Table `pharmacy`.`doctor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy`.`doctor` (
  `doctor_id` INT NOT NULL AUTO_INCREMENT,
  `ssn` CHAR(11) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `specialty` VARCHAR(45) NOT NULL,
  `practice_since` DATE NOT NULL,
  PRIMARY KEY (`doctor_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy`.`patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy`.`patient` (
  `patient_id` INT NOT NULL AUTO_INCREMENT,
  `ssn` CHAR(11) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `dob` DATE NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `doctorid` INT NOT NULL,
  PRIMARY KEY (`patient_id`),
  CONSTRAINT `fk_patient_doctorid`
    FOREIGN KEY (`doctorid`)
    REFERENCES `pharmacy`.`doctor` (`doctor_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy`.`pharmcompany`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy`.`pharmcompany` (
  `pharm_co_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `phone` CHAR(10) NOT NULL,
  PRIMARY KEY (`pharm_co_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy`.`drug`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy`.`drug` (
  `drug_id` INT NOT NULL AUTO_INCREMENT,
  `trade_name` VARCHAR(45) NOT NULL,
  `formula` VARCHAR(45) NOT NULL,
  `pharm_id` INT NOT NULL,
  PRIMARY KEY (`drug_id`),
  CONSTRAINT `fk_drug_pharmid`
    FOREIGN KEY (`pharm_id`)
    REFERENCES `pharmacy`.`pharmcompany` (`pharm_co_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy`.`pharmacy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy`.`pharmacy` (
  `pharmacy_id` INT NOT NULL AUTO_INCREMENT,
  `phone` CHAR(10) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`pharmacy_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy`.`rxprice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy`.`rxprice` (
  `pharmacy_id` INT NOT NULL,
  `drug_id` INT NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  PRIMARY KEY (`pharmacy_id`, `drug_id`),
  CONSTRAINT `fk_rxprice_pharmacy1`
    FOREIGN KEY (`pharmacy_id`)
    REFERENCES `pharmacy`.`pharmacy` (`pharmacy_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rxprice_drug1`
    FOREIGN KEY (`drug_id`)
    REFERENCES `pharmacy`.`drug` (`drug_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy`.`prescription`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy`.`prescription` (
  `rx_number` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `patient_id` INT NOT NULL,
  `drug_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `filled_date` DATE NULL,
  `pharmacy_id` INT NULL,
  PRIMARY KEY (`rx_number`),
  CONSTRAINT `fk_prescription_doctorid`
    FOREIGN KEY (`doctor_id`)
    REFERENCES `pharmacy`.`doctor` (`doctor_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_prescription_patientid`
    FOREIGN KEY (`patient_id`)
    REFERENCES `pharmacy`.`patient` (`patient_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prescription_pharmacy1`
    FOREIGN KEY (`pharmacy_id`)
    REFERENCES `pharmacy`.`pharmacy` (`pharmacy_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prescription_drug1`
    FOREIGN KEY (`drug_id`)
    REFERENCES `pharmacy`.`drug` (`drug_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy`.`supervisor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy`.`supervisor` (
  `supervisor_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`supervisor_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy`.`contract`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy`.`contract` (
  `pharcom_id` INT NOT NULL,
  `pharmacy_id` INT NOT NULL,
  `supervisor_id` INT NOT NULL,
  `contractual_term` VARCHAR(45) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  PRIMARY KEY (`pharcom_id`, `pharmacy_id`),
  CONSTRAINT `fk_contract_pharmid`
    FOREIGN KEY (`pharcom_id`)
    REFERENCES `pharmacy`.`pharmcompany` (`pharm_co_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_contract_supervisorid`
    FOREIGN KEY (`supervisor_id`)
    REFERENCES `pharmacy`.`supervisor` (`supervisor_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_contract_pharmacy1`
    FOREIGN KEY (`pharmacy_id`)
    REFERENCES `pharmacy`.`pharmacy` (`pharmacy_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
