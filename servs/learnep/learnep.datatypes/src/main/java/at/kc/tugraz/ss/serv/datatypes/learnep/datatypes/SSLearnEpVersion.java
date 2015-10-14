/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSJSONLDU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSTextComment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLearnEpVersion extends SSEntity{

  public SSUri                  learnEp              = null;
  public List<SSEntity>         learnEpEntities      = new ArrayList<>();
  public List<SSEntity>         learnEpCircles       = new ArrayList<>();
  public SSLearnEpTimelineState learnEpTimelineState = null;

  public String getLearnEp() throws Exception {
    return SSStrU.removeTrailingSlash(learnEp);
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld                 = (Map<String, Object>) super.jsonLDDesc();
    final Map<String, Object> learnEpCirclesObj  = new HashMap<>();
    final Map<String, Object> learnEpEntitiesObj = new HashMap<>();
    
    ld.put(SSVarNames.learnEp,                SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.learnEpTimelineState,   SSVarNames.sss + SSStrU.colon + SSLearnEpTimelineState.class.getName());
    
    learnEpEntitiesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSLearnEpEntity.class.getName());
    learnEpEntitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.learnEpEntities, learnEpEntitiesObj);
    
    learnEpCirclesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSLearnEpCircle.class.getName());
    learnEpCirclesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.learnEpCircles, learnEpCirclesObj);
    
    return ld;
  }
  
  public static SSLearnEpVersion get(
    final SSLearnEpVersion learnEpVersion,
    final SSEntity         entity) throws Exception{
    
    return new SSLearnEpVersion(learnEpVersion, entity);
  }
  
  public static SSLearnEpVersion get(
    final SSUri                  id,
    final SSLabel                label, 
    final SSTextComment          description, 
    final Long                   creationTime, 
    final SSEntity               author,
    final SSUri                  learnEp,
    final List<SSEntity>         learnEpEntities,
    final List<SSEntity>         learnEpCircles,
    final SSLearnEpTimelineState learnEpTimelineState) throws Exception{
    
    return new SSLearnEpVersion(
      id,
      label,
      description,
      creationTime,
      author,
      learnEp,
      learnEpEntities, 
      learnEpCircles, 
      learnEpTimelineState);
  }
  
  protected SSLearnEpVersion(
    final SSLearnEpVersion learnEpVersion,
    final SSEntity         entity) throws Exception{
    
    super(learnEpVersion, entity);
    
    if(learnEpVersion.learnEp != null){
      this.learnEp               = learnEpVersion.learnEp;
    }else{
      
      if(entity instanceof SSLearnEpVersion){
        this.learnEp               = ((SSLearnEpVersion)entity).learnEp;
      }
    }
    
    if(learnEpVersion.learnEpTimelineState != null){
      this.learnEpTimelineState               = learnEpVersion.learnEpTimelineState;
    }else{
      
      if(entity instanceof SSLearnEpVersion){
        this.learnEpTimelineState               = ((SSLearnEpVersion)entity).learnEpTimelineState;
      }
    }

    SSEntity.addEntitiesDistinctWithoutNull(this.learnEpEntities, learnEpVersion.learnEpEntities);
    SSEntity.addEntitiesDistinctWithoutNull(this.learnEpCircles,  learnEpVersion.learnEpCircles);
    
    if(entity instanceof SSLearnEpVersion){
      SSEntity.addEntitiesDistinctWithoutNull(this.learnEpEntities, ((SSLearnEpVersion)entity).learnEpEntities);
      SSEntity.addEntitiesDistinctWithoutNull(this.learnEpCircles,  ((SSLearnEpVersion)entity).learnEpCircles);
    }
  }
  
  protected SSLearnEpVersion(
    final SSUri                  id,
    final SSLabel                label, 
    final SSTextComment          description, 
    final Long                   creationTime, 
    final SSEntity               author,
    final SSUri                  learnEp,
    final List<SSEntity>         learnEpEntities,
    final List<SSEntity>         learnEpCircles,
    final SSLearnEpTimelineState learnEpTimelineState) throws Exception{
    
    super(
      id, 
      SSEntityE.learnEpVersion,
      label,
      description, 
      creationTime, 
      author);
    
    this.learnEp               = learnEp;
    this.learnEpTimelineState  = learnEpTimelineState;
    
    SSEntity.addEntitiesDistinctWithoutNull(this.learnEpEntities, learnEpEntities);
    SSEntity.addEntitiesDistinctWithoutNull(this.learnEpCircles,  learnEpCircles);
  }
}
