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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntityCircle extends SSEntity{
 
  public SSCircleE                      circleType     = null;
  public List<SSCircleRightE>           accessRights   = new ArrayList<>();
  public Boolean                        isSystemCircle = null;
  
   public String getCircleType(){
    return SSStrU.toStr(circleType);
  }

  public List<String> getAccessRights() throws Exception{
    return SSStrU.toStr(accessRights);
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld              = (Map<String, Object>) super.jsonLDDesc();
    final Map<String, Object> accessRightsObj = new HashMap<>();
    
    ld.put(SSVarNames.circleType,      SSVarNames.sss + SSStrU.colon + SSCircleE.class.getName());
    ld.put(SSVarNames.isSystemCircle,  SSVarNames.xsd + SSStrU.colon + SSStrU.valueBoolean);
    
    accessRightsObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSCircleRightE.class.getName());
    accessRightsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.accessRights, accessRightsObj);
    
    return ld;
  }
  
  public static SSEntityCircle get(
    final SSEntityCircle      circle,
    final SSEntity            entity) throws Exception{
    
    return new SSEntityCircle(circle, entity);
  }
  
  public static SSEntityCircle get(
    final SSUri                          id,
    final SSCircleE                      circleType,
    final Boolean                        isSystemCircle) throws Exception{
    
    return new SSEntityCircle(id, circleType, isSystemCircle);
  }
  
  protected SSEntityCircle(
    final SSEntityCircle     circle,
    final SSEntity           entity) throws Exception{
    
    super(circle, entity);
    
    this.circleType     = circle.circleType;
   
    if(circle.accessRights != null){
      this.accessRights.addAll(circle.accessRights);
    }
    
    this.isSystemCircle = circle.isSystemCircle;
  }

  protected SSEntityCircle(
    final SSUri                           id,
    final SSCircleE                       circleType, 
    final Boolean                         isSystemCircle) throws Exception{
    
    super(id, SSEntityE.circle);
    
    this.circleType     = circleType;
    this.isSystemCircle = isSystemCircle;
  }

  public static void addDistinctWithoutNull(
    final List<SSEntityCircle>     entities,
    final SSEntityCircle           entity){
    
    if(
      SSObjU.isNull  (entities, entity) ||
      SSStrU.contains(entities, entity)){
      return;
    }
    
    entities.add(entity);
  }
  
  public static void addDistinctWithoutNull(
    final List<SSEntityCircle>  entities,
    final List<SSEntityCircle>  toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSEntityCircle entity : toAddEntities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(entities, entity)){
        entities.add(entity);
      }
    }
  }  
}
