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

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.enums.*;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSUser extends SSEntity{

  @ApiModelProperty
  public List<SSEntity> friends        = new ArrayList<>();
  
  @ApiModelProperty
  public boolean        friend         = false;
  
  @ApiModelProperty
  public String         email          = null;
  
  public static SSUser get(
    final SSEntity       entity) throws SSErr{
    
    return new SSUser(entity);
  }
  
  public static SSUser get(
    final SSUser         user,
    final SSEntity       entity) throws SSErr{
    
    return new SSUser(user, entity);
  }
  
  public static SSUser get(
    final SSUri    id,
    final String   email) throws SSErr{
    
    return new SSUser(id, email);
  }
  
  public SSUser(){}
  
  protected SSUser(
    final SSEntity       entity) throws SSErr{
    
    super(entity);
    
    if(entity instanceof SSUser){
      
      this.email          = ((SSUser)entity).email;
      this.friend         = ((SSUser)entity).friend;
      
      SSEntity.addEntitiesDistinctWithoutNull(this.friends, ((SSUser) entity).friends);
    }
  }
  
  protected SSUser(
    final SSUser         user,
    final SSEntity       entity) throws SSErr{
    
    super(user, entity);
    
    if(user.email != null){
      this.email = user.email;
    }else{
      
      if(entity instanceof SSUser){
        this.email  = ((SSUser) entity).email;
      }
    }
    
    if(user.friend){
      this.friend = user.friend;
    }else{
      
      if(entity instanceof SSUser){
        this.friend  = ((SSUser) entity).friend;
      }
    }
    
    SSEntity.addEntitiesDistinctWithoutNull(this.friends, user.friends);
    
    if(entity instanceof SSUser){
      SSEntity.addEntitiesDistinctWithoutNull(this.friends, ((SSUser) entity).friends);
    }
  }
  
  protected SSUser(
    final SSUri   id,
    final String  email) throws SSErr{
    
    super(id, SSEntityE.user);
    
    this.email = email;
  }
}