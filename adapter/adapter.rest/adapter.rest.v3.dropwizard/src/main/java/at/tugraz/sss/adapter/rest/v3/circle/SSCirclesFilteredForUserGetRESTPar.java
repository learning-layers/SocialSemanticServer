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
package at.tugraz.sss.adapter.rest.v3.circle;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import io.swagger.annotations.*;

import java.util.List;

@ApiModel
public class SSCirclesFilteredForUserGetRESTPar{
  
  @ApiModelProperty(
    required = false)
  public List<SSEntityE>   entityTypesToIncludeOnly             = null;
  
  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly) throws SSErr{
     this.entityTypesToIncludeOnly = SSEntityE.get(entityTypesToIncludeOnly);
  }
  
  @ApiModelProperty(
    required = false)
  public boolean setProfilePicture = false;
  
  @ApiModelProperty(
    required = false)
  public boolean setThumb = false;
  
  @ApiModelProperty(
    required = false)
  public boolean setTags = false;
    
  @ApiModelProperty(
    required = false)
  public boolean invokeEntityHandlers = true;
    
  public SSCirclesFilteredForUserGetRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}