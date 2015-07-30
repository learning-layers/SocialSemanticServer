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
package at.kc.tugraz.ss.service.user.datatypes;

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import java.util.ArrayList;
import java.util.List;

public class SSUser extends SSEntity{

  public List<SSEntity> friends     = new ArrayList<>();
  public Boolean        friend      = null;
  public String         email       = null;
  
  @Override
  public Object jsonLDDesc(){
    throw new UnsupportedOperationException();
  }
  
  public static SSUser get(
    final SSUser         user,
    final SSEntity       entity) throws Exception{
    
    return new SSUser(user, entity);
  }
  
  protected SSUser(
    final SSUser         user,
    final SSEntity       entity) throws Exception{
    
    super(user, entity);
    
    this.email  = user.email;
    this.friend = user.friend;
    
    if(entity instanceof SSUser){
      SSEntity.addEntitiesDistinctWithoutNull(this.friends, ((SSUser) entity).friends);
    }
    
    SSEntity.addEntitiesDistinctWithoutNull(this.friends, user.friends);
  }
  
  public static SSUser get(
    final SSUri    id,
    final String   email) throws Exception{
    
    return new SSUser(id, email);
  }
  
  protected SSUser(
    final SSUri   id,
    final String  email) throws Exception{
    
    super(id, SSEntityE.user);
    
    this.email = email;
  }
}