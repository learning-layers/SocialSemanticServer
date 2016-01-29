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
package at.tugraz.sss.serv.datatype;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.enums.SSCircleE;
import at.tugraz.sss.serv.datatype.enums.SSCircleRightE;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSCircle extends SSEntity{
 
  @ApiModelProperty
  public SSCircleE                      circleType     = null;
  
  public void setCircleTpye(final String circleType) throws SSErr{
    this.circleType = SSCircleE.get(circleType);
  }
  
  public String getCircleType(){
    return SSStrU.toStr(circleType);
  }
  
  @ApiModelProperty
  public List<SSCircleRightE>           accessRights   = new ArrayList<>();
  
  public void setAccessRights(final List<String> accessRights) throws SSErr{
    this.accessRights.addAll(SSCircleRightE.get(accessRights));
  }
  
  public List<String> getAccessRights(){
    return SSStrU.toStr(accessRights);
  }

  @ApiModelProperty
  public boolean                        isSystemCircle = false;
  
  public static SSCircle get(
    final SSCircle      circle,
    final SSEntity            entity) throws SSErr {
    
    return new SSCircle(circle, entity);
  }
  
  public static SSCircle get(
    final SSUri                          id,
    final SSCircleE                      circleType,
    final boolean                        isSystemCircle) throws SSErr{
    
    return new SSCircle(id, circleType, isSystemCircle);
  }
  
  public SSCircle(){}
    
  protected SSCircle(
    final SSCircle     circle,
    final SSEntity           entity) throws SSErr{
    
    super(circle, entity);
    
    this.circleType     = circle.circleType;
   
    if(circle.accessRights != null){
      this.accessRights.addAll(circle.accessRights);
    }
    
    this.isSystemCircle = circle.isSystemCircle;
  }

  protected SSCircle(
    final SSUri                           id,
    final SSCircleE                       circleType, 
    final boolean                         isSystemCircle) throws SSErr{
    
    super(id, SSEntityE.circle);
    
    this.circleType     = circleType;
    this.isSystemCircle = isSystemCircle;
  }

  public static void addDistinctWithoutNull(
    final List<SSCircle>     entities,
    final SSCircle           entity){
    
    if(
      SSObjU.isNull  (entities, entity) ||
      SSStrU.contains(entities, entity)){
      return;
    }
    
    entities.add(entity);
  }
  
  public static void addDistinctWithoutNull(
    final List<SSCircle>  entities,
    final List<SSCircle>  toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSCircle entity : toAddEntities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(entities, entity)){
        entities.add(entity);
      }
    }
  }  
}
