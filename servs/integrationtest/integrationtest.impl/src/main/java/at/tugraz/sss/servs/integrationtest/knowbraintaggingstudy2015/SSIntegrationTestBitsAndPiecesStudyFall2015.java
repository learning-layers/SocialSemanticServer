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

import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import java.util.ArrayList;
import java.util.List;

public class SSIntegrationTestBitsAndPiecesStudyFall2015 {
  
  private final SSCircleServerI    circleServ;
  private final SSUserServerI      userServ;
  private final SSEntityServerI    entityServ;
  private final SSTagServerI       tagServ;
  private final SSCategoryServerI  categoryServ;
  private final SSRecommServerI    recommServ;
  private final SSUEServerI        userEventServ;
  
  public SSIntegrationTestBitsAndPiecesStudyFall2015() throws Exception{
    circleServ      = (SSCircleServerI)   SSServReg.getServ (SSCircleServerI.class);
    userServ        = (SSUserServerI)     SSServReg.getServ (SSUserServerI.class);
    entityServ      = (SSEntityServerI)   SSServReg.getServ (SSEntityServerI.class);
    tagServ         = (SSTagServerI)      SSServReg.getServ (SSTagServerI.class);
    categoryServ    = (SSCategoryServerI) SSServReg.getServ (SSCategoryServerI.class);
    recommServ      = (SSRecommServerI)   SSServReg.getServ (SSRecommServerI.class);
    userEventServ   = (SSUEServerI)       SSServReg.getServ (SSUEServerI.class);
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
  
  public List<SSEntity> getPredefinedCategories() {
    
    return new ArrayList<>();
  }
  
  public List<SSEntity> getUsers() {
    return new ArrayList<>();
  }
  
  public List<SSEntity> getLearningEpisodes() {
    return new ArrayList<>();
  }
}
