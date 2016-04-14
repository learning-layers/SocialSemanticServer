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

package at.tugraz.sss.servs.activity.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSServRetI; 
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSActivitiesGetRet extends SSServRetI{

  @ApiModelProperty
  public List<SSEntity> activities = new ArrayList<>();
  
  @ApiModelProperty
  public Long             queryTime  = null;

  public static SSActivitiesGetRet get(
    final List<SSEntity> activities, 
    final Long             queryTime){
    
    return new SSActivitiesGetRet(activities, queryTime);
  }
  
  private SSActivitiesGetRet(
    final List<SSEntity> activities, 
    final Long           queryTime) {

    super(SSVarNames.activitiesGet);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.activities, activities);
    
    this.queryTime = queryTime;
  }
}