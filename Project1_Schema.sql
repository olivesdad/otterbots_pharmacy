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
  `doctorssn` VARCHAR(9) NOT NULL,
  `licensedate` DATE NOT NULL,
  `specialty` VARCHAR(45) NULL,
  `doctorname` VARCHAR(45) NULL,
  PRIMARY KEY (`doctorssn`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`patient` (
  `patientssn` VARCHAR(9) NOT NULL,
  `doctorssn` VARCHAR(11) NOT NULL,
  `patientname` VARCHAR(45) NOT NULL,
  `dob` DATE NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`patientssn`),
  CONSTRAINT `pimary_care_giver`
    FOREIGN KEY (`doctorssn`)
    REFERENCES `OtterBot_Pharma`.`doctor` (`doctorssn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`pharmcompany`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`pharmcompany` (
  `pharmcompanyname` VARCHAR(50) NOT NULL,
  `phonenumber` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`pharmcompanyname`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`drug`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`drug` (
  `name` VARCHAR(50) NOT NULL,
  `pharmcompanyname` VARCHAR(50) NOT NULL,
  `genericformula` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`name`),
  CONSTRAINT `fk_Drug_Supplier1`
    FOREIGN KEY (`pharmcompanyname`)
    REFERENCES `OtterBot_Pharma`.`pharmcompany` (`pharmcompanyname`)
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
  `pharmacyphonenumber` VARCHAR(10) NOT NULL,
  `drug_name` VARCHAR(50) NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  PRIMARY KEY (`drug_name`, `pharmacyphonenumber`),
  CONSTRAINT `fk_Pharmacy_has_Drug_Pharmacy1`
    FOREIGN KEY (`pharmacyphonenumber`)
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
  `rxnumber` INT NOT NULL AUTO_INCREMENT,
  `patientssn` VARCHAR(11) NOT NULL,
  `drugname` VARCHAR(45) NOT NULL,
  `doctorssn` VARCHAR(9) NOT NULL,
  `quantity` INT NOT NULL,
  `pharmacyphonenumber` VARCHAR(10) NOT NULL,
  `filleddate` DATE NULL,
  PRIMARY KEY (`rxnumber`),
  CONSTRAINT `fk_Patient_has_Doctor_Patient1`
    FOREIGN KEY (`patientssn`)
    REFERENCES `OtterBot_Pharma`.`patient` (`patientssn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Patient_has_Doctor_Doctor1`
    FOREIGN KEY (`doctorssn`)
    REFERENCES `OtterBot_Pharma`.`doctor` (`doctorssn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Prescription_Pharmacy1`
    FOREIGN KEY (`pharmacyphonenumber`)
    REFERENCES `OtterBot_Pharma`.`pharmacy` (`phone_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Prescription_Drug1`
    FOREIGN KEY (`drugname`)
    REFERENCES `OtterBot_Pharma`.`drug` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`supervisor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`supervisor` (
  `supervisorid` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`supervisorid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`contract`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`contract` (
  `pharmacyphonenumber` VARCHAR(10) NOT NULL,
  `pharmcompanyname` VARCHAR(50) NOT NULL,
  `supervisorid` INT NOT NULL,
  `enddate` DATE NOT NULL,
  `startdate` DATE NOT NULL,
  PRIMARY KEY (`pharmacyphonenumber`, `pharmcompanyname`),
  CONSTRAINT `fk_Pharmacy_has_pharm_company_Pharmacy1`
    FOREIGN KEY (`pharmacyphonenumber`)
    REFERENCES `OtterBot_Pharma`.`pharmacy` (`phone_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pharmacy_has_pharm_company_pharm_company1`
    FOREIGN KEY (`pharmcompanyname`)
    REFERENCES `OtterBot_Pharma`.`pharmcompany` (`pharmcompanyname`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pharmacy_has_pharm_company_table11`
    FOREIGN KEY (`supervisorid`)
    REFERENCES `OtterBot_Pharma`.`supervisor` (`supervisorid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
