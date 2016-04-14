/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.conf.SSConf;
import io.swagger.annotations.*;

import java.util.List;

@ApiModel
public class SSCircleCreateRESTPar{
  
  @ApiModelProperty(
    required = true)
  public SSLabel                label          = null;
  
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty(
    required = false)
  public SSCircleE                type          = null;
  
  public void setType(final String type) throws SSErr{
    this.type = SSCircleE.get(type);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSUri>            entities       = null;
  
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSUri>            users          = null;
  
  
  public void setUsers(final List<String> users) throws SSErr{
    this.users = SSUri.get(users, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false)
  public List<String>            invitees          = null;
  
  public void setInvitees(final List<String> invitees) throws SSErr{
    this.invitees = invitees;
  }
  
  @ApiModelProperty(
    required = false)
  public SSTextComment          description    = null;
  
  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }
  
  public SSCircleCreateRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}
