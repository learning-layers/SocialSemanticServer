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
package at.tugraz.sss.adapter.rest.v2.evallog;

import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.datatype.*;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.datatypes.SSEvalLogE;

@ApiModel
public class SSEvalLogRESTAPIV2Par {
  
  @ApiModelProperty(
    required = false,
    value = "context in tool where log was triggered")
  public SSToolContextE   toolContext  = null;

  
  public void setToolContext(final String toolContext) throws Exception {
    this.toolContext = SSToolContextE.get(toolContext);
  }

  @ApiModelProperty(
    required = true,
    value = "type of the log event")
  public SSEvalLogE       type         = null;
  
  
  public void setType(final String type) throws Exception{
    this.type = SSEvalLogE.get(type);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entity to be logged")
  public SSUri         entity       = null;
  
  
  public void setEntity(final String entity) throws Exception{
   this.entity = SSUri.get(entity, SSConf.sssUri);
  }

  @ApiModelProperty(
    required = false,
    value = "entities to be logged")
  public List<SSUri>   entities     = new ArrayList<>();
  
  
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities, SSConf.sssUri);
  }

  @ApiModelProperty(
    required = false,
    value = "users to be logged")
  public List<SSUri>   users        = new ArrayList<>();
  
  
  public void setUsers(final List<String> users)throws Exception{
    this.users = SSUri.get(users, SSConf.sssUri);
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "content to be logged")
  public String           content      = null;
  
  public SSEvalLogRESTAPIV2Par(){}
}