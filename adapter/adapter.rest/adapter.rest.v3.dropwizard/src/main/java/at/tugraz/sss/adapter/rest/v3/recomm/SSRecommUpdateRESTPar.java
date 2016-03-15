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
package at.tugraz.sss.adapter.rest.v3.recomm;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.util.*;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSRecommUpdateRESTPar{

  @ApiModelProperty(
    required = true,
    value = "realm the user wants to query")
  public String       realm     = null;
  
  @ApiModelProperty(
    required = true,
    value = "forUser")
  public SSUri       forUser     = null;
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = true,
    value = "entity")
  public SSUri       entity     = null;
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "tags")
  public List<String>       tags     = null;
  
  @ApiModelProperty(
    required = false,
    value = "categories")
  public List<String>       categories     = null;
  
  public SSRecommUpdateRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}
