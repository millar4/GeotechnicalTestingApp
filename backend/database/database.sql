-- ============================================
-- 1 Create / Use database
-- ============================================
CREATE DATABASE IF NOT EXISTS GeotechnicalTests;
USE GeotechnicalTests;

-- ============================================
-- 2 Create GeotechnicalTable
-- ============================================
CREATE TABLE IF NOT EXISTS GeotechnicalTable (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    myGroup VARCHAR(100),
    classification VARCHAR(100),
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
    specimenMaxGrainFraction VARCHAR(100),
    databaseBelongsTo VARCHAR(100)
);


CREATE TABLE IF NOT EXISTS InSituTable (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    myGroup VARCHAR(100),
    test VARCHAR(1000),
    symbol VARCHAR(10),
    parameters VARCHAR(1000),
    testMethod VARCHAR(100),
    alt1 VARCHAR(100),
    alt2 VARCHAR(100),
    alt3 VARCHAR(100),
    materials VARCHAR(1000),
    applications VARCHAR(1000),
    databaseBelongsTo VARCHAR(1000)
);

-- ============================================
-- 2 Create RocksTable
-- ============================================
CREATE TABLE IF NOT EXISTS RocksTable(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    myGroup VARCHAR(100),
    classification VARCHAR(100),
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
    specimenMaxGrainFraction VARCHAR(100),
    schedulingNotes TEXT(300),
    databaseBelongsTo VARCHAR(100)
);

-- ============================================
-- 2 Create ConcreteTable
-- ============================================
CREATE TABLE IF NOT EXISTS ConcreteTable(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    myGroup VARCHAR(100),
    classification VARCHAR(100),
    test VARCHAR(1000),
    symbol VARCHAR(10),
    parameters VARCHAR(1000),
    testMethod VARCHAR(100),
    alt1 VARCHAR(100),
    alt2 VARCHAR(100),
    alt3 VARCHAR(100),
    sampleType VARCHAR(300),
    fieldSampleMass VARCHAR(100),
    specimenType VARCHAR(1000),
    specimenMass VARCHAR(100),
    specimenNumbers VARCHAR(100),
    specimenD VARCHAR(100),
    specimenL VARCHAR(100),
    specimenW VARCHAR(100),
    specimenH VARCHAR(100),
    specimenMaxGrainSize VARCHAR(100),
    specimenMaxGrainFraction VARCHAR(100),
    schedulingNotes TEXT(300),
    databaseBelongsTo VARCHAR(100)
);

-- ============================================
-- 3 Create AggregateTable
-- ============================================
CREATE TABLE IF NOT EXISTS AggregateTable (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    myGroup VARCHAR(100),
    classification VARCHAR(100),
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
    schedulingNotes TEXT(300),
    databaseBelongsTo VARCHAR(100)
);

-- ============================================
-- 4 Load CSV data into AggregateTable
-- ============================================
LOAD DATA INFILE '/var/lib/mysql-files/aggregate.csv'
INTO TABLE AggregateTable
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(
    myGroup, classification, test, symbol, parameters, testMethod,
    sampleType, fieldSampleMass, specimenType,
    specimenMass, specimenNumbers,
    specimenMaxGrainSize, specimenMaxGrainFraction,
    schedulingNotes, databaseBelongsTo
)
SET 
    myGroup = NULLIF(myGroup, 'NULL'),
    classification = NULLIF(classification, 'NULL'),
    test = NULLIF(test, 'NULL'),
    symbol = NULLIF(symbol, 'NULL'),
    parameters = NULLIF(parameters, 'NULL'),
    testMethod = NULLIF(testMethod, 'NULL'),
    sampleType = NULLIF(sampleType, 'NULL'),
    fieldSampleMass = NULLIF(fieldSampleMass, 'NULL'),
    specimenType = NULLIF(specimenType, 'NULL'),
    specimenMass = NULLIF(specimenMass, 'NULL'),
    specimenNumbers = NULLIF(specimenNumbers, 'NULL'),
    specimenMaxGrainSize = NULLIF(specimenMaxGrainSize, 'NULL'),
    specimenMaxGrainFraction = NULLIF(specimenMaxGrainFraction, 'NULL'),
    schedulingNotes = NULLIF(TRIM(schedulingNotes), 'NULL');

-- ============================================
-- 4 Load CSV data into RocksTable
-- ============================================
LOAD DATA INFILE '/var/lib/mysql-files/rockParameters.csv'
INTO TABLE RocksTable
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(
    myGroup, classification, test, symbol, parameters, testMethod,
    alt1, alt2, alt3, sampleType, fieldSampleMass,
    specimenType, specimenMass, specimenNumbers,
    specimenD, specimenL, specimenW, specimenH,
    specimenMaxGrainSize, specimenMaxGrainFraction,
    schedulingNotes, databaseBelongsTo
)
SET 
    myGroup = NULLIF(myGroup, 'NULL'),
    classification = NULLIF(classification, 'NULL'),
    test = NULLIF(test, 'NULL'),
    symbol = NULLIF(symbol, 'NULL'),
    parameters = NULLIF(parameters, 'NULL'),
    testMethod = NULLIF(testMethod, 'NULL'),
    alt1 = NULLIF(alt1, 'NULL'),
    alt2 = NULLIF(alt2, 'NULL'),
    alt3 = NULLIF(alt3, 'NULL'),
    sampleType = NULLIF(sampleType, 'NULL'),
    fieldSampleMass = NULLIF(fieldSampleMass, 'NULL'),
    specimenType = NULLIF(specimenType, 'NULL'),
    specimenMass = NULLIF(specimenMass, 'NULL'),
    specimenNumbers = NULLIF(specimenNumbers, 'NULL'),
    specimenD = NULLIF(specimenD, 'NULL'),
    specimenL = NULLIF(specimenL, 'NULL'),
    specimenW = NULLIF(specimenW, 'NULL'),
    specimenH = NULLIF(specimenH, 'NULL'),
    specimenMaxGrainSize = NULLIF(specimenMaxGrainSize, 'NULL'),
    specimenMaxGrainFraction = NULLIF(specimenMaxGrainFraction, 'NULL'),
    schedulingNotes = NULLIF(schedulingNotes, 'NULL'),
    databaseBelongsTo = NULLIF(databaseBelongsTo, 'NULL');  

-- ============================================
-- 5 Load CSV data into GeotechnicalTable
-- ============================================
LOAD DATA INFILE '/var/lib/mysql-files/parameters2.csv'
INTO TABLE GeotechnicalTable
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(
    myGroup, classification, test, symbol, parameters, testMethod,
    alt1, alt2, alt3, sampleType, fieldSampleMass,
    specimenType, specimenMass, specimenNumbers,
    specimenD, specimenL, specimenW, specimenH,
    specimenMaxGrainSize, specimenMaxGrainFraction, databaseBelongsTo
)
SET 
    myGroup = NULLIF(myGroup, 'NULL'),
    classification = NULLIF(classification, 'NULL'),
    test = NULLIF(test, 'NULL'),
    symbol = NULLIF(symbol, 'NULL'),
    parameters = NULLIF(parameters, 'NULL'),
    testMethod = NULLIF(testMethod, 'NULL'),
    alt1 = NULLIF(alt1, 'NULL'),
    alt2 = NULLIF(alt2, 'NULL'),
    alt3 = NULLIF(alt3, 'NULL'),
    sampleType = NULLIF(sampleType, 'NULL'),
    fieldSampleMass = NULLIF(fieldSampleMass, 'NULL'),
    specimenType = NULLIF(specimenType, 'NULL'),
    specimenMass = NULLIF(specimenMass, 'NULL'),
    specimenNumbers = NULLIF(specimenNumbers, 'NULL'),
    specimenD = NULLIF(specimenD, 'NULL'),
    specimenL = NULLIF(specimenL, 'NULL'),
    specimenW = NULLIF(specimenW, 'NULL'),
    specimenH = NULLIF(specimenH, 'NULL'),
    specimenMaxGrainSize = NULLIF(specimenMaxGrainSize, 'NULL'),
    specimenMaxGrainFraction = NULLIF(specimenMaxGrainFraction, 'NULL'),
    databaseBelongsTo = NULLIF(databaseBelongsTo, 'NULL');  

-- ============================================
-- 5 Load CSV data into ConcreteTable
-- ============================================
LOAD DATA INFILE '/var/lib/mysql-files/concreteParameters.csv'
INTO TABLE ConcreteTable
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(
    myGroup, test, symbol, parameters, testMethod,
    alt1, alt2, alt3, sampleType, fieldSampleMass,
    specimenType, specimenMass, specimenNumbers,
    specimenD, specimenL, specimenW, specimenH,
    specimenMaxGrainSize, specimenMaxGrainFraction,
    schedulingNotes, databaseBelongsTo
)
SET 
    myGroup = NULLIF(myGroup, 'NULL'),
    test = NULLIF(test, 'NULL'),
    symbol = NULLIF(symbol, 'NULL'),
    parameters = NULLIF(parameters, 'NULL'),
    testMethod = NULLIF(testMethod, 'NULL'),
    alt1 = NULLIF(alt1, 'NULL'),
    alt2 = NULLIF(alt2, 'NULL'),
    alt3 = NULLIF(alt3, 'NULL'),
    sampleType = NULLIF(sampleType, 'NULL'),
    fieldSampleMass = NULLIF(fieldSampleMass, 'NULL'),
    specimenType = NULLIF(specimenType, 'NULL'),
    specimenMass = NULLIF(specimenMass, 'NULL'),
    specimenNumbers = NULLIF(specimenNumbers, 'NULL'),
    specimenD = NULLIF(specimenD, 'NULL'),
    specimenL = NULLIF(specimenL, 'NULL'),
    specimenW = NULLIF(specimenW, 'NULL'),
    specimenH = NULLIF(specimenH, 'NULL'),
    specimenMaxGrainSize = NULLIF(specimenMaxGrainSize, 'NULL'),
    specimenMaxGrainFraction = NULLIF(specimenMaxGrainFraction, 'NULL'),
    schedulingNotes = NULLIF(schedulingNotes, 'NULL'),
    databaseBelongsTo = NULLIF(databaseBelongsTo, 'NULL');  

-- ============================================
-- 5 Load CSV data into InSituTable
-- ============================================
LOAD DATA INFILE '/var/lib/mysql-files/inSituParameters.csv'
INTO TABLE InSituTable
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(
    myGroup, test, symbol, parameters, testMethod,
    alt1, alt2, alt3, materials, applications,databaseBelongsTo
)
SET  
    myGroup = NULLIF(myGroup, 'NULL'),
    test = NULLIF(test, 'NULL'),
    symbol = NULLIF(symbol, 'NULL'),
    parameters = NULLIF(parameters, 'NULL'),
    testMethod = NULLIF(testMethod, 'NULL'),
    alt1 = NULLIF(alt1, 'NULL'),
    alt2 = NULLIF(alt2, 'NULL'),
    alt3 = NULLIF(alt3, 'NULL'),
    materials = NULLIF(materials, 'NULL'),
    applications = NULLIF(applications, 'NULL'),
    databaseBelongsTo = NULLIF(databaseBelongsTo, 'NULL');  

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

INSERT IGNORE INTO users (username, password, role)
VALUES ('admin', '$2a$10$/ai62630Vx53JzK61CaW0O7z7yY9jF3SPQwj8HD/y6yYlA/2AMuy2', 'ADMIN');
