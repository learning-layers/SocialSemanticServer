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
  
  public SSUri   user          = null;
  public SSUri   entity        = null;
  public SSFlagE flagType      = null;
  public Long    endTime       = null;
  public Integer value         = null;
  
  public static SSFlag get(
    final SSUri   id,
    final SSUri   user,
    final SSUri   entity,
    final SSFlagE flagType,
    final Long    endTime,
    final Integer value) throws Exception{
    
    return new SSFlag(id, user, entity, flagType, endTime, value);
  }
  
  protected SSFlag(
    final SSUri   id,
    final SSUri   user,
    final SSUri   entity,
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

  @Override
  public Object jsonLDDesc(){
  
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarNames.user,         SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.entity,       SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.flagType,     SSVarNames.sss + SSStrU.colon + SSFlagE.class.getName());
    ld.put(SSVarNames.endTime,      SSVarNames.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarNames.value,        SSVarNames.xsd + SSStrU.colon + SSStrU.valueInteger);
    
    return ld;
  }
  
  /* json getters */
  public String getUser() throws Exception{
    return SSStrU.removeTrailingSlash(user);
  }
  
  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getFlagType() throws Exception{
    return SSStrU.toStr(flagType);
  }
}