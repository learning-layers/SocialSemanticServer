CREATE DATABASE  IF NOT EXISTS `sss` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `sss`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: sss
-- ------------------------------------------------------
-- Server version	5.6.10

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
-- Table structure for table `circle`
--

DROP TABLE IF EXISTS `circle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `circle` (
  `circleId` varchar(100) NOT NULL,
  `circleType` varchar(100) NOT NULL,
  PRIMARY KEY (`circleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `circle`
--

LOCK TABLES `circle` WRITE;
/*!40000 ALTER TABLE `circle` DISABLE KEYS */;
/*!40000 ALTER TABLE `circle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `circleentities`
--

DROP TABLE IF EXISTS `circleentities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `circleentities` (
  `circleId` varchar(100) NOT NULL,
  `entityId` varchar(100) NOT NULL,
  PRIMARY KEY (`circleId`,`entityId`),
  CONSTRAINT `circleIdFKcircleentities` FOREIGN KEY (`circleId`) REFERENCES `circle` (`circleId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `circleentities`
--

LOCK TABLES `circleentities` WRITE;
/*!40000 ALTER TABLE `circleentities` DISABLE KEYS */;
/*!40000 ALTER TABLE `circleentities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `circlerights`
--

DROP TABLE IF EXISTS `circlerights`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `circlerights` (
  `circleId` varchar(100) NOT NULL,
  `accessRight` varchar(100) NOT NULL,
  PRIMARY KEY (`circleId`,`accessRight`),
  CONSTRAINT `circleIdFKcirclerights` FOREIGN KEY (`circleId`) REFERENCES `circle` (`circleId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `circlerights`
--

LOCK TABLES `circlerights` WRITE;
/*!40000 ALTER TABLE `circlerights` DISABLE KEYS */;
/*!40000 ALTER TABLE `circlerights` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `circleusers`
--

DROP TABLE IF EXISTS `circleusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `circleusers` (
  `circleId` varchar(100) NOT NULL,
  `userId` varchar(100) NOT NULL,
  PRIMARY KEY (`circleId`,`userId`),
  CONSTRAINT `circleIdFKcircleusers` FOREIGN KEY (`circleId`) REFERENCES `circle` (`circleId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `circleusers`
--

LOCK TABLES `circleusers` WRITE;
/*!40000 ALTER TABLE `circleusers` DISABLE KEYS */;
/*!40000 ALTER TABLE `circleusers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coll`
--

DROP TABLE IF EXISTS `coll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coll` (
  `collId` varchar(200) NOT NULL,
  PRIMARY KEY (`collId`),
  KEY `spaceFKcoll_idx` (`collId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coll`
--

LOCK TABLES `coll` WRITE;
/*!40000 ALTER TABLE `coll` DISABLE KEYS */;
/*!40000 ALTER TABLE `coll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collentrypos`
--

DROP TABLE IF EXISTS `collentrypos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collentrypos` (
  `collId` varchar(200) NOT NULL,
  `entryId` varchar(200) NOT NULL,
  `pos` varchar(200) NOT NULL,
  PRIMARY KEY (`collId`,`entryId`,`pos`),
  KEY `collIdFKcollentrypos_idx` (`collId`),
  KEY `entryIdFKcollentrypos_idx` (`entryId`),
  CONSTRAINT `collIdFKcollentrypos` FOREIGN KEY (`collId`) REFERENCES `coll` (`collId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collentrypos`
--

LOCK TABLES `collentrypos` WRITE;
/*!40000 ALTER TABLE `collentrypos` DISABLE KEYS */;
/*!40000 ALTER TABLE `collentrypos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collhierarchy`
--

DROP TABLE IF EXISTS `collhierarchy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collhierarchy` (
  `collParentId` varchar(200) NOT NULL,
  `collChildId` varchar(200) NOT NULL,
  PRIMARY KEY (`collParentId`,`collChildId`),
  KEY `collChildIdFKcollhierarchy_idx` (`collChildId`),
  CONSTRAINT `collChildIdFKcollhierarchy` FOREIGN KEY (`collChildId`) REFERENCES `coll` (`collId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `collParentIdFKcollhierarchy` FOREIGN KEY (`collParentId`) REFERENCES `coll` (`collId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collhierarchy`
--

LOCK TABLES `collhierarchy` WRITE;
/*!40000 ALTER TABLE `collhierarchy` DISABLE KEYS */;
/*!40000 ALTER TABLE `collhierarchy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collroot`
--

DROP TABLE IF EXISTS `collroot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collroot` (
  `collId` varchar(200) NOT NULL,
  `userId` varchar(200) NOT NULL,
  PRIMARY KEY (`collId`,`userId`),
  KEY `userId_idx` (`userId`),
  CONSTRAINT `collIdFKcollroot` FOREIGN KEY (`collId`) REFERENCES `coll` (`collId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collroot`
--

LOCK TABLES `collroot` WRITE;
/*!40000 ALTER TABLE `collroot` DISABLE KEYS */;
/*!40000 ALTER TABLE `collroot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colluser`
--

DROP TABLE IF EXISTS `colluser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `colluser` (
  `userId` varchar(200) NOT NULL,
  `collId` varchar(200) NOT NULL,
  PRIMARY KEY (`userId`,`collId`),
  KEY `collIdFKusercoll_idx` (`collId`),
  CONSTRAINT `collIdFKcolluser` FOREIGN KEY (`collId`) REFERENCES `coll` (`collId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colluser`
--

LOCK TABLES `colluser` WRITE;
/*!40000 ALTER TABLE `colluser` DISABLE KEYS */;
/*!40000 ALTER TABLE `colluser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disc`
--

DROP TABLE IF EXISTS `disc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disc` (
  `discId` varchar(200) NOT NULL,
  `target` varchar(200) NOT NULL,
  PRIMARY KEY (`discId`,`target`),
  KEY `discIdFKdisc_idx` (`discId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disc`
--

LOCK TABLES `disc` WRITE;
/*!40000 ALTER TABLE `disc` DISABLE KEYS */;
/*!40000 ALTER TABLE `disc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discentries`
--

DROP TABLE IF EXISTS `discentries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discentries` (
  `discId` varchar(200) NOT NULL,
  `discEntryId` varchar(200) NOT NULL,
  `pos` varchar(200) NOT NULL,
  PRIMARY KEY (`discId`,`discEntryId`),
  KEY `discEntryIdFKdiscentries_idx` (`discEntryId`),
  CONSTRAINT `discEntryIdFKdiscentries` FOREIGN KEY (`discEntryId`) REFERENCES `discentry` (`discEntryId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `discIdFKdiscentries` FOREIGN KEY (`discId`) REFERENCES `disc` (`discId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discentries`
--

LOCK TABLES `discentries` WRITE;
/*!40000 ALTER TABLE `discentries` DISABLE KEYS */;
/*!40000 ALTER TABLE `discentries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discentry`
--

DROP TABLE IF EXISTS `discentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discentry` (
  `discEntryId` varchar(200) NOT NULL,
  `discEntryContent` varchar(10000) NOT NULL,
  PRIMARY KEY (`discEntryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discentry`
--

LOCK TABLES `discentry` WRITE;
/*!40000 ALTER TABLE `discentry` DISABLE KEYS */;
/*!40000 ALTER TABLE `discentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entity`
--

DROP TABLE IF EXISTS `entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity` (
  `id` varchar(200) NOT NULL,
  `label` varchar(200) NOT NULL,
  `creationTime` varchar(200) NOT NULL,
  `type` varchar(200) NOT NULL,
  `author` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity`
--

LOCK TABLES `entity` WRITE;
/*!40000 ALTER TABLE `entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnep`
--

DROP TABLE IF EXISTS `learnep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnep` (
  `learnEpId` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnep`
--

LOCK TABLES `learnep` WRITE;
/*!40000 ALTER TABLE `learnep` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnep` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnepcircle`
--

DROP TABLE IF EXISTS `learnepcircle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnepcircle` (
  `learnEpCircleId` varchar(200) NOT NULL,
  `xLabel` varchar(200) NOT NULL,
  `yLabel` varchar(200) NOT NULL,
  `xR` varchar(200) NOT NULL,
  `yR` varchar(200) NOT NULL,
  `xC` varchar(200) NOT NULL,
  `yC` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpCircleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnepcircle`
--

LOCK TABLES `learnepcircle` WRITE;
/*!40000 ALTER TABLE `learnepcircle` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnepcircle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnepentity`
--

DROP TABLE IF EXISTS `learnepentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnepentity` (
  `learnEpEntityId` varchar(200) NOT NULL,
  `entityId` varchar(200) NOT NULL,
  `x` varchar(200) NOT NULL,
  `y` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpEntityId`,`entityId`),
  KEY `entityIdFKlearnepentity_idx` (`entityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnepentity`
--

LOCK TABLES `learnepentity` WRITE;
/*!40000 ALTER TABLE `learnepentity` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnepentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learneptimelinestate`
--

DROP TABLE IF EXISTS `learneptimelinestate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learneptimelinestate` (
  `learnEpTimelineStateId` varchar(200) NOT NULL,
  `startTime` varchar(200) NOT NULL,
  `endTime` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpTimelineStateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learneptimelinestate`
--

LOCK TABLES `learneptimelinestate` WRITE;
/*!40000 ALTER TABLE `learneptimelinestate` DISABLE KEYS */;
/*!40000 ALTER TABLE `learneptimelinestate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnepuser`
--

DROP TABLE IF EXISTS `learnepuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnepuser` (
  `userId` varchar(200) NOT NULL,
  `learnEpId` varchar(200) NOT NULL,
  `learnEpSpace` varchar(200) NOT NULL,
  PRIMARY KEY (`userId`,`learnEpId`,`learnEpSpace`),
  KEY `learnEpIdFKlearnepuser_idx` (`learnEpId`),
  KEY `learnEpSpace_idx` (`learnEpSpace`),
  CONSTRAINT `learnEpIdFKlearnepuser` FOREIGN KEY (`learnEpId`) REFERENCES `learnep` (`learnEpId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `learnEpSpace` FOREIGN KEY (`learnEpSpace`) REFERENCES `space` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnepuser`
--

LOCK TABLES `learnepuser` WRITE;
/*!40000 ALTER TABLE `learnepuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnepuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnepversion`
--

DROP TABLE IF EXISTS `learnepversion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnepversion` (
  `learnEpVersionId` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpVersionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnepversion`
--

LOCK TABLES `learnepversion` WRITE;
/*!40000 ALTER TABLE `learnepversion` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnepversion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnepversioncircles`
--

DROP TABLE IF EXISTS `learnepversioncircles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnepversioncircles` (
  `learnEpVersionId` varchar(200) NOT NULL,
  `learnEpCircleId` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpVersionId`,`learnEpCircleId`),
  KEY `learnEpCircleIdFKlearnepversioncircles_idx` (`learnEpCircleId`),
  CONSTRAINT `learnEpCircleIdFKlearnepversioncircles` FOREIGN KEY (`learnEpCircleId`) REFERENCES `learnepcircle` (`learnEpCircleId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `learnEpVersionIdFKlearnepversioncircles` FOREIGN KEY (`learnEpVersionId`) REFERENCES `learnepversion` (`learnEpVersionId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnepversioncircles`
--

LOCK TABLES `learnepversioncircles` WRITE;
/*!40000 ALTER TABLE `learnepversioncircles` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnepversioncircles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnepversioncurrent`
--

DROP TABLE IF EXISTS `learnepversioncurrent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnepversioncurrent` (
  `userId` varchar(200) NOT NULL,
  `learnEpVersionId` varchar(200) NOT NULL,
  PRIMARY KEY (`userId`,`learnEpVersionId`),
  KEY `learnEpVersionIdFKlearnepversioncurrent_idx` (`learnEpVersionId`),
  CONSTRAINT `learnEpVersionIdFKlearnepversioncurrent` FOREIGN KEY (`learnEpVersionId`) REFERENCES `learnepversion` (`learnEpVersionId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnepversioncurrent`
--

LOCK TABLES `learnepversioncurrent` WRITE;
/*!40000 ALTER TABLE `learnepversioncurrent` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnepversioncurrent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnepversionentities`
--

DROP TABLE IF EXISTS `learnepversionentities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnepversionentities` (
  `learnEpVersionId` varchar(200) NOT NULL,
  `learnEpEntityId` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpVersionId`,`learnEpEntityId`),
  KEY `learnEpEntityIdFKlearnepversionentities_idx` (`learnEpEntityId`),
  CONSTRAINT `learnEpEntityIdFKlearnepversionentities` FOREIGN KEY (`learnEpEntityId`) REFERENCES `learnepentity` (`learnEpEntityId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `learnEpVersionIdFKlearnepversionentities` FOREIGN KEY (`learnEpVersionId`) REFERENCES `learnepversion` (`learnEpVersionId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnepversionentities`
--

LOCK TABLES `learnepversionentities` WRITE;
/*!40000 ALTER TABLE `learnepversionentities` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnepversionentities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnepversions`
--

DROP TABLE IF EXISTS `learnepversions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnepversions` (
  `learnEpId` varchar(200) NOT NULL,
  `learnEpVersionId` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpId`,`learnEpVersionId`),
  KEY `learnEpVersionIdFKlearnepversions_idx` (`learnEpVersionId`),
  CONSTRAINT `learnEpIdFKlearnepversions` FOREIGN KEY (`learnEpId`) REFERENCES `learnep` (`learnEpId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `learnEpVersionIdFKlearnepversions` FOREIGN KEY (`learnEpVersionId`) REFERENCES `learnepversion` (`learnEpVersionId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnepversions`
--

LOCK TABLES `learnepversions` WRITE;
/*!40000 ALTER TABLE `learnepversions` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnepversions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnepversiontimelinestates`
--

DROP TABLE IF EXISTS `learnepversiontimelinestates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnepversiontimelinestates` (
  `learnEpVersionId` varchar(200) NOT NULL,
  `learnEpTimelineStateId` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpVersionId`,`learnEpTimelineStateId`),
  KEY `learnEpTimelineStateId_idx` (`learnEpTimelineStateId`),
  CONSTRAINT `learnEpTimelineStateIdFKlearnepversiontimelinestates` FOREIGN KEY (`learnEpTimelineStateId`) REFERENCES `learneptimelinestate` (`learnEpTimelineStateId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `learnEpVersionIdFKlearnepversiontimelinestates` FOREIGN KEY (`learnEpVersionId`) REFERENCES `learnepversion` (`learnEpVersionId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learnepversiontimelinestates`
--

LOCK TABLES `learnepversiontimelinestates` WRITE;
/*!40000 ALTER TABLE `learnepversiontimelinestates` DISABLE KEYS */;
/*!40000 ALTER TABLE `learnepversiontimelinestates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `locationId` varchar(200) NOT NULL,
  PRIMARY KEY (`locationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locations`
--

DROP TABLE IF EXISTS `locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locations` (
  `locationId` varchar(200) NOT NULL,
  `entityId` varchar(200) NOT NULL,
  `userId` varchar(200) NOT NULL,
  PRIMARY KEY (`locationId`,`entityId`,`userId`),
  KEY `userIdFKlocations_idx` (`userId`),
  CONSTRAINT `locationIdFKlocations` FOREIGN KEY (`locationId`) REFERENCES `location` (`locationId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locations`
--

LOCK TABLES `locations` WRITE;
/*!40000 ALTER TABLE `locations` DISABLE KEYS */;
/*!40000 ALTER TABLE `locations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ratingass`
--

DROP TABLE IF EXISTS `ratingass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ratingass` (
  `userId` varchar(200) NOT NULL,
  `entityId` varchar(200) NOT NULL,
  `ratingValue` varchar(200) NOT NULL,
  `ratingId` varchar(200) NOT NULL,
  PRIMARY KEY (`userId`,`entityId`,`ratingId`),
  KEY `entityIdFKratingass_idx` (`entityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratingass`
--

LOCK TABLES `ratingass` WRITE;
/*!40000 ALTER TABLE `ratingass` DISABLE KEYS */;
/*!40000 ALTER TABLE `ratingass` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `space`
--

DROP TABLE IF EXISTS `space`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `space` (
  `id` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `space`
--

LOCK TABLES `space` WRITE;
/*!40000 ALTER TABLE `space` DISABLE KEYS */;
INSERT INTO `space` VALUES ('followSpace'),('privateSpace'),('sharedSpace');
/*!40000 ALTER TABLE `space` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tagass`
--

DROP TABLE IF EXISTS `tagass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tagass` (
  `userId` varchar(200) NOT NULL,
  `entityId` varchar(200) NOT NULL,
  `tagId` varchar(200) NOT NULL,
  `tagSpace` varchar(200) NOT NULL,
  PRIMARY KEY (`userId`,`entityId`,`tagId`,`tagSpace`),
  KEY `entityIdFKtagass_idx` (`entityId`),
  KEY `tagIdFKtagass_idx` (`tagId`),
  KEY `tagSpaceFKtagass_idx` (`tagSpace`),
  CONSTRAINT `tagSpaceFKtagass` FOREIGN KEY (`tagSpace`) REFERENCES `space` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tagass`
--

LOCK TABLES `tagass` WRITE;
/*!40000 ALTER TABLE `tagass` DISABLE KEYS */;
/*!40000 ALTER TABLE `tagass` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ues`
--

DROP TABLE IF EXISTS `ues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ues` (
  `userEventId` varchar(200) NOT NULL,
  `userId` varchar(200) NOT NULL,
  `entityId` varchar(200) NOT NULL,
  `eventType` varchar(200) NOT NULL,
  `content` varchar(200) NOT NULL,
  PRIMARY KEY (`userEventId`),
  KEY `userIdFKues_idx` (`userId`),
  KEY `entityId_idx` (`entityId`),
  KEY `userEventIdFKues_idx` (`userEventId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ues`
--

LOCK TABLES `ues` WRITE;
/*!40000 ALTER TABLE `ues` DISABLE KEYS */;
/*!40000 ALTER TABLE `ues` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-04-23 15:09:59
