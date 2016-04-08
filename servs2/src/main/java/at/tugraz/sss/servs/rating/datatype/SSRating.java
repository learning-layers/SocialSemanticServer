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
 package at.tugraz.sss.servs.rating.datatype;

import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import io.swagger.annotations.*;

@ApiModel
public class SSRating extends SSEntity{

  @ApiModelProperty
  public SSUri      user     = null;
  
  public void setUser(final String user) throws SSErr{
    this.user = SSUri.get(user);
  }
  
  public String getUser() {
    return SSStrU.removeTrailingSlash(user);
  }
  
  @ApiModelProperty
  public SSUri      entity   = null;
  
  public void setEntity(final String entity) throws SSErr {
    this.entity = SSUri.get(entity);
  }
  
  public String getEntity() {
    return SSStrU.removeTrailingSlash(entity);
  }
  
  @ApiModelProperty
  public int    value    = -1;
  
  public static SSRating get(
    final SSUri   id,
    final SSUri   user,
    final SSUri   entity,
    final int     value) throws SSErr{
    
    return new SSRating(id, user, entity, value);
  }
  
  protected SSRating(
    final SSUri   id,
    final SSUri   user,
    final SSUri   entity,
    final int     value) throws SSErr{
    
    super(id, SSEntityE.rating);
    
    this.user        = user;
    this.entity      = entity;
    this.value       = value;
  }
}