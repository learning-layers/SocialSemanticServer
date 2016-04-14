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

package at.tugraz.sss.servs.flag.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.flag.datatype.SSFlagE;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSFlagsSetPar extends SSServPar{
  
  public List<SSUri>   entities       = new ArrayList<>();
  public List<SSFlagE> types          = new ArrayList<>();
  public Integer       value          = null;
  public Long          endTime        = null;
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities); 
  }
  
  public void setTypes(final List<String> types) throws SSErr{
    this.types = SSFlagE.get(types);
  }
  
  public List<String> getEntities() throws SSErr{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getTypes() throws SSErr{
    return SSStrU.toStr(types);
  }
  
  public SSFlagsSetPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSFlagsSetPar(
    final SSServPar servPar,
    final SSUri          user, 
    final List<SSUri>    entities, 
    final List<SSFlagE>  types, 
    final Integer        value, 
    final Long           endTime,
    final boolean        withUserRestriction,
    final boolean        shouldCommit){
    
    super(SSVarNames.flagsSet, null, user, servPar.sqlCon);
    
    SSUri.addDistinctWithoutNull    (this.entities, entities);
    SSFlagE.addDistinctWithoutNull  (this.types,    types);
    
    this.value               = value;
    this.endTime             = endTime;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}