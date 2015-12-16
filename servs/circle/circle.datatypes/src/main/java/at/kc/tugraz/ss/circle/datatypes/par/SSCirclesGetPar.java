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
package at.kc.tugraz.ss.circle.datatypes.par;


import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSVarNames;
import java.util.ArrayList;
import java.util.List;

public class SSCirclesGetPar extends SSServPar{

  public SSUri           forUser                  = null;
  public SSUri           entity                   = null;
  public List<SSEntityE> entityTypesToIncludeOnly = new ArrayList<>();
  public Boolean         withSystemCircles        = false;
  public Boolean         setEntities              = false;
  public Boolean         setUsers                 = false;
  public Boolean         invokeEntityHandlers     = false;
  public Boolean         setProfilePicture        = false;
  public Boolean         setThumb                 = false;
  public Boolean         setTags                  = false;

  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }
  
  public String getForUser() throws Exception{
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public void setEntity(final String entity)throws Exception{
    this.entity = SSUri.get(entity);
  }

  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly)throws Exception{
    this.entityTypesToIncludeOnly = SSEntityE.get(entityTypesToIncludeOnly);
  }

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getEntityTypesToIncludeOnly() throws Exception{
    return SSStrU.toStr(entityTypesToIncludeOnly);
  }
  
  public SSCirclesGetPar(){}
  
  public SSCirclesGetPar(
    final SSUri           user,
    final SSUri           forUser,
    final SSUri           entity,
    final List<SSEntityE> entityTypesToIncludeOnly,
    final Boolean         setEntities, 
    final Boolean         setUsers,
    final Boolean         withUserRestriction,
    final Boolean         withSystemCircles,
    final Boolean         invokeEntityHandlers) throws Exception{
    
    super(SSVarNames.circlesGet, null, user);
    
    this.forUser              = forUser;
    this.entity               = entity;
    
    SSEntityE.addDistinctWithoutNull(this.entityTypesToIncludeOnly, entityTypesToIncludeOnly);
    
    this.setEntities          = setEntities;
    this.setUsers             = setUsers;
    this.withUserRestriction  = withUserRestriction;
    this.withSystemCircles    = withSystemCircles;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}