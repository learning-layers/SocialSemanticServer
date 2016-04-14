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

import at.tugraz.sss.servs.util.SSStrU;
import io.swagger.annotations.*;

@ApiModel
public class SSEntityContext {
  
  @ApiModelProperty
  public SSUri     id        = null;
  
  public void setId(final String id) throws SSErr{
    this.id = SSUri.get(id);
  }
  
  public String getId(){
    return SSStrU.removeTrailingSlash(id);
  }
  
  @ApiModelProperty
  public SSEntityE context   = null; //e.g. "tag", "category"
  
  public void setContext(final String context) throws SSErr{
    this.context = SSEntityE.get(context);
  }
  
  public String getContext(){
    return SSStrU.toStr(context);
  }
  
  @ApiModelProperty
  public String    content   = null;
  
  @ApiModelProperty
  public Long      timestamp = null;
  
  public SSEntityContext(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSEntityContext(
    final SSUri     id,
    final SSEntityE context,
    final String    content,
    final Long      timestamp){
    
    this.id        = id;
    this.context   = context;
    this.content   = content;
    this.timestamp = timestamp;
  }
}
