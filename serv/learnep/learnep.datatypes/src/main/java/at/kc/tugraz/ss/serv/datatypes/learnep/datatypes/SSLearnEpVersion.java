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
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSJSONLDU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLearnEpVersion extends SSEntity{

  public SSUri                  learnEp              = null;
  public List<SSLearnEpEntity>  learnEpEntities      = new ArrayList<>();
  public List<SSLearnEpCircle>  learnEpCircles       = new ArrayList<>();
  public SSLearnEpTimelineState learnEpTimelineState = null;

  public static SSLearnEpVersion get(
    final SSLearnEpVersion learnEpVersion,
    final SSEntity         entity) throws Exception{
    
    return new SSLearnEpVersion(learnEpVersion, entity);
  }
  
  public static SSLearnEpVersion get(
    final SSUri                  id, 
    final SSUri                  learnEp,
    final List<SSLearnEpEntity>  learnEpEntities,
    final List<SSLearnEpCircle>  learnEpCircles,
    final SSLearnEpTimelineState learnEpTimelineState) throws Exception{
    
    return new SSLearnEpVersion(id, learnEp, learnEpEntities, learnEpCircles, learnEpTimelineState);
  }
  
  protected SSLearnEpVersion(
    final SSLearnEpVersion learnEpVersion,
    final SSEntity         entity) throws Exception{
    
    super(entity);
    
    this.learnEp               = learnEpVersion.learnEp;
    this.learnEpTimelineState  = learnEpVersion.learnEpTimelineState;
    
    if(learnEpEntities != null){
      this.learnEpEntities.addAll(learnEpVersion.learnEpEntities);
    }
    
    if(learnEpCircles != null){
      this.learnEpCircles.addAll(learnEpVersion.learnEpCircles);
    }
  }
  
  protected SSLearnEpVersion(
    final SSUri                  id, 
    final SSUri                  learnEp,
    final List<SSLearnEpEntity>  learnEpEntities,
    final List<SSLearnEpCircle>  learnEpCircles,
    final SSLearnEpTimelineState learnEpTimelineState) throws Exception{
    
    super(id, SSEntityE.learnEpVersion);
    
    this.learnEp               = learnEp;
    this.learnEpTimelineState  = learnEpTimelineState;
    
    if(learnEpEntities != null){
      this.learnEpEntities.addAll(learnEpEntities);
    }
    
    if(learnEpCircles != null){
      this.learnEpCircles.addAll(learnEpCircles);
    }
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld                 = (Map<String, Object>) super.jsonLDDesc();
    final Map<String, Object> learnEpCirclesObj  = new HashMap<>();
    final Map<String, Object> learnEpEntitiesObj = new HashMap<>();
    
    ld.put(SSVarU.learnEp,                SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.learnEpTimelineState,   SSVarU.sss + SSStrU.colon + SSLearnEpTimelineState.class.getName());
    
    learnEpEntitiesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSLearnEpEntity.class.getName());
    learnEpEntitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.learnEpEntities, learnEpEntitiesObj);
    
    learnEpCirclesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSLearnEpCircle.class.getName());
    learnEpCirclesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.learnEpCircles, learnEpCirclesObj);
    
    return ld;
  }
  
  /* json getters */
  
  public String getLearnEp() throws Exception {
    return SSStrU.removeTrailingSlash(learnEp);
  }

  public List<SSLearnEpEntity> getLearnEpEntities(){
    return learnEpEntities;
  }
  
  public List<SSLearnEpCircle> getLearnEpCircles(){
    return learnEpCircles;
  }
  
  public SSLearnEpTimelineState getLearnEpTimelineState(){
    return learnEpTimelineState;
  }
}
