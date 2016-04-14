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

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.conf.SSConf;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSEntityShareRESTPar{

  @ApiModelProperty(
    required = false,
    value = "users to share with")
  public List<SSUri> users = null;
  
  
  public void setUsers(final List<String> users) throws SSErr{
    this.users = SSUri.get(users, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "circles to share with")
  public List<SSUri> circles = null;
  
  
  public void setCircles(final List<String> circles) throws SSErr{
    this.circles = SSUri.get(circles, SSConf.sssUri);
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "set the entity public")
  public boolean setPublic = false;
  
  @ApiModelProperty(
    required = false,
    value = "comment to add the share process")
  public SSTextComment comment = null;
  
  
  public void setComment(final String comment) throws SSErr{
    this.comment = SSTextComment.get(comment);
  }
  
  public SSEntityShareRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}