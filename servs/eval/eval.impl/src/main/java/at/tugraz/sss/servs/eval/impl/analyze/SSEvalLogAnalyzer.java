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
package at.tugraz.sss.servs.eval.impl.analyze;

import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.SSEvalLogEntry;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalEpisodeShareInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalUserInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalWorkedOnReceivedSharedEpisodeInfo;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.*;
import at.kc.tugraz.ss.service.disc.api.SSDiscServerI;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.kc.tugraz.ss.service.disc.datatypes.pars.*;
import at.kc.tugraz.ss.service.tag.api.*;
import at.kc.tugraz.ss.service.tag.datatypes.pars.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.servs.livingdocument.api.SSLivingDocServerI;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocsGetPar;
import sss.serv.eval.datatypes.par.*;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalEpisodeCreationInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalActionInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalImportInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalLDInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalStartDiscussionInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalWorkedOnBitInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalWorkedOnEpisodeInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalWorkedOnOwnBitInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalWorkedOnOwnEpisodeInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalWorkedOnReceivedDiscussionInfo;
import at.tugraz.sss.servs.eval.impl.analyze.SSEvalWorkedOnReceivedSharedBitInfo;

//&lt; --> < 
//&amp; --> &
//
//
//done
//•	Number of imported bits
//•	Number of accepted (tag) recommendations overall
//•	Number of episodes shared/copied [(and how)]
//•	Number of interactions in each episode
//•	Number of bits used in episodes
//•	Number of taggings in each episode
//•	Number of taggings overall
//•	Number of discussions
//•	Numbers of persons discussing
//•	Number of bits in discussions
//•	Number of tags in discussions
//•	Number of interactions in discussions

//outstanding
//•	Number of persons collaborating in each episode
//•	Number of persons discussing in each episode
//the episodes for bits
//time span of edits in episodes
//Number of relabelled categories

//users
//    ignoredUsers.add("vikki.sykes@gp-b82028.nhs.uk");
//    ignoredUsers.add("lois.brown1@nhs.net");
//    ignoredUsers.add("helen.pow@gp-b82028.nhs.uk");
//    ignoredUsers.add("fiona.parker@gp-B82028.nhs.uk");
//    ignoredUsers.add("danny.meachin@gp-b82028.nhs.uk");
//    ignoredUsers.add("linden.veitch@gp-B82028.nhs.uk");
//    ignoredUsers.add("rosemary.dewey@nhs.net");
//    ignoredUsers.add("richard.price@yas.nhs.uk");
//    ignoredUsers.add("jamesthomas2@nhs.net");

public class SSEvalLogAnalyzer {
  
//  private final Long                   timeBeginStudy this.timeBeginStudy = timeBeginStudy;
  private final Map<String, SSEntity>  episodes     = new HashMap<>();
  
  public void analyzeLDs(
    final SSServPar            servPar,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    try{
      
      final SSLivingDocServerI    ldServ   = (SSLivingDocServerI) SSServReg.getServ(SSLivingDocServerI.class);
      final SSDiscServerI         discServ = (SSDiscServerI)      SSServReg.getServ(SSDiscServerI.class);
      final Map<String, SSEvalLDInfo> ldInfos  = new HashMap<>();
      
      final SSLivingDocsGetPar ldDocsGetPar =
        new SSLivingDocsGetPar(
          servPar,
          SSConf.systemUserUri,
          null, //forUser,
          false, //withUserRestriction,
          true); //invokeEntityHandlers
      
      ldDocsGetPar.setDiscs = true;
      
      final SSDiscGetPar discGetPar =
        new SSDiscGetPar(
          servPar,
          SSConf.systemUserUri,
          null, //disc
          true, //setEntries
          false, //withUserRestriction
          true); //invokeEntityHandlers
      
      discGetPar.setAttachedEntities = true;
      
      SSEntity disc;
      SSEvalLDInfo ldInfo;
      
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
      
      for(SSEvalLDInfo docInfo : ldInfos.values()){
        
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
  
  public void analyzeNumberOfTags(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of tags");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final List<String> tags = new ArrayList<>();
      
      for(SSEvalLogEntry logEntry : logEntries){
        
        if(SSStrU.equalsOne  (logEntry.userLabel, ignoredUsers)){
          continue;
        }
        
        if(logEntry.timestamp < timeBeginStudy){
          continue;
        }
        
        switch(logEntry.logType){
          
          case addTag:
          case tagAdd:{
            SSStrU.addDistinctNotNull(tags, logEntry.content);
            break;
          }
        }
      }
      
      System.out.println(tags.size() + " " + tags);
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfLogEntries(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of log entries");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final List<SSEvalLogEntry> finalLogEntries = new ArrayList<>();
      
      for(SSEvalLogEntry logEntry : logEntries){
        
        if(SSStrU.equalsOne  (logEntry.userLabel, ignoredUsers)){
          continue;
        }
        
        if(logEntry.timestamp < timeBeginStudy){
          continue;
        }
        
        finalLogEntries.add(logEntry);
      }
      
      System.out.println(finalLogEntries.size());
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeDistinctUsers(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("distinct users");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final List<SSLabel> distinctUsers = new ArrayList<>();
      
      for(SSEvalLogEntry logEntry : logEntries){
        
        if(SSStrU.equalsOne  (logEntry.userLabel, ignoredUsers)){
          continue;
        }
        
        SSLabel.addDistinctNotNull(distinctUsers, logEntry.userLabel);
      }
      
      for(SSLabel distinctUser : distinctUsers){
        System.out.println(distinctUser);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfLivingDocs(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of living docs");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final SSEntityServerI  entityServ     = (SSEntityServerI)      SSServReg.getServ(SSEntityServerI.class);
      final SSEntitiesGetPar entitiesGetPar =
        new SSEntitiesGetPar(
          par,
          null, //user
          null, //entities
          null, //descPar
          false);//withUserRestriction
      
//      entitiesGetPar.types.add(SSEntityE.qa);
//      entitiesGetPar.types.add(SSEntityE.qaEntry);
//      entitiesGetPar.types.add(SSEntityE.discEntry);
//      entitiesGetPar.types.add(SSEntityE.evernoteNote);
//      entitiesGetPar.types.add(SSEntityE.evernoteResource);
//      entitiesGetPar.types.add(SSEntityE.evernoteNotebook);
//      entitiesGetPar.types.add(SSEntityE.learnEp);
//      entitiesGetPar.types.add(SSEntityE.learnEpCircle);
//      entitiesGetPar.types.add(SSEntityE.learnEpEntity);
//      entitiesGetPar.types.add(SSEntityE.learnEpVersion);
//      entitiesGetPar.types.add(SSEntityE.learnEpTimelineState);
      entitiesGetPar.types.add(SSEntityE.livingDoc);
//      entitiesGetPar.types.add(SSEntityE.mail);
//      entitiesGetPar.types.add(SSEntityE.message);
//      entitiesGetPar.types.add(SSEntityE.placeholder);
      
      final List<SSEntity> entities =
        entityServ.entitiesGet(entitiesGetPar);
      
      final List<SSEntity> finalEntities = new ArrayList<>();
      
      for(SSEntity entity : entities){
        
        if(SSStrU.equalsOne(entity.author.label, ignoredUsers)){
          continue;
        }
        
        if(entity.creationTime < timeBeginStudy){
          continue;
        }
        
        finalEntities.add(entity);
      }
      
      System.out.println(finalEntities.size() + " " + entitiesGetPar.types);
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberArtifacts(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of artifacts");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final SSEntityServerI  entityServ     = (SSEntityServerI)      SSServReg.getServ(SSEntityServerI.class);
      final SSEntitiesGetPar entitiesGetPar =
        new SSEntitiesGetPar(
          par,
          null, //user
          null, //entities
          null, //descPar
          false);//withUserRestriction
      
      entitiesGetPar.types.add(SSEntityE.qa);
      entitiesGetPar.types.add(SSEntityE.qaEntry);
      entitiesGetPar.types.add(SSEntityE.evernoteNote);
      entitiesGetPar.types.add(SSEntityE.evernoteResource);
      entitiesGetPar.types.add(SSEntityE.evernoteNotebook);
      entitiesGetPar.types.add(SSEntityE.learnEp);
//      entitiesGetPar.types.add(SSEntityE.learnEpCircle);
//      entitiesGetPar.types.add(SSEntityE.learnEpEntity);
      entitiesGetPar.types.add(SSEntityE.livingDoc);
//      entitiesGetPar.types.add(SSEntityE.mail);
//      entitiesGetPar.types.add(SSEntityE.message);
      entitiesGetPar.types.add(SSEntityE.placeholder);
      
      final List<SSEntity> entities =
        entityServ.entitiesGet(entitiesGetPar);
      
      final List<SSEntity> finalEntities = new ArrayList<>();
      
      for(SSEntity entity : entities){
        
        if(SSStrU.equalsOne(entity.author.label, ignoredUsers)){
          continue;
        }
        
        if(entity.creationTime < timeBeginStudy){
          continue;
        }
        
        finalEntities.add(entity);
      }
      
      System.out.println(finalEntities.size() + " " + entitiesGetPar.types);
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfEntriesInDiscussions(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of entries in discussions");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final SSDiscServerI    discServ   = (SSDiscServerI)      SSServReg.getServ(SSDiscServerI.class);
      final SSDiscsGetPar    discGetPar =
        new SSDiscsGetPar(
          par,
          par.user,
          true, //setEntries,
          null, //forUser,
          null, //discs,
          null, //targets,
          false, //withUserRestriction,
          true);  //invokeEntityHandlers
        
      int    totalNumberOfEntries = 0;
      SSDisc disc;
      
      discGetPar.setAttachedEntities = true;
      
      for(SSEntity discEntity : discServ.discsGet(discGetPar)){
        
        if(SSStrU.equalsOne  (discEntity.author.label, ignoredUsers)){
          continue;
        }
        
        if(discEntity.creationTime < timeBeginStudy){
          continue;
        }
        
        disc = (SSDisc) discEntity;
        
        totalNumberOfEntries += disc.entries.size(); 
        
        System.out.println(disc.id + ": " + disc.entries.size());
      }
      
      System.out.println("total number of entries: " + totalNumberOfEntries);
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfTagsInDiscussions(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of tags in discussions");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final SSDiscServerI               discServ               = (SSDiscServerI)      SSServReg.getServ(SSDiscServerI.class);
      final SSTagServerI                tagServ                = (SSTagServerI)       SSServReg.getServ(SSTagServerI.class);
      final List<String>                differentTags          = new ArrayList<>();
      final Map<String, List<SSEntity>> entitiesPerDiscussions = new HashMap<>();
      final SSDiscsGetPar               discGetPar =
        new SSDiscsGetPar(
          par,
          par.user,
          true, //setEntries,
          null, //forUser,
          null, //discs,
          null, //targets,
          false, //withUserRestriction,
          true);  //invokeEntityHandlers
        
      SSDisc disc;
      
      discGetPar.setAttachedEntities = true;
      
      final List<SSEntity> tags = new ArrayList<>();
      final SSTagsGetPar   tagsGetPar =
        new SSTagsGetPar(
          par,
          par.user,
          null, //forUser
          null, //entities
          null, // labels
          SSSearchOpE.or, //labelSearchOp,
          null, //spaces,
          null, //circles,
          null, //startTime,
          false); //withUserRestriction
       
      for(SSEntity discEntity : discServ.discsGet(discGetPar)){
        
        if(SSStrU.equalsOne  (discEntity.author.label, ignoredUsers)){
          continue;
        }
        
        if(discEntity.creationTime < timeBeginStudy){
          continue;
        }
        
        disc = (SSDisc) discEntity;
        
        entitiesPerDiscussions.put(SSStrU.toStr(disc), new ArrayList<>());
        
        for(SSEntity discEntry : disc.entries){
          SSEntity.addEntitiesDistinctWithoutNull(entitiesPerDiscussions.get(SSStrU.toStr(disc)), discEntry.attachedEntities);
        }
      }
      
      for(Map.Entry<String, List<SSEntity>> entitiesForDisc : entitiesPerDiscussions.entrySet()){
        
        tagsGetPar.entities = SSUri.getDistinctNotNullFromEntities(entitiesForDisc.getValue());
       
        if(tagsGetPar.entities.isEmpty()){
          System.out.println(entitiesForDisc.getKey() + ": " + 0);
          continue;
        }
        
        tags.clear();
        tags.addAll(tagServ.tagsGet(tagsGetPar));
          
        System.out.println(entitiesForDisc.getKey() + ": " + tags.size());
        
        for(SSEntity tag : tags){
          
          SSStrU.addDistinctNotNull(differentTags, tag.label);
          
          System.out.println("    " + tag.label);
        }
      }
      
      System.out.println("total number of different tags used in discussions: " + differentTags.size() + " " + differentTags);
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfEntitiesInDiscussions(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of entities in discussions");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      final SSDiscServerI               discServ               = (SSDiscServerI)      SSServReg.getServ(SSDiscServerI.class);
      final List<SSUri>                 differentEntities      = new ArrayList<>();
      final Map<String, List<SSEntity>> entitiesPerDiscussions = new HashMap<>();
      final SSDiscsGetPar               discGetPar =
        new SSDiscsGetPar(
          par,
          par.user,
          true, //setEntries,
          null, //forUser,
          null, //discs,
          null, //targets,
          false, //withUserRestriction,
          true);  //invokeEntityHandlers
        
      SSDisc disc;
      
      discGetPar.setAttachedEntities = true;
      
      for(SSEntity discEntity : discServ.discsGet(discGetPar)){
        
        if(SSStrU.equalsOne  (discEntity.author.label, ignoredUsers)){
          continue;
        }
        
        if(discEntity.creationTime < timeBeginStudy){
          continue;
        }
        
        disc = (SSDisc) discEntity;
        
        entitiesPerDiscussions.put(SSStrU.toStr(disc), new ArrayList<>());
        
        for(SSEntity discEntry : disc.entries){
          SSEntity.addEntitiesDistinctWithoutNull(entitiesPerDiscussions.get(SSStrU.toStr(disc)), discEntry.attachedEntities);
        }
      }
      
      for(Map.Entry<String, List<SSEntity>> entitiesForDisc : entitiesPerDiscussions.entrySet()){
        
        System.out.println(entitiesForDisc.getKey() + ": " +entitiesForDisc.getValue().size());
        
        for(SSEntity attachedEntity : entitiesForDisc.getValue()){
          
          SSUri.addDistinctWithoutNull(differentEntities, attachedEntity.id);
          
          System.out.println("    " + attachedEntity.label);
        }
      }
      
      System.out.println("total number of different entities in discussions: " + differentEntities.size());
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfAuthorsInDiscussions(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of authors in discussions");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      final SSDiscServerI              discServ              = (SSDiscServerI)      SSServReg.getServ(SSDiscServerI.class);
      final List<String>               differentAuthors      = new ArrayList<>();
      final Map<String, List<SSLabel>> authorsPerDiscussions = new HashMap<>();
      SSDisc disc;
      
      for(SSEntity discEntity : 
        discServ.discsGet(
          new SSDiscsGetPar(
            par, 
            par.user, 
            true, //setEntries, 
            null, //forUser, 
            null, //discs, 
            null, //targets, 
            false, //withUserRestriction, 
            false))){ //invokeEntityHandlers));
        
        if(SSStrU.equalsOne  (discEntity.author.label, ignoredUsers)){
          continue;
        }
        
        if(discEntity.creationTime < timeBeginStudy){
          continue;
        }
         
        disc = (SSDisc) discEntity;
        
        authorsPerDiscussions.put(SSStrU.toStr(disc), new ArrayList<>());
        
        authorsPerDiscussions.get(SSStrU.toStr(disc)).add(disc.author.label);
          
        for(SSEntity discEntry : disc.entries){
          
          if(SSStrU.equalsOne  (discEntry.author.label, ignoredUsers)){
            continue;
          }
          
          SSLabel.addDistinctNotNull(authorsPerDiscussions.get(SSStrU.toStr(disc)), discEntry.author.label);
        }
      }
      
      for(Map.Entry<String, List<SSLabel>> authorsForDisc : authorsPerDiscussions.entrySet()){
        
        SSStrU.addDistinctNotNull(differentAuthors, SSStrU.toStr(authorsForDisc.getValue()));
        
        System.out.println(authorsForDisc.getKey() + ": " + authorsForDisc.getValue().size() + " " + authorsForDisc.getValue());
      }
      
      System.out.println("total number of different authors in discussions:" + differentAuthors.size());
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfDiscussions(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of discussions");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final SSDiscServerI discServ    = (SSDiscServerI)      SSServReg.getServ(SSDiscServerI.class);
      final List<SSLabel> discussions = new ArrayList<>();
      
      for(SSEntity disc : 
        discServ.discsGet(
          new SSDiscsGetPar(
            par, 
            par.user, 
            false, //setEntries, 
            null, //forUser, 
            null, //discs, 
            null, //targets, 
            false, //withUserRestriction, 
            false))){ //invokeEntityHandlers));
        
        if(SSStrU.equalsOne  (disc.author.label, ignoredUsers)){
          continue;
        }
        
        if(disc.creationTime < timeBeginStudy){
          continue;
        }
        
        discussions.add(disc.label);
        
        System.out.println(disc.label);
      }
      
      System.out.println("total number of discussions: " + discussions.size());
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfTagActivitiesDistinctUserEntityTag(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of tag activities distinct user entity tag");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final List<String> distinctUserEntityTag = new ArrayList<>();
      
      for(SSEvalLogEntry logEntry : logEntries){
        
        if(SSStrU.equalsOne  (logEntry.userLabel, ignoredUsers)){
          continue;
        }
        
        if(logEntry.timestamp < timeBeginStudy){
          continue;
        }
        
        switch(logEntry.logType){
          
          case addTag:
          case tagAdd:{
            
            SSStrU.addDistinctNotNull(distinctUserEntityTag, logEntry.userLabel + " " + logEntry.entity + " " + logEntry.content);
            break;
          }
        }
      }
      
      System.out.println("total number of tag activities: " + distinctUserEntityTag.size());
      
      for(String line : distinctUserEntityTag){
        System.out.println(line);
      }

      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfTagsForBitsUsedInEpisodes(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of tags for bits used in episodes");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final SSTagServerI             tagServ            = (SSTagServerI) SSServReg.getServ(SSTagServerI.class);
      final List<String>             differentTags      = new ArrayList<>();
      final Map<String, List<SSUri>> entitiesPerLearnEp = new HashMap<>();

      SSLearnEp        learnEp;
      SSLearnEpVersion learnEpVersion;
      SSLearnEpEntity  learnEpEntity;
        
      for(Map.Entry<String, SSEntity> learnEpEntry : episodes.entrySet()){
        
        learnEp = (SSLearnEp) learnEpEntry.getValue();
        
        if(SSStrU.equalsOne(learnEp.author.label, ignoredUsers)){
          continue;
        }
        
//        if(learnEp.creationTime < timeBeginStudy){
//          continue;
//        }

        for(SSEntity learnEpVersionEntity : learnEp.getVersions()){
          
          learnEpVersion = (SSLearnEpVersion) learnEpVersionEntity;
          
          for(SSEntity entity : learnEpVersion.learnEpEntities){
          
            learnEpEntity = (SSLearnEpEntity) entity;
            
            if(!SSStrU.containsKey(entitiesPerLearnEp, learnEp.id)){
              entitiesPerLearnEp.put(SSStrU.toStr(learnEp.id), new ArrayList<>());
            }
            
            entitiesPerLearnEp.get(SSStrU.toStr(learnEp.id)).add(learnEpEntity.entity.id);  
          }
        }
      }
          
      final List<String> tagLabels  = new ArrayList<>();
      final SSTagsGetPar tagsGetPar =
        new SSTagsGetPar(
          par,
          par.user,
          null, //forUser
          null, //entities
          null, // labels
          SSSearchOpE.or, //labelSearchOp,
          null, //spaces,
          null, //circles,
          null, //startTime,
          false); //withUserRestriction
      
      for(Map.Entry<String, List<SSUri>> entitiesForLearnEp : entitiesPerLearnEp.entrySet()){
        
        tagLabels.clear();
        
        tagsGetPar.entities = entitiesForLearnEp.getValue();
        
        for(SSEntity tag : tagServ.tagsGet(tagsGetPar)){
          
          SSStrU.addDistinctNotNull(differentTags, tag.label);
          
          SSStrU.addDistinctNotNull(tagLabels, tag.label);
        }
        
        System.out.println(entitiesForLearnEp.getKey() + ": " + tagLabels.size() + " " + tagLabels);
      }
      
      System.out.println("total number of different tags used: " + differentTags.size());
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfLearnEpCircleLabels(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of learnep circle labels");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final List<String> differentLabels    = new ArrayList<>();

      SSLearnEp        learnEp;
      SSLearnEpVersion learnEpVersion;
      SSLearnEpCircle  learnEpCircle;
        
      for(Map.Entry<String, SSEntity> learnEpEntry : episodes.entrySet()){
        
        learnEp = (SSLearnEp) learnEpEntry.getValue();
        
        if(SSStrU.equalsOne(learnEp.author.label, ignoredUsers)){
          continue;
        }
        
//        if(learnEp.creationTime < timeBeginStudy){
//          continue;
//        }

        for(SSEntity learnEpVersionEntity : learnEp.getVersions()){
          
          learnEpVersion = (SSLearnEpVersion) learnEpVersionEntity;
          
          for(SSEntity circle : learnEpVersion.learnEpCircles){
          
            learnEpCircle = (SSLearnEpCircle) circle;
            
            SSStrU.addDistinctNotNull(differentLabels, SSStrU.toStr(learnEpCircle.label).trim());
          }
        }
      }
            
      System.out.println("total number of different circle labels in episodes: " + differentLabels.size() + " " + differentLabels);
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfBitsUsedInEpisodes(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of bits used in episodes");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final List<SSUri>                differentBits      = new ArrayList<>();
      final Map<String, List<SSLabel>> entitiesPerLearnEp = new HashMap<>();

      SSLearnEp        learnEp;
      SSLearnEpVersion learnEpVersion;
      SSLearnEpEntity  learnEpEntity;
        
      for(Map.Entry<String, SSEntity> learnEpEntry : episodes.entrySet()){
        
        learnEp = (SSLearnEp) learnEpEntry.getValue();
        
        if(SSStrU.equalsOne(learnEp.author.label, ignoredUsers)){
          continue;
        }
        
        for(SSEntity learnEpVersionEntity : learnEp.getVersions()){
          
          learnEpVersion = (SSLearnEpVersion) learnEpVersionEntity;
          
          for(SSEntity entity : learnEpVersion.learnEpEntities){
          
            learnEpEntity = (SSLearnEpEntity) entity;
            
            if(!SSStrU.containsKey(entitiesPerLearnEp, learnEp.id)){
              entitiesPerLearnEp.put(SSStrU.toStr(learnEp.id), new ArrayList<>());
            
            }
            
            SSUri.addDistinctWithoutNull(differentBits, learnEpEntity.entity.id);
              
            entitiesPerLearnEp.get(SSStrU.toStr(learnEp.id)).add(learnEpEntity.entity.label);  
          }
        }
      }
            
      for(Map.Entry<String, List<SSLabel>> entitiesForLearnEp : entitiesPerLearnEp.entrySet()){
        System.out.println(entitiesForLearnEp.getKey() + ": " + entitiesForLearnEp.getValue().size() + " " + entitiesForLearnEp.getValue());
      }
      
      System.out.println("total number of different bits used in episodes: " + differentBits.size());
      
      return;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfInteractionsInEpisodes(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of interactions in episodes");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      final SSEntityServerI      entityServ             = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final Map<String, Integer> interactionsPerEpisode = new HashMap<>();
      
      int     totalNumberOfInteractions = 0;
      Integer value;
        
      for(SSEvalLogEntry logEntry : logEntries){
        
        if(SSStrU.equalsOne(logEntry.userLabel, ignoredUsers)){
          continue;
        }
        
        if(logEntry.timestamp < timeBeginStudy){
          continue;
        }
        
        switch(logEntry.logType){
          
          case addCircleToLearnEpVersion:
          case addEntityToLearnEpCircle:
          case addEntityToLearnEpVersion:
          case removeEntityFromLearnEpCircle:
          case removeLearnEpVersionCircle:
          case removeLearnEpVersionCircleWithEntitites:
          case removeLearnEpVersionEntity:
          case requestEditButton:
          case releaseEditButton:{
            
            if(logEntry.entity != null){
              
              switch(logEntry.entityType){

                case learnEp:{
                  
                  if(SSStrU.containsKey(interactionsPerEpisode, logEntry.entity)){
                    value = interactionsPerEpisode.get(SSStrU.toStr(logEntry.entity));
                    
                    value++;
                  }else{
                    value = 0;
                  }
                  
                  interactionsPerEpisode.put(SSStrU.toStr(logEntry.entity), value);
                  
                  break;
                }
              }
              
              continue;
            }
            
            for(
              SSEntity entity :
              entityServ.entitiesGet(
                new SSEntitiesGetPar(
                  par,
                  par.user,
                  logEntry.entityIDs,  //entities
                  null, //descPar
                  false))){ //withUserRestriction
              
              switch(entity.type){
                
                case learnEp:{
                  
                  if(SSStrU.containsKey(interactionsPerEpisode, entity.id)){
                    value = interactionsPerEpisode.get(SSStrU.toStr(entity.id));
                    
                    value++;
                  }else{
                    value = 0;
                  }
                  
                  interactionsPerEpisode.put(SSStrU.toStr(entity.id), value);
                  
                  break;
                }
              }
            }

            break;
          }
        }
      }
      
      for(Map.Entry<String, Integer> interactionsInEpisode : interactionsPerEpisode.entrySet()){
        
        totalNumberOfInteractions += interactionsInEpisode.getValue();
          
        System.out.println(interactionsInEpisode.getKey() + ": " + interactionsInEpisode.getValue());
      }
      
      System.out.println("total number of interactions in episodes: " + totalNumberOfInteractions);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfSharedEpisodes(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of shared and copied episodes");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final List<SSUri> sharedLearnEps = new ArrayList<>();
      final List<SSUri> copiedLearnEps = new ArrayList<>();
      
      for(SSEvalLogEntry logEntry : logEntries){
        
        if(SSStrU.equalsOne(logEntry.userLabel, ignoredUsers)){
          continue;
        }
        
        if(logEntry.timestamp < timeBeginStudy){
          continue;
        }
        
        switch(logEntry.logType){
          
          case shareEntityWithUsers:
          case shareEntityWithCircles:{
            
            switch(logEntry.entityType){
              
              case learnEp:{
                SSUri.addDistinctWithoutNull(sharedLearnEps, logEntry.entity);
                break;
              }
            }
            
            break;
          }
          
          case shareLearnEpWithUser:{
            SSUri.addDistinctWithoutNull(sharedLearnEps, logEntry.entity);
            break;
          }
          
          case copyLearnEpForUser:{
            SSUri.addDistinctWithoutNull(copiedLearnEps, logEntry.entity);
            break;
          }
        }
      }
      
      System.out.println("shared: " + sharedLearnEps.size());
      System.out.println("copied: " + copiedLearnEps.size());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfAcceptedRecommendations(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of accepted tag recommendations");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final List<Long> timestamps  = new ArrayList<>();
      
      for(SSEvalLogEntry logEntry : logEntries){
        
        if(SSStrU.equalsOne(logEntry.userLabel, ignoredUsers)){
          continue;
        }
        
        if(logEntry.timestamp < timeBeginStudy){
          continue;
        }
        
        switch(logEntry.logType){
          
          case clickTagRecommendation:
            timestamps.add(logEntry.timestamp);
            break;
        }
      }
      
      System.out.println(timestamps.size());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeNumberOfImportedResources(
    final SSEvalAnalyzePar     par,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    System.out.println();
    System.out.println();
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println("number of imported resources");
    System.out.println("##################################");
    System.out.println("##################################");
    System.out.println();
    
    try{
      
      final List<SSUri>   importedBits  = new ArrayList<>();
      
      for(SSEvalLogEntry logEntry : logEntries){
      
        if(SSStrU.equalsOne  (logEntry.userLabel, ignoredUsers)){
          continue;
        }
        
        if(logEntry.timestamp < timeBeginStudy){
          continue;
        }
        
        switch(logEntry.logType){
          
          case addNote:
          case addNotebook:
          case addResource:{

            SSUri.addDistinctWithoutNull(importedBits, logEntry.entity);
            break;
          }
        }
      }
      
      System.out.println(importedBits.size());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void analyzeUsers(
    final SSServPar            servPar,
    final List<String>         ignoredUsers,
    final long                 timeBeginStudy,
    final List<SSEvalLogEntry> logEntries) throws SSErr{
    
    try{
      final SSEntityServerI       entityServ          = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final Map<String, SSEntity> originalEpisodes    = new HashMap<>();
      final List<SSEntity>        episodesForLabel    = new ArrayList<>();
      
      for(SSEvalLogEntry logEntry : logEntries){
        
        if(logEntry.timestamp < timeBeginStudy){
          continue;
        }
        
        episodesForLabel.clear();
        
        switch(logEntry.logType){
          
          case copyLearnEpForUser:{
            
            episodesForLabel.addAll(
              entityServ.entityFromTypeAndLabelGet(
                new SSEntityFromTypeAndLabelGetPar(
                  servPar,
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
      
      final Map<String, SSEvalUserInfo> userInfos = new HashMap<>();
      SSEvalUserInfo                    userInfo;
      SSEntity                      originalEpisode;
      
      for(SSEntity episode : episodes.values()){
        
        if(SSStrU.equalsOne  (episode.author.label, ignoredUsers)){
          continue;
        }
        
        if(!SSStrU.containsKey(userInfos, episode.author.label)){
          
          userInfo = new SSEvalUserInfo();
          
          userInfos.put(episode.author.label.toString(), userInfo);
        }else{
          userInfo = userInfos.get(episode.author.label.toString());
        }
        
        if(episode.creationTime < timeBeginStudy){
          System.out.println(episode.label);
          continue;
        }
        
        originalEpisode = originalEpisodes.get(episode.label.toString());
        
        if(
          originalEpisode != null &&
          !SSStrU.isEqual(originalEpisode.author.label, episode.author.label)){
          continue;
        }
        
        userInfo.createdEpisodeInfos.add(new SSEvalEpisodeCreationInfo(
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
          
          case addDiscEntry:{
            
            addWorkedOnReceivedDiscussion(servPar,userInfos, logEntry);
            break;
          }
          
          case createDisc:{
            
            addStartedDiscussionInfos(userInfos, logEntry);
            break;
          }
          
          case changeLabel:
          case changeDescription:
          case addTag:
          case setImportance:
          case removeTag:{
          
            addWorkedOnOwnBit           (servPar,userInfos, logEntry);
            addWorkedOnReceivedSharedBit(servPar,userInfos, logEntry);
            break;
          }

          case shareEntityWithUsers:
          case shareEntityWithCircles:{
            
            switch(logEntry.entityType){
              
              case learnEp:{
                addSharedEpisodeInfos           (ignoredUsers, userInfos, logEntry);
                break;
              }
            }
            
            break;
          }
          
          case copyLearnEpForUser:
          case shareLearnEpWithUser:{
            
            addSharedEpisodeInfos           (ignoredUsers, userInfos, logEntry);
            break;
          }
          
          case removeLearnEpVersionCircle:
          case removeLearnEpVersionEntity:
          case addEntityToLearnEpVersion:
          case addCircleToLearnEpVersion:
          case addEntityToLearnEpCircle:
          case removeEntityFromLearnEpCircle:
          case removeLearnEpVersionCircleWithEntitites:{

            addWorkedOnOwnEpisode           (servPar, timeBeginStudy, userInfos, logEntry);
            addWorkedOnReceivedSharedEpisode(servPar, timeBeginStudy, userInfos, logEntry);
            break;
          }
        }
      }
      
      for(Map.Entry<String, SSEvalUserInfo> user : userInfos.entrySet()){
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
        
        printUserStartedDiscussions               (user.getValue().startDiscussionInfos);
        printUserWorkedOnReceivedDiscussions      (user.getValue().workedOnReceivedDiscussionInfos);
        
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addImportInfos(
    final Map<String, SSEvalUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws SSErr{
    
    try{
      
      final SSEvalUserInfo   userInfo = userInfos.get(logEntry.userLabel.toString());
      final SSEvalImportInfo info     = 
        new SSEvalImportInfo(
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
        
        final List<SSEvalImportInfo> importInfos = new ArrayList<>();
        
        importInfos.add(info);
        
        userInfo.importInfos.put(info.bitID.toString(), importInfos);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
      
  private void addWorkedOnReceivedSharedBit(
    final SSServPar servPar,
    final Map<String, SSEvalUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws SSErr{
    
    try{
      
      final SSEntityServerI                     entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEvalUserInfo                          userInfo     = getOrCreateUserInfo(userInfos, logEntry);
      final SSEvalWorkedOnReceivedSharedBitInfo     workedOnBit;
      
      final SSEntity entity =
        entityServ.entityGet(
          new SSEntityGetPar(
            servPar,
            SSConf.systemUserUri,
            logEntry.entity,
            false, //withUserRestriction
            null)); //descPar
      
      if(SSStrU.isEqual(entity.author.label, logEntry.userLabel)){
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
        
        workedOnBit = new SSEvalWorkedOnReceivedSharedBitInfo();
        
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
      
      addBitActionDetails(servPar, workedOnBit, logEntry);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private void addStartedDiscussionInfos(
    final Map<String, SSEvalUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws SSErr{
    
    try{
      
      final SSEvalUserInfo            userInfo  = getOrCreateUserInfo(userInfos, logEntry);
      final SSEvalStartDiscussionInfo info      = 
        new SSEvalStartDiscussionInfo(
          logEntry.userLabel, 
          SSLabel.get(SSStrU.toCommaSeparatedStrNotNull(logEntry.entityLabels)), 
          logEntry.entityLabel); //targetLabel
      
      userInfo.startDiscussionInfos.add(info);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private SSEvalUserInfo getOrCreateUserInfo(
    final Map<String, SSEvalUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws SSErr{
    
    try{
      
      final SSEvalUserInfo userInfo;
      
      if(!SSStrU.containsKey(userInfos, logEntry.userLabel)){
        
        userInfo = new SSEvalUserInfo();
        
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

  private SSEvalLDInfo getOrCreateLDInfo(
    final Map<String, SSEvalLDInfo> ldInfos, 
    final SSEntity              doc) throws SSErr{
    
    try{
      
      final SSEvalLDInfo ldInfo;
      
      if(!SSStrU.containsKey(ldInfos, doc.id)){
        
        ldInfo = new SSEvalLDInfo();
        
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
    final SSServPar servPar,
    final Map<String, SSEvalUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws SSErr{
    
    try{
      
      final SSEntityServerI                     entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEvalUserInfo                          userInfo     = getOrCreateUserInfo(userInfos, logEntry);
      final SSEvalWorkedOnOwnBitInfo                workedOnBit;
      
      final SSEntity entity =
        entityServ.entityGet(
          new SSEntityGetPar(
            servPar,
            SSConf.systemUserUri,
            logEntry.entity,
            false, //withUserRestriction
            null)); //descPar
      
      if(!SSStrU.isEqual(entity.author.label, logEntry.userLabel)){
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
        
        workedOnBit = new SSEvalWorkedOnOwnBitInfo();
        
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
      
      
      addBitActionDetails(servPar,workedOnBit, logEntry);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addWorkedOnReceivedDiscussion(
    final SSServPar servPar,
    final Map<String, SSEvalUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws SSErr{
    
    try{
      
      final SSDiscServerI                    discServ     = (SSDiscServerI) SSServReg.getServ(SSDiscServerI.class);
      final SSEvalUserInfo                       userInfo     = getOrCreateUserInfo(userInfos, logEntry);
      final SSEvalWorkedOnReceivedDiscussionInfo info;
      
      if(SSStrU.containsKey(userInfo.workedOnReceivedDiscussionInfos, logEntry.entity)){
        info = userInfo.workedOnReceivedDiscussionInfos.get(logEntry.entity.toString());
      }else{
        
        info = new SSEvalWorkedOnReceivedDiscussionInfo();
        
        info.discussionLabel = logEntry.entityLabel;
        
        final SSEntity disc = 
          discServ.discGet(
            new SSDiscGetPar(
              servPar,
              SSConf.systemUserUri, 
              logEntry.entity, //disc
              true, //setEntries
              false, //withUserRestriction, 
              false)); //invokeEntityHandlers
        
        if(SSStrU.isEqual(disc.author.label, logEntry.userLabel)){
          return; 
        }
        
        SSLabel.addDistinctNotNull(info.contributors, disc.author.label);
        
        for(SSEntity entry : disc.entries){
          
          if(!SSStrU.isEqual(entry.author.label, logEntry.userLabel)){
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
    final SSServPar               servPar,
    final long                    timeBeginStudy,
    final Map<String, SSEvalUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws SSErr{
    
    try{
      
      final SSEvalUserInfo                          userInfo            = getOrCreateUserInfo(userInfos, logEntry);
      final SSEvalWorkedOnReceivedSharedEpisodeInfo workedOnEpisode;
      SSUri                                     episodeID           = null;
      SSLabel                                   episodeLabel        = null;
      SSLabel                                   episodeAuthor       = null;
      Long                                      episodeCreationTime = null;
      
      if(SSStrU.isEqual(logEntry.entityType, SSEntityE.learnEp)){
        
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
        SSLogU.warn("episode not found", null);
        return;
      }
      
      if(SSStrU.isEqual(episodeAuthor, logEntry.userLabel)){
        return;
      }
      
      if(episodeCreationTime < timeBeginStudy){
        return;
      }
      
      if(!SSStrU.containsKey(userInfo.workedOnReceivedEpisodeInfos, episodeID)){
        
        workedOnEpisode = new SSEvalWorkedOnReceivedSharedEpisodeInfo();
          
        userInfo.workedOnReceivedEpisodeInfos.put(SSStrU.toStr(episodeID), workedOnEpisode);
      }else{
        workedOnEpisode = userInfo.workedOnReceivedEpisodeInfos.get(SSStrU.toStr(episodeID));
      }
      
      workedOnEpisode.episodeID = episodeID;
      workedOnEpisode.totalActionsDone++;
      
      SSEvalLogE.addDistinctNotNull(workedOnEpisode.actions, logEntry.logType);
      
      addEpisodeActionDetails(servPar, workedOnEpisode, logEntry);
          
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addWorkedOnOwnEpisode(
    final SSServPar               servPar,
    final long                    timeBeginStudy,
    final Map<String, SSEvalUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws SSErr{
    
    try{
      
      final SSEvalUserInfo                          userInfo            = getOrCreateUserInfo(userInfos, logEntry);
      final SSEvalWorkedOnOwnEpisodeInfo            workedOnEpisode;
      SSUri                                     episodeID           = null;
      SSLabel                                   episodeLabel        = null;
      SSLabel                                   episodeAuthor       = null;
      Long                                      episodeCreationTime = null;
      
      if(SSStrU.isEqual(logEntry.entityType, SSEntityE.learnEp)){
        
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
        SSLogU.warn("episode not found", null);
        return;
      }
      
      if(!SSStrU.isEqual(episodeAuthor, logEntry.userLabel)){
        return;
      }
      
      if(episodeCreationTime < timeBeginStudy){
        return;
      }
      
      if(!SSStrU.containsKey(userInfo.workedOnOwnEpisodeInfos, episodeID)){
        
        workedOnEpisode = new SSEvalWorkedOnOwnEpisodeInfo();
          
        userInfo.workedOnOwnEpisodeInfos.put(SSStrU.toStr(episodeID), workedOnEpisode);
      }else{
        workedOnEpisode = userInfo.workedOnOwnEpisodeInfos.get(SSStrU.toStr(episodeID));
      }
      
      workedOnEpisode.episodeID = episodeID;
      workedOnEpisode.totalActionsDone++;
      
      SSEvalLogE.addDistinctNotNull(workedOnEpisode.actions, logEntry.logType);
      
      addEpisodeActionDetails(servPar, workedOnEpisode, logEntry);
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  private void addBitActionDetails(
    final SSServPar                servPar,
    final SSEvalWorkedOnBitInfo        workedOnBitInfo,
    final SSEvalLogEntry           logEntry) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntityGetPar  entityGetPar =
        new SSEntityGetPar(
          servPar,
          SSConf.systemUserUri,
          null, //entity,
          false, //withUserRestriction,
          null);
      
      final SSEntitiesGetPar entitiesGetPar = 
        new SSEntitiesGetPar(
          servPar,
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
    final SSServPar                servPar,
    final SSEvalWorkedOnEpisodeInfo    workedOnEpisodeInfo,
    final SSEvalLogEntry           logEntry) throws SSErr{
    
    try{
      final SSEntityServerI entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntityGetPar  entityGetPar =
        new SSEntityGetPar(
          servPar,
          SSConf.systemUserUri,
          null, //entity,
          false, //withUserRestriction,
          null);
      
      final SSEntitiesGetPar entitiesGetPar = 
        new SSEntitiesGetPar(
          servPar,
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
    final List<String>            ignoredUsers,
    final Map<String, SSEvalUserInfo> userInfos,
    final SSEvalLogEntry          logEntry) throws SSErr{
    
    try{
      final SSEvalUserInfo         userInfo  = getOrCreateUserInfo(userInfos, logEntry);
      final SSEvalEpisodeShareInfo shareInfo = new SSEvalEpisodeShareInfo();
      final SSEntity           episode   = episodes.get(logEntry.entity.toString());
      
      if(episode == null){
        SSLogU.warn("episode not found", null);
        return;
      }
      
      shareInfo.id                  = episode.id;
      shareInfo.label               = episode.label;
      shareInfo.creationTime        = episode.creationTime;
      shareInfo.selectedBitsMeasure = logEntry.selectedBitsMeasure;
      shareInfo.shareType           = logEntry.logType;
      shareInfo.timestamp           = logEntry.timestamp;
      
      removeIgnoredUsers(ignoredUsers, logEntry.userLabels);
      
      SSLabel.addDistinctNotNull(shareInfo.targetUsers, logEntry.userLabels);
      
      userInfo.sharedEpisodeInfos.add(shareInfo);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void printUserWorkedOnReceivedSharedBit(
    final Map<String, SSEvalWorkedOnReceivedSharedBitInfo> workedOnReceivedBitInfos) throws SSErr{
    
     try{
      
      if(workedOnReceivedBitInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on shared bits");
      System.out.println("#####################");
      System.out.println();
      
      for(Map.Entry<String, SSEvalWorkedOnReceivedSharedBitInfo> workedOnBit : workedOnReceivedBitInfos.entrySet()){
        
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
    final Map<String, SSEvalWorkedOnOwnBitInfo> workedOnOwnBitInfos) throws SSErr{
    
     try{
      
      if(workedOnOwnBitInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on own bits");
      System.out.println("##################");
      System.out.println();
      
      for(Map.Entry<String, SSEvalWorkedOnOwnBitInfo> workedOnBit : workedOnOwnBitInfos.entrySet()){
        
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
    final Map<String, SSEvalWorkedOnReceivedSharedEpisodeInfo> workedOnReceivedSharedEpisodeInfos) throws SSErr{
    
    try{
      
      if(workedOnReceivedSharedEpisodeInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on shared episodes");
      System.out.println("#########################");
      System.out.println();
      
      SSEntity episode;
      
      for(Map.Entry<String, SSEvalWorkedOnReceivedSharedEpisodeInfo> workedOnEpisode : workedOnReceivedSharedEpisodeInfos.entrySet()){
        
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
    final Map<String, SSEvalWorkedOnReceivedDiscussionInfo> workedOnReceivedDiscussionInfos) throws SSErr{
    
    try{
      
      if(workedOnReceivedDiscussionInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on received discussions");
      System.out.println("##############################");
      System.out.println();
      
      for(SSEvalWorkedOnReceivedDiscussionInfo workedOnDisc : workedOnReceivedDiscussionInfos.values()){
      
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
    final Map<String, SSEvalWorkedOnOwnEpisodeInfo> workedOnOwnEpisodeInfos) throws SSErr{
    
    try{
      
      if(workedOnOwnEpisodeInfos.isEmpty()){
        return;
      }
      
      System.out.println("worked on own episodes");
      System.out.println("######################");
      System.out.println();
      
      SSEntity episode;
      
      for(Map.Entry<String, SSEvalWorkedOnOwnEpisodeInfo> workedOnEpisode : workedOnOwnEpisodeInfos.entrySet()){
        
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
    final List<SSEvalEpisodeShareInfo> sharedEpisodeInfos) throws SSErr{
    
    try{
      
      if(sharedEpisodeInfos.isEmpty()){
        return;
      }
      
      System.out.println("user shared episodes");
      System.out.println("####################");
      System.out.println();
      
      for(SSEvalEpisodeShareInfo share : sharedEpisodeInfos){
        System.out.println(share.label + " (" + new Date(share.creationTime) + " " + share.id + ")");
        System.out.println("    " + new Date(share.timestamp) + " | " + share.shareType + " (" + share.selectedBitsMeasure + ") | " + share.targetUsers);
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void removeIgnoredUsers(
    final List<String>           ignoredUsers,
    final List<? extends Object> users){
    
    for(Object user : users){
  
      if(SSStrU.isEqual(user, ignoredUsers)){
        users.remove(user);
        removeIgnoredUsers(ignoredUsers, users);
      }
    }
  }

  private void printUserEpisodeCreated(List<SSEvalEpisodeCreationInfo> createdEpisodeInfos) throws SSErr {
    
    try{
      
      if(createdEpisodeInfos.isEmpty()){
        return;
      }
      
      System.out.println("user created episodes");
      System.out.println("#####################");
      System.out.println();
      
      for(SSEvalEpisodeCreationInfo created : createdEpisodeInfos){
        System.out.println(created.episodeLabel);
        System.out.println("    " + new Date(created.timestamp) + " " + created.episodeID);
        System.out.println();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private void printUserImports(final Map<String, List<SSEvalImportInfo>> importInfos) throws SSErr {
   
    try{
      
      if(importInfos.isEmpty()){
        return;
      }
      
      System.out.println("user imported");
      System.out.println("#############");
      System.out.println();
      
      int counter = 0;
      
      for(Map.Entry<String, List<SSEvalImportInfo>> info : importInfos.entrySet()){
        
        System.out.println(info.getValue().get(0).bitLabel + " | " + info.getValue().get(0).bitType);
        
        counter = 0;
          
        for(SSEvalImportInfo detail : info.getValue()){
          
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
              break;
            }
          }
        }
        
        System.out.println();
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private void printUserStartedDiscussions(
    final List<SSEvalStartDiscussionInfo> startDiscussionInfos) throws SSErr {
    
    try{
      
      if(startDiscussionInfos.isEmpty()){
        return;
      }
      
      System.out.println("user started discussions");
      System.out.println("########################");
      System.out.println();
      
      for(SSEvalStartDiscussionInfo info : startDiscussionInfos){
        
        System.out.println(info.discussionLabel);
        System.out.println("    " + info.targetLabel);
        System.out.println();
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setEpisodes(
    final SSServPar servPar) throws SSErr{
    
    try{
      
      final SSLearnEpServerI learnEpServ   = (SSLearnEpServerI) SSServReg.getServ(SSLearnEpServerI.class);
      
      for(SSEntity learnEp :
        learnEpServ.learnEpsGet(
          new SSLearnEpsGetPar(
            servPar,
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