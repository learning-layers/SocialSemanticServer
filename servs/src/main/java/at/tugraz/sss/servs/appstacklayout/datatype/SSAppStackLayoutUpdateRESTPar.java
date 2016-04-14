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

package at.tugraz.sss.servs.appstacklayout.datatype;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.conf.SSConf;
import io.swagger.annotations.*;

@ApiModel
public class SSAppStackLayoutUpdateRESTPar{
  
  @ApiModelProperty(
    required = false,
    value = "app this stack is for")
  public SSUri               app        = null;
  
  
  public void setApp(final String app) throws SSErr{
    this.app = SSUri.get(app, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "name")
  public SSLabel               label        = null;
  
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty(
    required = false,
    value = "description")
  public SSTextComment               description        = null;
  
  
  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }
  
  public SSAppStackLayoutUpdateRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}