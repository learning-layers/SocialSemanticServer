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
package at.tugraz.sss.servs.recomm.datatype;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSConf;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSRecommUpdateBulkEntitiesRESTPar{
  
  
  @ApiModelProperty(
    required = true,
    value = "realm the user wants to query")
  public String       realm     = null;  
  
  @ApiModelProperty(
    required = true,
    value = "forUser")
  public SSUri       forUser     = null;
  
  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = true,
    value = "entities")
  public List<SSUri>       entities     = null;
  
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities, SSConf.sssUri);
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "tags")
  public List<List<String>>       tags     = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "categories")
  public List<List<String>>       categories     = null;
  
  public SSRecommUpdateBulkEntitiesRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}