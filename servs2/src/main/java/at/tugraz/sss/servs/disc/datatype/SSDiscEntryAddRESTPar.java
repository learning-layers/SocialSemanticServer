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
package at.tugraz.sss.servs.disc.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import io.swagger.annotations.*;

import java.util.List;

@ApiModel
public class SSDiscEntryAddRESTPar{
  
  @ApiModelProperty(
    required = false,
    value = "discussion name (optional in case of an existing discussion)")
  public SSLabel                label          = null;
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label); 
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  @ApiModelProperty(
    required = false,
    value = "describes the discussion in more detail (optional, except in case of a new discussion of type qa)")
  public SSTextComment          description    = null;
  
  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities to be attached either to corresponding discussion if new discussion to be added or to respective entry in the other case")
  public List<SSUri>            entities       = null;
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities, SSConf.sssUri);
  }
  
  public List<String> getEntities(){
    return SSStrU.removeTrailingSlash(entities);
  }
  
  @ApiModelProperty(
    required = false,
    value = "labels for the entities to be attached")
  public List<SSLabel>            entityLabels       = null;
  
  public void setEntityLabels(final List<String> entityLabels) throws SSErr{
   this.entityLabels = SSLabel.get(entityLabels);
  }

  public List<String> getEntityLabels(){
    return SSStrU.toStr(entityLabels);
  }
  
  @ApiModelProperty(
    required = false,
    value = "users to share this discussion with upon creation of a new discussion (optional, though works only for a new discussion)")
  public List<SSUri>            users          = null;
  
  public void setUsers(final List<String> users) throws SSErr{
    this.users = SSUri.get(users, SSConf.sssUri);
  }
  
  public List<String> getUsers(){
    return SSStrU.removeTrailingSlash(users);
  }
 
  @ApiModelProperty(
    required = false,
    value = "circles to share this discussion with upon creation of a new discussion (optional, though works only for a new discussion)")
  public List<SSUri>            circles          = null;
  
  public void setCircles(final List<String> circles) throws SSErr{
    this.circles = SSUri.get(circles, SSConf.sssUri);
  }
  
  public List<String> getCircles(){
    return SSStrU.removeTrailingSlash(circles);
  }
  
  @ApiModelProperty(
    required = false,
    value = "text for the comment / answer / opinion (optional in case of a new discussion)")
  public SSTextComment          entry    = null;

  public void setEntry(final String entry) throws SSErr{
    this.entry = SSTextComment.get(entry);
  }
  
  public String getEntry(){
    return SSStrU.removeTrailingSlash(entry);
  }
      
  @ApiModelProperty(
    required = false,
    value = "discussion to add an entry for (optional in case of a new discussion)")
  public SSUri                disc          = null;
  
  public void setDisc(final String disc) throws SSErr{
    this.disc = SSUri.get(disc, SSConf.sssUri);
  }
  
  public String getDisc(){
    return SSStrU.removeTrailingSlash(disc);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities to start a discussion for (optional)")
  public List<SSUri>                targets          = null;
  
  public void setTargets(final List<String> targets) throws SSErr{
    this.targets = SSUri.get(targets, SSConf.sssUri);
  }
  
  public List<String> getTargets(){
    return SSStrU.removeTrailingSlash(targets);
  }
    
  @ApiModelProperty(
    required = false,
    value = "discussion type: disc, qa or chat (optional in case of an existing discussion)")
  public SSEntityE                type          = null;
  
  public void setType(final String type) throws SSErr{
    this.type = SSEntityE.get(type);
  }
  
  public String getType(){
    return SSStrU.removeTrailingSlash(type);
  }
    
  @ApiModelProperty(
    required = false,
    value = "whether a new disc should be created")
  public boolean          addNewDisc    = false;
}