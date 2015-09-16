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
package at.tugraz.sss.servs.entity.datatypes.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSStrU;

public class SSEntityAttachEntitiesPar extends SSServPar{

  public SSUri               entity    = null;
  public List<SSUri>         entities  = new ArrayList<>();

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  public List<String> getEntities(){
    return SSStrU.removeTrailingSlash(entities);
  }

  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  public SSEntityAttachEntitiesPar(){}
  
  public SSEntityAttachEntitiesPar(
    final SSUri               user,
    final SSUri               entity,
    final List<SSUri>         entities,
    final Boolean             withUserRestriction, 
    final Boolean             shouldCommit){

    super(SSServOpE.entityEntitiesAttach, null, user);
  
    this.entity         = entity;
    
    SSUri.addDistinctWithoutNull(this.entities, entities);
    
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}