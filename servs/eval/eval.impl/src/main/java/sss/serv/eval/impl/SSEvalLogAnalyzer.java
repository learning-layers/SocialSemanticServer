package sss.serv.eval.impl;

import at.kc.tugraz.ss.message.api.SSMessageServerI;
import at.kc.tugraz.ss.message.datatypes.SSMessage;
import at.kc.tugraz.ss.message.datatypes.par.SSMessagesGetPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.SSEvalLogEntry;
import sss.serv.eval.impl.helpers.SSEpisodeShareInfo;
import sss.serv.eval.impl.helpers.SSUserInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnReceivedSharedEpisodeInfo;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.service.disc.api.SSDiscServerI;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.servs.livingdocument.api.SSLivingDocServerI;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocsGetPar;
import sss.serv.eval.impl.helpers.SSEpisodeCreationInfo;
import sss.serv.eval.impl.helpers.SSEvalActionInfo;
import sss.serv.eval.impl.helpers.SSImportInfo;
import sss.serv.eval.impl.helpers.SSLDInfo;
import sss.serv.eval.impl.helpers.SSMessageSentInfo;
import sss.serv.eval.impl.helpers.SSStartDiscussionInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnBitInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnEpisodeInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnOwnBitInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnOwnEpisodeInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnReceivedDiscussionInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnReceivedSharedBitInfo;

public class SSEvalLogAnalyzer {
  
  private final SSLearnEpServerI       learnEpServ;
  private final SSEntityServerI        entityServ;
  private final SSMessageServerI       messageServ;
  private final SSDiscServerI          discServ;
  private final SSLivingDocServerI     ldServ;
  private final Long                   timeBeginStudy;
  private final Long                   timeBeginLogAnalyze;
  private final Map<String, SSEntity>  episodes     = new HashMap<>();
  private final List<String>           ignoredUsers = new ArrayList<>();
  
  public SSEvalLogAnalyzer(
    final SSLearnEpServerI   learnEpServ,
    final SSEntityServerI    entityServ,
    final SSMessageServerI   messageServ,
    final SSDiscServerI      discServ,
    final SSLivingDocServerI ldServ,
    final Long               timeBeginStudy,
    final Long               timeBeginLogAnalyze){
    
    this.learnEpServ          = learnEpServ;
    this.entityServ           = entityServ;
    this.messageServ          = messageServ;
    this.ldServ               = ldServ;
    this.discServ             = discServ;
    this.timeBeginStudy       = timeBeginStudy;
    this.timeBeginLogAnalyze  = timeBeginLogAnalyze;
      
    ignoredUsers.add("t.treasure-jones@leeds.ac.uk");
    ignoredUsers.add("bn-testuser7@know-center.at");
    ignoredUsers.add("bn-testuser8@know-center.at");
    ignoredUsers.add("john.bibby@nhs.net");
    ignoredUsers.add("m.p.kerr@leeds.ac.uk");
    ignoredUsers.add("david.zaki@hotmail.com");
    ignoredUsers.add("mar7in.bachl@gmail.com");
    ignoredUsers.add("mar7in.bachl@hotmail.com");
  }
  
  public void analyzeLDs(final List<SSEvalLogEntry> logEntries) throws Exception{
    
    try{
      
      final Map<String, SSLDInfo> ldInfos = new HashMap<>();
      
      final SSLivingDocsGetPar ldDocsGetPar =
        new SSLivingDocsGetPar(
          SSConf.systemUserUri,
          null, //forUser,
          false, //withUserRestriction,
          true); //invokeEntityHandlers
      
      ldDocsGetPar.setDiscs = true;
      
      final SSDiscGetPar discGetPar =
        new SSDiscGetPar(
          SSConf.systemUserUri,
          null, //disc
          true, //setEntries
          false, //withUserRestriction
          true); //invokeEntityHandlers
      
      discGetPar.setAttachedEntities = true;
      
      SSEntity disc;
      SSLDInfo ldInfo;
      
      for(SSEntity doc : ldServ.livingDocsGet(ldDocsGetPar)){
        
        if(SSStrU.equalsOne(doc.author.label, ignoredUsers)){
          continue;
        }

        if(doc.creationTime < timeBeginStudy){
          continue;
        }
        
        ldInfo         = getOrCreateLDInfo(ldInfos, doc);
        ldInfo.ldID    = doc.id;
        ldInfo.ldLabel = doc.label;
        
        for(SSEntity discEntity : doc.discs){
          
          discGetPar.disc = discEntity.id;
          disc            = discServ.discGet(discGetPar);
          
          for(SSEntity attachedEntity : disc.attachedEntities){
            ldInfo.attachedBitsFromDT.add(attachedEntity);
          }
          
          for(SSEntity discEntry : disc.entries){
            
            for(SSEntity attachedEntity : discEntry.attachedEntities){
              ldInfo.attachedBitsFromDT.add(attachedEntity);
            }
          }
        }
      }
      
      for(SSLDInfo docInfo : ldInfos.values()){
        
        if(docInfo.attachedBitsFromDT.isEmpty()){
          continue;
        }
        
        System.out.println();
        System.out.println();
        System.out.println(docInfo.ldLabel);
        System.out.println("##################################");
        System.out.println();
        
        for(SSEntity attachedEntity : docInfo.attachedBitsFromDT){
          System.out.println("    " + attachedEntity.label + " | " + attachedEntity.type + " | (" + attachedEntity.id + ")");
        }
        
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeUsers(final List<SSEvalLogEntry> logEntries) throws Exception{
    
    try{
      
      final Map<String, SSEntity> originalEpisodes    = new HashMap<>();
      final List<SSEntity>        episodesForLabel    = new ArrayList<>();
      
      for(SSEvalLogEntry logEntry : logEntries){
        
        episodesForLabel.clear();
        
        switch(logEntry.logType){
          
          case copyLearnEpForUser:{
            
            episodesForLabel.addAll(
              entityServ.entityFromTypeAndLabelGet(
                new SSEntityFromTypeAndLabelGetPar(
                  SSConf.systemUserUri,
                  logEntry.entityLabel,
                  SSEntityE.learnEp,
                  false)));
            
            for(SSEntity episode : episodesForLabel){
              
              if(!SSStrU.containsKey(originalEpisodes, episode.label)){
                originalEpisodes.put(episode.label.toString(), episode);
              }
              
              if(episode.creationTime < originalEpisodes.get(episode.label.toString()).creationTime){
                originalEpisodes.put(episode.label.toString(), episode);
              }
            }
            
            break;
          }
        }
      }
      
      final Map<String, SSUserInfo> userInfos = new HashMap<>();
      SSUserInfo                    userInfo;
      SSEntity                      originalEpisode;
      
      for(SSEntity episode : episodes.values()){
        
        if(SSStrU.equalsOne  (episode.author.label, ignoredUsers)){
          continue;
        }
        
        if(!SSStrU.containsKey(userInfos, episode.author.label)){
          
          userInfo = new SSUserInfo();
          
          userInfos.put(episode.author.label.toString(), userInfo);
        }else{
          userInfo = userInfos.get(episode.author.label.toString());
        }
        
        if(episode.creationTime < timeBeginLogAnalyze){
          System.out.println(episode.label);
          continue;
        }
        
        originalEpisode = originalEpisodes.get(episode.label.toString());
        
        if(
          originalEpisode != null &&
          !SSStrU.equals(originalEpisode.author.label, episode.author.label)){
          continue;
        }
        
        userInfo.createdEpisodeInfos.add(
          new SSEpisodeCreationInfo(
            episode.id,
            episode.label,
            episode.creationTime));
      }
      
      SSMessage message;
      
      for(SSEntity messageEntity :
        messageServ.messagesGet(
          new SSMessagesGetPar(
            SSConf.systemUserUri,
            null, //forUser,
            true, //includeRead,
            timeBeginLogAnalyze, //startTime,
            false, //withUserRestriction,
            false))){ //invokeEntityHandlers
      
        message = (SSMessage) messageEntity;
        
        if(
          SSStrU.equalsOne(message.author.label,  ignoredUsers) ||
          SSStrU.equalsOne(message.forUser.label, ignoredUsers)){
          continue;
        }
        
        if(!SSStrU.containsKey(userInfos, message.author.label)){
          
          userInfo = new SSUserInfo();
          
          userInfos.put(message.author.label.toString(), userInfo);
        }else{
          userInfo = userInfos.get(message.author.label.toString());
        }
        
        userInfo.messageSentInfos.add(
          new SSMessageSentInfo(
            message.author.label, 
            message.forUser.label, 
            SSStrU.toStr(message.content), 
            message.creationTime));
      }
      
      for(SSEvalLogEntry logEntry : logEntries){
        
        if(SSStrU.equalsOne  (logEntry.userLabel, ignoredUsers)){
          continue;
        }

        if(logEntry.toolContext != null){
         
          switch(logEntry.toolContext){

            case evernoteImport:{
              addImportInfos(userInfos, logEntry);
              break;
            }
          }
        }
        
        switch(logEntry.logType){
          
          case addDiscEntry:{
            
            addWorkedOnReceivedDiscussion(userInfos, logEntry);
            break;
          }
          
          case discussEntity:{
            
            addStartedDiscussionInfos(userInfos, logEntry);
            break;
          }
          
          case changeLabel:
          case changeDescription:
          case addTag:
          case setImportance:
          case removeTag:{
          
            addWorkedOnOwnBit           (userInfos, logEntry);
            addWorkedOnReceivedSharedBit(userInfos, logEntry);
            break;
          }

          case copyLearnEpForUser:
          case shareLearnEpWithUser:{
            
            addSharedEpisodeInfos           (userInfos, logEntry);
            break;
          }
          
          case removeLearnEpVersionCircle:
          case removeLearnEpVersionEntity:
          case addEntityToLearnEpVersion:
          case addCircleToLearnEpVersion:
          case addEntityToLearnEpCircle:
          case removeEntityFromLearnEpCircle:
          case removeLearnEpVersionCircleWithEntitites:{

            addWorkedOnOwnEpisode           (userInfos, logEntry);
            addWorkedOnReceivedSharedEpisode(userInfos, logEntry);
            break;
          }
        }
      }
      
      for(Map.Entry<String, SSUserInfo> user : userInfos.entrySet()){
        System.out.println();
        System.out.println();
        System.out.println("##################################");
        System.out.println("##################################");
        System.out.println(user.getKey());
        System.out.println("##################################");
        System.out.println("##################################");
        System.out.println();
       
        printUserImports                          (user.getValue().importInfos);
        
        printUserEpisodeCreated                   (user.getValue().createdEpisodeInfos);
        printUserWorkedOnOwnEpisode               (user.getValue().workedOnOwnEpisodeInfos);
        printUserWorkedOnOwnBit                   (user.getValue().workedOnOwnBitInfos);
        
        printUserEpisodeShares                    (user.getValue().sharedEpisodeInfos);
        printUserWorkedOnReceivedSharedEpisode    (user.getValue().workedOnReceivedEpisodeInfos);
        printUserWorkedOnReceivedSharedBit        (user.getValue().workedOnReceivedBitInfos);
        
        printUserSentMessages                     (user.getValue().messageSentInfos);
        
        printUserStartedDiscussions               (user.getValue().startDiscussionInfos);
        printUserWorkedOnReceivedDiscussions      (user.getValue().workedOnReceivedDiscussionInfos);
        
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addImportInfos(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo   userInfo = userInfos.get(logEntry.userLabel.toString());
      final SSImportInfo info     = 
        new SSImportInfo(
          logEntry.entity,
          logEntry.entityLabel, 
          logEntry.timestamp);
        
      switch(logEntry.logType){
        
        case addTag:{
          
          info.bitType   = SSEntityE.tag;
          info.targetBit = logEntry.entityLabel;
          info.content   = logEntry.content;
          break;
        }
        
        case addNotebook:{

          info.bitType = SSEntityE.evernoteNotebook;
          break;
        }
        
        case addNote:{
          info.bitType = SSEntityE.evernoteNote;
          break;
        }
        
        case addResource:{
          info.bitType = SSEntityE.evernoteResource;
          break;
        }
      }
      
      if(SSStrU.containsKey(userInfo.importInfos, info.bitID)){
        userInfo.importInfos.get(info.bitID.toString()).add(info);
      }else{
        
        final List<SSImportInfo> importInfos = new ArrayList<>();
        
        importInfos.add(info);
        
        userInfo.importInfos.put(info.bitID.toString(), importInfos);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
      
  private void addWorkedOnReceivedSharedBit(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo                          userInfo  = getOrCreateUserInfo(userInfos, logEntry);
      final SSWorkedOnReceivedSharedBitInfo     workedOnBit;
      
      final SSEntity entity =
        entityServ.entityGet(
          new SSEntityGetPar(
            SSConf.systemUserUri,
            logEntry.entity,
            false, //withUserRestriction
            null)); //descPar
      
      if(SSStrU.equals(entity.author.label, logEntry.userLabel)){
        return;
      }
      
      if(logEntry.toolContext != null){
        
        switch(logEntry.toolContext){
          
          case evernoteImport:{
            return;
          }
        }
      }
      
      if(!SSStrU.containsKey(userInfo.workedOnReceivedBitInfos, entity.id)){
        
        workedOnBit = new SSWorkedOnReceivedSharedBitInfo();
        
        workedOnBit.entity = entity;
          
        userInfo.workedOnReceivedBitInfos.put(SSStrU.toStr(entity.id), workedOnBit);
      }else{
        workedOnBit = userInfo.workedOnReceivedBitInfos.get(SSStrU.toStr(entity.id));
      }
      
      workedOnBit.totalActionsDone++;
      
      SSEvalLogE.addDistinctNotNull(workedOnBit.actions, logEntry.logType);

//      workedOnBit.actionDetails.add(
//        new SSEvalActionInfo(
//          logEntry.logType, 
//          logEntry.timestamp,
//          logEntry.content));
      
      addBitActionDetails(workedOnBit, logEntry);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private void addStartedDiscussionInfos(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo            userInfo  = getOrCreateUserInfo(userInfos, logEntry);
      final SSStartDiscussionInfo info      = 
        new SSStartDiscussionInfo(
          logEntry.userLabel, 
          SSLabel.get(SSStrU.toCommaSeparatedStrNotNull(logEntry.entityLabels)), 
          logEntry.entityLabel); //targetLabel
      
      userInfo.startDiscussionInfos.add(info);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private SSUserInfo getOrCreateUserInfo(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo userInfo;
      
      if(!SSStrU.containsKey(userInfos, logEntry.userLabel)){
        
        userInfo = new SSUserInfo();
        
        userInfos.put(logEntry.userLabel.toString(), userInfo);
      }else{
        userInfo = userInfos.get(logEntry.userLabel.toString());
      }
      
      return userInfo;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  private SSLDInfo getOrCreateLDInfo(
    final Map<String, SSLDInfo> ldInfos, 
    final SSEntity              doc) throws Exception{
    
    try{
      
      final SSLDInfo ldInfo;
      
      if(!SSStrU.containsKey(ldInfos, doc.id)){
        
        ldInfo = new SSLDInfo();
        
        ldInfos.put(doc.id.toString(), ldInfo);
      }else{
        ldInfo = ldInfos.get(doc.id.toString());
      }
      
      return ldInfo;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void addWorkedOnOwnBit(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo                          userInfo = getOrCreateUserInfo(userInfos, logEntry);
      final SSWorkedOnOwnBitInfo                workedOnBit;
      
      final SSEntity entity =
        entityServ.entityGet(
          new SSEntityGetPar(
            SSConf.systemUserUri,
            logEntry.entity,
            false, //withUserRestriction
            null)); //descPar
      
      if(!SSStrU.equals(entity.author.label, logEntry.userLabel)){
        return;
      }
      
      if(logEntry.toolContext != null){
        
        switch(logEntry.toolContext){
          
          case evernoteImport:{
            return;
          }
        }
      }
      
      if(!SSStrU.containsKey(userInfo.workedOnOwnBitInfos, entity.id)){
        
        workedOnBit = new SSWorkedOnOwnBitInfo();
        
        workedOnBit.entity = entity;
          
        userInfo.workedOnOwnBitInfos.put(SSStrU.toStr(entity.id), workedOnBit);
      }else{
        workedOnBit = userInfo.workedOnOwnBitInfos.get(SSStrU.toStr(entity.id));
      }
      
      workedOnBit.totalActionsDone++;
      
      SSEvalLogE.addDistinctNotNull(workedOnBit.actions, logEntry.logType);

//      workedOnBit.actionDetails.add(
//        new SSEvalActionInfo(
//          logEntry.logType, 
//          logEntry.timestamp,
//          logEntry.content));
      
      
      addBitActionDetails(workedOnBit, logEntry);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addWorkedOnReceivedDiscussion(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo                       userInfo  = getOrCreateUserInfo(userInfos, logEntry);
      final SSWorkedOnReceivedDiscussionInfo info;
      
      if(SSStrU.containsKey(userInfo.workedOnReceivedDiscussionInfos, logEntry.entity)){
        info = userInfo.workedOnReceivedDiscussionInfos.get(logEntry.entity.toString());
      }else{
        
        info = new SSWorkedOnReceivedDiscussionInfo();
        
        info.discussionLabel = logEntry.entityLabel;
        
        final SSEntity disc = 
          discServ.discGet(
            new SSDiscGetPar(
              SSConf.systemUserUri, 
              logEntry.entity, //disc
              true, //setEntries
              false, //withUserRestriction, 
              false)); //invokeEntityHandlers
        
        if(SSStrU.equals(disc.author.label, logEntry.userLabel)){
          return; 
        }
        
        SSLabel.addDistinctNotNull(info.contributors, disc.author.label);
        
        for(SSEntity entry : disc.entries){
          
          if(!SSStrU.equals(entry.author.label, logEntry.userLabel)){
            SSLabel.addDistinctNotNull(info.contributors, entry.author.label);
          }
        }
          
        userInfo.workedOnReceivedDiscussionInfos.put(logEntry.entity.toString(), info);
      }
      
      info.totalActionsDone++;
      
      SSEvalLogE.addDistinctNotNull(info.actions, logEntry.logType);
      
      info.actionDetails.add(
        new SSEvalActionInfo(
          logEntry.logType,
          logEntry.timestamp,
          logEntry.content));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addWorkedOnReceivedSharedEpisode(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo                          userInfo            = getOrCreateUserInfo(userInfos, logEntry);
      final SSWorkedOnReceivedSharedEpisodeInfo workedOnEpisode;
      SSUri                                     episodeID           = null;
      SSLabel                                   episodeLabel        = null;
      SSLabel                                   episodeAuthor       = null;
      Long                                      episodeCreationTime = null;
      
      if(SSStrU.equals(logEntry.entityType, SSEntityE.learnEp)){
        
        if(SSStrU.containsKey(episodes, logEntry.entity)){
          
          episodeID              = logEntry.entity;
          episodeLabel           = episodes.get(episodeID.toString()).label;
          episodeAuthor          = episodes.get(episodeID.toString()).author.label;
          episodeCreationTime    = episodes.get(episodeID.toString()).creationTime;
        }
        
      }else{
        
         for(SSUri possibleLearnEpID : logEntry.entityIDs){
          
          if(SSStrU.containsKey(episodes, possibleLearnEpID)){
            episodeID              = possibleLearnEpID;
            episodeLabel           = episodes.get(episodeID.toString()).label;
            episodeAuthor          = episodes.get(episodeID.toString()).author.label;
            episodeCreationTime    = episodes.get(episodeID.toString()).creationTime;
            break;
          }
        }
      }
      
      if(
        SSObjU.isNull(episodeID, episodeLabel, episodeAuthor)){
        SSLogU.warn("episode not found");
        return;
      }
      
      if(SSStrU.equals(episodeAuthor, logEntry.userLabel)){
        return;
      }
      
      if(episodeCreationTime < timeBeginStudy){
        return;
      }
      
      if(!SSStrU.containsKey(userInfo.workedOnReceivedEpisodeInfos, episodeID)){
        
        workedOnEpisode = new SSWorkedOnReceivedSharedEpisodeInfo();
          
        userInfo.workedOnReceivedEpisodeInfos.put(SSStrU.toStr(episodeID), workedOnEpisode);
      }else{
        workedOnEpisode = userInfo.workedOnReceivedEpisodeInfos.get(SSStrU.toStr(episodeID));
      }
      
      workedOnEpisode.episodeID = episodeID;
      workedOnEpisode.totalActionsDone++;
      
      SSEvalLogE.addDistinctNotNull(workedOnEpisode.actions, logEntry.logType);
      
      addEpisodeActionDetails(workedOnEpisode, logEntry);
          
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addWorkedOnOwnEpisode(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo                          userInfo            = getOrCreateUserInfo(userInfos, logEntry);
      final SSWorkedOnOwnEpisodeInfo            workedOnEpisode;
      SSUri                                     episodeID           = null;
      SSLabel                                   episodeLabel        = null;
      SSLabel                                   episodeAuthor       = null;
      Long                                      episodeCreationTime = null;
      
      if(SSStrU.equals(logEntry.entityType, SSEntityE.learnEp)){
        
        if(SSStrU.containsKey(episodes, logEntry.entity)){
          
          episodeID           = logEntry.entity;
          episodeLabel        = episodes.get(episodeID.toString()).label;
          episodeAuthor       = episodes.get(episodeID.toString()).author.label;
          episodeCreationTime = episodes.get(episodeID.toString()).creationTime;
        }
        
      }else{
        
         for(SSUri possibleLearnEpID : logEntry.entityIDs){
          
          if(SSStrU.containsKey(episodes, possibleLearnEpID)){
            episodeID           = possibleLearnEpID;
            episodeLabel        = episodes.get(episodeID.toString()).label;
            episodeAuthor       = episodes.get(episodeID.toString()).author.label;
            episodeCreationTime = episodes.get(episodeID.toString()).creationTime;
            break;
          }
        }
      }
      
      if(
        SSObjU.isNull(episodeID, episodeLabel, episodeAuthor)){
        SSLogU.warn("episode not found");
        return;
      }
      
      if(!SSStrU.equals(episodeAuthor, logEntry.userLabel)){
        return;
      }
      
      if(episodeCreationTime < timeBeginStudy){
        return;
      }
      
      if(!SSStrU.containsKey(userInfo.workedOnOwnEpisodeInfos, episodeID)){
        
        workedOnEpisode = new SSWorkedOnOwnEpisodeInfo();
          
        userInfo.workedOnOwnEpisodeInfos.put(SSStrU.toStr(episodeID), workedOnEpisode);
      }else{
        workedOnEpisode = userInfo.workedOnOwnEpisodeInfos.get(SSStrU.toStr(episodeID));
      }
      
      workedOnEpisode.episodeID = episodeID;
      workedOnEpisode.totalActionsDone++;
      
      SSEvalLogE.addDistinctNotNull(workedOnEpisode.actions, logEntry.logType);
      
      addEpisodeActionDetails(workedOnEpisode, logEntry);
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  private void addBitActionDetails(
    final SSWorkedOnBitInfo        workedOnBitInfo,
    final SSEvalLogEntry           logEntry) throws Exception{
    
    try{
      
      final SSEntityGetPar entityGetPar =
        new SSEntityGetPar(
          SSConf.systemUserUri,
          null, //entity,
          false, //withUserRestriction,
          null);
      
      final SSEntitiesGetPar entitiesGetPar = 
        new SSEntitiesGetPar(
          SSConf.systemUserUri, 
          null, //entities, 
          null, //descPar, 
          false); //withUserRestriction); 
      
      entityGetPar.entity = logEntry.entity;
      
      entitiesGetPar.entities.addAll(logEntry.entityIDs);
        
      final SSEntity       entity      = entityServ.entityGet  (entityGetPar);
      final List<SSEntity> entities    = entityServ.entitiesGet(entitiesGetPar);
      final String         entityStr   = "entity: " + SSStrU.trim(entity.label, 1000) + " (" + entity.type + ")";
      String               entitiesStr = "entities: ";
      
      for(SSEntity entity1 : entities){
        entitiesStr += SSStrU.trim(entity1.label, 1000) + " (" + entity1.type + "), ";
      }
      
      workedOnBitInfo.actionDetails.add(
        new SSEvalActionInfo(
          logEntry.logType,
          logEntry.timestamp,
          entityStr
            + " | "
            + entitiesStr
            + " | "
            + logEntry.content));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addEpisodeActionDetails(
    final SSWorkedOnEpisodeInfo    workedOnEpisodeInfo,
    final SSEvalLogEntry           logEntry) throws Exception{
    
    try{
      
      final SSEntityGetPar entityGetPar =
        new SSEntityGetPar(
          SSConf.systemUserUri,
          null, //entity,
          false, //withUserRestriction,
          null);
      
      final SSEntitiesGetPar entitiesGetPar = 
        new SSEntitiesGetPar(
          SSConf.systemUserUri, 
          null, //entities, 
          null, //descPar, 
          false); //withUserRestriction); 
      
      entityGetPar.entity = logEntry.entity;
      
      entitiesGetPar.entities.addAll(logEntry.entityIDs);
        
      final SSEntity       entity      = entityServ.entityGet  (entityGetPar);
      final List<SSEntity> entities    = entityServ.entitiesGet(entitiesGetPar);
      final String         entityStr   = "entity: " + SSStrU.trim(entity.label, 1000) + " (" + entity.type + ")";
      String               entitiesStr = "entities: ";
      
      for(SSEntity entity1 : entities){
        entitiesStr += SSStrU.trim(entity1.label, 1000) + " (" + entity1.type + "), ";
      }
      
      workedOnEpisodeInfo.actionDetails.add(
        new SSEvalActionInfo(
          logEntry.logType,
          logEntry.timestamp,
          entityStr
            + " | "
            + entitiesStr
            + " | "
            + logEntry.content));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addSharedEpisodeInfos(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      final SSUserInfo         userInfo  = getOrCreateUserInfo(userInfos, logEntry);
      final SSEpisodeShareInfo shareInfo = new SSEpisodeShareInfo();
      final SSEntity           episode   = episodes.get(logEntry.entity.toString());
      
      if(episode == null){
        SSLogU.warn("episode not found");
        return;
      }
      
      shareInfo.id                  = episode.id;
      shareInfo.label               = episode.label;
      shareInfo.creationTime        = episode.creationTime;
      shareInfo.selectedBitsMeasure = logEntry.selectedBitsMeasure;
      shareInfo.shareType           = logEntry.logType;
      shareInfo.timestamp           = logEntry.timestamp;
      
      removeIgnoredUsers(logEntry.userLabels);
      
      SSLabel.addDistinctNotNull(shareInfo.targetUsers, logEntry.userLabels);
      
      userInfo.sharedEpisodeInfos.add(shareInfo);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void printUserWorkedOnReceivedSharedBit(
    final Map<String, SSWorkedOnReceivedSharedBitInfo> workedOnReceivedBitInfos) throws Exception{
    
     try{
      
      if(workedOnReceivedBitInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on shared bits");
      System.out.println("#####################");
      System.out.println();
      
      for(Map.Entry<String, SSWorkedOnReceivedSharedBitInfo> workedOnBit : workedOnReceivedBitInfos.entrySet()){
        
        System.out.println(workedOnBit.getValue().entity.label + " | #actions " + workedOnBit.getValue().totalActionsDone + " | types: " + workedOnBit.getValue().actions);
        System.out.println("action details: ");
        
        for(SSEvalActionInfo actionDetail : workedOnBit.getValue().actionDetails){
          System.out.println(actionDetail.type + " | " + new Date(actionDetail.timestamp).toString() + " | author: " + workedOnBit.getValue().entity.author.label + " | " + actionDetail.content);
        }
        
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void printUserWorkedOnOwnBit(
    final Map<String, SSWorkedOnOwnBitInfo> workedOnOwnBitInfos) throws Exception{
    
     try{
      
      if(workedOnOwnBitInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on own bits");
      System.out.println("##################");
      System.out.println();
      
      for(Map.Entry<String, SSWorkedOnOwnBitInfo> workedOnBit : workedOnOwnBitInfos.entrySet()){
        
        System.out.println(workedOnBit.getValue().entity.label + " | #actions " + workedOnBit.getValue().totalActionsDone + " | types: " + workedOnBit.getValue().actions);
        
        for(SSEvalActionInfo actionDetail : workedOnBit.getValue().actionDetails){
          System.out.println("    " + actionDetail.type + " | " + new Date(actionDetail.timestamp).toString() + " | " + actionDetail.content);
        }
        
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void printUserWorkedOnReceivedSharedEpisode(
    final Map<String, SSWorkedOnReceivedSharedEpisodeInfo> workedOnReceivedSharedEpisodeInfos) throws Exception{
    
    try{
      
      if(workedOnReceivedSharedEpisodeInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on shared episodes");
      System.out.println("#########################");
      System.out.println();
      
      SSEntity episode;
      
      for(Map.Entry<String, SSWorkedOnReceivedSharedEpisodeInfo> workedOnEpisode : workedOnReceivedSharedEpisodeInfos.entrySet()){
        
        episode = episodes.get(workedOnEpisode.getValue().episodeID.toString());
        
        System.out.println(episode.label + " (" + new Date(episode.creationTime) + " " + episode.id + ") | #actions " + workedOnEpisode.getValue().totalActionsDone + " | types: " + workedOnEpisode.getValue().actions);
        
        for(SSEvalActionInfo actionDetail : workedOnEpisode.getValue().actionDetails){
          System.out.println("    " + actionDetail.type + " | " + new Date(actionDetail.timestamp).toString() + " | author: " + episode.author.label + " | " + actionDetail.content);
        }
        
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void printUserWorkedOnReceivedDiscussions(
    final Map<String, SSWorkedOnReceivedDiscussionInfo> workedOnReceivedDiscussionInfos) throws Exception{
    
    try{
      
      if(workedOnReceivedDiscussionInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on received discussions");
      System.out.println("##############################");
      System.out.println();
      
      for(SSWorkedOnReceivedDiscussionInfo workedOnDisc : workedOnReceivedDiscussionInfos.values()){
      
        System.out.println(workedOnDisc.discussionLabel + " | #actions " + workedOnDisc.totalActionsDone + " | types: " + workedOnDisc.actions);
        System.out.println("    " + workedOnDisc.contributors);
        
        for(SSEvalActionInfo actionDetail : workedOnDisc.actionDetails){
          System.out.println("    " + actionDetail.type + " | " + new Date(actionDetail.timestamp).toString() + " | " + actionDetail.content + " | ");
        }
        
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void printUserWorkedOnOwnEpisode(
    final Map<String, SSWorkedOnOwnEpisodeInfo> workedOnOwnEpisodeInfos) throws Exception{
    
    try{
      
      if(workedOnOwnEpisodeInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on own episodes");
      System.out.println("######################");
      System.out.println();
      
      SSEntity episode;
      
      for(Map.Entry<String, SSWorkedOnOwnEpisodeInfo> workedOnEpisode : workedOnOwnEpisodeInfos.entrySet()){
        
        episode = episodes.get(workedOnEpisode.getValue().episodeID.toString());
        
        System.out.println(episode.label + " (" + new Date(episode.creationTime) + " " + episode.id + ") | #actions " + workedOnEpisode.getValue().totalActionsDone + " | types: " + workedOnEpisode.getValue().actions);
        
        for(SSEvalActionInfo actionDetail : workedOnEpisode.getValue().actionDetails){
          System.out.println("    " + actionDetail.type + " | " + new Date(actionDetail.timestamp).toString() + " | " + actionDetail.content);
        }
        
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void printUserEpisodeShares(
    final List<SSEpisodeShareInfo> sharedEpisodeInfos) throws Exception{
    
    try{
      
      if(sharedEpisodeInfos.isEmpty()){
        return;
      }
      
      System.out.println("user shared episodes");
      System.out.println("####################");
      System.out.println();
      
      for(SSEpisodeShareInfo share : sharedEpisodeInfos){
        System.out.println(share.label + " (" + new Date(share.creationTime) + " " + share.id + ")");
        System.out.println("    " + new Date(share.timestamp) + " | " + share.shareType + " (" + share.selectedBitsMeasure + ") | " + share.targetUsers);
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void removeIgnoredUsers(List<? extends Object> users){
    
    for(Object user : users){
  
      if(SSStrU.equals(user, ignoredUsers)){
        users.remove(user);
        removeIgnoredUsers(users);
      }
    }
  }

  private void printUserEpisodeCreated(List<SSEpisodeCreationInfo> createdEpisodeInfos) throws Exception {
    
    try{
      
      if(createdEpisodeInfos.isEmpty()){
        return;
      }
      
      System.out.println("user created episodes");
      System.out.println("#####################");
      System.out.println();
      
      for(SSEpisodeCreationInfo created : createdEpisodeInfos){
        System.out.println(created.episodeLabel);
        System.out.println("    " + new Date(created.timestamp) + " " + created.episodeID);
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private void printUserImports(final Map<String, List<SSImportInfo>> importInfos) throws Exception {
   
    try{
      
      if(importInfos.isEmpty()){
        return;
      }
      
      System.out.println("user imported");
      System.out.println("#############");
      System.out.println();
      
      int counter = 0;
      
      for(Map.Entry<String, List<SSImportInfo>> info : importInfos.entrySet()){
        
        System.out.println(info.getValue().get(0).bitLabel + " | " + info.getValue().get(0).bitType);
        
        counter = 0;
          
        for(SSImportInfo detail : info.getValue()){
          
          switch(detail.bitType){
            
            case tag:{
              System.out.println("    " + new Date(detail.timestamp) + " | TAG: " + detail.content);
              break;
            }
            
            default:{
              
              if(counter > 10){
                break;
              }
              
              System.out.println("    " + new Date(detail.timestamp));
              
              counter++;
            }
          }
        }
        
        System.out.println();
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private void printUserSentMessages(List<SSMessageSentInfo> messageSentInfos) throws Exception {
    
    try{
      
      if(messageSentInfos.isEmpty()){
        return;
      }
      
      System.out.println("user sent messages");
      System.out.println("##################");
      System.out.println();
      
      for(SSMessageSentInfo info : messageSentInfos){
        
        System.out.println(info.targetLabel + " | " + info.timestamp + " | " + info.content);
        System.out.println();
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private void printUserStartedDiscussions(
    final List<SSStartDiscussionInfo> startDiscussionInfos) throws Exception {
    
    try{
      
      if(startDiscussionInfos.isEmpty()){
        return;
      }
      
      System.out.println("user started discussions");
      System.out.println("########################");
      System.out.println();
      
      for(SSStartDiscussionInfo info : startDiscussionInfos){
        
        System.out.println(info.discussionLabel);
        System.out.println("    " + info.targetLabel);
        System.out.println();
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setEpisodes() throws Exception{
    
    try{
      
      for(SSEntity learnEp :
        learnEpServ.learnEpsGet(
          new SSLearnEpsGetPar(
            SSConf.systemUserUri,
            null, //forUser
            false,  //withUserRestriction
            false))){ //invokeEntityHandlers
        
        episodes.put(learnEp.id.toString(), learnEp);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}