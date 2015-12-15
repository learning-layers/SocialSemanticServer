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
package at.tugraz.sss.adapter.rest.v2.activity;

import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.serv.SSUri;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "activitiesGet request parameter")
public class SSActivitiesGetRESTAPIV2Par{
  
  @ApiModelProperty(
    required = false,
    value = "types")
  public List<SSActivityE>      types                     = null;
  
  @XmlElement
  public void setTypes(final List<String> types) throws Exception{
    this.types = SSActivityE.get(types);
  }
  
  @ApiModelProperty(
    required = false,
    value = "false")
  public List<SSUri>            users                     = null;
  
   @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities")
  public List<SSUri>            entities                  = null;
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "circles")
  public List<SSUri>            circles                   = null;
  
  @XmlElement
  public void setCircles(final List<String> circles) throws Exception{
    this.circles = SSUri.get(circles, SSVocConf.sssUri);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "startTime")
  public Long                   startTime                 = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "endTime")
  public Long                   endTime                   = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "includeOnlyLastActivities")
  public Boolean                includeOnlyLastActivities = null;
  
  public SSActivitiesGetRESTAPIV2Par(){}
}
