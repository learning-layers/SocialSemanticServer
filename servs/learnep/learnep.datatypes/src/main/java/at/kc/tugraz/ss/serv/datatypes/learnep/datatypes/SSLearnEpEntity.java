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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import io.swagger.annotations.*;

@ApiModel
public class SSLearnEpEntity extends SSEntity {
  
  @ApiModelProperty
  public SSEntity  entity   = null;
  
  @ApiModelProperty
  public Float     x        = null;
  
  @ApiModelProperty
  public Float     y        = null;
  
  public static SSLearnEpEntity get(
    final SSLearnEpEntity learnEpEntity, 
    final SSEntity        entity) throws Exception{
    
    return new SSLearnEpEntity(learnEpEntity, entity);
  }
  
  public static SSLearnEpEntity get(
    final SSUri    id, 
    final SSEntity entity, 
    final Float    x, 
    final Float    y)throws Exception{
    
    return new SSLearnEpEntity(id, entity, x, y);
  }
  
  public SSLearnEpEntity(){}
  
  protected SSLearnEpEntity(
    final SSLearnEpEntity learnEpEntity,
    final SSEntity        entity) throws Exception{
    
    super(learnEpEntity, entity);
    
    if(learnEpEntity.entity != null){
      this.entity             = learnEpEntity.entity;
    }else{
      
      if(entity instanceof SSLearnEpEntity){
        this.entity = ((SSLearnEpEntity) entity).entity;
      }
    }
    
    if(learnEpEntity.x != null){
      this.x             = learnEpEntity.x;
    }else{
      
      if(entity instanceof SSLearnEpEntity){
        this.x = ((SSLearnEpEntity) entity).x;
      }
    }
    
    if(learnEpEntity.y != null){
      this.y            = learnEpEntity.y;
    }else{
      
      if(entity instanceof SSLearnEpEntity){
        this.y = ((SSLearnEpEntity) entity).y;
      }
    }
  }
  
  protected SSLearnEpEntity(
    final SSUri    id,
    final SSEntity entity,
    final Float    x,
    final Float    y)throws Exception{
    
    super(id, SSEntityE.learnEpEntity);
    
    this.entity             = entity;
    this.x                  = x;
    this.y                  = y;
  }
}
