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
package at.tugraz.sss.servs.integrationtest.knowbraintaggingstudy2015;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivitiesGetPar;
import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedGetPar;
import at.kc.tugraz.ss.message.api.SSMessageServerI;
import at.kc.tugraz.ss.message.datatypes.par.SSMessagesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCirclesWithEntriesGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import java.util.List;

public class SSIntegrationTestBitsAndPiecesStudyFall2015 {
  
  private final SSUserServerI      userServ;
  private final SSCategoryServerI  categoryServ;
  private final SSUEServerI        userEventServ;
  private final SSLearnEpServerI   learnEpServ;
  private final SSMessageServerI   messageServ;
  private final SSActivityServerI  activityServ;
  private final SSEntityServerI  entityServ;
  
  public SSIntegrationTestBitsAndPiecesStudyFall2015() throws Exception{
    userServ        = (SSUserServerI)      SSServReg.getServ (SSUserServerI.class);
    categoryServ    = (SSCategoryServerI)  SSServReg.getServ (SSCategoryServerI.class);
    userEventServ   = (SSUEServerI)        SSServReg.getServ (SSUEServerI.class);
    learnEpServ     = (SSLearnEpServerI)   SSServReg.getServ (SSLearnEpServerI.class);
    messageServ     = (SSMessageServerI)   SSServReg.getServ (SSMessageServerI.class);
    activityServ    = (SSActivityServerI)  SSServReg.getServ (SSActivityServerI.class);
    entityServ      = (SSEntityServerI)    SSServReg.getServ (SSEntityServerI.class);
  }
  
  public void doLearnEpManagement(
    final SSUri testUser7) throws Exception {
    
    try{
      
      final List<SSEntity> learnEps =
        learnEpServ.learnEpsGet(
          new SSLearnEpsGetPar(
            testUser7,
            true,
            false));
      
      List<SSEntity> versions;
      List<SSEntity> circlesWithEntries;
      
      for(SSEntity learnEp : learnEps){
        
        versions =
          learnEpServ.learnEpVersionsGet(
            new SSLearnEpVersionsGetPar(
              testUser7,
              learnEp.id,
              true,
              false));
        
        for(SSEntity version : versions){
          
          circlesWithEntries =
            learnEpServ.learnEpVersionCirclesWithEntriesGet(
              new SSLearnEpVersionCirclesWithEntriesGetPar(
                testUser7,
                version.id,
                true,
                false));
          
          System.out.println(circlesWithEntries);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
    
  public List<SSEntity> getUserEvents(
    final SSUri user) throws Exception {
    
    try{
      
      final List<SSEntity> userEvents =
        userEventServ.userEventsGet(
          new SSUEsGetPar(
            user,
            null, //forUser,
            null, //entity,
            null, //types,
            null, //startTime,
            null, //endTime,
            true, //withUserRestriction,
            true)); //invokeEntityHandlers)
      
      return userEvents;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<String> getPredefinedCategories(
    final SSUri user) throws Exception {
    
    try{
      
      final List<String> predefinedCategories =
        categoryServ.categoriesPredefinedGet(
          new SSCategoriesPredefinedGetPar(
            user));
    
      return predefinedCategories;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntity> getUsers(
    final SSUri user) throws Exception {
   
    try{
      
      final List<SSEntity> users =
        userServ.usersGet(
          new SSUsersGetPar(
            user,
            null,  //users
            true)); //invokeEntityHandlers
    
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntity> getLearningEpisodes(
    final SSUri user) throws Exception {
    
    try{
      
      final List<SSEntity> learnEps =
        learnEpServ.learnEpsGet(
          new SSLearnEpsGetPar(
            user,
            true,  //withUserRestriction
            true)); //invokeEntityHandlers
    
      return learnEps;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntity> getMessages(
    final SSUri user) throws Exception {
    
    try{
      
      final List<SSEntity> messages =
        messageServ.messagesGet(
          new SSMessagesGetPar(
            user,
            true, //includeRead
            null, //startTime
            true,  //withUserRestriction
            true)); //invokeEntityHandlers
      
      return messages;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntity> getActivities(
    final SSUri user) throws Exception {
    
    try{
      
      final List<SSEntity> activities =
        activityServ.activitiesGet(
          new SSActivitiesGetPar(
            user,
            null, //types
            null, //users,
            null, //entities
            null, //circles
            null, //startTime
            null, //endTime
            false, //includeOnlyLastActivities
            true, //withUserRestriction
            true)); //invokeEntityHandlers
      
      return activities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSUri createLearnEpWithEntitiesAndCircles(
    final SSUri user) throws Exception {
    
    try{
    
      final SSUri learnEp = 
        learnEpServ.learnEpCreate(
        new SSLearnEpCreatePar(
          user,
          SSLabel.get("learn ep 1"),
          SSTextComment.get("description"), 
          true,
          false));
      
      final SSUri learnEpVersion = 
        learnEpServ.learnEpVersionCreate(
          new SSLearnEpVersionCreatePar(
            user, 
            learnEp, 
            true,
            false));
      
      final SSUri circle =
        learnEpServ.learnEpVersionCircleAdd(
          new SSLearnEpVersionCircleAddPar(
            user,
            learnEpVersion,
            SSLabel.get("version 1"),
            0F,
            0F,
            1F,
            2F,
            3F,
            4F,
            true,
            false));
      
      final SSUri entity = 
        learnEpServ.learnEpVersionEntityAdd(
          new SSLearnEpVersionEntityAddPar(
            user, 
            learnEpVersion, 
            SSUri.get("http://linkToEnttiy"), 
            10F, 
            11F, 
            true,
            false));
      
      return learnEp;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSLearnEp getLearnEp(
    final SSUri user, 
    final SSUri learnEpURI) throws Exception{
    
    try{
      
      final SSLearnEp learnEp = 
        learnEpServ.learnEpGet(
        new SSLearnEpGetPar(
          user,
          learnEpURI, 
          true,  //withUserRestriction
          true)); //invokeEntityHandlers
      
      return learnEp;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEntity getTestUser7() throws Exception {
    
    try{
      
      final SSEntity testUser7 =
        entityServ.entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            SSVocConf.systemUserUri,
            SSLabel.get("bn-testuser7@know-center.at"),
            SSEntityE.user,
            true));
      
      return testUser7;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  public SSEntity getDieterUser() throws Exception {
    
    try{
      
      final SSEntity dieter =
        entityServ.entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            SSVocConf.systemUserUri,
            SSLabel.get("dieter"),
            SSEntityE.user,
            true));
      
      return dieter;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void updateLearnEpEntity(
    final SSUri     user,
    final SSLearnEp learnEp) throws Exception {
    
    try{

      final SSUri learnEpEntity = 
        ((SSLearnEpEntity)((SSLearnEpVersion) learnEp.entries.get(0)).learnEpEntities.get(0)).id;
      
      learnEpServ.learnEpVersionEntityUpdate(
        new SSLearnEpVersionEntityUpdatePar(
          user, 
          learnEpEntity, 
          SSUri.get("http://entity2"), 
          100F, 
          100F, 
          true,
          false));

      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          user, 
          learnEpEntity, 
          null, //type, 
          SSLabel.get("new label"), 
          SSTextComment.get("new description"), 
          null, //creationTime, 
          null, //read, 
          false, //setPublic, 
          true, //withUserRestriction, 
          false)); //shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateLearnEpCircle(
    final SSUri     user,
    final SSLearnEp learnEp) throws Exception {
    
    try{
      
      final SSUri learnEpCircle =
        ((SSLearnEpCircle)((SSLearnEpVersion) learnEp.entries.get(0)).learnEpCircles.get(0)).id;
      
      learnEpServ.learnEpVersionCircleUpdate(
        new SSLearnEpVersionCircleUpdatePar(
          user,
          learnEpCircle,
          SSLabel.get("new circle label"),
          3F,
          3F,
          3F,
          3F,
          3F,
          3F,
          true,
          false));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}