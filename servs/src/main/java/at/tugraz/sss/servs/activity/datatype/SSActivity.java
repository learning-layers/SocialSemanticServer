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
package at.tugraz.sss.servs.activity.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSActivity extends SSEntity{

  @ApiModelProperty
  public SSActivityE              activityType = null;
  
  public void setActivityType(final String activityType) throws SSErr{
    this.activityType = SSActivityE.get(activityType);
  }
  
  public String getActivityType() throws SSErr{
    return SSStrU.toStr(activityType);
  }
  
  @ApiModelProperty
  public SSEntity                 entity       = null;
  
  @ApiModelProperty
  public List<SSActivityContent>  contents     = new ArrayList<>();
  
  public void setContents(final List<String> contents) throws SSErr{
    this.contents = SSActivityContent.get(contents);
  }
    
  public List<String> getContents() throws SSErr {
    return SSStrU.toStr(contents);
  }
  
  public static SSActivity get(
    final SSActivity              activity,
    final SSEntity                entity) throws SSErr{
    
    return new SSActivity(activity, entity);
  }
  
  public static SSActivity get(
    final SSUri                   id,
    final SSLabel                 label,
    final SSTextComment           description,
    final Long                    creationTime,
    final SSEntity                author,
    final SSActivityE             activityType,
    final SSEntity                entity,
    final List<SSActivityContent> contents) throws SSErr{
    
    return new SSActivity(
      id,
      label,
      description,
      creationTime,
      author,
      activityType,
      entity,
      contents);
  }
  
  public SSActivity(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSActivity(
    final SSActivity           activity,
    final SSEntity             entity) throws SSErr{
    
    super(activity, entity);
    
    this.activityType = activity.activityType;
    this.entity       = activity.entity;
    
    if(activity.contents != null){
      this.contents.addAll(activity.contents);
    }
  }
  
  protected SSActivity(
    final SSUri                   id,
    final SSLabel                 label,
    final SSTextComment           description,
    final Long                    creationTime,
    final SSEntity                author,
    final SSActivityE             activityType,
    final SSEntity                entity,
    final List<SSActivityContent> contents) throws SSErr{
    
    super(
      id, 
      SSEntityE.activity,
      label,
      description,
      creationTime,
      author);
    
    this.activityType = activityType;
    this.entity       = entity;
    
    if(contents != null){
      this.contents.addAll(contents);
    }
  }
}