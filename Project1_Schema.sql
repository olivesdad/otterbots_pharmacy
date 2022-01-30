
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
  `doctoid` INT NOT NULL AUTO_INCREMENT,
  `doctorssn` CHAR(11) NOT NULL,
  `doctorname` VARCHAR(45) NOT NULL,
  `specialty` VARCHAR(45) NOT NULL,
  `licensedate` DATE NOT NULL,
  PRIMARY KEY (`doctoid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`patient` (
  `patientid` INT NOT NULL AUTO_INCREMENT,
  `patientssn` CHAR(11) NOT NULL,
  `patientname` VARCHAR(45) NOT NULL,
  `dob` DATE NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `doctoid` INT NOT NULL,
  PRIMARY KEY (`patientid`),
  CONSTRAINT `fk_patient_doctorid`
    FOREIGN KEY (`doctoid`)
    REFERENCES `OtterBot_Pharma`.`doctor` (`doctoid`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`pharmcompany`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`pharmcompany` (
  `pharmid` INT NOT NULL AUTO_INCREMENT,
  `phone` CHAR(10) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`pharmid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`drug`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`drug` (
  `drugtradename` VARCHAR(45) NOT NULL,
  `genericformula` VARCHAR(45) NOT NULL,
  `pharmid` INT NOT NULL,
  PRIMARY KEY (`drugtradename`),
  CONSTRAINT `fk_drug_pharmid`
    FOREIGN KEY (`pharmid`)
    REFERENCES `OtterBot_Pharma`.`pharmcompany` (`pharmid`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`pharmacy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`pharmacy` (
  `pharmacyphone` CHAR(10) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`pharmacyphone`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`rxprice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`rxprice` (
  `pharmacyphone` CHAR(10) NOT NULL,
  `drugtradename` VARCHAR(45) NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  PRIMARY KEY (`drugtradename`, `pharmacyphone`),
  CONSTRAINT price_chk CHECK ( price > 0 AND price <100000 ),
  CONSTRAINT `fk_rxprice_pharmacyphone`
    FOREIGN KEY (`pharmacyphone`)
    REFERENCES `OtterBot_Pharma`.`pharmacy` (`pharmacyphone`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_rxprice_drugtradename`
    FOREIGN KEY (`drugtradename`)
    REFERENCES `OtterBot_Pharma`.`drug` (`drugtradename`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OtterBot_Pharma`.`prescription`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OtterBot_Pharma`.`prescription` (
  `rxnumber` INT NOT NULL AUTO_INCREMENT,
  `doctor_doctoid` INT NOT NULL,
  `drugtradename` VARCHAR(45) NOT NULL,
  `quantity` INT NOT NULL,
  `pharmacyphone` VARCHAR(10) NULL,
  `filleddate` DATE NULL,
  `patient_patientid` INT NOT NULL,
  PRIMARY KEY (`rxnumber`, `doctor_doctoid`, `patient_patientid`),
  CONSTRAINT `fk_prescription_pharmacyphone`
    FOREIGN KEY (`pharmacyphone`)
    REFERENCES `OtterBot_Pharma`.`pharmacy` (`pharmacyphone`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_prescription_drugtradename`
    FOREIGN KEY (`drugtradename`)
    REFERENCES `OtterBot_Pharma`.`drug` (`drugtradename`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_prescription_doctorid`
    FOREIGN KEY (`doctor_doctoid`)
    REFERENCES `OtterBot_Pharma`.`doctor` (`doctoid`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_prescription_patientid`
    FOREIGN KEY (`patient_patientid`)
    REFERENCES `OtterBot_Pharma`.`patient` (`patientid`)
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
  `pharmacyphone` CHAR(10) NOT NULL,
  `pharmid` INT NOT NULL,
  `supervisorid` INT NOT NULL,
  `contractualterm` VARCHAR(45) NOT NULL,
  `startdate` DATE NOT NULL,
  `enddate` DATE NOT NULL,
  PRIMARY KEY (`pharmacyphone`, `pharmid`),
  CONSTRAINT `fk_contract_pharmacyphone`
    FOREIGN KEY (`pharmacyphone`)
    REFERENCES `OtterBot_Pharma`.`pharmacy` (`pharmacyphone`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_contract_pharmid`
    FOREIGN KEY (`pharmid`)
    REFERENCES `OtterBot_Pharma`.`pharmcompany` (`pharmid`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_contract_supervisorid`
    FOREIGN KEY (`supervisorid`)
    REFERENCES `OtterBot_Pharma`.`supervisor` (`supervisorid`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

