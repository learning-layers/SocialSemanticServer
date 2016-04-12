/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
import io.swagger.annotations.*;

@ApiModel
public class SSTagEditRESTPar{

  @ApiModelProperty(
    required = true,
    value = "label for the tag")
  public SSTagLabel     label        = null;
  
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSTagLabel.get(label);
  }
  
  @ApiModelProperty(
    required = true,
    value = "entity to be tagged")
  public SSUri     entity        = null;
  
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = true,
    value = "new label of the tag")
  public SSTagLabel      newLabel   = null;
  
  
  public void setNewLabel(final String newLabel) throws SSErr{
    this.newLabel = SSTagLabel.get(newLabel);
  }
  
  public SSTagEditRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}
