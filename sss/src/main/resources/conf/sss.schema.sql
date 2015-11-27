-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: sss
-- ------------------------------------------------------
-- Server version	5.6.26-log

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
  `entityId` varchar(255) NOT NULL,
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
-- Table structure for table `activitycontents`
--

DROP TABLE IF EXISTS `activitycontents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activitycontents` (
  `activityId` varchar(200) NOT NULL,
  `content` varchar(200) NOT NULL,
  `contentType` varchar(200) NOT NULL,
  PRIMARY KEY (`activityId`,`content`,`contentType`),
  CONSTRAINT `activityIdFKactivitycontents` FOREIGN KEY (`activityId`) REFERENCES `activity` (`activityId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitycontents`
--

LOCK TABLES `activitycontents` WRITE;
/*!40000 ALTER TABLE `activitycontents` DISABLE KEYS */;
/*!40000 ALTER TABLE `activitycontents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activityentities`
--

DROP TABLE IF EXISTS `activityentities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activityentities` (
  `activityId` varchar(100) NOT NULL,
  `entityId` varchar(255) NOT NULL,
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
-- Table structure for table `app`
--

DROP TABLE IF EXISTS `app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app` (
  `appId` varchar(200) NOT NULL,
  `descriptionShort` varchar(5000) NOT NULL,
  `descriptionFunctional` varchar(5000) NOT NULL,
  `descriptionTechnical` varchar(5000) NOT NULL,
  `descriptionInstall` varchar(5000) NOT NULL,
  `downloadIOS` varchar(255) NOT NULL,
  `downloadAndroid` varchar(255) NOT NULL,
  `fork` varchar(255) NOT NULL,
  PRIMARY KEY (`appId`),
  CONSTRAINT `appIdFKapp` FOREIGN KEY (`appId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app`
--

LOCK TABLES `app` WRITE;
/*!40000 ALTER TABLE `app` DISABLE KEYS */;
/*!40000 ALTER TABLE `app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appstacklayout`
--

DROP TABLE IF EXISTS `appstacklayout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appstacklayout` (
  `stackId` varchar(200) NOT NULL,
  `app` varchar(255) NOT NULL,
  PRIMARY KEY (`stackId`),
  KEY `appFKappstacklayout_idx` (`app`),
  CONSTRAINT `stackIdFKappstacklayout` FOREIGN KEY (`stackId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appstacklayout`
--

LOCK TABLES `appstacklayout` WRITE;
/*!40000 ALTER TABLE `appstacklayout` DISABLE KEYS */;
/*!40000 ALTER TABLE `appstacklayout` ENABLE KEYS */;
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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `categoryId` varchar(100) NOT NULL,
  `entityId` varchar(255) NOT NULL,
  `userId` varchar(100) NOT NULL,
  `categorySpace` varchar(100) NOT NULL,
  `creationTime` varchar(200) NOT NULL,
  `circleId` varchar(150) NOT NULL DEFAULT '',
  PRIMARY KEY (`categoryId`,`entityId`,`userId`,`categorySpace`,`circleId`),
  KEY `categorySpaceFKcategoryass_idx` (`categorySpace`),
  KEY `circleIdFKcategoryass_idx` (`circleId`),
  CONSTRAINT `categoryIdFKcategoryass` FOREIGN KEY (`categoryId`) REFERENCES `category` (`categoryId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `categorySpaceFKcategoryass` FOREIGN KEY (`categorySpace`) REFERENCES `space` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `circleIdFKcategoryass` FOREIGN KEY (`circleId`) REFERENCES `circle` (`circleId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
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
-- Table structure for table `circle`
--

DROP TABLE IF EXISTS `circle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `circle` (
  `circleId` varchar(100) NOT NULL,
  `circleType` varchar(100) NOT NULL,
  `isSystemCircle` varchar(200) NOT NULL,
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
  `entityId` varchar(255) NOT NULL,
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
-- Table structure for table `circleinvitees`
--

DROP TABLE IF EXISTS `circleinvitees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `circleinvitees` (
  `circleId` varchar(255) NOT NULL,
  `inviteeId` varchar(255) NOT NULL,
  PRIMARY KEY (`circleId`,`inviteeId`),
  CONSTRAINT `circleIdFKcircleinvitees` FOREIGN KEY (`circleId`) REFERENCES `circle` (`circleId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `circleinvitees`
--

LOCK TABLES `circleinvitees` WRITE;
/*!40000 ALTER TABLE `circleinvitees` DISABLE KEYS */;
/*!40000 ALTER TABLE `circleinvitees` ENABLE KEYS */;
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
  `entryId` varchar(255) NOT NULL,
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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `commentId` varchar(200) NOT NULL,
  `commentContent` varchar(1000) NOT NULL,
  PRIMARY KEY (`commentId`),
  CONSTRAINT `commentId` FOREIGN KEY (`commentId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `entityId` varchar(255) NOT NULL,
  `commentId` varchar(200) NOT NULL,
  PRIMARY KEY (`entityId`,`commentId`),
  KEY `commentId_idx` (`commentId`),
  KEY `commentIdFKcomments_idx` (`commentId`),
  CONSTRAINT `commentIdFKcomments` FOREIGN KEY (`commentId`) REFERENCES `comment` (`commentId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `entityIdFKcomments` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disc`
--

DROP TABLE IF EXISTS `disc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disc` (
  `discId` varchar(200) NOT NULL,
  PRIMARY KEY (`discId`),
  KEY `discIdFKdisc_idx` (`discId`),
  CONSTRAINT `discIdFKdisc` FOREIGN KEY (`discId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  `accepted` varchar(200) NOT NULL,
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
-- Table structure for table `disctargets`
--

DROP TABLE IF EXISTS `disctargets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disctargets` (
  `discId` varchar(255) NOT NULL,
  `targetId` varchar(255) NOT NULL,
  PRIMARY KEY (`discId`,`targetId`),
  KEY `targetIdFKdisctargets_idx` (`targetId`),
  CONSTRAINT `discIdFKdisctargets` FOREIGN KEY (`discId`) REFERENCES `disc` (`discId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `targetIdFKdisctargets` FOREIGN KEY (`targetId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disctargets`
--

LOCK TABLES `disctargets` WRITE;
/*!40000 ALTER TABLE `disctargets` DISABLE KEYS */;
/*!40000 ALTER TABLE `disctargets` ENABLE KEYS */;
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
  `id` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `creationTime` bigint(13) NOT NULL,
  `type` varchar(200) NOT NULL,
  `author` varchar(200) NOT NULL,
  `description` varchar(10000) NOT NULL,
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
-- Table structure for table `entityattachedentities`
--

DROP TABLE IF EXISTS `entityattachedentities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entityattachedentities` (
  `entityId` varchar(255) NOT NULL,
  `attachedEntityId` varchar(255) NOT NULL,
  PRIMARY KEY (`entityId`,`attachedEntityId`),
  KEY `attachedEntityIdFKentityattachedentities_idx` (`attachedEntityId`),
  CONSTRAINT `attachedEntityIdFKentityattachedentities` FOREIGN KEY (`attachedEntityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `entityIdFKentityattachedentities` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entityattachedentities`
--

LOCK TABLES `entityattachedentities` WRITE;
/*!40000 ALTER TABLE `entityattachedentities` DISABLE KEYS */;
/*!40000 ALTER TABLE `entityattachedentities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entitydownloads`
--

DROP TABLE IF EXISTS `entitydownloads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entitydownloads` (
  `entityId` varchar(255) NOT NULL,
  `downloadId` varchar(255) NOT NULL,
  PRIMARY KEY (`entityId`,`downloadId`),
  CONSTRAINT `entityIdFKentitydownloads` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entitydownloads`
--

LOCK TABLES `entitydownloads` WRITE;
/*!40000 ALTER TABLE `entitydownloads` DISABLE KEYS */;
/*!40000 ALTER TABLE `entitydownloads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entityfiles`
--

DROP TABLE IF EXISTS `entityfiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entityfiles` (
  `entityId` varchar(255) NOT NULL,
  `fileId` varchar(255) NOT NULL,
  PRIMARY KEY (`entityId`,`fileId`),
  KEY `fileIdFKentityfiles_idx` (`fileId`),
  CONSTRAINT `entityIdFKentityfiles` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fileIdFKentityfiles` FOREIGN KEY (`fileId`) REFERENCES `file` (`fileId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entityfiles`
--

LOCK TABLES `entityfiles` WRITE;
/*!40000 ALTER TABLE `entityfiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `entityfiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entityimages`
--

DROP TABLE IF EXISTS `entityimages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entityimages` (
  `entityId` varchar(255) NOT NULL,
  `imageId` varchar(255) NOT NULL,
  PRIMARY KEY (`entityId`,`imageId`),
  KEY `imageId_idx` (`imageId`),
  CONSTRAINT `entityIdFKentityimages` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `imageId` FOREIGN KEY (`imageId`) REFERENCES `image` (`imageId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entityimages`
--

LOCK TABLES `entityimages` WRITE;
/*!40000 ALTER TABLE `entityimages` DISABLE KEYS */;
/*!40000 ALTER TABLE `entityimages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entitylocations`
--

DROP TABLE IF EXISTS `entitylocations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entitylocations` (
  `locationId` varchar(200) NOT NULL,
  `entityId` varchar(255) NOT NULL,
  PRIMARY KEY (`locationId`,`entityId`),
  KEY `entityIdFKentitylocations_idx` (`entityId`),
  CONSTRAINT `entityIdFKentitylocations` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `locationIdFKentitylocations` FOREIGN KEY (`locationId`) REFERENCES `location` (`locationId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entitylocations`
--

LOCK TABLES `entitylocations` WRITE;
/*!40000 ALTER TABLE `entitylocations` DISABLE KEYS */;
/*!40000 ALTER TABLE `entitylocations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entityprofilepictures`
--

DROP TABLE IF EXISTS `entityprofilepictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entityprofilepictures` (
  `entityId` varchar(255) NOT NULL,
  `imageId` varchar(255) NOT NULL,
  PRIMARY KEY (`entityId`,`imageId`),
  KEY `imageId_idx` (`imageId`),
  CONSTRAINT `entityIdFKentityprofilepictures` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `imageIdFKentityprofilepictures` FOREIGN KEY (`imageId`) REFERENCES `image` (`imageId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entityprofilepictures`
--

LOCK TABLES `entityprofilepictures` WRITE;
/*!40000 ALTER TABLE `entityprofilepictures` DISABLE KEYS */;
/*!40000 ALTER TABLE `entityprofilepictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entityreads`
--

DROP TABLE IF EXISTS `entityreads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entityreads` (
  `entityId` varchar(255) NOT NULL,
  `userId` varchar(200) NOT NULL,
  PRIMARY KEY (`entityId`,`userId`),
  KEY `userIdFKentityreads_idx` (`userId`),
  CONSTRAINT `entityIdFKentityreads` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKentityreads` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entityreads`
--

LOCK TABLES `entityreads` WRITE;
/*!40000 ALTER TABLE `entityreads` DISABLE KEYS */;
/*!40000 ALTER TABLE `entityreads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entityvideos`
--

DROP TABLE IF EXISTS `entityvideos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entityvideos` (
  `entityId` varchar(255) NOT NULL,
  `videoId` varchar(255) NOT NULL,
  PRIMARY KEY (`entityId`,`videoId`),
  KEY `videoIdFKentityvideos_idx` (`videoId`),
  CONSTRAINT `entityIdFKentityvideos` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `videoIdFKentityvideos` FOREIGN KEY (`videoId`) REFERENCES `video` (`videoId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entityvideos`
--

LOCK TABLES `entityvideos` WRITE;
/*!40000 ALTER TABLE `entityvideos` DISABLE KEYS */;
/*!40000 ALTER TABLE `entityvideos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evernotenote`
--

DROP TABLE IF EXISTS `evernotenote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evernotenote` (
  `noteId` varchar(255) NOT NULL,
  `notebookId` varchar(255) NOT NULL,
  PRIMARY KEY (`noteId`),
  KEY `noteIdFKevernotenotes_idx` (`noteId`),
  KEY `notebookIdFKevernotenote` (`notebookId`),
  CONSTRAINT `noteIdFKevernotenote` FOREIGN KEY (`noteId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `notebookIdFKevernotenote` FOREIGN KEY (`notebookId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
  `entityId` varchar(255) NOT NULL,
  `noteId` varchar(255) NOT NULL,
  PRIMARY KEY (`entityId`),
  KEY `noteIdFKevernoteresource_idx` (`noteId`),
  CONSTRAINT `entityIdFKevernoteresource` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `noteIdFKevernoteresource` FOREIGN KEY (`noteId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
-- Table structure for table `evernoteuser`
--

DROP TABLE IF EXISTS `evernoteuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evernoteuser` (
  `userId` varchar(200) NOT NULL,
  `authToken` varchar(200) NOT NULL,
  `usn` varchar(200) NOT NULL,
  PRIMARY KEY (`userId`),
  CONSTRAINT `userIdFKevernoteuser` FOREIGN KEY (`userId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evernoteuser`
--

LOCK TABLES `evernoteuser` WRITE;
/*!40000 ALTER TABLE `evernoteuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `evernoteuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file` (
  `fileId` varchar(255) NOT NULL,
  PRIMARY KEY (`fileId`),
  KEY `fileIdFKfiles_idx` (`fileId`),
  CONSTRAINT `fileIdFKfiles` FOREIGN KEY (`fileId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flag`
--

DROP TABLE IF EXISTS `flag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flag` (
  `flagId` varchar(200) NOT NULL,
  `type` varchar(200) NOT NULL,
  `value` varchar(200) NOT NULL,
  `endTime` varchar(200) NOT NULL,
  PRIMARY KEY (`flagId`),
  CONSTRAINT `flagIdFKflag` FOREIGN KEY (`flagId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flag`
--

LOCK TABLES `flag` WRITE;
/*!40000 ALTER TABLE `flag` DISABLE KEYS */;
/*!40000 ALTER TABLE `flag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flags`
--

DROP TABLE IF EXISTS `flags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flags` (
  `flagId` varchar(200) NOT NULL,
  `entityId` varchar(255) NOT NULL,
  `userId` varchar(200) NOT NULL,
  PRIMARY KEY (`flagId`,`entityId`,`userId`),
  KEY `entityId_idx` (`entityId`),
  KEY `userId_idx` (`userId`),
  CONSTRAINT `entityId` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `flagId` FOREIGN KEY (`flagId`) REFERENCES `flag` (`flagId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userId` FOREIGN KEY (`userId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flags`
--

LOCK TABLES `flags` WRITE;
/*!40000 ALTER TABLE `flags` DISABLE KEYS */;
/*!40000 ALTER TABLE `flags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friends` (
  `userId` varchar(200) NOT NULL,
  `friendId` varchar(200) NOT NULL,
  PRIMARY KEY (`userId`,`friendId`),
  KEY `friendIdFKfriends_idx` (`friendId`),
  CONSTRAINT `friendIdFKfriends` FOREIGN KEY (`friendId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKfriends` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friends`
--

LOCK TABLES `friends` WRITE;
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `imageId` varchar(255) NOT NULL,
  `type` varchar(200) NOT NULL,
  `link` varchar(255) NOT NULL,
  PRIMARY KEY (`imageId`),
  CONSTRAINT `imageIdFKimage` FOREIGN KEY (`imageId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
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
  `entityId` varchar(255) NOT NULL,
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
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `likes` (
  `userId` varchar(200) NOT NULL,
  `entityId` varchar(255) NOT NULL,
  `value` varchar(45) NOT NULL,
  PRIMARY KEY (`userId`,`entityId`),
  KEY `entityIdFKlikes_idx` (`entityId`),
  CONSTRAINT `entityIdFKlikes` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKlikes` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `livingdoc`
--

DROP TABLE IF EXISTS `livingdoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `livingdoc` (
  `livingDocId` varchar(255) NOT NULL,
  PRIMARY KEY (`livingDocId`),
  CONSTRAINT `livingDocIdFKlivingdoc` FOREIGN KEY (`livingDocId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `livingdoc`
--

LOCK TABLES `livingdoc` WRITE;
/*!40000 ALTER TABLE `livingdoc` DISABLE KEYS */;
/*!40000 ALTER TABLE `livingdoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `livingdocusers`
--

DROP TABLE IF EXISTS `livingdocusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `livingdocusers` (
  `livingDocId` varchar(255) NOT NULL,
  `userId` varchar(255) NOT NULL,
  PRIMARY KEY (`livingDocId`,`userId`),
  KEY `userId_idx` (`userId`),
  CONSTRAINT `livingDocIdFKlivingdocusers` FOREIGN KEY (`livingDocId`) REFERENCES `livingdoc` (`livingDocId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKlivingdocusers` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `livingdocusers`
--

LOCK TABLES `livingdocusers` WRITE;
/*!40000 ALTER TABLE `livingdocusers` DISABLE KEYS */;
/*!40000 ALTER TABLE `livingdocusers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `locationId` varchar(200) NOT NULL,
  `latitude` varchar(200) NOT NULL,
  `longitude` varchar(200) NOT NULL,
  `accuracy` varchar(200) NOT NULL,
  PRIMARY KEY (`locationId`),
  CONSTRAINT `locationIdFKlocation` FOREIGN KEY (`locationId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
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
-- Table structure for table `mail`
--

DROP TABLE IF EXISTS `mail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail` (
  `mailId` varchar(255) NOT NULL,
  `receiverEmail` varchar(255) NOT NULL,
  `hash` varchar(255) NOT NULL,
  PRIMARY KEY (`mailId`),
  CONSTRAINT `mailIdFKmail` FOREIGN KEY (`mailId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mail`
--

LOCK TABLES `mail` WRITE;
/*!40000 ALTER TABLE `mail` DISABLE KEYS */;
/*!40000 ALTER TABLE `mail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `messageId` varchar(200) NOT NULL,
  `userId` varchar(200) NOT NULL,
  `forEntityId` varchar(200) NOT NULL,
  `messageContent` varchar(10000) NOT NULL,
  PRIMARY KEY (`messageId`),
  KEY `userIdFKmessage_idx` (`userId`),
  KEY `forEntityIdFKmessage_idx` (`forEntityId`),
  CONSTRAINT `forEntityIdFKmessage` FOREIGN KEY (`forEntityId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `messageIdFKmessage` FOREIGN KEY (`messageId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `userIdFKmessage` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ratings` (
  `userId` varchar(200) NOT NULL,
  `entityId` varchar(255) NOT NULL,
  `ratingValue` varchar(200) NOT NULL,
  `ratingId` varchar(200) NOT NULL,
  PRIMARY KEY (`userId`,`entityId`,`ratingId`),
  KEY `entityIdFKratingass_idx` (`entityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recommuserrealms`
--

DROP TABLE IF EXISTS `recommuserrealms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recommuserrealms` (
  `userId` varchar(255) NOT NULL,
  `realm` varchar(255) NOT NULL,
  PRIMARY KEY (`userId`,`realm`),
  CONSTRAINT `userIdFKrecommuserrealms` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommuserrealms`
--

LOCK TABLES `recommuserrealms` WRITE;
/*!40000 ALTER TABLE `recommuserrealms` DISABLE KEYS */;
/*!40000 ALTER TABLE `recommuserrealms` ENABLE KEYS */;
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
INSERT INTO `space` VALUES ('circleSpace'),('followSpace'),('privateSpace'),('sharedSpace');
/*!40000 ALTER TABLE `space` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `userId` varchar(200) NOT NULL,
  `entityId` varchar(255) NOT NULL,
  `tagId` varchar(200) NOT NULL,
  `tagSpace` varchar(200) NOT NULL,
  `creationTime` varchar(200) NOT NULL,
  `circleId` varchar(150) NOT NULL DEFAULT '',
  PRIMARY KEY (`userId`,`entityId`,`tagId`,`tagSpace`,`circleId`),
  KEY `entityIdFKtagass_idx` (`entityId`),
  KEY `tagIdFKtagass_idx` (`tagId`),
  KEY `tagSpaceFKtagass_idx` (`tagSpace`),
  KEY `circleIdFKtagass_idx` (`circleId`),
  CONSTRAINT `circleIdFKtagass` FOREIGN KEY (`circleId`) REFERENCES `circle` (`circleId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `tagIdFKtagass` FOREIGN KEY (`tagId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `tagSpaceFKtagass` FOREIGN KEY (`tagSpace`) REFERENCES `space` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
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
  `entityId` varchar(255) NOT NULL,
  `eventType` varchar(200) NOT NULL,
  `content` varchar(100) NOT NULL,
  PRIMARY KEY (`userEventId`),
  KEY `userIdFKues_idx` (`userId`),
  KEY `entityId_idx` (`entityId`),
  KEY `userEventIdFKues_idx` (`userEventId`),
  CONSTRAINT `userEventIdFKues` FOREIGN KEY (`userEventId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ues`
--

LOCK TABLES `ues` WRITE;
/*!40000 ALTER TABLE `ues` DISABLE KEYS */;
/*!40000 ALTER TABLE `ues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userId` varchar(255) NOT NULL,
  `email` varchar(200) NOT NULL,
  PRIMARY KEY (`userId`),
  CONSTRAINT `userIdFKuser` FOREIGN KEY (`userId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video` (
  `videoId` varchar(200) NOT NULL,
  `genre` varchar(200) NOT NULL,
  `link` varchar(255) NOT NULL,
  `videoType` varchar(200) NOT NULL,
  PRIMARY KEY (`videoId`),
  CONSTRAINT `videoIdFKvideo` FOREIGN KEY (`videoId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videoannotation`
--

DROP TABLE IF EXISTS `videoannotation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `videoannotation` (
  `videoAnnotationId` varchar(200) NOT NULL,
  `timePoint` varchar(200) NOT NULL,
  `x` varchar(200) NOT NULL,
  `y` varchar(200) NOT NULL,
  PRIMARY KEY (`videoAnnotationId`),
  CONSTRAINT `videoAnnotationIdvideoannotation` FOREIGN KEY (`videoAnnotationId`) REFERENCES `entity` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videoannotation`
--

LOCK TABLES `videoannotation` WRITE;
/*!40000 ALTER TABLE `videoannotation` DISABLE KEYS */;
/*!40000 ALTER TABLE `videoannotation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videoannotations`
--

DROP TABLE IF EXISTS `videoannotations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `videoannotations` (
  `videoId` varchar(200) NOT NULL,
  `videoAnnotationId` varchar(200) NOT NULL,
  PRIMARY KEY (`videoId`,`videoAnnotationId`),
  KEY `videoAnnotationIdFKvideoannotations_idx` (`videoAnnotationId`),
  CONSTRAINT `videoAnnotationIdFKvideoannotations` FOREIGN KEY (`videoAnnotationId`) REFERENCES `videoannotation` (`videoAnnotationId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `videoIdFKvideoannotations` FOREIGN KEY (`videoId`) REFERENCES `video` (`videoId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videoannotations`
--

LOCK TABLES `videoannotations` WRITE;
/*!40000 ALTER TABLE `videoannotations` DISABLE KEYS */;
/*!40000 ALTER TABLE `videoannotations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videousers`
--

DROP TABLE IF EXISTS `videousers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `videousers` (
  `userId` varchar(200) NOT NULL,
  `videoId` varchar(255) NOT NULL,
  PRIMARY KEY (`userId`,`videoId`),
  KEY `videoIdFKuservideos_idx` (`videoId`),
  CONSTRAINT `userIdFKuservideos` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `videoIdFKuservideos` FOREIGN KEY (`videoId`) REFERENCES `video` (`videoId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videousers`
--

LOCK TABLES `videousers` WRITE;
/*!40000 ALTER TABLE `videousers` DISABLE KEYS */;
/*!40000 ALTER TABLE `videousers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-27  7:51:32
