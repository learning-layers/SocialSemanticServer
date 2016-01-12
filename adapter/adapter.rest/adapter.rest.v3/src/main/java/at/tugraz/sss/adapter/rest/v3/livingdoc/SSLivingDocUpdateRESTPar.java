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
package at.tugraz.sss.adapter.rest.v3.livingdoc;

import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import io.swagger.annotations.*;

@ApiModel
public class SSLivingDocUpdateRESTPar{
  
  @ApiModelProperty(
    required = false, 
    value = "")
  public SSLabel   label       = null;
  
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty(
    required = false, 
    value = "")
  public SSTextComment   description       = null;
  
  
  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }
  
  @ApiModelProperty(
    required = false,
    value = "")
  public SSUri discussion       = null;
  
  
  public void setDiscussion(final String discussion) throws SSErr{
    this.discussion = SSUri.get(discussion, SSConf.sssUri);
  }
  
  public SSLivingDocUpdateRESTPar(){}
}
