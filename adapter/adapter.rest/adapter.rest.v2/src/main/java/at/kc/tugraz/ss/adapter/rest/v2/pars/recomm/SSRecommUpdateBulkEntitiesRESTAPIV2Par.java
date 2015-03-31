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
package at.kc.tugraz.ss.adapter.rest.v2.pars.recomm;

import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "recommUpdateBulk request parameter")
public class SSRecommUpdateBulkEntitiesRESTAPIV2Par{
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "recomm realm the user wants to query")
  public String       realm     = null;  
  
  @ApiModelProperty(
    required = true,
    value = "forUser")
  public SSUri       forUser     = null;
  
  @XmlElement
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = true,
    value = "entities")
  public List<SSUri>       entities     = new ArrayList<>();
  
  @XmlElement
  public void setEntiy(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities, SSVocConf.sssUri);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "tags")
  public List<List<String>>       tags     = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "categories")
  public List<List<String>>       categories     = new ArrayList<>();
  
  public SSRecommUpdateBulkEntitiesRESTAPIV2Par(){}
}
