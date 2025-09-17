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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` (`id`, `first_name`, `last_name`, `timezone`) VALUES (1,'Elena','Nelson','Asia/Kolkata'),(2,'Grace','Ortiz','America/New_York'),(3,'Emily','Hernandez','America/New_York'),(4,'Maya','Peterson','Atlantic/Reykjavik');
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
INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES (1,'1','Init','SQL','V1__Init.sql',-195151338,'healthcare_user','2025-09-15 20:59:41',315,1),(2,'2','Fix names','SQL','V2__Fix_names.sql',-419225767,'healthcare_user','2025-09-15 22:00:20',91,1),(3,'3','Fix names final','SQL','V3__Fix_names_final.sql',1511562010,'healthcare_user','2025-09-15 22:09:01',47,1),(4,'4','Drop data','SQL','V4__Drop_data.sql',145782112,'healthcare_user','2025-09-17 17:55:10',30,1),(5,'5','Drop column','SQL','V5__Drop_column.sql',1660891123,'healthcare_user','2025-09-17 18:35:38',55,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` (`id`, `first_name`, `last_name`) VALUES (1,'Scarlett','Lopez'),(2,'Leah','Martin'),(3,'Isabella','Mitchell'),(4,'Lillian','Cooper'),(5,'Ava','Wood'),(6,'Eliana','Collins'),(7,'Nova','Reyes'),(8,'Gianna','Parker'),(9,'Grace','Cook');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;

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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit`
--

/*!40000 ALTER TABLE `visit` DISABLE KEYS */;
INSERT INTO `visit` (`id`, `patient_id`, `doctor_id`, `start_date_time`, `end_date_time`) VALUES (1,1,2,'2025-09-12 14:00:00','2025-09-12 15:00:00'),(2,1,1,'2025-08-22 06:00:00','2025-08-22 07:00:00'),(3,2,1,'2025-08-21 12:00:00','2025-08-21 13:00:00'),(4,2,3,'2025-09-17 10:00:00','2025-09-17 11:00:00'),(5,2,2,'2025-09-02 11:00:00','2025-09-02 12:00:00'),(6,3,3,'2025-09-11 06:00:00','2025-09-11 07:00:00'),(7,3,1,'2025-09-16 14:00:00','2025-09-16 15:00:00'),(8,3,4,'2025-09-07 12:00:00','2025-09-07 13:00:00'),(9,4,1,'2025-09-09 11:00:00','2025-09-09 12:00:00'),(11,4,3,'2025-08-29 06:00:00','2025-08-29 07:00:00'),(12,5,2,'2025-09-11 11:00:00','2025-09-11 12:00:00'),(13,5,1,'2025-08-31 12:00:00','2025-08-31 13:00:00'),(15,6,1,'2025-09-09 12:00:00','2025-09-09 13:00:00'),(16,7,2,'2025-09-08 10:00:00','2025-09-08 11:00:00'),(17,8,2,'2025-08-28 10:00:00','2025-08-28 11:00:00'),(18,8,1,'2025-09-07 11:00:00','2025-09-07 12:00:00'),(19,8,3,'2025-08-25 06:00:00','2025-08-25 07:00:00'),(20,9,3,'2025-08-20 06:00:00','2025-08-20 07:00:00'),(21,9,1,'2025-09-09 06:00:00','2025-09-09 07:00:00'),(22,9,2,'2025-08-24 09:00:00','2025-08-24 10:00:00');
/*!40000 ALTER TABLE `visit` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-17 21:37:42
