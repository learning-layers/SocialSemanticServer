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
package at.kc.tugraz.ss.adapter.rest.v2.pars.tag;

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "tagsUserGet request parameter")
public class SSTagsGetRESTAPIV2Par{
  
  @ApiModelProperty(
    required = false, 
    value = "user to retrieve tag assignments for")
  public SSUri              forUser        = null;
  
  @XmlElement
  public void setForUser(final String forUser) throws Exception{
    try{ this.forUser = SSUri.get(forUser, SSVocConf.sssUri); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false, 
    value = "entities to retrieve tag assignments for")
  public List<SSUri>        entities       = new ArrayList<>();
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    try{ this.entities = SSUri.get(entities, SSVocConf.sssUri); }catch(Exception error){}
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "tag labels to consider for retrieving tag assignments")
  public List<SSTagLabel>   labels         = new ArrayList<>();
  
    @XmlElement
  public void setLabels(final List<String> labels) throws Exception{
    try{ this.labels = SSTagLabel.get(labels); }catch(Exception error){}
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "access restriction for to be retrieved tag assignments (i.e. privateSpace, sharedSpace)")
  public SSSpaceE           space          = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "timestamp to retrieve tag assignments from a certain point in time (optional)")
  public Long               startTime      = null;
  
  public SSTagsGetRESTAPIV2Par(){}
}
