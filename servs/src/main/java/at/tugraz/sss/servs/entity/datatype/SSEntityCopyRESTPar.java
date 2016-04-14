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
public class SSEntityCopyRESTPar{

  @ApiModelProperty(
    required = false,
    value = "the entity merge the to be copied in")
  public SSUri targetEntity = null;
  
  
  public void setTargetEntity(final String targetEntity) throws SSErr{
    this.targetEntity = SSUri.get(targetEntity, SSConf.sssUri);
  }
    
  @ApiModelProperty(
    required = false,
    value = "users to copy for")
  public List<SSUri> forUsers = null;
  
  
  public void setForUsers(final List<String> forUsers) throws SSErr{
    this.forUsers = SSUri.get(forUsers, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "label for the copy")
  public SSLabel label = null;
  
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "whether the label of a single 'forUser' shall be appended to the label")
  public boolean appendUserNameToLabel                              = false;
    
  
  @ApiModelProperty(
    required = false,
    value = "include users from the origin entity")
  public boolean includeUsers                              = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "include entities from the origin entity")
  public boolean includeEntities                           = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "include metadata which is specific to the copied entity and its entities within")
  public boolean includeMetaSpecificToEntityAndItsEntities = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "include the user which triggers the copy process")
  public boolean includeOriginUser = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "entities to exclude in copying")
  public List<SSUri>   entitiesToExclude = null;
  
  
  public void setEntitiesToExclude(final List<String> entitiesToExclude) throws SSErr{
    this.entitiesToExclude = SSUri.get(entitiesToExclude, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "comment to add the share process")
  public SSTextComment comment = null;
  
  
  public void setComment(final String comment) throws SSErr{
    this.comment = SSTextComment.get(comment);
  }
  
  public SSEntityCopyRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}