/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.like.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServOpE;

public class SSLikesUserGetPar extends SSServPar{
  
  public SSUri               entity        = null;
  public SSUri               forUser        = null;
  
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public SSLikesUserGetPar(){}
  
  public SSLikesUserGetPar(
    final SSUri   user,
    final SSUri   entity,
    final SSUri   forUser,
    final Boolean withUserRestriction){
    
    super(SSServOpE.likesGet, null, user);
    
    this.entity              = entity;
    this.forUser             = forUser;
    this.withUserRestriction = withUserRestriction;
  }
}