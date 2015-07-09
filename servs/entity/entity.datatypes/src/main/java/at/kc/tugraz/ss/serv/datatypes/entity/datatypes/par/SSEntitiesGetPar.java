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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import java.util.ArrayList;
import java.util.List;

public class SSEntitiesGetPar extends SSServPar{
  
  public List<SSUri>          entities              = new ArrayList<>();
  public SSUri                forUser               = null;
  public List<SSEntityE>      types                 = new ArrayList<>();
  public SSEntityDescriberPar descPar               = null;

  public List<String> getEntities(){
    return SSStrU.removeTrailingSlash(entities);
  }

  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public void setForUser(String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }
  
  public List<String> getTypes(){
    return SSStrU.toStr(types);
  }
  
  public void setTypes(List<String> types) throws Exception{
    this.types = SSEntityE.get(types);
  }
  
  public SSEntitiesGetPar(){}
  
  public SSEntitiesGetPar(
    final SSServOpE            op,
    final String               key,
    final SSUri                user,
    final List<SSUri>          entities,
    final SSUri                forUser,
    final List<SSEntityE>      types,
    final SSEntityDescriberPar descPar,
    final Boolean              withUserRestriction) throws Exception{
    
    super(op, key, user);
    
    SSUri.addDistinctWithoutNull(this.entities, entities);
    
    this.forUser              = forUser;
    
    SSEntityE.addDistinctWithoutNull(this.types, types);
    
    this.descPar              = descPar;
    this.withUserRestriction  = withUserRestriction;
  }
}