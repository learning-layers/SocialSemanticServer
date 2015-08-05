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
package at.kc.tugraz.sss.flag.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import java.util.Map;

public class SSFlag extends SSEntity{
  
  public SSEntity   user          = null;
  public SSEntity   entity        = null;
  public SSFlagE    flagType      = null;
  public Long       endTime       = null;
  public Integer    value         = null;
  
  public String getFlagType() throws Exception{
    return SSStrU.toStr(flagType);
  }
  
  @Override
  public Object jsonLDDesc(){
  
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarNames.user,         SSVarNames.sss + SSStrU.colon + SSEntity.class.getName());
    ld.put(SSVarNames.entity,       SSVarNames.sss + SSStrU.colon + SSEntity.class.getName());
    ld.put(SSVarNames.flagType,     SSVarNames.sss + SSStrU.colon + SSFlagE.class.getName());
    ld.put(SSVarNames.endTime,      SSVarNames.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarNames.value,        SSVarNames.xsd + SSStrU.colon + SSStrU.valueInteger);
    
    return ld;
  }
  
  public static SSFlag get(
    final SSFlag     flag,
    final SSEntity   entity) throws Exception{
    
    return new SSFlag(flag, entity);
  }
  
  public SSFlag(
    final SSFlag     flag,
    final SSEntity   entity) throws Exception{
   
    super(flag, entity);

    if(flag.user != null){
      this.user = flag.user;
    }else{
      
      if(entity instanceof SSFlag){
        this.user = ((SSFlag) entity).user;
      }
    }
    
    if(flag.entity != null){
      this.entity = flag.entity;
    }else{
      
      if(entity instanceof SSFlag){
        this.entity = ((SSFlag) entity).entity;
      }
    }
    
    if(flag.flagType != null){
      this.flagType = flag.flagType;
    }else{
      
      if(entity instanceof SSFlag){
        this.flagType = ((SSFlag) entity).flagType;
      }
    }
    
    if(flag.endTime != null){
      this.endTime = flag.endTime;
    }else{
      
      if(entity instanceof SSFlag){
        this.endTime = ((SSFlag) entity).endTime;
      }
    }
    
    if(flag.value != null){
      this.value = flag.value;
    }else{
      
      if(entity instanceof SSFlag){
        this.value = ((SSFlag) entity).value;
      }
    }
  }
    
  public static SSFlag get(
    final SSUri      id,
    final SSEntity   user,
    final SSEntity   entity,
    final SSFlagE    flagType,
    final Long       endTime,
    final Integer    value) throws Exception{
    
    return new SSFlag(id, user, entity, flagType, endTime, value);
  }
  
  protected SSFlag(
    final SSUri   id,
    final SSEntity   user,
    final SSEntity   entity,
    final SSFlagE flagType,
    final Long    endTime,
    final Integer value) throws Exception{
    
    super(id, SSEntityE.flag);
    
    this.user         = user;
    this.entity       = entity;
    this.flagType     = flagType;
    this.endTime      = endTime;
    this.value        = value;
  }
}