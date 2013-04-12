CREATE DATABASE  IF NOT EXISTS `wnc` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `wnc`;
-- MySQL dump 10.13  Distrib 5.6.10, for Win64 (x86_64)
--
-- Host: localhost    Database: wnc
-- ------------------------------------------------------
-- Server version	5.6.10-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activatedchallenges`
--

DROP TABLE IF EXISTS `activatedchallenges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activatedchallenges` (
  `UserName1` varchar(15) NOT NULL,
  `UserName2` varchar(15) NOT NULL,
  `ChallengeID` int(11) NOT NULL,
  `Winner` varchar(15) DEFAULT NULL,
  `ExpirationDate` date NOT NULL,
  PRIMARY KEY (`UserName1`,`ChallengeID`,`UserName2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activatedchallenges`
--

LOCK TABLES `activatedchallenges` WRITE;
/*!40000 ALTER TABLE `activatedchallenges` DISABLE KEYS */;
/*!40000 ALTER TABLE `activatedchallenges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `challengelist`
--

DROP TABLE IF EXISTS `challengelist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `challengelist` (
  `ChallengeID` int(11) NOT NULL,
  `ChallengeName` varchar(20) DEFAULT NULL,
  `Description` varchar(300) DEFAULT NULL,
  `PointValue` int(11) DEFAULT NULL,
  `ChallengeLifeSpan` int(11) DEFAULT NULL,
  PRIMARY KEY (`ChallengeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `challengelist`
--

LOCK TABLES `challengelist` WRITE;
/*!40000 ALTER TABLE `challengelist` DISABLE KEYS */;
/*!40000 ALTER TABLE `challengelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membershiplvl`
--

DROP TABLE IF EXISTS `membershiplvl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `membershiplvl` (
  `Name` int(11) NOT NULL,
  `Description` varchar(100) NOT NULL,
  `Cost` decimal(10,0) NOT NULL,
  `StartDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membershiplvl`
--

LOCK TABLES `membershiplvl` WRITE;
/*!40000 ALTER TABLE `membershiplvl` DISABLE KEYS */;
/*!40000 ALTER TABLE `membershiplvl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrcompleted`
--

DROP TABLE IF EXISTS `qrcompleted`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrcompleted` (
  `UserName` varchar(15) NOT NULL,
  `QRID` int(11) NOT NULL,
  `DateTime` datetime NOT NULL,
  PRIMARY KEY (`UserName`,`QRID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrcompleted`
--

LOCK TABLES `qrcompleted` WRITE;
/*!40000 ALTER TABLE `qrcompleted` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrcompleted` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrs`
--

DROP TABLE IF EXISTS `qrs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrs` (
  `QRID` int(11) NOT NULL,
  `QRCode` varchar(100) NOT NULL,
  `Description` varchar(300) NOT NULL,
  `Lat` double NOT NULL,
  `Long` double NOT NULL,
  `PointValue` int(11) NOT NULL,
  PRIMARY KEY (`QRID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrs`
--

LOCK TABLES `qrs` WRITE;
/*!40000 ALTER TABLE `qrs` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tracks`
--

DROP TABLE IF EXISTS `tracks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tracks` (
  `TrackNum` int(11) NOT NULL,
  `Time` time NOT NULL,
  `Lat` double NOT NULL,
  `Long` double NOT NULL,
  `TrackType` varchar(20) NOT NULL,
  `UserName` varchar(15) NOT NULL,
  PRIMARY KEY (`TrackNum`,`Time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tracks`
--

LOCK TABLES `tracks` WRITE;
/*!40000 ALTER TABLE `tracks` DISABLE KEYS */;
/*!40000 ALTER TABLE `tracks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraddress`
--

DROP TABLE IF EXISTS `useraddress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraddress` (
  `Address` varchar(80) NOT NULL,
  `City` varchar(45) NOT NULL,
  `State` varchar(45) NOT NULL,
  `Zip` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraddress`
--

LOCK TABLES `useraddress` WRITE;
/*!40000 ALTER TABLE `useraddress` DISABLE KEYS */;
/*!40000 ALTER TABLE `useraddress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `Username` varchar(15) NOT NULL,
  `MembershipID` int(11) NOT NULL,
  `FirstName` varchar(15) NOT NULL,
  `LastName` varchar(15) NOT NULL,
  `Phone` varchar(14) NOT NULL,
  `EmailAddress` varchar(40) NOT NULL,
  `Contributions` decimal(10,0) DEFAULT NULL,
  `BirthDate` date NOT NULL,
  `Gender` char(1) DEFAULT NULL,
  `Height` int(11) DEFAULT NULL,
  `Weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`Username`),
  UNIQUE KEY `Username_UNIQUE` (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-04-11 20:43:56
