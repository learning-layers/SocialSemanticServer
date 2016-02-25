/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
* For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package at.tugraz.sss.servs.eval.impl;

import at.tugraz.sss.servs.eval.impl.analyze.SSEvalLogAnalyzer;
import at.kc.tugraz.ss.activity.api.*;
import at.kc.tugraz.ss.activity.datatypes.*;
import at.kc.tugraz.ss.activity.datatypes.par.*;
import at.kc.tugraz.ss.serv.auth.api.*;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvalLogFilePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.*;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.api.SSEvalClientI;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogEntry;
import sss.serv.eval.datatypes.par.SSEvalAnalyzePar;
import sss.serv.eval.datatypes.par.SSEvalLogPar;
import sss.serv.eval.datatypes.ret.SSEvalLogRet;
import at.kc.tugraz.ss.service.user.api.*;
import at.kc.tugraz.ss.service.user.datatypes.*;
import at.kc.tugraz.ss.service.user.datatypes.pars.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.auth.datatype.par.*;
import java.util.Date;
import sss.serv.eval.conf.*;

public class SSEvalImpl 
extends SSServImplWithDBA 
implements 
  SSEvalClientI, 
  SSEvalServerI{

  private static final long oct1  = new Date(Long.valueOf("1443679508904")).getTime();
  private static final long nov17 = new Date(Long.valueOf("1447752600000")).getTime();
  
  private final SSUserCommons             userCommons = new SSUserCommons();
  private final SSEvalLogAnalyzer         logAnalyzer = new SSEvalLogAnalyzer();//timeBeginStudy
  
  public SSEvalImpl(final SSConfA conf) throws SSErr{

    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
  }

  @Override
  public void evalAnalyze(final SSEvalAnalyzePar par) throws SSErr{
    
    try{
      final List<SSEvalLogEntry> logEntries = getLogEntries(par);
      
      logAnalyzer.setEpisodes (par);
      
      logAnalyzer.analyzeNumberOfLogEntries                         (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfImportedResources                  (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeDistinctUsers                              (par, ((SSEvalConf) conf).ignoredUserLabels,       logEntries);
      logAnalyzer.analyzeNumberArtifacts                            (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfLivingDocs                         (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfTags                               (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfEntriesInDiscussions               (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfTagsInDiscussions                  (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfEntitiesInDiscussions              (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfAuthorsInDiscussions               (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfDiscussions                        (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfTagActivitiesDistinctUserEntityTag (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfTagsForBitsUsedInEpisodes          (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfBitsUsedInEpisodes                 (par, ((SSEvalConf) conf).ignoredUserLabels,       logEntries);
      logAnalyzer.analyzeNumberOfLearnEpCircleLabels                (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfInteractionsInEpisodes             (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfSharedEpisodes                     (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      logAnalyzer.analyzeNumberOfAcceptedRecommendations            (par, ((SSEvalConf) conf).ignoredUserLabels, oct1, logEntries);
      
      System.out.println();
      System.out.println();
      System.out.println("##################################");
      System.out.println("##################################");
      System.out.println("log analyze done");
      System.out.println("##################################");
      System.out.println("##################################");
      System.out.println();
      
//      
//      System.out.println();
//      System.out.println();
//      System.out.println("##################################");
//      System.out.println("##################################");
//      System.out.println("Users");
//      System.out.println("##################################");
//      System.out.println("##################################");
//      System.out.println();
//        
//      logAnalyzer.analyzeUsers  (par, logEntries);
//      
//      System.out.println();
//      System.out.println();
//      System.out.println("##################################");
//      System.out.println("##################################");
//      System.out.println("Living Docs");
//      System.out.println("##################################");
//      System.out.println("##################################");
//      System.out.println();
//      
//      logAnalyzer.analyzeLDs(par, logEntries);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI evalLog(final SSClientE clientType, final SSServPar parA) throws SSErr{

    try{
      userCommons.checkKeyAndSetUser(parA);

      final SSEvalLogPar par = (SSEvalLogPar) parA.getFromClient(clientType, parA, SSEvalLogPar.class);

      return SSEvalLogRet.get(evalLog(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public boolean evalLog(final SSEvalLogPar par) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(par.type, par.user)){
        return false;
      }
      
      final SSUser           originUser          = prepareOriginUser     (par);
      final SSEntity         targetEntity        = prepareTargetEntity   (par);
      final List<SSEntity>   targetEntities      = prepareTargetEntities (par);
      final List<SSEntity>   targetUsers         = prepareTargetUsers    (par);
      final SSCircle         circle              = prepareCircle         (par, originUser,   targetEntity);
      final SSActivity       activity            = prepareActivity       (par, targetEntity, targetEntities, targetUsers);
      final List<SSEntity>   notSelectedEntities = new ArrayList<>();
      final List<SSCircleE>  episodeSpaces       = new ArrayList<>();
      final String           selectedBitsMeasure;
      String                 logText             = SSStrU.empty;
      String                 blankLogText        = SSStrU.empty;
      
      prepareEpisodeSpaces(
        par,
        originUser,
        targetEntity,
        episodeSpaces);
      
      selectedBitsMeasure = 
        prepareSelectedBitsMeasure(
          par, 
          originUser, 
          targetEntity, 
          targetEntities, 
          episodeSpaces);
      
      //timestamp;tool context;user label;user email;log type;entity;entity type;entity label;content;tag type;entities' ids;entities' labels;
      //users' labels;episodespace;selected bits measure;not selected entities' ids;not selected entities' labels;circle type;query;result;
      
      //time stamp
      if(par.creationTime != null){
        logText       += par.creationTime;
        blankLogText  += par.creationTime;
      }else{
        logText      += SSDateU.dateAsLong();
        blankLogText += SSDateU.dateAsLong();
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //tool context
      if(par.toolContext != null){
        logText      += par.toolContext;
        blankLogText += par.toolContext;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //user label
      logText      += SSStrU.escapeColonSemiColonComma(originUser.label);
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.escapeColonSemiColonComma(originUser.oidcSub);
      blankLogText += SSStrU.semiColon;
      
      //log type
      logText      += par.type;
      blankLogText += par.type;
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      // entity
      if(targetEntity != null){
        logText      += targetEntity.id;
        blankLogText += targetEntity.id;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //  entity type
      if(targetEntity != null){
        logText      += targetEntity.type;
        blankLogText += targetEntity.type;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //  entity label
      if(targetEntity != null){
        logText      += SSStrU.escapeColonSemiColonComma(targetEntity.label);
        blankLogText += SSStrU.escapeColonSemiColonComma(targetEntity.label);
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //content
      if(par.content != null){
        logText      += SSStrU.escapeColonSemiColonComma(par.content);
        blankLogText += SSStrU.escapeColonSemiColonComma(par.content);
      }else{
        if(activity != null){
          logText      += activity.activityType;
          blankLogText += activity.activityType;
        }
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      // tag type
      switch(par.type){
        
        case addTag:{
          
          if(par.content != null){
            
            if(par.content.startsWith("*")){
              logText      += 2;
              blankLogText += 2;
            }else{
              logText      += 1;
              blankLogText += 1;
            }
          }
          
          break;
        }
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //  entities' ids
      for(SSEntity entity : targetEntities){
        
        logText      += entity.id;
        blankLogText += entity.id;
        logText      += SSStrU.comma;
        blankLogText += SSStrU.comma;
        
        if(!episodeSpaces.isEmpty()){
          continue;
        }
        
        prepareEpisodeSpaces(
          par, 
          originUser, 
          entity, 
          episodeSpaces);
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      // entity labels
      for(SSEntity entity : targetEntities){
        logText      += SSStrU.escapeColonSemiColonComma(entity.label);
        blankLogText += SSStrU.escapeColonSemiColonComma(entity.label);
        logText      += SSStrU.comma;
        blankLogText += SSStrU.comma;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      // user labels
      for(SSEntity user : targetUsers){
        logText      += SSStrU.escapeColonSemiColonComma(user.label);
        blankLogText += SSStrU.escapeColonSemiColonComma(((SSUser) user).oidcSub);
        logText      += SSStrU.comma;
        blankLogText += SSStrU.comma;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      // episode space
      for(SSCircleE space : episodeSpaces){
        logText      += space.toString();
        blankLogText += space.toString();
        logText      += SSStrU.comma;
        blankLogText += SSStrU.comma;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //selected bits measure
      logText      += selectedBitsMeasure;
      blankLogText += selectedBitsMeasure;
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //not selected entities' ids
      for(SSEntity entity : notSelectedEntities){
        logText      += entity.id;
        blankLogText += entity.id;
        logText      += SSStrU.comma;
        blankLogText += SSStrU.comma;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //not selected entities' labels
      for(SSEntity entity : notSelectedEntities){
        logText      += entity.id;
        blankLogText += entity.id;
        logText      += SSStrU.comma;
        blankLogText += SSStrU.comma;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //circleType
      if(circle != null){
        logText      += circle.circleType;
        blankLogText += circle.circleType;
      }else{
        logText      += SSStrU.empty;
        blankLogText += SSStrU.empty;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //query
      if(par.query != null){
        logText      += par.query;
        blankLogText += par.query;
      }else{
        logText      += SSStrU.empty;
        blankLogText += SSStrU.empty;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      //result
      if(par.result != null){
        logText      += par.result;
        blankLogText += par.result;
      }else{
        logText      += SSStrU.empty;
        blankLogText += SSStrU.empty;
      }
      
      logText      += SSStrU.semiColon;
      blankLogText += SSStrU.semiColon;
      
      logText      = SSStrU.replaceAllLineFeedsWithTextualRepr(logText);
      blankLogText = SSStrU.replaceAllLineFeedsWithTextualRepr(blankLogText);
      
      SSLogU.evalTrace      (logText);
      SSLogU.evalBlankTrace (blankLogText);
      
      return true;
    }catch(Exception error){
      SSLogU.warn(SSWarnE.evalLogFailed, error);
      return false;
    }
  }
  
  private List<SSEvalLogEntry> getLogEntries(final SSServPar servPar) throws SSErr{
    
    try{
      final SSDataImportServerI dataImportServ = (SSDataImportServerI) SSServReg.getServ(SSDataImportServerI.class);
      
      return dataImportServ.dataImportEvalLogFile(
        new SSDataImportEvalLogFilePar(
          servPar,
          servPar.user,
          conf.getSssWorkDirDataCsv() + SSFileU.fileNameSSSEvalLog,
          oct1)); //startTime
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void prepareEpisodeSpaces(
    final SSServPar       servPar,
    final SSEntity        originUser,
    final SSEntity        targetEntity,
    final List<SSCircleE> episodeSpaces) throws SSErr{
    
    try{
      
      if(targetEntity == null){
        return;
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      switch(targetEntity.type){
        
        case learnEp:{
          
          episodeSpaces.addAll(
            entityServ.circleTypesGet(
              new SSCircleTypesGetPar(
                servPar,
                originUser.id,
                targetEntity.id,
                true)));
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private SSCircle prepareCircle(
    final SSServPar  servPar,
    final SSEntity   originUser,
    final SSEntity   targetEntity) throws SSErr{
    
    try{
      
      if(targetEntity == null){
        return null;
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      switch(targetEntity.type){
        
        case circle:{
          
          return entityServ.circleGet(
            new SSCircleGetPar(
              servPar,
              originUser.id, //user
              targetEntity.id, //circle
              null, //entityTypesToIncludeOnly
              false,  //setTags
              null,  //tagSpace
              false,  //setEntities
              false,  //setUsers
              false,  //withUserRestriction
              false)); //invokeEntityHandlers
        }
      }
      
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSActivity prepareActivity(
    final SSEvalLogPar   par,
    final SSEntity       targetEntity,
    final List<SSEntity> targetEntities, 
    final List<SSEntity> targetUsers) throws SSErr{
     
    try{
      
      if(targetEntity == null){
        return null;
      }
      
      switch(par.type){
        
        case clickBit:{
          
          switch(targetEntity.type){
            
            case activity:{
              
              final SSActivityServerI actServ    = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
              final List<SSEntity>    activities =
                actServ.activitiesGet(
                  new SSActivitiesGetPar(
                    par,
                    par.user,
                    SSUri.asListNotNull(targetEntity.id),
                    null, //types,
                    null, //users,
                    null, //entities,
                    null, //circles,
                    null, //startTime,
                    null, //endTime,
                    false, //includeOnlyLastActivities,
                    false, //withUserRestriction,
                    false)); //invokeEntityHandlers));
              
              if(activities.isEmpty()){
                SSLogU.err(SSErrE.entityDoesNotExist);
                return null;
              }
              
              final SSActivity activity = (SSActivity) activities.get(0);
              
              targetEntities.addAll (activity.entities);
              targetUsers.addAll    (activity.users);
              
              return activity;
            }
          }
          
          break;
        }
      }
      
      return null;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSEntity prepareTargetEntity(final SSEvalLogPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        return null;
      }
      
      final SSEntityServerI  entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      return entityServ.entityGet(
        new SSEntityGetPar(
          par,
          null,
          par.entity,  //entity
          false, //withUserRestriction
          null)); //descPar

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSUser prepareOriginUser(final SSEvalLogPar par) throws SSErr{
    
    try{
      
      final SSUserServerI  userServ    = (SSUserServerI) SSServReg.getServ(SSUserServerI.class);
      final List<SSEntity> originUsers =
        userServ.usersGet(
          new SSUsersGetPar(
            par,
            par.user,
            SSUri.asListNotNull(par.user),
            false)); //invokeEntityHandlers
      
      if(originUsers.isEmpty()){
        SSServErrReg.regErrThrow(SSErrE.entityDoesNotExist);
        return null;
      }
      
      final SSUser        originUser = (SSUser) originUsers.get(0);
      final SSAuthServerI authServ   = (SSAuthServerI) SSServReg.getServ(SSAuthServerI.class);
      
      originUser.oidcSub =
        authServ.authUserOIDCSubGet(
          new SSAuthUserOIDCSubGetPar(
            par,
            originUser.email));
      
      return originUser;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSEntity> prepareTargetEntities(
    final SSEvalLogPar   par) throws SSErr{
    
    try{
      
      final SSEntityServerI  entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      if(par.entities.isEmpty()){
        return new ArrayList<>();
      }
        
      return entityServ.entitiesGet(
        new SSEntitiesGetPar(
          par,
          par.user,
          par.entities,  //entities
          null,  //descPar
          false)); //withUserRestriction
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSEntity> prepareTargetUsers(
    final SSEvalLogPar par) throws SSErr{
    
    try{
      final SSUserServerI  userServ    = (SSUserServerI) SSServReg.getServ(SSUserServerI.class);
      
      if(par.users.isEmpty()){
        return new ArrayList<>();
      }
      
      final List<SSEntity> targetUsers =
        userServ.usersGet(
          new SSUsersGetPar(
            par,
            par.user,
            par.users,
            false)); //invokeEntityHandlers
      
      if(targetUsers.isEmpty()){
        return targetUsers;
      }
      
      final SSAuthServerI authServ = (SSAuthServerI) SSServReg.getServ(SSAuthServerI.class);
      
      for(SSEntity targetUser : targetUsers){
        
        ((SSUser)targetUser).oidcSub =
          authServ.authUserOIDCSubGet(
            new SSAuthUserOIDCSubGetPar(
              par,
              ((SSUser)targetUser).email));
      }
      
      return targetUsers;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private String prepareSelectedBitsMeasure(
    final SSEvalLogPar    par,
    final SSUser          originUser,
    final SSEntity        targetEntity,
    final List<SSEntity>  targetEntities,
    final List<SSCircleE> episodeSpaces) throws SSErr{
    
    try{
      
      String selectedBitsMeasure = SSStrU.empty;
      
      switch(par.type){
        
        case copyLearnEpForUser:{
          
          if(
            episodeSpaces.isEmpty() ||
            targetEntity  == null   ||
            targetEntities.isEmpty()){
            break;
          }
          
          try{
            
            final SSLearnEpServerI learnEpServ    = (SSLearnEpServerI) SSServReg.getServ(SSLearnEpServerI.class);
            final SSLearnEpVersion learnEpVersion =
              learnEpServ.learnEpVersionCurrentGet(
                new SSLearnEpVersionCurrentGetPar(
                  par,
                  originUser.id,
                  false,
                  false));
            
            final Integer itemCount =
              learnEpVersion.learnEpCircles.size() +
              learnEpVersion.learnEpEntities.size();
            
            selectedBitsMeasure = targetEntities.size() + SSStrU.slash + itemCount;
            
          }catch(Exception error){
            SSLogU.warn("learn ep version couldnt be retrieved", error);
          }
          
          break;
        }
      }
      
      return selectedBitsMeasure;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}