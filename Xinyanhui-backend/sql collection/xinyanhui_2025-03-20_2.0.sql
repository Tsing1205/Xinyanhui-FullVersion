-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 8.154.40.232    Database: xinyanhui
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `Supervisors`
--

DROP TABLE IF EXISTS `Supervisors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Supervisors` (
  `supervisor_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `professional_info` text,
  `password_HashwithSalt` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  PRIMARY KEY (`supervisor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Supervisors`
--

/*!40000 ALTER TABLE `Supervisors` DISABLE KEYS */;
INSERT INTO `Supervisors` VALUES (24,'x12345',NULL,'KG8dztw2IlPl8NPVuWx+3JtXLyzb5qGbSFJssRH5WAw=','axYY5fZLIuHcCwqZObNqdw=='),(25,'y12345',NULL,'5eVX+bQhFv8xyB9dEwDnQ086bjrAcMj7slyLo1GyvjM=','I6xRjcxT9q9koOa/K2fKiQ=='),(26,'z12345',NULL,'bIVc5PYwrrz3OMGDKxsOnLq89Zr1TQlPRVheBrSmXzE=','0A6jdJzktj/mH6ARIuJR8g==');
/*!40000 ALTER TABLE `Supervisors` ENABLE KEYS */;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password_HashwithSalt` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `register_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (1, 'atest_user', 'msIeM51n51BNFXtwClRYCI50+acRXVHznQYZX6lIGog=', '8AirqsS6Qta7gZNe+inB1w==', 'leta@outlook.com', '168-3036-6101', '2025-03-20 09:04:48');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (2, 'btest_user', 's/3o4c8ym6jAaexc+FLdYq1NioOZB5NZDOgfUKEnGBU=', 'N0eLA6yAuwfr3iQteP+cSg==', 'letb@outlook.com', '168-3036-6102', '2025-03-20 10:43:31');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (3, 'ctest_user', 'lRZ8mK6r4DIWczuGOBVK1KU+paNsz1I0iQ6BHOzpakQ=', '/3kyohzBlLAXPXSX0H42fg==', 'letc@outlook.com', '168-3036-6103', '2025-03-20 10:43:31');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (4, 'dtest_user', 'aAsFCVdH5vNr1hYA1bXoNqbwOdCF7RLpk71KFewD1Ok=', 'sl2gr5KlUGpioue6VYLvxw==', 'letd@outlook.com', '168-3036-6104', '2025-03-20 10:43:31');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (5, 'etest_user', 'sNsBxrLDnaNiDm5kIOHBTh8GC1/CHhywqv2N+pX3PYM=', '131ZAFcPkojYGtK69Ghrbg==', 'lete@outlook.com', '168-3036-6105', '2025-03-20 10:44:49');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (6, 'ftest_user', 'wGgCt3PiT0jwDhBosMljGkIYWoKD11Gsj7kmA+xgQAc=', 'VQFmtPdcSHgNrc/GxujeHA==', 'letf@outlook.com', '168-3036-6106', '2025-03-20 10:44:49');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (7, 'gtest_user', 'OYXhuBJ+oyVtGEtaKR/Ydf3rRgv8pofP3gqMJLRhYk4=', '4OAgXiKJD1ddx3yuBHt6Zw==4OAgXiKJD1ddx3yuBHt6Zw==', 'letg@outlook.com', '168-3036-6107', '2025-03-20 10:44:49');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (8, 'htest_user', '2esLPYGVB9VpQI5xNcUl0XWYZYy6bN7VwHI2v+73P5o=', 'ZCxY6ep1tsg2J32ARzecYQ==', 'leth@outlook.com', '168-3036-6108', '2025-03-20 10:45:37');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (9, 'itest_user', 'eXhPVbI1NQtcpMgA7vmyDdUUusSescRrwY6xa89tqAk=', '/wlM0ZR4b3TRNtMEGPXpyQ==', 'leti@outlook.com', '168-3036-6109', '2025-03-20 10:46:34');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (10, 'jtest_user', 'laCyhCMa2Gu9+yqMcqZarTmzHKIOWbVQJT2KAKDWTEQ=', 'JMkUTZYaiX1c+7FpxrI8zA==', 'letj@outlook.com', '168-3036-6110', '2025-03-20 10:46:34');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (11, 'ktest_user', 'v9oNoC0AZVwJva/ybizNvB9sVEFjp1blVWJt/opQCmM=', 'm9RK/kOZIh0NQ3XRY6eHvg==', 'letk@outlook.com', '168-3036-6111', '2025-03-20 10:46:34');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (12, 'ltest_user', 'T0KyVvs6t1+8ins5SNu9VnMbH7HO3rAPJVLkij0eRhU=', 'CsPFKUmmbsl58YCCL+HOMg==', 'letl@outlook.com', '168-3036-6112', '2025-03-20 10:47:55');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (13, 'mtest_user', 'zdtNa2+nWG+yJSMa2f9fcmqfZahOZsnu/TJK1O0gT9g=', 'w/QGBnsrzq3cEIIpYyxb5Q==', 'letm@outlook.com', '168-3036-6113', '2025-03-20 10:47:55');
INSERT INTO `users` (`user_id`, `username`, `password_HashwithSalt`, `salt`, `email`, `phone`, `register_date`) VALUES (14, 'ntest_user', 'a7mz1auygmACq/ur2WXdh65flJyvlZrP0vZAym2Su4U=', '1byQYEf3TENecR1gNs6Tpw==', 'letn@outlook.com', '168-3036-6114', '2025-03-20 10:47:55');

/*!40000 ALTER TABLE `Users` ENABLE KEYS */;

--
-- Table structure for table `Admins`
--

DROP TABLE IF EXISTS `Admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Admins` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password_HashwithSalt` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=454 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Admins`
--

/*!40000 ALTER TABLE `Admins` DISABLE KEYS */;
INSERT INTO `Admins` VALUES (401,'杨柳依','6PqE5nGuFeJCMy8WSirzlJC3cUde270DJHvassucMXI=','5+5bIRtBn7R81FBleEOILA=='),(406,'曾妮','lhnKb3TAdU9YAjLAk7tALanqhxkxPFQawFiAR0UsxNk=','+2n2de/ewwZUWGU3bz0bQg=='),(411,'王家沂','A0kQiEqdJMzsd2sDMPp+zFEDyIIgKrKdKUkzW4TnNF4=','kqc8ElDkDjMQUiM5YogjRw=='),(416,'林子岚','ViPX3GDqid83yxO3mRJqUSE/FR4zZU2K2NU5J7mY7RQ=','ySQCVJPlxXH2Cv89APvn+Q=='),(441,'韩希媛','+QdiOEoM0H+l+gEFb64jyuhOhrAzkQIzbHGvShHquZU=','eqMgO+MeBGu5w9mce+376g=='),(453,'刘珊珊','HMr74KJ/rhqKq16HBMgBKEKx+mZUmN2syBrWkJaGJXo=','8UrC71U+5L6iHrozV7HKkQ==');
/*!40000 ALTER TABLE `Admins` ENABLE KEYS */;

--
-- Table structure for table `Appointments`
--

--
-- Table structure for table `Consultants`
--

DROP TABLE IF EXISTS `Consultants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Consultants` (
  `consultant_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `professional_info` text,
  `password_HashwithSalt` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  PRIMARY KEY (`consultant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Consultants`
--

/*!40000 ALTER TABLE `Consultants` DISABLE KEYS */;
INSERT INTO `Consultants` VALUES (1,'a12345',NULL,'VHvWbDSdlptvyvyUlsm12JlniWNevoZWP7+1uwJSFfQ=','baVcmscvWdZYnKRPk/jJJg=='),(2,'b12345',NULL,'fLtQIeNKXjqkzqWo80wvG6pIKjeBpcfcitfIJKL6/40=','3Ah+N0McCilWRmWZL7Tb+Q=='),(3,'c12345',NULL,'++WjCPSDaPzYIfHu6ARsGzvetzbZD6mi/HP/juzo18w=','YzeLI5dS6tclZp4/iSgFUg=='),(4,'d12345',NULL,'Mp015uXnbgc89PShu6l46CwRB0OR5HEDjA24lXgOBNk=','3lmuJjVgO/LwYtWODpokWA=='),(5,'e12345',NULL,'VUP9oFS9jdUH710oHHrpKw9fO7Iw3BSPmNl7hYz7k54=','IMUGnDvYvqG2nj1bzIehTA=='),(6,'f12345',NULL,'gCkzdsaGh5fXxW4b+zdmRxkvhdplKrkRKCxoQu7wm/g=','3xsj2ae2NBRREAvN39t3NA=='),(7,'g12345',NULL,'jjNLCVhcSZZV6r9HZB1eC8LHHw13I7KgMUVhFzvAOgQ=','EX7rfNwuon6gbIu1bxZUUQ=='),(8,'h12345',NULL,'8VVpZglgjDjg6mvj2qMAQnqg7sVyXKsrjowXYMuWgTc=','we9sf2Pqc/G13BALcqpqOQ=='),(9,'i12345',NULL,'PMjWgrsNOn5Dbb44N4S4tFb8qvaCDRfruyEhFrOhPdc=','lZpSyKNvNAfOGXt+caV6og=='),(10,'j12345',NULL,'qQqARAa39mHZX7Yxhucqvdo2t1GdDGpK1tg16jhPrLg=','FhUX6n1/5oK9EIcDtPDJjg=='),(11,'k12345',NULL,'h7Vt7eDzU0CEAKZ4wnF4s8wqzMqZWfdOLgJDIGB7Mu8=','9UohjpET1kwKGJk74cyD2A=='),(12,'l12345',NULL,'ZrDLKjJku+C829I/6ZZZxZLpXd7wTDJ37NCPZqyGGK8=','BJfXXLLf6BSKHxQDfzu/RQ=='),(13,'m12345',NULL,'l826/fopztw+ttGYE4+XVi5x0DYUt+gew1MmDUr0GmY=','CZFm3nRDclccX5eIO29RSw=='),(14,'n12345',NULL,'gQZIstF85f680hx0zQSEID4YrVPUMF9Igq2jsl0iLE8=','MuVujgCibRzE0goakxmONg=='),(15,'o12345',NULL,'XrYvA/57Ac82dZRqbR604RCg1R5MeqBQopu0LiSSOKU=','j66rbk/M6FtJvkP3HuMQlw=='),(16,'p12345',NULL,'66BtNGw6tvMS4B4cCB3v5uPQr8k9aRBUb/pYRqIn0yg=','n5qOHjkhSFWyHR/GR2ORlQ=='),(17,'q12345',NULL,'SjK9TDE7bxF3FKGdgIdXId4YP7yBk1XptxUxISZV4G8=','bjlq088QdYQ1nriDyaFgZQ=='),(18,'r12345',NULL,'C3gzSM3TRmOrtVdAqhFed4qDjDt2iHJUeNfhkrnDuv4=','a5sTzwsOvGlHfRKbHFrRQg=='),(19,'s12345',NULL,'xxEiK+32J4WnV0yCDy0C/TRyzC4QLVaPKObF53s0LYY=','SQLeg9RCrfaS9wMhOJvaIQ=='),(20,'t12345',NULL,'gZmNV889E5q9GZmMYTi89fQiogSs/nVFeWZJtGJ7V1o=','vJZwtcp2eZOTWtcIL5nPqw=='),(21,'u12345',NULL,'FTw5OBoVSvGOpWXU9GOA1SK3MFOzxzhBySrxQHgCAEs=','ZvvHTdY4dQtfYED/5teA+w=='),(22,'v12345',NULL,'Uxyh1TWqy0gNIkt7KLEHZ/1Lyb4IwtNX9TqWUaQxudM=','GJdC4aFZ8SOL4FjKuMALmw=='),(23,'w12345',NULL,'du0EeDei2vfK6TuddSGYCDlemk27qm5G+Dx18EyJqqI=','7msFT4g4kLyZj+D2YCYDTw==');
/*!40000 ALTER TABLE `Consultants` ENABLE KEYS */;

DROP TABLE IF EXISTS `Appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Appointments` (
  `appointment_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `consultant_id` int NOT NULL,
  `appointment_date` date NOT NULL,
  `appointment_time` time NOT NULL,
  `booking_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('booked','canceled','completed') NOT NULL,
  `cancellation_time` datetime DEFAULT NULL,
  `cancellation_reason` text,
  PRIMARY KEY (`appointment_id`),
  KEY `user_id` (`user_id`),
  KEY `consultant_id` (`consultant_id`),
  CONSTRAINT `Appointments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`),
  CONSTRAINT `Appointments_ibfk_2` FOREIGN KEY (`consultant_id`) REFERENCES `Consultants` (`consultant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Appointments`
--

/*!40000 ALTER TABLE `Appointments` DISABLE KEYS */;
INSERT INTO `Appointments` VALUES (1,1,1,'2025-03-21','12:00:00','2025-03-20 00:00:00','booked',NULL,NULL),(2,1,1,'2025-03-20','11:00:00','2025-03-20 10:21:57','booked',NULL,NULL),(3,1,1,'2025-03-21','10:00:00','2025-03-20 10:23:28','booked',NULL,NULL),(4,5,3,'2025-03-21','18:00:00','2025-03-20 10:53:22','booked',NULL,NULL);
/*!40000 ALTER TABLE `Appointments` ENABLE KEYS */;

--
-- Table structure for table `ConsultantSchedules`
--

DROP TABLE IF EXISTS `ConsultantSchedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ConsultantSchedules` (
  `schedule_id` int NOT NULL AUTO_INCREMENT,
  `consultant_id` int NOT NULL,
  `available_date` date NOT NULL,
  `start_time` int NOT NULL,
  `end_time` int NOT NULL,
  `slot_capacity` int DEFAULT '5',
  `status` enum('normal','request','leave') NOT NULL DEFAULT 'normal' COMMENT '正常，申请（请假），请假',
  `note` text,
  PRIMARY KEY (`schedule_id`),
  KEY `consultant_id` (`consultant_id`),
  CONSTRAINT `ConsultantSchedules_ibfk_1` FOREIGN KEY (`consultant_id`) REFERENCES `Consultants` (`consultant_id`),
  CONSTRAINT `ConsultantSchedules_chk_1` CHECK (((`start_time` >= 0) and (`start_time` <= 24))),
  CONSTRAINT `ConsultantSchedules_chk_2` CHECK (((`end_time` >= 0) and (`end_time` <= 24)))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ConsultantSchedules`
--

/*!40000 ALTER TABLE `ConsultantSchedules` DISABLE KEYS */;
INSERT INTO `ConsultantSchedules` VALUES (1,1,'2025-03-19',8,20,5,'normal',NULL),(2,1,'2025-03-20',8,10,5,'normal',NULL),(3,1,'2025-03-20',12,15,5,'normal',NULL),(4,2,'2025-03-21',8,12,5,'normal',NULL),(5,5,'2025-03-21',13,18,5,'normal',NULL),(6,3,'2025-03-21',12,20,5,'normal',NULL);
/*!40000 ALTER TABLE `ConsultantSchedules` ENABLE KEYS */;


--
-- Table structure for table `ConsultationSessions`
--

DROP TABLE IF EXISTS `ConsultationSessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ConsultationSessions` (
  `session_id` int NOT NULL AUTO_INCREMENT,
  `appointment_id` int DEFAULT NULL,
  `user_id` int NOT NULL,
  `consultant_id` int NOT NULL,
  `start_time` datetime NOT NULL,
  `chat_log` text NOT NULL,
  `session_status` enum('active','completed','ready') NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `rating` tinyint DEFAULT NULL,
  `feedback` text,
  PRIMARY KEY (`session_id`),
  KEY `user_id` (`user_id`),
  KEY `consultant_id` (`consultant_id`),
  KEY `appointment_id` (`appointment_id`),
  CONSTRAINT `ConsultationSessions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`),
  CONSTRAINT `ConsultationSessions_ibfk_2` FOREIGN KEY (`consultant_id`) REFERENCES `Consultants` (`consultant_id`),
  CONSTRAINT `ConsultationSessions_ibfk_3` FOREIGN KEY (`appointment_id`) REFERENCES `Appointments` (`appointment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ConsultationSessions`
--

/*!40000 ALTER TABLE `ConsultationSessions` DISABLE KEYS */;
INSERT INTO `ConsultationSessions` VALUES (1,1,1,1,'2025-03-20 12:00:00','1111','active','2025-03-20 13:00:00',NULL,NULL);
/*!40000 ALTER TABLE `ConsultationSessions` ENABLE KEYS */;

--
-- Table structure for table `PsychologicalEvaluations`
--

DROP TABLE IF EXISTS `PsychologicalEvaluations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PsychologicalEvaluations` (
  `evaluation_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `evaluation_date` datetime NOT NULL,
  `questionnaire_results` json NOT NULL,
  `auto_report` text,
  `risk_level` enum('low','medium','high') NOT NULL,
  `suggestions` text,
  PRIMARY KEY (`evaluation_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `PsychologicalEvaluations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PsychologicalEvaluations`
--

/*!40000 ALTER TABLE `PsychologicalEvaluations` DISABLE KEYS */;
/*!40000 ALTER TABLE `PsychologicalEvaluations` ENABLE KEYS */;

--
-- Table structure for table `SupervisorConsultations`
--

DROP TABLE IF EXISTS `SupervisorConsultations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SupervisorConsultations` (
  `record_id` int NOT NULL AUTO_INCREMENT,
  `consultant_id` int NOT NULL,
  `supervisor_id` int NOT NULL,
  `session_id` int NOT NULL,
  `request_time` datetime NOT NULL,
  `response_time` datetime DEFAULT NULL,
  `chat_log` text NOT NULL,
  PRIMARY KEY (`record_id`),
  KEY `consultant_id` (`consultant_id`),
  KEY `supervisor_id` (`supervisor_id`),
  KEY `session_id` (`session_id`),
  CONSTRAINT `SupervisorConsultations_ibfk_1` FOREIGN KEY (`consultant_id`) REFERENCES `Consultants` (`consultant_id`),
  CONSTRAINT `SupervisorConsultations_ibfk_2` FOREIGN KEY (`supervisor_id`) REFERENCES `Supervisors` (`supervisor_id`),
  CONSTRAINT `SupervisorConsultations_ibfk_3` FOREIGN KEY (`session_id`) REFERENCES `ConsultationSessions` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SupervisorConsultations`
--

/*!40000 ALTER TABLE `SupervisorConsultations` DISABLE KEYS */;
/*!40000 ALTER TABLE `SupervisorConsultations` ENABLE KEYS */;

--
-- Table structure for table `SupervisorSchedules`
--

DROP TABLE IF EXISTS `SupervisorSchedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SupervisorSchedules` (
  `schedule_id` int NOT NULL AUTO_INCREMENT,
  `supervisor_id` int NOT NULL,
  `available_date` date NOT NULL,
  `start_time` int NOT NULL,
  `end_time` int NOT NULL,
  `slot_capacity` int DEFAULT '5',
  PRIMARY KEY (`schedule_id`),
  KEY `supervisor_id` (`supervisor_id`),
  CONSTRAINT `SupervisorSchedules_ibfk_1` FOREIGN KEY (`supervisor_id`) REFERENCES `Supervisors` (`supervisor_id`),
  CONSTRAINT `SupervisorSchedules_chk_1` CHECK (((`start_time` >= 0) and (`start_time` <= 24))),
  CONSTRAINT `SupervisorSchedules_chk_2` CHECK (((`end_time` >= 0) and (`end_time` <= 24)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SupervisorSchedules`
--

/*!40000 ALTER TABLE `SupervisorSchedules` DISABLE KEYS */;
/*!40000 ALTER TABLE `SupervisorSchedules` ENABLE KEYS */;


--
-- Dumping routines for database 'xinyanhui'
--

--
-- Final view structure for view `ConsultantsAvailable`
--


-- Dump completed on 2025-03-20 11:04:00
