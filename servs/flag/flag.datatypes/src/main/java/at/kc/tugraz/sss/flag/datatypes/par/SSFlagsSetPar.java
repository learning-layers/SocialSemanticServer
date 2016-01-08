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
package at.kc.tugraz.sss.flag.datatypes.par;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; import at.tugraz.sss.serv.util.*;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;

import java.util.ArrayList;
import java.util.List;

public class SSFlagsSetPar extends SSServPar{
  
  public List<SSUri>   entities       = new ArrayList<>();
  public List<SSFlagE> types          = new ArrayList<>();
  public Integer       value          = null;
  public Long          endTime        = null;
  
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities); 
  }
  
  public void setTypes(final List<String> types) throws Exception{
    this.types = SSFlagE.get(types);
  }
  
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getTypes() throws Exception{
    return SSStrU.toStr(types);
  }
  
  public SSFlagsSetPar(){}
  
  public SSFlagsSetPar(
    final SSUri          user, 
    final List<SSUri>    entities, 
    final List<SSFlagE>  types, 
    final Integer        value, 
    final Long           endTime,
    final boolean        withUserRestriction,
    final boolean        shouldCommit){
    
    super(SSVarNames.flagsSet, null, user);
    
    SSUri.addDistinctWithoutNull    (this.entities, entities);
    SSFlagE.addDistinctWithoutNull  (this.types,    types);
    
    this.value               = value;
    this.endTime             = endTime;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}