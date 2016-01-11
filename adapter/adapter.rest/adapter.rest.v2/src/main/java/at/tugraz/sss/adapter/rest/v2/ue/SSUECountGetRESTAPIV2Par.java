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
package at.tugraz.sss.adapter.rest.v2.ue;

import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import io.swagger.annotations.*;

@ApiModel
public class SSUECountGetRESTAPIV2Par{
  
  @ApiModelProperty(
    required = false,
    value = "user to count user events for")
  public SSUri            forUser    = null;
  
  
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entity to count user events for")
  public SSUri            entity     = null;
  
  
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "user event type to retrieve")
  public SSUEE            type       = null;

  
  public void setType(final String type) throws Exception{
    this.type = SSUEE.get(type);
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "begin for user event inclusion")
  public Long             startTime  = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "end for user event inclusion")
  public Long             endTime    = null;
  
  public SSUECountGetRESTAPIV2Par(){}
}
