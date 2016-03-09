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
package at.kc.tugraz.ss.recomm.datatypes.par;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import java.util.ArrayList;
import java.util.List;

public class SSRecommTagsPar extends SSServPar{
  
  public String         realm                = null;
  public SSUri          forUser              = null;
  public List<SSUri>    entities             = new ArrayList<>();
  public List<String>   categories           = new ArrayList<>();
  public Integer        maxTags              = 10;
  public boolean        includeOwn           = true;
  public boolean        ignoreAccessRights   = false;
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities.addAll(SSUri.get(entities));
  }
  
  public List<String> getEntities(){
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser);
  }
  
  public SSRecommTagsPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSRecommTagsPar(
    final SSServPar servPar,
    final SSUri         user,
    final String        realm,
    final SSUri         forUser, 
    final List<SSUri>   entities, 
    final List<String>  categories, 
    final Integer       maxTags, 
    final boolean       includeOwn, 
    final boolean       ignoreAccessRights,
    final boolean       withUserRestriction){
    
    super(SSVarNames.recommTags, null, user, servPar.sqlCon);
    
    this.realm   = realm;
    this.forUser = forUser;
    
    SSUri.addDistinctWithoutNull(this.entities,   entities);
    SSStrU.addDistinctNotNull   (this.categories, categories);
    
    this.maxTags              = maxTags;
    this.includeOwn           = includeOwn;
    this.ignoreAccessRights   = ignoreAccessRights;
    this.withUserRestriction  = withUserRestriction;
  }
}