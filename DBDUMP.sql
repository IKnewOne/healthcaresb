-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: healthcare
-- ------------------------------------------------------
-- Server version	8.1.0

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
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `timezone` varchar(50) NOT NULL,
  `total_patients` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` (`id`, `first_name`, `last_name`, `timezone`, `total_patients`) VALUES (1,'Doctor','Healovich','America/New_York',2),(2,'Hermaeus','Mora','Atlantic/Reykjavik',1),(3,'Willow','Edwards','Europe/London',4),(4,'Penelope','Kelly','Europe/Moscow',2),(5,'Camila','Brown','Europe/Moscow',5),(6,'Elizabeth','Myers','Europe/London',5);
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES (1,'1','Init','SQL','V1__Init.sql',-195151338,'healthcare_user','2025-09-15 20:59:41',315,1),(2,'2','Fix names','SQL','V2__Fix_names.sql',-419225767,'healthcare_user','2025-09-15 22:00:20',91,1),(3,'3','Fix names final','SQL','V3__Fix_names_final.sql',1511562010,'healthcare_user','2025-09-15 22:09:01',47,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` (`id`, `first_name`, `last_name`) VALUES (1,'Patient','Damaged'),(2,'Handless','Cook'),(3,'Scarlett','Hill'),(4,'Sophia','Scott'),(5,'Sophia','Brown'),(6,'Olivia','Ramos'),(7,'Ivy','Long'),(8,'Sophie','Harris'),(9,'Ella','Flores'),(10,'Emily','Cox'),(11,'Camila','Brooks');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;

--
-- Table structure for table `patient_doctor_data`
--

DROP TABLE IF EXISTS `patient_doctor_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_doctor_data` (
  `patient_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `last_visit_id` bigint NOT NULL,
  PRIMARY KEY (`patient_id`,`doctor_id`),
  KEY `fk_summary_visit` (`last_visit_id`),
  KEY `idx_summary_doctor` (`doctor_id`),
  CONSTRAINT `fk_summary_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `fk_summary_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`),
  CONSTRAINT `fk_summary_visit` FOREIGN KEY (`last_visit_id`) REFERENCES `visit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_doctor_data`
--

/*!40000 ALTER TABLE `patient_doctor_data` DISABLE KEYS */;
INSERT INTO `patient_doctor_data` (`patient_id`, `doctor_id`, `last_visit_id`) VALUES (1,1,4),(2,1,8),(1,2,9),(3,5,10),(4,4,11),(4,3,12),(4,6,13),(5,4,14),(5,6,15),(6,3,16),(7,6,17),(7,5,18),(8,3,19),(9,6,20),(9,5,21),(10,5,22),(11,6,23),(11,5,24),(11,3,25);
/*!40000 ALTER TABLE `patient_doctor_data` ENABLE KEYS */;

--
-- Table structure for table `visit`
--

DROP TABLE IF EXISTS `visit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `start_date_time` datetime NOT NULL,
  `end_date_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_doctor` (`doctor_id`),
  KEY `idx_visits_patient_doctor_start` (`patient_id`,`doctor_id`,`start_date_time` DESC),
  CONSTRAINT `fk_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `fk_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit`
--

/*!40000 ALTER TABLE `visit` DISABLE KEYS */;
INSERT INTO `visit` (`id`, `patient_id`, `doctor_id`, `start_date_time`, `end_date_time`) VALUES (4,1,1,'2017-12-28 19:25:05','2017-12-28 20:25:05'),(5,1,1,'2017-11-28 19:25:05','2017-11-28 20:25:05'),(6,1,1,'2017-10-28 19:25:05','2017-10-28 20:25:05'),(7,1,1,'2017-10-18 19:25:05','2017-10-18 20:25:05'),(8,2,1,'2017-10-17 19:25:05','2017-10-17 20:25:05'),(9,1,2,'2017-10-17 23:25:05','2017-10-18 00:25:05'),(10,3,5,'2025-08-30 09:00:00','2025-08-30 10:00:00'),(11,4,4,'2025-09-11 13:00:00','2025-09-11 14:00:00'),(12,4,3,'2025-08-21 13:00:00','2025-08-21 14:00:00'),(13,4,6,'2025-09-07 06:00:00','2025-09-07 07:00:00'),(14,5,4,'2025-09-10 06:00:00','2025-09-10 07:00:00'),(15,5,6,'2025-08-28 10:00:00','2025-08-28 11:00:00'),(16,6,3,'2025-08-18 09:00:00','2025-08-18 10:00:00'),(17,7,6,'2025-09-12 13:00:00','2025-09-12 14:00:00'),(18,7,5,'2025-09-01 13:00:00','2025-09-01 14:00:00'),(19,8,3,'2025-09-04 10:00:00','2025-09-04 11:00:00'),(20,9,6,'2025-09-07 13:00:00','2025-09-07 14:00:00'),(21,9,5,'2025-08-23 07:00:00','2025-08-23 08:00:00'),(22,10,5,'2025-08-25 13:00:00','2025-08-25 14:00:00'),(23,11,6,'2025-08-29 08:00:00','2025-08-29 09:00:00'),(24,11,5,'2025-08-18 14:00:00','2025-08-18 15:00:00'),(25,11,3,'2025-08-31 06:00:00','2025-08-31 07:00:00');
/*!40000 ALTER TABLE `visit` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-16  2:37:04
