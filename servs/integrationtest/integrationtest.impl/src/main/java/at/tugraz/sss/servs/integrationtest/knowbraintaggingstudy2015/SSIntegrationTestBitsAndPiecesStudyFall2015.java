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
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import java.util.List;

public class SSIntegrationTestBitsAndPiecesStudyFall2015 {
  
  private final SSUserServerI      userServ;
  private final SSCategoryServerI  categoryServ;
  private final SSUEServerI        userEventServ;
  private final SSLearnEpServerI   learnEpServ;
  private final SSMessageServerI   messageServ;
  private final SSActivityServerI  activityServ;
  
  public SSIntegrationTestBitsAndPiecesStudyFall2015() throws Exception{
    userServ        = (SSUserServerI)      SSServReg.getServ (SSUserServerI.class);
    categoryServ    = (SSCategoryServerI)  SSServReg.getServ (SSCategoryServerI.class);
    userEventServ   = (SSUEServerI)        SSServReg.getServ (SSUEServerI.class);
    learnEpServ     = (SSLearnEpServerI)   SSServReg.getServ (SSLearnEpServerI.class);
    messageServ     = (SSMessageServerI)   SSServReg.getServ (SSMessageServerI.class);
    activityServ    = (SSActivityServerI)  SSServReg.getServ (SSActivityServerI.class);
  }
  
  public List<SSEntity> getUserEvents() throws Exception {
    
    try{
      
      final List<SSEntity> userEvents =
        userEventServ.userEventsGet(
          new SSUEsGetPar(
            null,
            null,
            SSVocConf.systemUserUri,
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
  
  public List<String> getPredefinedCategories() throws Exception {
    
    try{
      
      final List<String> predefinedCategories =
        categoryServ.categoriesPredefinedGet(
          new SSCategoriesPredefinedGetPar(
            null,
            null,
            SSVocConf.systemUserUri));
    
      return predefinedCategories;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntity> getUsers() throws Exception {
   
    try{
      
      final List<SSEntity> users =
        userServ.usersGet(
          new SSUsersGetPar(
            null,
            null,
            SSVocConf.systemUserUri,
            null,  //users
            true)); //invokeEntityHandlers
    
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntity> getLearningEpisodes() throws Exception {
    
    try{
      
      final List<SSEntity> learnEps =
        learnEpServ.learnEpsGet(
          new SSLearnEpsGetPar(
            null,
            null,
            SSVocConf.systemUserUri,
            true,  //withUserRestriction
            true)); //invokeEntityHandlers
    
      return learnEps;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntity> getMessages() throws Exception {
    
    try{
      
      final List<SSEntity> messages =
        messageServ.messagesGet(
          new SSMessagesGetPar(
            null,
            null,
            SSVocConf.systemUserUri,
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
  
  public List<SSEntity> getActivities() throws Exception {
    
    try{
      
      final List<SSEntity> activities =
        activityServ.activitiesGet(
          new SSActivitiesGetPar(
            null,
            null,
            SSVocConf.systemUserUri,
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
}