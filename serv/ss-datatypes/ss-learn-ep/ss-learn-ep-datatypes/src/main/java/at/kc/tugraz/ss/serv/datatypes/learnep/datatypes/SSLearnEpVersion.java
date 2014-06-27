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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLearnEpVersion extends SSEntityA {

  public SSUri                  id                   = null;
  public SSUri                  learnEp              = null;
  public String                 timestamp            = null;
  public SSLearnEpTimelineState learnEpTimelineState = null;
  public List<SSLearnEpEntity>  entities             = new ArrayList<>();
  public List<SSLearnEpCircle>  circles              = new ArrayList<>();

  public static SSLearnEpVersion get(
    final SSUri                  id, 
    final SSUri                  learnEpUri, 
    final String                 timestamp, 
    final List<SSLearnEpEntity>  entities, 
    final List<SSLearnEpCircle>  circles,
    final SSLearnEpTimelineState learnEpTimelineState) throws Exception{
    
    return new SSLearnEpVersion(id, learnEpUri, timestamp, entities, circles, learnEpTimelineState);
  }
  
  private SSLearnEpVersion(
    final SSUri                  id, 
    final SSUri                  learnEpUri, 
    final String                 timestamp, 
    final List<SSLearnEpEntity>  entities, 
    final List<SSLearnEpCircle>  circles,
    final SSLearnEpTimelineState learnEpTimelineState) throws Exception{
    
    super(id);
    
    this.id                    = id;
    this.learnEp               = learnEpUri;
    this.timestamp             = timestamp;
    this.learnEpTimelineState  = learnEpTimelineState;
    
    if(entities != null){
      this.entities = entities;
    }
    
    if(circles != null){
      this.circles = circles;
    }
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld          = new HashMap<>();
    final Map<String, Object> entitiesObj = new HashMap<>();
    final Map<String, Object> circlesObj  = new HashMap<>();
    
    ld.put(SSVarU.id,                     SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.learnEp,                SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.timestamp,              SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.learnEpTimelineState,   SSVarU.sss + SSStrU.colon + SSLearnEpTimelineState.class.getName());
    
    entitiesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSLearnEpEntity.class.getName());
    entitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entities, entitiesObj);
    
    circlesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSLearnEpCircle.class.getName());
    circlesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circles, circlesObj);
    
    return ld;
  }
  
  /* json getters */
  
  public String getId() throws Exception {
    return SSStrU.removeTrailingSlash(id);
  }

  public String getLearnEp() throws Exception {
    return SSStrU.removeTrailingSlash(learnEp);
  }

  public String getTimestamp() {
    return timestamp;
  }
  
  public List<SSLearnEpEntity> getEntities(){
    return entities;
  }
  
  public List<SSLearnEpCircle> getCircles(){
    return circles;
  }
  
  public SSLearnEpTimelineState getLearnEpTimelineState(){
    return learnEpTimelineState;
  }
}
