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

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSEntitiesGetPar extends SSServPar{
  
  public List<SSUri>          entities              = new ArrayList<>();
  public List<SSEntityE>      types                 = new ArrayList<>();
  public List<SSUri>          authors               = new ArrayList<>();
  public SSEntityDescriberPar descPar               = null;

  public List<String> getEntities(){
    return SSStrU.removeTrailingSlash(entities);
  }

  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities);
  }
  
  public List<String> getTypes(){
    return SSStrU.toStr(types);
  }
  
  public void setTypes(List<String> types) throws SSErr{
    this.types = SSEntityE.get(types);
  }
  
  public List<String> getAuthors(){
    return SSStrU.removeTrailingSlash(authors);
  }

  public void setAuthors(final List<String> authors) throws SSErr{
    this.authors = SSUri.get(authors);
  }
  
  public SSEntitiesGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSEntitiesGetPar(
    final SSServPar servPar,
    final SSUri                user,
    final List<SSUri>          entities,
    final SSEntityDescriberPar descPar,
    final boolean              withUserRestriction){
    
    super(SSVarNames.entitiesGet, null, user, servPar.sqlCon);
    
    SSUri.addDistinctWithoutNull     (this.entities, entities);
    
    this.descPar              = descPar;
    this.withUserRestriction  = withUserRestriction;
  }
}