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
package at.kc.tugraz.ss.activity.datatypes.ret;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServRetI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSActivitiesGetRet extends SSServRetI{
 
  public List<SSEntity>   activities = new ArrayList<>();
  public Long             queryTime  = null;

  @Override
  public Map<String, Object> jsonLDDesc(){
    throw new UnsupportedOperationException();
  }
  
  public static SSActivitiesGetRet get(
    final List<SSEntity> activities, 
    final Long             queryTime){
    
    return new SSActivitiesGetRet(activities, queryTime);
  }
  
  private SSActivitiesGetRet(
    final List<SSEntity> activities, 
    final Long           queryTime) {

    super(SSServOpE.activitiesGet);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.activities, activities);
    
    this.queryTime = queryTime;
  }
}