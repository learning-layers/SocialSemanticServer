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
package at.tugraz.sss.adapter.rest.v2.ue;

import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@ApiModel(value = "uesGet request parameter")
@XmlRootElement
public class SSUEsGetRESTAPIV2Par{
  
  @ApiModelProperty(
    required = false,
    value = "user to retrieve user events for")
  public SSUri           forUser        = null;
  
  @XmlElement
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entity to retrieve user events for")
  public SSUri           entity         = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "user event types to retrieve")
  public List<SSUEE>           types           = null;
  
  public void setTypes(final List<String> types) throws Exception{
    this.types = SSUEE.get(types);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "start timestamp for retrieving user events for")
  public Long            startTime      = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "end timestamp for retrieving user events for")
  public Long            endTime        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "whether tags for entitis shall be set")
  public Boolean            setTags  = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "whether flags for entitis shall be set")
  public Boolean            setFlags  = false;
  
  public SSUEsGetRESTAPIV2Par(){}
}