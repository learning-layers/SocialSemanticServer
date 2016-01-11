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
package at.tugraz.sss.adapter.rest.v2.circle;

import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.enums.SSCircleE;
import io.swagger.annotations.*;

import java.util.List;

@ApiModel
public class SSCircleCreateRESTAPIV2Par{
  
  @ApiModelProperty(
    required = true,
    value = "circle name")
  public SSLabel                label          = null;
  
  
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty(
    required = false,
    value = "circle type (i.e., group or pubCircle")
  public SSCircleE                type          = null;
  
  
  public void setType(final String type) throws Exception{
    this.type = SSCircleE.get(type);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities to add")
  public List<SSUri>            entities       = null;
  
  
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "users to add")
  public List<SSUri>            users          = null;
  
  
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "user (emails) to invite")
  public List<String>            invitees          = null;
  
  
  public void setInvitees(final List<String> invitees) throws Exception{
    this.invitees = invitees;
  }
  
  @ApiModelProperty(
    required = false,
    value = "textual annotation")
  public SSTextComment          description    = null;

  
  public void setDescription(final String description) throws Exception{
    this.description = SSTextComment.get(description);
  }
  
  public SSCircleCreateRESTAPIV2Par(){}
}
