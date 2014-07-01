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
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `activityId` varchar(100) NOT NULL,
  `activityType` varchar(100) NOT NULL,
  `textComment` varchar(5000) NOT NULL,
  PRIMARY KEY (`activityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activityentities`
--

DROP TABLE IF EXISTS `activityentities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activityentities` (
  `activityId` varchar(100) NOT NULL,
  `entityId` varchar(100) NOT NULL,
  PRIMARY KEY (`activityId`,`entityId`),
  CONSTRAINT `activityIdactivitytargetentities` FOREIGN KEY (`activityId`) REFERENCES `activity` (`activityId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activityentities`
--

LOCK TABLES `activityentities` WRITE;
/*!40000 ALTER TABLE `activityentities` DISABLE KEYS */;
/*!40000 ALTER TABLE `activityentities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activityusers`
--

DROP TABLE IF EXISTS `activityusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activityusers` (
  `activityId` varchar(100) NOT NULL,
  `userId` varchar(100) NOT NULL,
  PRIMARY KEY (`activityId`,`userId`),
  CONSTRAINT `activityIdactivityusers` FOREIGN KEY (`activityId`) REFERENCES `activity` (`activityId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activityusers`
--

LOCK TABLES `activityusers` WRITE;
/*!40000 ALTER TABLE `activityusers` DISABLE KEYS */;
/*!40000 ALTER TABLE `activityusers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth`
--

DROP TABLE IF EXISTS `auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth` (
  `userId` varchar(100) NOT NULL,
  `authKey` varchar(100) NOT NULL,
  PRIMARY KEY (`userId`,`authKey`),
  CONSTRAINT `userIdFKauth` FOREIGN KEY (`userId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth`
--

LOCK TABLES `auth` WRITE;
/*!40000 ALTER TABLE `auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `categoryId` varchar(200) NOT NULL,
  `isPredefined` varchar(200) NOT NULL,
  PRIMARY KEY (`categoryId`),
  CONSTRAINT `categoryIdFKcategory` FOREIGN KEY (`categoryId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoryass`
--

DROP TABLE IF EXISTS `categoryass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categoryass` (
  `categoryId` varchar(100) NOT NULL,
  `entityId` varchar(100) NOT NULL,
  `userId` varchar(100) NOT NULL,
  `categorySpace` varchar(100) NOT NULL,
  PRIMARY KEY (`categoryId`,`entityId`,`userId`,`categorySpace`),
  KEY `categorySpaceFKcategoryass_idx` (`categorySpace`),
  CONSTRAINT `categoryIdFKcategoryass` FOREIGN KEY (`categoryId`) REFERENCES `category` (`categoryId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `categorySpaceFKcategoryass` FOREIGN KEY (`categorySpace`) REFERENCES `space` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoryass`
--

LOCK TABLES `categoryass` WRITE;
/*!40000 ALTER TABLE `categoryass` DISABLE KEYS */;
/*!40000 ALTER TABLE `categoryass` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `circle`
--

DROP TABLE IF EXISTS `circle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `circle` (
  `circleId` varchar(100) NOT NULL,
  `circleType` varchar(100) NOT NULL,
  PRIMARY KEY (`circleId`),
  CONSTRAINT `circleIdcircle` FOREIGN KEY (`circleId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  KEY `entityIdFKcircleentities_idx` (`entityId`),
  CONSTRAINT `circleIdFKcircleentities` FOREIGN KEY (`circleId`) REFERENCES `circle` (`circleId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `entityIdFKcircleentities` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
-- Table structure for table `circleusers`
--

DROP TABLE IF EXISTS `circleusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `circleusers` (
  `circleId` varchar(100) NOT NULL,
  `userId` varchar(100) NOT NULL,
  PRIMARY KEY (`circleId`,`userId`),
  KEY `userIdFKcircleusers_idx` (`userId`),
  CONSTRAINT `circleIdFKcircleusers` FOREIGN KEY (`circleId`) REFERENCES `circle` (`circleId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKcircleusers` FOREIGN KEY (`userId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  KEY `spaceFKcoll_idx` (`collId`),
  CONSTRAINT `collIdcoll` FOREIGN KEY (`collId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  CONSTRAINT `collEntryIdFKcollentrypos` FOREIGN KEY (`entryId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
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
-- Table structure for table `collspecial`
--

DROP TABLE IF EXISTS `collspecial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collspecial` (
  `collId` varchar(100) NOT NULL,
  `userId` varchar(100) NOT NULL,
  PRIMARY KEY (`collId`),
  CONSTRAINT `collIdFKcollspecial` FOREIGN KEY (`collId`) REFERENCES `coll` (`collId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collspecial`
--

LOCK TABLES `collspecial` WRITE;
/*!40000 ALTER TABLE `collspecial` DISABLE KEYS */;
/*!40000 ALTER TABLE `collspecial` ENABLE KEYS */;
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
  CONSTRAINT `collIdFKcolluser` FOREIGN KEY (`collId`) REFERENCES `coll` (`collId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKcolluser` FOREIGN KEY (`userId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  `entityId` varchar(200) NOT NULL,
  PRIMARY KEY (`discId`,`entityId`),
  KEY `discIdFKdisc_idx` (`discId`),
  KEY `targetFKdisc_idx` (`entityId`),
  CONSTRAINT `discIdFKdisc` FOREIGN KEY (`discId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `entityIdFKdisc` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  PRIMARY KEY (`discEntryId`),
  CONSTRAINT `discEntryIdFKdiscEntry` FOREIGN KEY (`discEntryId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
-- Table structure for table `discuser`
--

DROP TABLE IF EXISTS `discuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discuser` (
  `discId` varchar(100) NOT NULL,
  `userId` varchar(100) NOT NULL,
  PRIMARY KEY (`discId`,`userId`),
  KEY `userIdFKdiscuser_idx` (`userId`),
  CONSTRAINT `discIdFKdiscuser` FOREIGN KEY (`discId`) REFERENCES `disc` (`discId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKdiscuser` FOREIGN KEY (`userId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discuser`
--

LOCK TABLES `discuser` WRITE;
/*!40000 ALTER TABLE `discuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `discuser` ENABLE KEYS */;
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
  `description` varchar(500) NOT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `labelDescriptionIndexEntity` (`label`,`description`),
  FULLTEXT KEY `labelIndexEntity` (`label`),
  FULLTEXT KEY `descriptionIndexEntity` (`description`)
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
-- Table structure for table `evernotenote`
--

DROP TABLE IF EXISTS `evernotenote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evernotenote` (
  `notebookId` varchar(200) NOT NULL,
  `noteId` varchar(200) NOT NULL,
  PRIMARY KEY (`noteId`),
  KEY `noteIdFKevernotenotes_idx` (`noteId`),
  KEY `notebookIdFKevernotenote` (`notebookId`),
  CONSTRAINT `notebookIdFKevernotenote` FOREIGN KEY (`notebookId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `noteIdFKevernotenote` FOREIGN KEY (`noteId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evernotenote`
--

LOCK TABLES `evernotenote` WRITE;
/*!40000 ALTER TABLE `evernotenote` DISABLE KEYS */;
/*!40000 ALTER TABLE `evernotenote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evernoteresource`
--

DROP TABLE IF EXISTS `evernoteresource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evernoteresource` (
  `noteId` varchar(200) NOT NULL,
  `entityId` varchar(200) NOT NULL,
  PRIMARY KEY (`entityId`),
  KEY `noteIdFKevernoteresource_idx` (`noteId`),
  CONSTRAINT `noteIdFKevernoteresource` FOREIGN KEY (`noteId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `entityIdFKevernoteresource` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evernoteresource`
--

LOCK TABLES `evernoteresource` WRITE;
/*!40000 ALTER TABLE `evernoteresource` DISABLE KEYS */;
/*!40000 ALTER TABLE `evernoteresource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learnep`
--

DROP TABLE IF EXISTS `learnep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learnep` (
  `learnEpId` varchar(200) NOT NULL,
  PRIMARY KEY (`learnEpId`),
  CONSTRAINT `learnEpIdFKlearnep` FOREIGN KEY (`learnEpId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  PRIMARY KEY (`learnEpCircleId`),
  CONSTRAINT `learnEpCircleIdFKlearnepcircle` FOREIGN KEY (`learnEpCircleId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  KEY `entityIdFKlearnepentity_idx` (`entityId`),
  CONSTRAINT `entityIdFKlearnepentity` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `learnEpEntityIdFKlearnepentity` FOREIGN KEY (`learnEpEntityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  PRIMARY KEY (`learnEpTimelineStateId`),
  CONSTRAINT `learnEpTimelineStateIdFKlearneptimelinestate` FOREIGN KEY (`learnEpTimelineStateId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  PRIMARY KEY (`userId`,`learnEpId`),
  KEY `learnEpIdFKlearnepuser_idx` (`learnEpId`),
  CONSTRAINT `learnEpIdFKlearnepuser` FOREIGN KEY (`learnEpId`) REFERENCES `learnep` (`learnEpId`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  PRIMARY KEY (`learnEpVersionId`),
  CONSTRAINT `learnEpVersionIdFKlearnepversion` FOREIGN KEY (`learnEpVersionId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  CONSTRAINT `learnEpVersionIdFKlearnepversioncurrent` FOREIGN KEY (`learnEpVersionId`) REFERENCES `learnepversion` (`learnEpVersionId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKlearnepversioncurrent` FOREIGN KEY (`userId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
INSERT INTO `tagass` VALUES ('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00014.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00015.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00016.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00017.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00018.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00019.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00020.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00021.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00022.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00023.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00024.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/183620848056471/','http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/00025.mp4/','http://eval.bp/tag/1837102774514621/','sharedSpace'),('http://eval.bp/201992186152267/','http://eval.bp/coll/2020022818597910/','http://eval.bp/tag/2024343535075619/','sharedSpace'),('http://eval.bp/201992186152267/','http://eval.bp/coll/2020022818597910/','http://eval.bp/tag/2024592213400920/','privateSpace'),('http://eval.bp/84532825920159/','http://eval.bp/coll/845393902671012/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/014228b3-5928-4e3a-9ef0-90654484f5ce.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/0200cb58-8a9e-4734-8e3a-fef97b1569ff.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/021def12-96b7-4bce-ba18-297955882e0d.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837324535880677/','http://tosini.informatik.rwth-aachen.de:8134/videos/0e3fbd8d-ca13-46e5-91ae-2eebfcce85fe.mp4/','http://eval.bp/tag/1837352585601281/','sharedSpace'),('http://eval.bp/1837324535880677/','http://tosini.informatik.rwth-aachen.de:8134/videos/0e3fbd8d-ca13-46e5-91ae-2eebfcce85fe.mp4/','http://eval.bp/tag/1837352833041682/','sharedSpace'),('http://eval.bp/1837324535880677/','http://tosini.informatik.rwth-aachen.de:8134/videos/0e3fbd8d-ca13-46e5-91ae-2eebfcce85fe.mp4/','http://eval.bp/tag/1837353287326683/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/161bef25-ad01-4972-8cae-f9fb24c0f6d9.mp4/','http://eval.bp/tag/183635522161035/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/161bef25-ad01-4972-8cae-f9fb24c0f6d9.mp4/','http://eval.bp/tag/183637514911886/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/161bef25-ad01-4972-8cae-f9fb24c0f6d9.mp4/','http://eval.bp/tag/1837282878883664/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/161bef25-ad01-4972-8cae-f9fb24c0f6d9.mp4/','http://eval.bp/tag/1837283022931765/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/161bef25-ad01-4972-8cae-f9fb24c0f6d9.mp4/','http://eval.bp/tag/1837283157662366/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/161bef25-ad01-4972-8cae-f9fb24c0f6d9.mp4/','http://eval.bp/tag/1837283303927467/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/161bef25-ad01-4972-8cae-f9fb24c0f6d9.mp4/','http://eval.bp/tag/1837283422509568/','sharedSpace'),('http://eval.bp/1837171282768430/','http://tosini.informatik.rwth-aachen.de:8134/videos/309a6921-abab-4119-a667-e3ebc7c30147.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/39f2875c-0849-4e88-8cdb-2c66bdf451d3.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/18373786408182106/','http://tosini.informatik.rwth-aachen.de:8134/videos/3c52e2cb-3b85-4599-8706-382ee6f1275d.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/4a732e15-b325-46be-99a4-b4ffbd0196bb.mp4/','http://eval.bp/tag/183635522161035/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/4a732e15-b325-46be-99a4-b4ffbd0196bb.mp4/','http://eval.bp/tag/183637514911886/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/4a732e15-b325-46be-99a4-b4ffbd0196bb.mp4/','http://eval.bp/tag/183644430999357/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/4a732e15-b325-46be-99a4-b4ffbd0196bb.mp4/','http://eval.bp/tag/183644473383898/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/4a732e15-b325-46be-99a4-b4ffbd0196bb.mp4/','http://eval.bp/tag/183644516062059/','sharedSpace'),('http://eval.bp/1837171282768430/','http://tosini.informatik.rwth-aachen.de:8134/videos/4d0618f7-64e9-4a60-9515-070b4fb314f5.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837120165781126/','http://tosini.informatik.rwth-aachen.de:8134/videos/53c47d35-f5a4-42ab-916e-31122d513aea.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837171282768430/','http://tosini.informatik.rwth-aachen.de:8134/videos/608a41b7-5530-4afc-8a9f-0badc1ce110c.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837120165781126/','http://tosini.informatik.rwth-aachen.de:8134/videos/810fa456-589c-40a9-af75-c8b6bab21025.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/18373786408182106/','http://tosini.informatik.rwth-aachen.de:8134/videos/843798c7-0c8e-4a96-b4b5-4bbedcc4f220.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/880d4791-5800-4333-833e-234f75bf6afc.mp4/','http://eval.bp/tag/183635522161035/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/880d4791-5800-4333-833e-234f75bf6afc.mp4/','http://eval.bp/tag/183644516062059/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/880d4791-5800-4333-833e-234f75bf6afc.mp4/','http://eval.bp/tag/1836932986994613/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/880d4791-5800-4333-833e-234f75bf6afc.mp4/','http://eval.bp/tag/1836933344328614/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/880d4791-5800-4333-833e-234f75bf6afc.mp4/','http://eval.bp/tag/1836933713347115/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/880d4791-5800-4333-833e-234f75bf6afc.mp4/','http://eval.bp/tag/1836934031733116/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/880d4791-5800-4333-833e-234f75bf6afc.mp4/','http://eval.bp/tag/1836934421693717/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/880d4791-5800-4333-833e-234f75bf6afc.mp4/','http://eval.bp/tag/1836934754160918/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/8846ac11-af2a-4e1f-999d-e191d5a06af9.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837171282768430/','http://tosini.informatik.rwth-aachen.de:8134/videos/9783fde0-a031-42ba-9d8d-f6215c9775bd.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/a97be40d-568d-4e14-835f-8f2f8c02ad6d.mp4/','http://eval.bp/tag/1837254468892861/','sharedSpace'),('http://eval.bp/1837120165781126/','http://tosini.informatik.rwth-aachen.de:8134/videos/b769a72d-8a6e-441d-8370-649b701737d7.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c170c3c9-389d-44ee-82c8-b371164d83c2.mp4/','http://eval.bp/tag/183635522161035/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c170c3c9-389d-44ee-82c8-b371164d83c2.mp4/','http://eval.bp/tag/183644516062059/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c170c3c9-389d-44ee-82c8-b371164d83c2.mp4/','http://eval.bp/tag/1837283157662366/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c170c3c9-389d-44ee-82c8-b371164d83c2.mp4/','http://eval.bp/tag/1837296372431570/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c170c3c9-389d-44ee-82c8-b371164d83c2.mp4/','http://eval.bp/tag/1837296532508371/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c170c3c9-389d-44ee-82c8-b371164d83c2.mp4/','http://eval.bp/tag/1837296670174972/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c170c3c9-389d-44ee-82c8-b371164d83c2.mp4/','http://eval.bp/tag/1837296809489473/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c3a688e7-ab60-40cf-a23d-fa747bb9b86d.mp4/','http://eval.bp/tag/183635522161035/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c3a688e7-ab60-40cf-a23d-fa747bb9b86d.mp4/','http://eval.bp/tag/1837222363623853/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c3a688e7-ab60-40cf-a23d-fa747bb9b86d.mp4/','http://eval.bp/tag/1837222637009754/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c3a688e7-ab60-40cf-a23d-fa747bb9b86d.mp4/','http://eval.bp/tag/1837224005886555/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c3a688e7-ab60-40cf-a23d-fa747bb9b86d.mp4/','http://eval.bp/tag/1837224311329756/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c3a688e7-ab60-40cf-a23d-fa747bb9b86d.mp4/','http://eval.bp/tag/1837224611589857/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/c8871ab6-d4c5-4f01-8c13-6c890a8a25f7.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837171282768430/','http://tosini.informatik.rwth-aachen.de:8134/videos/cc980cdb-f55e-4829-80e7-25d6ead4945a.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/ea901369-3ae0-42c6-aece-41151e474472.mp4/','http://eval.bp/tag/1837254468892861/','sharedSpace'),('http://eval.bp/1837104291544022/','http://tosini.informatik.rwth-aachen.de:8134/videos/eb7eca8a-b247-427f-974f-dea0833c3b47.mp4/','http://eval.bp/tag/1837322544283276/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/f0aacc13-283e-4cbb-88c0-00f52e3bf8a3.mp4/','http://eval.bp/tag/865773697971623/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/gandhara_rwth_profi.mp4/','http://eval.bp/tag/1837180146642634/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/gandhara_rwth_profi.mp4/','http://eval.bp/tag/1837180472458735/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/gandhara_rwth_profi.mp4/','http://eval.bp/tag/1837180759865936/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/gandhara_rwth_profi.mp4/','http://eval.bp/tag/1837181032892237/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/gandhara_rwth_profi.mp4/','http://eval.bp/tag/1837181334141038/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/gandhara_rwth_profi.mp4/','http://eval.bp/tag/1837181604530939/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/gandhara_rwth_profi.mp4/','http://eval.bp/tag/1837181887174440/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/gandhara_rwth_profi.mp4/','http://eval.bp/tag/1837182187194841/','sharedSpace'),('http://eval.bp/183620848056471/','http://tosini.informatik.rwth-aachen.de:8134/videos/gandhara_rwth_profi.mp4/','http://eval.bp/tag/1837182555734042/','sharedSpace'),('http://eval.bp/1837195366701043/','http://tosini.informatik.rwth-aachen.de:8134/videos/Rebuilding Bamiyan - 23 Jul 07 - Part 1.mp4/','http://eval.bp/tag/1837215380494747/','sharedSpace'),('http://eval.bp/1837195366701043/','http://tosini.informatik.rwth-aachen.de:8134/videos/Rebuilding Bamiyan - 23 Jul 07 - Part 1.mp4/','http://eval.bp/tag/1837215654689548/','sharedSpace'),('http://eval.bp/1837195366701043/','http://tosini.informatik.rwth-aachen.de:8134/videos/Rebuilding Bamiyan - 23 Jul 07 - Part 1.mp4/','http://eval.bp/tag/1837215911687249/','sharedSpace');
/*!40000 ALTER TABLE `tagass` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thumbnails`
--

DROP TABLE IF EXISTS `thumbnails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `thumbnails` (
  `entityId` varchar(200) NOT NULL,
  `thumbId` varchar(200) NOT NULL,
  PRIMARY KEY (`entityId`,`thumbId`),
  KEY `thumbIdFKthumbnails_idx` (`thumbId`),
  CONSTRAINT `entityIdFKthumbnails` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `thumbIdFKthumbnails` FOREIGN KEY (`thumbId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thumbnails`
--

LOCK TABLES `thumbnails` WRITE;
/*!40000 ALTER TABLE `thumbnails` DISABLE KEYS */;
/*!40000 ALTER TABLE `thumbnails` ENABLE KEYS */;
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

-- Dump completed on 2014-07-01 12:15:41
