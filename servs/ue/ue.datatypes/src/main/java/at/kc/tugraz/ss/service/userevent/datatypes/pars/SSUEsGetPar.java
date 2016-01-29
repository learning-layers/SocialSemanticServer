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
package at.kc.tugraz.ss.service.userevent.datatypes.pars;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSUEsGetPar extends SSServPar{
  
  public List<SSUri>     userEvents           = new ArrayList<>();
  public SSUri           forUser              = null;
  public SSUri           entity               = null;
  public List<SSUEE>     types                = new ArrayList<>();
  public Long            startTime            = null;
  public Long            endTime              = null;
  public boolean         setTags              = false;
  public boolean         setFlags             = false;

  public List<String> getUserEvents() {
    return SSStrU.removeTrailingSlash(userEvents);
  }

  public void setUserEvents(final List<String> userEvents) throws SSErr {
    this.userEvents = SSUri.get(userEvents);
  }
  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser);
  }
  
  public void setEntity(final String entity)throws SSErr{
    this.entity = SSUri.get(entity);
  }
  
  public void setTypes(final List<String> types)throws SSErr{
    this.types = SSUEE.get(types);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getTypes() throws SSErr{
    return SSStrU.toStr(types);
  }
  
  public SSUEsGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSUEsGetPar(
    final SSServPar servPar,
    final SSUri       user, 
    final List<SSUri> userEvents,
    final SSUri       forUser, 
    final SSUri       entity, 
    final List<SSUEE> types, 
    final Long        startTime, 
    final Long        endTime,
    final boolean     withUserRestriction, 
    final boolean     invokeEntityHandlers){
    
    super(SSVarNames.userEventsGet, null, user, servPar.sqlCon);

    SSUri.addDistinctWithoutNull(this.userEvents, userEvents);
    
    this.forUser = forUser;
    this.entity  = entity;
    
    if(types !=  null){
      this.types.addAll(types);
    }
    
    this.startTime            = startTime;
    this.endTime              = endTime;
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}