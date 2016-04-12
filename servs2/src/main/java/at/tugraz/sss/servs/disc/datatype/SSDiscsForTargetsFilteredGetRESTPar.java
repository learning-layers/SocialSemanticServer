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

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSConf;
import io.swagger.annotations.*;

@ApiModel
public class SSDiscsForTargetsFilteredGetRESTPar {
  
  @ApiModelProperty( 
    required = false, 
    value = "user for which discussion will be retrieved")
  public SSUri    forUser       = null;

  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser, SSConf.sssUri);
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "whether comments of threads and entries shall be retrieved")
  public boolean    setComments       = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "whether likes for entries shall be set")
  public boolean    setLikes       = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "whether the types of circles the discussion is in shall be set")
  public boolean    setCircleTypes   = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "whether the entries of the discussion shall be returned")
  public boolean    setEntries   = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "whether tags for disc and entries shall be set")
  public boolean    setTags   = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "whether attached entities shall be set for disc and entries")
  public boolean    setAttachedEntities   = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "whether information on whether querying user read given entries shall be provided")
  public boolean    setReads   = false;
}
