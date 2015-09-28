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
@ApiModel(value = "disc entry update request parameter")
public class SSDiscEntryUpdateRESTAPIV2Par{
  
  @ApiModelProperty(
    required = false,
    value = "attachments to remove")
  public List<SSUri>            entitiesToRemove      = null;
  
  @XmlElement
  public void setEntitiesToRemove(final List<String> entitiesToRemove) throws Exception{
    this.entitiesToRemove = SSUri.get(entitiesToRemove, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities to attach")
  public List<SSUri>            entitiesToAttach      = null;
  
  @XmlElement
  public void setEntitiesToAttach(final List<String> entitiesToAttach) throws Exception{
    this.entitiesToAttach = SSUri.get(entitiesToAttach, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "labels for the entities to be attached")
  public List<SSLabel>            entityLabels       = null;
  
  @XmlElement
  public void setEntityLabels(final List<String> entityLabels) throws Exception{
   this.entityLabels = SSLabel.get(entityLabels);
  }

  @ApiModelProperty(
    required = false,
    value = "text for the comment / answer / opinion")
  public SSTextComment          content    = null;

  @XmlElement
  public void setContent(final String content) throws Exception{
    this.content = SSTextComment.get(content);
  }
}