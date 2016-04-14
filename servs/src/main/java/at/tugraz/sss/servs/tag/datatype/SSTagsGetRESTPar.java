/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.tag.datatype;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.tag.datatype.SSTagLabel;
import at.tugraz.sss.servs.entity.datatype.SSSpaceE;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSTagsGetRESTPar{
  
  @ApiModelProperty(
    required = false, 
    value = "user to retrieve tag assignments for")
  public SSUri              forUser        = null;
  
  
  public void setForUser(final String forUser) throws SSErr {
    this.forUser = SSUri.get(forUser, SSConf.sssUri); 
  }
  
  @ApiModelProperty(
    required = false, 
    value = "entities to retrieve tag assignments for")
  public List<SSUri>        entities       = null;
  
  
  public void setEntities(final List<String> entities) throws SSErr {
    this.entities = SSUri.get(entities, SSConf.sssUri);
  }
  
  
  @ApiModelProperty(
    required = false, 
    value = "tag labels to consider for retrieving tag assignments")
  public List<SSTagLabel>   labels         = null;
  
    
  public void setLabels(final List<String> labels) throws SSErr{
    this.labels = SSTagLabel.get(labels);
  }
  
  @ApiModelProperty(
    required = false, 
    value = "access restriction for to be retrieved tag assignments (i.e. privateSpace, sharedSpace)")
  public SSSpaceE           space          = null;
      
  
  public void setSpace(final String space) throws SSErr{
    this.space = SSSpaceE.get(space);
  }
  
  @ApiModelProperty(
    required = false, 
    value = "circles to limit tag assignments found")
  public List<SSUri>        circles       = null;
  
  
  public void setCircles(final List<String> circles) throws SSErr {
    this.circles = SSUri.get(circles, SSConf.sssUri);
  }

  
  @ApiModelProperty(
    required = false, 
    value = "timestamp to retrieve tag assignments from a certain point in time (optional)")
  public Long               startTime      = null;
  
  public SSTagsGetRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}
