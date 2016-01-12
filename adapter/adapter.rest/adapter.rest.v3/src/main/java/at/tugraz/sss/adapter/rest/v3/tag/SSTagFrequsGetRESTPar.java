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
package at.tugraz.sss.adapter.rest.v3.tag;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.SSSpaceE;
import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import io.swagger.annotations.*;

import java.util.List;

@ApiModel
public class SSTagFrequsGetRESTPar{
  
  @ApiModelProperty(
    required = false, 
    value = "user to retrieve tags for")
  public SSUri              forUser    = null;
  
  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false, 
    value = "entities to retrieve tags for")
  public List<SSUri>        entities   = null;
  
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false, 
    value = "tag labels to consider for retrieving tags")
  public List<SSTagLabel>   labels     = null;
  
  
  public void setLabels(final List<String> labels) throws SSErr{
    this.labels = SSTagLabel.get(labels);
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "access restriction for to be retrieved tags (i.e. privateSpace, sharedSpace)")
  public SSSpaceE           space      = null;
  
  @ApiModelProperty(
    required = false,
    value = "circles to limit tags found")
  public List<SSUri>        circles       = null;
  
  
  public void setCircles(final List<String> circles) throws SSErr {
    this.circles = SSUri.get(circles, SSConf.sssUri);
  }
  
  
  @ApiModelProperty(
    required = false, 
    value = "timestamp to retrieve tags from a certain point in time")
  public Long               startTime  = null;
  
  
  @ApiModelProperty(
    required = false, 
    value = "whether all user's entities in the system shall be considered to retrieve tag frequencies")
  public boolean               useUsersEntities  = false;
  
  public SSTagFrequsGetRESTPar(){}
}