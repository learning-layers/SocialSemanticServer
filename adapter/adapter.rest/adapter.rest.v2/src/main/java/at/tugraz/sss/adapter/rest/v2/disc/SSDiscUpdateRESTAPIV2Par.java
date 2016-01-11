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
package at.tugraz.sss.adapter.rest.v2.disc;

import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import io.swagger.annotations.*;

import java.util.List;

@ApiModel
public class SSDiscUpdateRESTAPIV2Par{
  
  @ApiModelProperty(
    required = false,
    value = "attachments to remove")
  public List<SSUri>            entitiesToRemove      = null;
  
  
  public void setEntitiesToRemove(final List<String> entitiesToRemove) throws Exception{
    this.entitiesToRemove = SSUri.get(entitiesToRemove, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities to attach")
  public List<SSUri>            entitiesToAttach      = null;
  
  
  public void setEntitiesToAttach(final List<String> entitiesToAttach) throws Exception{
    this.entitiesToAttach = SSUri.get(entitiesToAttach, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "labels for the entities to be attached")
  public List<SSLabel>            entityLabels       = null;
  
  
  public void setEntityLabels(final List<String> entityLabels) throws Exception{
   this.entityLabels = SSLabel.get(entityLabels);
  }
  
  @ApiModelProperty(
    required = false,
    value = "disc label")
  public SSLabel          label    = null;

  
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }

  @ApiModelProperty(
    required = false,
    value = "disc text")
  public SSTextComment          content    = null;

  
  public void setContent(final String content) throws Exception{
    this.content = SSTextComment.get(content);
  }
  
   
   @ApiModelProperty(
     required = false,
     value = "whether the discussion with is entries is read by the user")
   public Boolean          read    = null;
}