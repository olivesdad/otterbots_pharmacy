-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema OtterBot_Pharma
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema OtterBot_Pharma
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `OtterBot_Pharma` ;
USE `OtterBot_Pharma` ;

-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`doctor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`doctor` (
  `ssn` VARCHAR(9) NOT NULL,
  `license_date` DATE NOT NULL,
  `specialty` VARCHAR(45) NULL,
  PRIMARY KEY (`ssn`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`patient` (
  `ssn` VARCHAR(9) NOT NULL,
  `primary_care_giver` VARCHAR(11) NOT NULL,
  `patient_name` VARCHAR(45) NOT NULL,
  `dob` DATE NOT NULL,
  PRIMARY KEY (`ssn`),
  CONSTRAINT `pimary_care_giver`
    FOREIGN KEY (`primary_care_giver`)
    REFERENCES `OtterBot_Pharma`.`doctor` (`ssn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`pharm_company`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`pharm_company` (
  `name` VARCHAR(50) NOT NULL,
  `phone_number` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`drug`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`drug` (
  `name` VARCHAR(50) NOT NULL,
  `pharm_company_name` VARCHAR(50) NOT NULL,
  `generic_formula` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`name`),
  CONSTRAINT `fk_Drug_Supplier1`
    FOREIGN KEY (`pharm_company_name`)
    REFERENCES `OtterBot_Pharma`.`pharm_company` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`pharmacy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`pharmacy` (
  `phone_number` VARCHAR(10) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`phone_number`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`rxprice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`rxprice` (
  `pharmacy_phone_number` VARCHAR(10) NOT NULL,
  `drug_name` VARCHAR(50) NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  PRIMARY KEY (`drug_name`, `pharmacy_phone_number`),
  CONSTRAINT `fk_Pharmacy_has_Drug_Pharmacy1`
    FOREIGN KEY (`pharmacy_phone_number`)
    REFERENCES `OtterBot_Pharma`.`pharmacy` (`phone_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pharmacy_has_Drug_Drug1`
    FOREIGN KEY (`drug_name`)
    REFERENCES `OtterBot_Pharma`.`drug` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`rx`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`rx` (
  `rx_number` INT NOT NULL AUTO_INCREMENT,
  `patient_ssn` VARCHAR(11) NOT NULL,
  `drug_name` VARCHAR(45) NOT NULL,
  `doctor_ssn` VARCHAR(9) NOT NULL,
  `quantity` INT NOT NULL,
  `filled_by` VARCHAR(10) NOT NULL,
  `filled_date` DATE NULL,
  PRIMARY KEY (`rx_number`),
  CONSTRAINT `fk_Patient_has_Doctor_Patient1`
    FOREIGN KEY (`patient_ssn`)
    REFERENCES `OtterBot_Pharma`.`patient` (`ssn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Patient_has_Doctor_Doctor1`
    FOREIGN KEY (`doctor_ssn`)
    REFERENCES `OtterBot_Pharma`.`doctor` (`ssn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Prescription_Pharmacy1`
    FOREIGN KEY (`filled_by`)
    REFERENCES `OtterBot_Pharma`.`pharmacy` (`phone_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Prescription_Drug1`
    FOREIGN KEY (`drug_name`)
    REFERENCES `OtterBot_Pharma`.`drug` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`supervisor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`supervisor` (
  `supervisor_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`supervisor_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`contract`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`contract` (
  `pharmacy_phone_number` VARCHAR(10) NOT NULL,
  `pharm_company_name` VARCHAR(50) NOT NULL,
  `supervisor_id` INT NOT NULL,
  `end_date` DATE NOT NULL,
  `start_date` DATE NOT NULL,
  PRIMARY KEY (`pharmacy_phone_number`, `pharm_company_name`),
  CONSTRAINT `fk_Pharmacy_has_pharm_company_Pharmacy1`
    FOREIGN KEY (`pharmacy_phone_number`)
    REFERENCES `OtterBot_Pharma`.`pharmacy` (`phone_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pharmacy_has_pharm_company_pharm_company1`
    FOREIGN KEY (`pharm_company_name`)
    REFERENCES `OtterBot_Pharma`.`pharm_company` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pharmacy_has_pharm_company_table11`
    FOREIGN KEY (`supervisor_id`)
    REFERENCES `OtterBot_Pharma`.`supervisor` (`supervisor_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
