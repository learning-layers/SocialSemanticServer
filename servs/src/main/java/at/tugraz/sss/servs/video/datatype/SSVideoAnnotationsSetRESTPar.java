  /**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.video.datatype;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSVideoAnnotationsSetRESTPar {

  @ApiModelProperty(
    required = true,
    value = "")
  public List<Long>               timePoints        = null;
  
  
  @ApiModelProperty(
    required = true,
    value = "")
  public List<Float>               x        = null;
  
  
  @ApiModelProperty(
    required = true,
    value = "")
  public List<Float>               y        = null;
  
  @ApiModelProperty(
    required = true,
    value = "")
  public List<SSLabel>               labels        = null;
  
  
  public void setLabels(final List<String> labels) throws SSErr{
    this.labels = SSLabel.get(labels);
  }
  
  @ApiModelProperty(
    required = false,
    value = "")
  public List<SSTextComment>               descriptions        = null;
  
  
  public void setDescriptions(final List<String> descriptions) throws SSErr{
    this.descriptions = SSTextComment.get(descriptions);
  }
  
  
  @ApiModelProperty(
    required = true,
    value = "")
  public boolean               removeExisting        = false;
    
  public SSVideoAnnotationsSetRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}