package sss.serv.eval.impl;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
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
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import sss.serv.eval.impl.helpers.SSEpisodeCreationInfo;
import sss.serv.eval.impl.helpers.SSEvalActionInfo;
import sss.serv.eval.impl.helpers.SSImportInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnOwnBitInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnOwnEpisodeInfo;
import sss.serv.eval.impl.helpers.SSWorkedOnReceivedSharedBitInfo;

public class SSEvalLogAnalyzer {
  
  private final SSLearnEpServerI       learnEpServ;
  private final SSEntityServerI        entityServ;
  private final Long                   startTime;
  private final Map<String, SSEntity>  episodes     = new HashMap<>();
  private final List<String>           ignoredUsers = new ArrayList<>();
  
  public SSEvalLogAnalyzer(
    final SSLearnEpServerI learnEpServ,
    final SSEntityServerI  entityServ, 
    final Long             startTime){
    
    this.learnEpServ = learnEpServ;
    this.entityServ  = entityServ;
    this.startTime   = startTime;
    
    ignoredUsers.add("t.treasure-jones@leeds.ac.uk");
    ignoredUsers.add("bn-testuser7@know-center.at");
    ignoredUsers.add("bn-testuser8@know-center.at");
    ignoredUsers.add("john.bibby@nhs.net");
    ignoredUsers.add("m.p.kerr@leeds.ac.uk");
    ignoredUsers.add("david.zaki@hotmail.com");
    ignoredUsers.add("mar7in.bachl@gmail.com");
  }
  
  public void setEpisodes() throws Exception{
    
    try{
      
      for(SSEntity learnEp :
        learnEpServ.learnEpsGet(
          new SSLearnEpsGetPar(
            SSVocConf.systemUserUri,
            null, //forUser
            false,  //withUserRestriction
            false))){ //invokeEntityHandlers
        
        episodes.put(learnEp.id.toString(), learnEp);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeUsers(final List<SSEvalLogEntry> logEntries) throws Exception{
    
    try{
      
      final Map<String, SSUserInfo> userInfos = new HashMap<>();
      SSUserInfo                    userInfo;
      
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
        
        if(episode.creationTime < startTime){
          continue;
        }
        
        userInfo.createdEpisodeInfos.add(
          new SSEpisodeCreationInfo(
            episode.id, 
            episode.label, 
            episode.creationTime));
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
      
      final SSUserInfo                          userInfo      = userInfos.get(logEntry.userLabel.toString());
      final SSWorkedOnReceivedSharedBitInfo     workedOnBit;
      
      final SSEntity entity =
        entityServ.entityGet(
          new SSEntityGetPar(
            SSVocConf.systemUserUri,
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

      workedOnBit.actionDetails.add(
        new SSEvalActionInfo(
          logEntry.logType, 
          logEntry.timestamp,
          logEntry.content));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addWorkedOnOwnBit(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo                          userInfo      = userInfos.get(logEntry.userLabel.toString());
      final SSWorkedOnOwnBitInfo                workedOnBit;
      
      final SSEntity entity =
        entityServ.entityGet(
          new SSEntityGetPar(
            SSVocConf.systemUserUri,
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

      workedOnBit.actionDetails.add(
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
      
      final SSUserInfo                          userInfo            = userInfos.get(logEntry.userLabel.toString());
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
      
      if(episodeCreationTime < startTime){
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
      
      workedOnEpisode.actionDetails.add(
        new SSEvalActionInfo(
          logEntry.logType, 
          logEntry.timestamp,
          "entity: " + logEntry.entityLabel + " | entities: " + SSStrU.toCommaSeparatedStrNotNull(logEntry.entityLabels) + " | " + logEntry.content));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addWorkedOnOwnEpisode(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
      final SSUserInfo                          userInfo            = userInfos.get(logEntry.userLabel.toString());
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
      
      if(episodeCreationTime < startTime){
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
      
      workedOnEpisode.actionDetails.add(
        new SSEvalActionInfo(
          logEntry.logType, 
          logEntry.timestamp,
          "entity: " + logEntry.entityLabel + " | entities: " + SSStrU.toCommaSeparatedStrNotNull(logEntry.entityLabels) + " | " + logEntry.content));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addSharedEpisodeInfos(
    final Map<String, SSUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws Exception{
    
    try{
      
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
      
      SSLabel.addDistinctWithoutNull(shareInfo.targetUsers, logEntry.userLabels);
      
      userInfos.get(logEntry.userLabel.toString()).sharedEpisodeInfos.add(shareInfo);
      
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
        
        System.out.println(episode.label + " | #actions " + workedOnEpisode.getValue().totalActionsDone + " | types: " + workedOnEpisode.getValue().actions);
        
        for(SSEvalActionInfo actionDetail : workedOnEpisode.getValue().actionDetails){
          System.out.println("    " + actionDetail.type + " | " + new Date(actionDetail.timestamp).toString() + " | author: " + episode.author.label + " | " + actionDetail.content);
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
        
        System.out.println(episode.label + " | #actions " + workedOnEpisode.getValue().totalActionsDone + " | types: " + workedOnEpisode.getValue().actions);
        
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
        System.out.println(share.label + " (" + new Date(share.creationTime) + ")");
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
        System.out.println("    " + new Date(created.timestamp));
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
}
