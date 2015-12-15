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
package at.tugraz.sss.adapter.rest.v2.tag;

import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSUri;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "tagUserEdit request parameter")
public class SSTagEditRESTAPIV2Par{

  @ApiModelProperty(
    required = true,
    value = "label for the tag")
  public SSTagLabel     label        = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSTagLabel.get(label);
  }
  
  @ApiModelProperty(
    required = true,
    value = "entity to be tagged")
  public SSUri     entity        = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = true,
    value = "new label of the tag")
  public SSTagLabel      newLabel   = null;
  
  @XmlElement
  public void setNewLabel(final String newLabel) throws Exception{
    this.newLabel = SSTagLabel.get(newLabel);
  }
  
  public SSTagEditRESTAPIV2Par(){}
}
