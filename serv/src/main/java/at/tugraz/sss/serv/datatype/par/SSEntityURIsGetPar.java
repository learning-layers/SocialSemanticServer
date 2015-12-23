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
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.SSServPar;import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
 import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.*;
import java.util.ArrayList;
import java.util.List;

public class SSEntityURIsGetPar extends SSServPar{
  
  public List<SSUri>          entities              = new ArrayList<>();
  public Boolean              getAccessible         = false;
  public List<SSEntityE>      types                 = new ArrayList<>();
  public List<SSUri>          authors               = new ArrayList<>();
  public Long                 startTime             = null;
  public Long                 endTime               = null;
  
  public List<String> getEntities(){
    return SSStrU.removeTrailingSlash(entities);
  }

  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  public List<String> getTypes(){
    return SSStrU.toStr(types);
  }
  
  public void setTypes(List<String> types) throws Exception{
    this.types = SSEntityE.get(types);
  }
  
  public List<String> getAuthors(){
    return SSStrU.removeTrailingSlash(authors);
  }

  public void setAuthors(final List<String> authors) throws Exception{
    this.authors = SSUri.get(authors);
  }
  
  public SSEntityURIsGetPar(){}
  
  public SSEntityURIsGetPar(
    final SSUri                user,
    final List<SSUri>          entities, 
    final Boolean              getAccessible, 
    final List<SSEntityE>      types,
    final List<SSUri>          authors,
    final Long                 startTime,
    final Long                 endTime) throws Exception{
    
    super(SSVarNames.entitiesGet, null, user);
    
    SSUri.addDistinctWithoutNull (this.entities, entities);
    
    this.getAccessible = getAccessible;
    
    SSEntityE.addDistinctWithoutNull(this.types,   types);
    SSUri.addDistinctWithoutNull    (this.authors, authors);
    
    this.startTime = startTime;
    this.endTime   = endTime;
  }
}