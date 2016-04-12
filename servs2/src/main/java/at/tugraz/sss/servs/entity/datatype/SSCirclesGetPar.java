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
package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSCirclesGetPar extends SSServPar{

  public SSUri           forUser                  = null;
  public SSUri           entity                   = null;
  public List<SSEntityE> entityTypesToIncludeOnly = new ArrayList<>();
  public boolean         withSystemCircles        = false;
  public boolean         setEntities              = false;
  public boolean         setUsers                 = false;
  public boolean         setProfilePicture        = false;
  public boolean         setThumb                 = false;
  public boolean         setTags                  = false;

  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser);
  }
  
  public String getForUser() throws SSErr{
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public void setEntity(final String entity)throws SSErr{
    this.entity = SSUri.get(entity);
  }

  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly)throws SSErr{
    this.entityTypesToIncludeOnly = SSEntityE.get(entityTypesToIncludeOnly);
  }

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getEntityTypesToIncludeOnly() throws SSErr{
    return SSStrU.toStr(entityTypesToIncludeOnly);
  }
  
  public SSCirclesGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSCirclesGetPar(
    final SSServPar servPar,
    final SSUri           user,
    final SSUri           forUser,
    final SSUri           entity,
    final List<SSEntityE> entityTypesToIncludeOnly,
    final boolean         setEntities, 
    final boolean         setUsers,
    final boolean         withUserRestriction,
    final boolean         withSystemCircles,
    final boolean         invokeEntityHandlers) {
    
    super(SSVarNames.circlesGet, null, user, servPar.sqlCon);
    
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