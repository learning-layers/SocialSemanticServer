/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (coffee) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.activity.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServOpE;

public class SSActivitiesGetPar extends SSServPar{
  
  public List<SSActivityE>      types                     = new ArrayList<>();
  public List<SSUri>            users                     = new ArrayList<>();
  public List<SSUri>            entities                  = new ArrayList<>();
  public List<SSUri>            circles                   = new ArrayList<>();
  public Long                   startTime                 = null;
  public Long                   endTime                   = null;
  public Boolean                includeOnlyLastActivities = null;
  
  public void setTypes(final List<String> types) throws Exception{
    this.types = SSActivityE.get(types);
  }
  
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
  
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  public void setCircles(final List<String> circles) throws Exception{
    this.circles = SSUri.get(circles);
  }
  
  public List<String> getCircles() throws Exception{
    return SSStrU.removeTrailingSlash(circles);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getTypes() throws Exception{
    return SSStrU.toStr(types);
  }
  
  public SSActivitiesGetPar(){}
  
  public SSActivitiesGetPar(
    final SSServOpE             op,
    final String                key,
    final SSUri                 user,
    final List<SSActivityE>     types, 
    final List<SSUri>           users, 
    final List<SSUri>           entities, 
    final List<SSUri>           circles,
    final Long                  startTime,
    final Long                  endTime,
    final Boolean               includeOnlyLastActivities){
    
    super(op, key, user);
    
    if(types != null){
      this.types.addAll(types);
    }
    
    if(users != null){
      this.users.addAll(users);
    }
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    if(circles != null){
      this.circles.addAll(circles);
    }
    
    this.startTime                   = startTime;
    this.endTime                     = endTime;
    this.includeOnlyLastActivities   = includeOnlyLastActivities;
  }
}