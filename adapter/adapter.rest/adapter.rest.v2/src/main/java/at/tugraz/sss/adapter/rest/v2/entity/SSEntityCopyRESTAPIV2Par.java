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
package at.tugraz.sss.adapter.rest.v2.entity;

import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entity copy request parameter")
public class SSEntityCopyRESTAPIV2Par{

  @ApiModelProperty(
    required = false,
    value = "the entity merge the to be copied in")
  public SSUri targetEntity = null;
  
  @XmlElement
  public void setTargetEntity(final String targetEntity) throws Exception{
    this.targetEntity = SSUri.get(targetEntity, SSVocConf.sssUri);
  }
    
  @ApiModelProperty(
    required = false,
    value = "users to copy for")
  public List<SSUri> forUsers = null;
  
  @XmlElement
  public void setForUsers(final List<String> forUsers) throws Exception{
    this.forUsers = SSUri.get(forUsers, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "label for the copy")
  public SSLabel label = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "include users from the origin entity")
  public Boolean includeUsers                              = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "include entities from the origin entity")
  public Boolean includeEntities                           = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "include metadata which is specific to the copied entity and its entities within")
  public Boolean includeMetaSpecificToEntityAndItsEntities = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "include the user which triggers the copy process")
  public Boolean includeOriginUser = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "entities to exclude in copying")
  public List<SSUri>   entitiesToExclude = null;
  
  @XmlElement
  public void setEntitiesToExclude(final List<String> entitiesToExclude) throws Exception{
    this.entitiesToExclude = SSUri.get(entitiesToExclude, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "comment to add the share process")
  public SSTextComment comment = null;
  
  @XmlElement
  public void setComment(final String comment) throws Exception{
    this.comment = SSTextComment.get(comment);
  }
  
  public SSEntityCopyRESTAPIV2Par(){}
}