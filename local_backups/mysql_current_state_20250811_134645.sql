-- MySQL dump 10.13  Distrib 8.4.6, for Linux (aarch64)
--
-- Host: mysql    Database: GeotechnicalTests
-- ------------------------------------------------------
-- Server version	8.4.6

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `AggregateTable`
--

DROP TABLE IF EXISTS `AggregateTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AggregateTable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `myGroup` varchar(100) DEFAULT NULL,
  `classification` varchar(100) DEFAULT NULL,
  `test` varchar(1000) DEFAULT NULL,
  `testAlsoKnownAs` varchar(1000) DEFAULT NULL,
  `symbol` varchar(100) DEFAULT NULL,
  `parameters` varchar(1000) DEFAULT NULL,
  `testMethod` varchar(100) DEFAULT NULL,
  `sampleType` varchar(30) DEFAULT NULL,
  `fieldSampleMass` varchar(100) DEFAULT NULL,
  `specimenType` varchar(1000) DEFAULT NULL,
  `specimenMass` varchar(100) DEFAULT NULL,
  `specimenNumbers` varchar(100) DEFAULT NULL,
  `specimenMaxGrainSize` varchar(100) DEFAULT NULL,
  `specimenMaxGrainFraction` varchar(100) DEFAULT NULL,
  `schedulingNotes` text,
  `databaseBelongsTo` varchar(100) DEFAULT NULL,
  `imagePath` varchar(100) DEFAULT NULL,
  `testDescription` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AggregateTable`
--

LOCK TABLES `AggregateTable` WRITE;
/*!40000 ALTER TABLE `AggregateTable` DISABLE KEYS */;
INSERT INTO `AggregateTable` VALUES (1,'Physical & Geometrical Properties',NULL,'Water content',NULL,'w',NULL,'BS EN 1097-5:2008','Bulk (loose material)',NULL,'Bulk','0.2 0.2Dmax','1','<1 >1',NULL,NULL,'aggregate',NULL,'NULL\r'),(2,'Physical & Geometrical Properties',NULL,'Particle size distribution',NULL,'%fines G',NULL,'BS EN 933-1:2012','Bulk (loose material)',NULL,'Bulk','1 2 3 30 80','1','4 8 16 32 90',NULL,NULL,'aggregate',NULL,'NULL\r'),(3,'Physical & Geometrical Properties',NULL,'Flakiness index',NULL,'FI',NULL,'BS EN 933-3:2012','Bulk (loose material)',NULL,'Bulk','5 10 20 40 80',NULL,'8 16 32 63 100','4/100',NULL,'aggregate',NULL,'NULL\r'),(4,'Physical & Geometrical Properties','K5','Methylene blue',NULL,'MB',NULL,'BS EN 933-9:2022','Bulk (loose material)',NULL,'Bulk','0.2 0.03','2','2 0.125','0/2 0/0.125',NULL,'aggregate',NULL,'NULL\r'),(5,'Physical & Geometrical Properties','K132','Bulk density & voids',NULL,'BDV',NULL,'BS EN 1097-3:1998','Bulk (loose material)',NULL,'Bulk','2 8 15 30','3','4 16 32 63',NULL,NULL,'aggregate',NULL,'NULL\r'),(6,'Physical & Geometrical Properties',NULL,'Particle density & water absorption',NULL,'PD&WA',NULL,'BS EN 1097-6:2022','Bulk (loose material)',NULL,'Bulk','0.5 1 2 5 7 15','1','4 8 16 32 45 63','0/4 4/32 32/63','Define the fraction to be tested','aggregate',NULL,'NULL\r'),(7,'Physical & Geometrical Properties',NULL,'Constituents of recycled aggregates',NULL,'Rc Ru Rb Ra Rg X',NULL,'BS EN 933-11:2009','Bulk (loose material)',NULL,'Bulk','20 50','1','32 63','4/63',NULL,'aggregate',NULL,'NULL\r'),(8,'Physical & Geometrical Properties',NULL,'Metal content of municipal incinerator bottom ash aggregates',NULL,'PF PNF',NULL,'BS EN 1744-8:2012','Bulk (loose material)',NULL,'Bulk',NULL,NULL,NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(9,'Strength & Durability','K133','Resistance to wear (micro-Deval coefficient)',NULL,'MDE MDS MDRB','Micro-Deval coefficient','BS EN 1097-1:2023','Bulk (loose material)',NULL,'Bulk','0.5','2',NULL,'10/14','alternative fractions are 0.2/2 0.2/4 0.2/5.6 4/6.3 4/8 6.3/10 8/11.2 and 11.2/16','aggregate',NULL,'NULL\r'),(10,'Strength & Durability','MULL','Resistance to fragmentation (Los Angeles coefficient) ',NULL,'LA LARB','LA coefficient','BS EN 1097-2:2020','Bulk (loose material)',NULL,'Bulk','15','1',NULL,'10/14','alternative fractions are 4/6.3 4/8 6.3/10 8/11.2 and 11.2/16','aggregate',NULL,'NULL\r'),(11,'Strength & Durability','K128','Magnesium sulphate value',NULL,'MS','Magnesium sulphate value','BS 1367-2:2009','Bulk (loose material)',NULL,'Bulk','0.5','2',NULL,'10/14','Alternative fractions are 20/28 14/20 6/10 5/6 3/5 2/3 1/2 and 0.3/0.6','aggregate',NULL,'NULL\r'),(12,'Strength & Durability',NULL,'Aggregate crushing value',NULL,'ACV','Aggregate crushing value','BS 812:110','Bulk (loose material)',NULL,'Bulk','45 60','3','20 40','10/14',NULL,'aggregate',NULL,'NULL\r'),(13,'Strength & Durability',NULL,'Ten percent fines value ',NULL,'TFV','Ten percent fines value ','BS 812:111','Bulk (loose material)',NULL,'Bulk','45 60','3','20 40',NULL,NULL,'aggregate',NULL,'NULL\r'),(14,'Strength & Durability','K134','Aggregate abrasion value ',NULL,'AAV','Aggregate abrasion value ','BS EN 1097-8:2020','Bulk (loose material)',NULL,'Bulk','2','2',NULL,'10/14',NULL,'aggregate',NULL,'NULL\r'),(15,'Strength & Durability','K135','Polished stone value ',NULL,'PSV','Polished stone value ','BS EN 1097-8:2020','Bulk (loose material)',NULL,'Bulk','2','2',NULL,'10/14',NULL,'aggregate',NULL,'NULL\r'),(16,'Chemical & Petrography',NULL,'Water-soluble chloride content',NULL,'C','Water-soluble chloride content','BS EN 1744-1:8 (2009+A1:2012)','Bulk (loose material)',NULL,'Bulk','5 15 35 50','2','22 32 45 63',NULL,NULL,'aggregate',NULL,'NULL\r'),(17,'Chemical & Petrography',NULL,'Acid-soluble chloride content',NULL,'C','Acid-soluble chloride content','BS EN 1744-1:9 (2009+A1:2012)','Bulk (loose material)',NULL,'Bulk',NULL,'2',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(18,'Chemical & Petrography',NULL,'Water-soluble sulphate content',NULL,'soluble SO3','Water-soluble sulphate content','BS EN 1744-1:10 (2009+A1:2012)','Bulk (loose material)',NULL,'Bulk',NULL,'2',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(19,'Chemical & Petrography',NULL,'Total sulphur',NULL,'S','Total sulphur','BS EN 1744-1:11 (2009+A1:2012)','Bulk (loose material)',NULL,'Bulk',NULL,'2',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(20,'Chemical & Petrography',NULL,'Acid-soluble sulphate content',NULL,'sulphate content','Acid-soluble sulphate content','BS EN 1744-1:12 (2009+A1:2012)','Bulk (loose material)',NULL,'Bulk',NULL,'2',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(21,'Chemical & Petrography',NULL,'Acid-soluble sulphide ',NULL,'S','Acid-soluble sulphide ','BS EN 1744-1:13 (2009+A1:2012)','Bulk (loose material)',NULL,'Bulk',NULL,'2',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(22,'Chemical & Petrography',NULL,'Carbonate content',NULL,'CaO CO2','Carbonate content','BS EN 196-2:2013','Bulk (loose material)',NULL,'Bulk',NULL,'2',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(23,'Chemical & Petrography',NULL,'Lightweight contaminators',NULL,'mLPC','Lightweight contaminators','BS EN 1744-1:14.2 (2009+A1:2012)','Bulk (loose material)',NULL,'Bulk',NULL,'2',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(24,'Chemical & Petrography',NULL,'Humus content',NULL,NULL,'Humus content','BS EN 1744-1:15.1  (2009+A1:2012)','Bulk (loose material)',NULL,'Bulk',NULL,'2',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(25,'Chemical & Petrography',NULL,'Fulvo acid content',NULL,NULL,'Fulvo acid content','BS EN 1744-1:15.2 (2009+A1:2012)','Bulk (loose material)',NULL,'Bulk',NULL,'2',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(26,'Chemical & Petrography',NULL,'Petrography',NULL,NULL,'Mineral characterisation & lithology','BS EN 932-3:2022','Bulk (loose material)',NULL,'Bulk','0.5 2 8 25 50','1','4 8 16 32 63',NULL,NULL,'aggregate',NULL,'NULL\r'),(27,'Chemical & Petrography',NULL,'SEM/EDS',NULL,NULL,'Mineral characterisation',NULL,'Bulk (loose material)',NULL,'Bulk','0.2','1',NULL,NULL,NULL,'aggregate',NULL,'NULL\r'),(28,'Chemical & Petrography',NULL,'Powder XRD',NULL,NULL,'Mineral characterisation',NULL,'Bulk (loose material)',NULL,'Bulk','0.2','1',NULL,NULL,NULL,'aggregate',NULL,NULL);
/*!40000 ALTER TABLE `AggregateTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ConcreteTable`
--

DROP TABLE IF EXISTS `ConcreteTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ConcreteTable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `myGroup` varchar(100) DEFAULT NULL,
  `classification` varchar(100) DEFAULT NULL,
  `test` varchar(1000) DEFAULT NULL,
  `testAlsoKnownAs` varchar(1000) DEFAULT NULL,
  `symbol` varchar(10) DEFAULT NULL,
  `parameters` varchar(1000) DEFAULT NULL,
  `testMethod` varchar(100) DEFAULT NULL,
  `alt1` varchar(100) DEFAULT NULL,
  `alt2` varchar(100) DEFAULT NULL,
  `alt3` varchar(100) DEFAULT NULL,
  `sampleType` varchar(300) DEFAULT NULL,
  `fieldSampleMass` varchar(100) DEFAULT NULL,
  `specimenType` varchar(1000) DEFAULT NULL,
  `specimenMass` varchar(100) DEFAULT NULL,
  `specimenNumbers` varchar(100) DEFAULT NULL,
  `specimenD` varchar(100) DEFAULT NULL,
  `specimenL` varchar(100) DEFAULT NULL,
  `specimenW` varchar(100) DEFAULT NULL,
  `specimenH` varchar(100) DEFAULT NULL,
  `specimenMaxGrainSize` varchar(100) DEFAULT NULL,
  `specimenMaxGrainFraction` varchar(100) DEFAULT NULL,
  `schedulingNotes` text,
  `databaseBelongsTo` varchar(100) DEFAULT NULL,
  `imagePath` varchar(100) DEFAULT NULL,
  `testDescription` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ConcreteTable`
--

LOCK TABLES `ConcreteTable` WRITE;
/*!40000 ALTER TABLE `ConcreteTable` DISABLE KEYS */;
/*!40000 ALTER TABLE `ConcreteTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EarthworksTable`
--

DROP TABLE IF EXISTS `EarthworksTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EarthworksTable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `myGroup` varchar(100) DEFAULT NULL,
  `classification` varchar(100) DEFAULT NULL,
  `test` varchar(1000) DEFAULT NULL,
  `testAlsoKnownAs` varchar(1000) DEFAULT NULL,
  `symbol` varchar(10) DEFAULT NULL,
  `parameters` varchar(1000) DEFAULT NULL,
  `testMethod` varchar(100) DEFAULT NULL,
  `alt1` varchar(100) DEFAULT NULL,
  `alt2` varchar(100) DEFAULT NULL,
  `alt3` varchar(100) DEFAULT NULL,
  `sampleType` varchar(30) DEFAULT NULL,
  `fieldSampleMass` varchar(100) DEFAULT NULL,
  `specimenType` varchar(1000) DEFAULT NULL,
  `specimenMass` varchar(100) DEFAULT NULL,
  `specimenNumbers` varchar(100) DEFAULT NULL,
  `specimenD` varchar(100) DEFAULT NULL,
  `specimenL` varchar(100) DEFAULT NULL,
  `specimenW` varchar(100) DEFAULT NULL,
  `specimenH` varchar(100) DEFAULT NULL,
  `specimenMaxGrainSize` varchar(100) DEFAULT NULL,
  `specimenMaxGrainFraction` varchar(100) DEFAULT NULL,
  `schedulingNotes` text,
  `databaseBelongsTo` varchar(100) DEFAULT NULL,
  `imagePath` varchar(100) DEFAULT NULL,
  `testDescription` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EarthworksTable`
--

LOCK TABLES `EarthworksTable` WRITE;
/*!40000 ALTER TABLE `EarthworksTable` DISABLE KEYS */;
/*!40000 ALTER TABLE `EarthworksTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GeotechnicalTable`
--

DROP TABLE IF EXISTS `GeotechnicalTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GeotechnicalTable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `myGroup` varchar(100) DEFAULT NULL,
  `classification` varchar(100) DEFAULT NULL,
  `test` varchar(1000) DEFAULT NULL,
  `testAlsoKnownAs` varchar(1000) DEFAULT NULL,
  `symbol` varchar(10) DEFAULT NULL,
  `parameters` varchar(1000) DEFAULT NULL,
  `testMethod` varchar(100) DEFAULT NULL,
  `alt1` varchar(100) DEFAULT NULL,
  `alt2` varchar(100) DEFAULT NULL,
  `alt3` varchar(100) DEFAULT NULL,
  `sampleType` varchar(30) DEFAULT NULL,
  `fieldSampleMass` varchar(100) DEFAULT NULL,
  `specimenType` varchar(1000) DEFAULT NULL,
  `specimenMass` varchar(100) DEFAULT NULL,
  `specimenNumbers` varchar(100) DEFAULT NULL,
  `specimenD` varchar(100) DEFAULT NULL,
  `specimenL` varchar(100) DEFAULT NULL,
  `specimenW` varchar(100) DEFAULT NULL,
  `specimenH` varchar(100) DEFAULT NULL,
  `specimenMaxGrainSize` varchar(100) DEFAULT NULL,
  `specimenMaxGrainFraction` varchar(100) DEFAULT NULL,
  `databaseBelongsTo` varchar(100) DEFAULT NULL,
  `imagePath` varchar(100) DEFAULT NULL,
  `testDescription` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GeotechnicalTable`
--

LOCK TABLES `GeotechnicalTable` WRITE;
/*!40000 ALTER TABLE `GeotechnicalTable` DISABLE KEYS */;
/*!40000 ALTER TABLE `GeotechnicalTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `InSituTable`
--

DROP TABLE IF EXISTS `InSituTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `InSituTable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `myGroup` varchar(100) DEFAULT NULL,
  `test` varchar(1000) DEFAULT NULL,
  `testAlsoKnownAs` varchar(1000) DEFAULT NULL,
  `symbol` varchar(10) DEFAULT NULL,
  `parameters` varchar(1000) DEFAULT NULL,
  `testMethod` varchar(100) DEFAULT NULL,
  `alt1` varchar(100) DEFAULT NULL,
  `alt2` varchar(100) DEFAULT NULL,
  `alt3` varchar(100) DEFAULT NULL,
  `materials` varchar(1000) DEFAULT NULL,
  `applications` varchar(1000) DEFAULT NULL,
  `databaseBelongsTo` varchar(1000) DEFAULT NULL,
  `imagePath` varchar(100) DEFAULT NULL,
  `testDescription` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `InSituTable`
--

LOCK TABLES `InSituTable` WRITE;
/*!40000 ALTER TABLE `InSituTable` DISABLE KEYS */;
/*!40000 ALTER TABLE `InSituTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RocksTable`
--

DROP TABLE IF EXISTS `RocksTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RocksTable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `myGroup` varchar(100) DEFAULT NULL,
  `classification` varchar(100) DEFAULT NULL,
  `test` varchar(1000) DEFAULT NULL,
  `testAlsoKnownAs` varchar(1000) DEFAULT NULL,
  `symbol` varchar(10) DEFAULT NULL,
  `parameters` varchar(1000) DEFAULT NULL,
  `testMethod` varchar(100) DEFAULT NULL,
  `alt1` varchar(100) DEFAULT NULL,
  `alt2` varchar(100) DEFAULT NULL,
  `alt3` varchar(100) DEFAULT NULL,
  `sampleType` varchar(30) DEFAULT NULL,
  `fieldSampleMass` varchar(100) DEFAULT NULL,
  `specimenType` varchar(1000) DEFAULT NULL,
  `specimenMass` varchar(100) DEFAULT NULL,
  `specimenNumbers` varchar(100) DEFAULT NULL,
  `specimenD` varchar(100) DEFAULT NULL,
  `specimenL` varchar(100) DEFAULT NULL,
  `specimenW` varchar(100) DEFAULT NULL,
  `specimenH` varchar(100) DEFAULT NULL,
  `specimenMaxGrainSize` varchar(100) DEFAULT NULL,
  `specimenMaxGrainFraction` varchar(100) DEFAULT NULL,
  `schedulingNotes` text,
  `databaseBelongsTo` varchar(100) DEFAULT NULL,
  `imagePath` varchar(100) DEFAULT NULL,
  `testDescription` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RocksTable`
--

LOCK TABLES `RocksTable` WRITE;
/*!40000 ALTER TABLE `RocksTable` DISABLE KEYS */;
/*!40000 ALTER TABLE `RocksTable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-11 13:46:45
