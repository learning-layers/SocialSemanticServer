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
package at.tugraz.sss.servs.tag.datatype;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.tag.datatype.SSTagLabel;
import at.tugraz.sss.servs.entity.datatype.SSSpaceE;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSTagsAddRESTPar{
  
  @ApiModelProperty(
    required = true,
    value = "tag labels")
  public List<SSTagLabel>     labels        = null;
  
  
  public void setLabels(final List<String> labels) throws SSErr{
    this.labels = SSTagLabel.get(labels);
  }
  
  @ApiModelProperty(
    required = true,
    value = "access restriction for the tag (i.e. privateSpace, sharedSpace)")
  public SSSpaceE     space        = null;
  
  
  public void setSpace(final String space) throws SSErr{
    this.space = SSSpaceE.get(space);
  }
  
  @ApiModelProperty(
    required = true,
    value = "circle, if space is circleSpace; restricts the tag to be visible to users in circle")
  public SSUri     circle        = null;
  
  
  public void setCircle(final String circle) throws SSErr{
    this.circle = SSUri.get(circle, SSConf.sssUri);
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "timestamp for the tag assignment to be created at in milliseconds")
  public Long         creationTime = null;
  
  public SSTagsAddRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}
