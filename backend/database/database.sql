-- ============================================
-- 1) Create / Use database
-- ============================================
CREATE DATABASE IF NOT EXISTS GeotechnicalTests;
USE GeotechnicalTests;

-- ============================================
-- 2) Create GeotechnicalTable
-- ============================================
CREATE TABLE IF NOT EXISTS GeotechnicalTable (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    myGroup VARCHAR(100),
    test VARCHAR(1000),
    symbol VARCHAR(10),
    parameters VARCHAR(1000),
    testMethod VARCHAR(100),
    alt1 VARCHAR(100),
    alt2 VARCHAR(100),
    alt3 VARCHAR(100),
    sampleType VARCHAR(30),
    fieldSampleMass VARCHAR(100),
    specimenType VARCHAR(1000),
    specimenMass VARCHAR(100),
    specimenNumbers VARCHAR(100),
    specimenD VARCHAR(100),
    specimenL VARCHAR(100),
    specimenW VARCHAR(100),
    specimenH VARCHAR(100),
    specimenMaxGrainSize VARCHAR(100),
    specimenMaxGrainFraction VARCHAR(100)
);

-- ============================================
-- 3) Create RocksTable
-- ============================================
CREATE TABLE IF NOT EXISTS RocksTable (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    myGroup VARCHAR(100),
    test VARCHAR(1000),
    symbol VARCHAR(100),
    parameters VARCHAR(1000),
    testMethod VARCHAR(100),
    sampleType VARCHAR(30),
    fieldSampleMass VARCHAR(100),
    specimenType VARCHAR(1000),
    specimenMass VARCHAR(100),
    specimenNumbers VARCHAR(100),
    specimenMaxGrainSize VARCHAR(100),
    specimenMaxGrainFraction VARCHAR(100),
    schedulingNotes VARCHAR(300)
);

-- ============================================
-- 4) Load CSV data into RocksTable
-- ============================================
LOAD DATA INFILE '/var/lib/mysql-files/rockParameters2.csv'
INTO TABLE RocksTable
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(
    myGroup, test, symbol, parameters, testMethod,
    sampleType, fieldSampleMass, specimenType,
    specimenMass, specimenNumbers,
    specimenMaxGrainSize, specimenMaxGrainFraction,
    schedulingNotes
);

-- ============================================
-- 5) Load CSV data into GeotechnicalTable
-- ============================================
LOAD DATA INFILE '/var/lib/mysql-files/parameters2.csv'
INTO TABLE GeotechnicalTable
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(
    myGroup, test, symbol, parameters, testMethod,
    alt1, alt2, alt3, sampleType, fieldSampleMass,
    specimenType, specimenMass, specimenNumbers,
    specimenD, specimenL, specimenW, specimenH,
    specimenMaxGrainSize, specimenMaxGrainFraction
);


CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

INSERT IGNORE INTO users (username, password, role)
VALUES ('admin', '$2a$10$/ai62630Vx53JzK61CaW0O7z7yY9jF3SPQwj8HD/y6yYlA/2AMuy2', 'ADMIN');
