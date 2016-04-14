/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.recomm.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSRecommResourcesPar extends SSServPar{
  
  public String          realm                = null;
  public SSUri           forUser              = null;
  public SSUri           entity               = null;
  public List<String>    categories           = new ArrayList<>();
  public Integer         maxResources         = 10;
  public boolean         setCircleTypes       = false;
  public List<SSEntityE> typesToRecommOnly    = new ArrayList<>();
  public boolean         includeOwn           = true;
  public boolean         ignoreAccessRights   = false;
  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser);
  }
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity); 
  }

  public void setTypesToRecommOnly(final List<String> typesToRecommOnly) throws SSErr{
    this.typesToRecommOnly = SSEntityE.get(typesToRecommOnly);
  }

  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getTypesToRecommOnly() throws SSErr{
    return SSStrU.toStr(typesToRecommOnly);
  }

  public SSRecommResourcesPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSRecommResourcesPar(
    final SSServPar servPar,
    final SSUri           user,
    final String          realm, 
    final SSUri           forUser, 
    final SSUri           entity, 
    final List<String>    categories, 
    final Integer         maxResources, 
    final List<SSEntityE> typesToRecommOnly, 
    final boolean         setCircleTypes, 
    final boolean         includeOwn, 
    final boolean         ignoreAccessRights, 
    final boolean         withUserRestriction, 
    final boolean         invokeEntityHandlers){
    
    super(SSVarNames.recommResources, null, user, servPar.sqlCon);
    
    this.realm = realm;
    this.forUser = forUser;
    this.entity   = entity;
    
    SSStrU.addDistinctNotNull(this.categories, categories);
    
    this.maxResources = maxResources;
    
    SSEntityE.addDistinctWithoutNull(this.typesToRecommOnly, typesToRecommOnly);
    
    this.setCircleTypes       = setCircleTypes;
    this.includeOwn           = includeOwn;
    this.ignoreAccessRights   = ignoreAccessRights;
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}