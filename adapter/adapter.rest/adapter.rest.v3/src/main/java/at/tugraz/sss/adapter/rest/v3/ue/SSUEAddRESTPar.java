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
package at.tugraz.sss.adapter.rest.v3.ue;

import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.tugraz.sss.serv.datatype.*;
import io.swagger.annotations.*;

@ApiModel
public class SSUEAddRESTPar{
  
  @ApiModelProperty(
    required = true,
    value = "type of the user event")
  public SSUEE            type    = null;
  
  
  public void setType(final String type) throws SSErr{
    this.type = SSUEE.get(type);
  }
  
  @ApiModelProperty(
    required = true,
    value = "entity with which some interaction shall be traced")
  public SSUri            entity     = null;
  
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity, SSConf.sssUri);
  }  
  
  
  @ApiModelProperty(
    required = true,
    value = "possible additional textual information of the trace")
  public String           content    = null;
  
  public SSUEAddRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}