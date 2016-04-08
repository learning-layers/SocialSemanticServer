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
package at.tugraz.sss.servs.activity.datatype;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import java.util.ArrayList;
import java.util.List;

public class SSActivitiesGetPar extends SSServPar{
  
  public List<SSUri>            activities                = new ArrayList<>();
  public List<SSActivityE>      types                     = new ArrayList<>();
  public List<SSUri>            users                     = new ArrayList<>();
  public List<SSUri>            entities                  = new ArrayList<>();
  public List<SSUri>            circles                   = new ArrayList<>();
  public Long                   startTime                 = null;
  public Long                   endTime                   = null;
  public int                    maxActivities             = 30;
  public boolean                includeOnlyLastActivities = false;

  public List<String> getActivities() {
    return SSStrU.removeTrailingSlash(activities);
  }

  public void setActivities(final List<String> activities) throws SSErr {
    this.activities = SSUri.get(activities);
  }
  
  public void setTypes(final List<String> types) throws SSErr{
    this.types = SSActivityE.get(types);
  }
  
  public void setUsers(final List<String> users) throws SSErr{
    this.users = SSUri.get(users);
  }
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities);
  }
  
  public void setCircles(final List<String> circles) throws SSErr{
    this.circles = SSUri.get(circles);
  }
  
  public List<String> getCircles() throws SSErr{
    return SSStrU.removeTrailingSlash(circles);
  }
  
  public List<String> getUsers() throws SSErr{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getEntities() throws SSErr{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getTypes() throws SSErr{
    return SSStrU.toStr(types);
  }
  
  public SSActivitiesGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSActivitiesGetPar(
    final SSServPar             servPar,
    final SSUri                 user,
    final List<SSUri>           activities,
    final List<SSActivityE>     types, 
    final List<SSUri>           users, 
    final List<SSUri>           entities, 
    final List<SSUri>           circles,
    final Long                  startTime,
    final Long                  endTime,
    final int                   maxActivities, 
    final boolean               includeOnlyLastActivities, 
    final boolean               withUserRestriction,
    final boolean               invokeEntityHandlers){
    
    super(SSVarNames.activitiesGet, null, user, servPar.sqlCon);

    SSUri.addDistinctWithoutNull(this.activities, activities);
    
    if(types != null){
      this.types.addAll(types);
    }
    
    SSUri.addDistinctWithoutNull(this.users,    users);
    SSUri.addDistinctWithoutNull(this.entities, entities);
    SSUri.addDistinctWithoutNull(this.circles,  circles);

    this.startTime                   = startTime;
    this.endTime                     = endTime;
    this.maxActivities               = maxActivities;
    this.includeOnlyLastActivities   = includeOnlyLastActivities;
    this.withUserRestriction         = withUserRestriction;
    this.invokeEntityHandlers        = invokeEntityHandlers;
  }
}