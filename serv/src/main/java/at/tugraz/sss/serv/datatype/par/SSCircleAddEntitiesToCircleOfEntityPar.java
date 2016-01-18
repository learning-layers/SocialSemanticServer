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
package at.tugraz.sss.serv.datatype.par;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSCircleAddEntitiesToCircleOfEntityPar extends SSServPar{
  
  public SSUri                 entity               = null;
  public List<SSUri>           entityURIs           = new ArrayList<>();

  public String getEntity() {
    return SSStrU.removeTrailingSlash(entity);
  }

  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity);
  }

  public List<String> getEntityURIs() {
    return SSStrU.removeTrailingSlash(entityURIs);
  }

  public void setEntityURIs(List<String> entityURIs) throws SSErr{
    this.entityURIs = SSUri.get(entityURIs);
  }

  public SSCircleAddEntitiesToCircleOfEntityPar(
    final SSServPar servPar,
    final SSUri                 user,
    final SSUri                 entity,
    final List<SSUri>           entityURIs,
    final boolean               withUserRestriction,
    final boolean               invokeEntityHandlers,
    final boolean               shouldCommit){
    
    super(SSVarNames.circleAddEntitiesToCircleOfEntity, null, user, servPar.sqlCon);
    
    this.entity = entity;
    
    SSUri.addDistinctWithoutNull(this.entityURIs, entityURIs);
    
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
    this.shouldCommit         = shouldCommit;
  }
}