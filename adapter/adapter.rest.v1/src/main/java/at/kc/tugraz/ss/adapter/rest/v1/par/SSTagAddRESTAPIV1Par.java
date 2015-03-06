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
package at.kc.tugraz.ss.adapter.rest.v1.par;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "tagAdd request parameter")
public class SSTagAddRESTAPIV1Par{
  
  @ApiModelProperty( 
    value = "the user's identifier", 
    required = true)
  public        SSUri                user          = null;
  
  @XmlElement 
  public void setUser(final String user) throws Exception{
    this.user = SSUri.get(user);
  }
  
  @XmlElement 
  @ApiModelProperty( 
    value = "the user's access tocken", 
    required = true)
  public String key                    = null;
  
  @ApiModelProperty(
    required = true,
    value = "entity to add tag to")
  public SSUri        entity       = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @ApiModelProperty(
    required = true,
    value = "label of the tag to add")
  public SSTagLabel   label        = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSTagLabel.get(label);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "access restriction for the tag (i.e. privateSpace, sharedSpace)")
  public SSSpaceE     space        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "timestamp for the tag assignment to be created at in milliseconds")
  public Long         creationTime = null;
  
  public SSTagAddRESTAPIV1Par(){}
}
