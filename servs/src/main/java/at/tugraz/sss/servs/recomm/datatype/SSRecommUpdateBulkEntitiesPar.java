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
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSRecommUpdateBulkEntitiesPar extends SSServPar{
  
  public String               realm       = null;
  public SSUri                forUser     = null;
  public List<SSUri>          entities    = new ArrayList<>();
  public List<List<String>>   tags        = new ArrayList<>();
  public List<List<String>>   categories  = new ArrayList<>();
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public List<String> getEntities() throws SSErr{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public SSRecommUpdateBulkEntitiesPar(
    final SSServPar servPar,
    final SSUri               user,
    final String              realm,
    final SSUri               forUser, 
    final List<SSUri>         entities, 
    final List<List<String>>  tags,
    final List<List<String>>  categories){
    
    super(SSVarNames.recommUpdateBulkEntities, null, user, servPar.sqlCon);
    
    this.realm   = realm;
    this.forUser = forUser;
    
    SSUri.addDistinctWithoutNull(this.entities, entities);
    
    if(tags != null){
      this.tags.addAll(tags);
    }
    
    if(categories != null){
      this.categories.addAll(categories);
    }
  }
}