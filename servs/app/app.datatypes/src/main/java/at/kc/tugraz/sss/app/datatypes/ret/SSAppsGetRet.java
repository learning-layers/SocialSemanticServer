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
package at.kc.tugraz.sss.app.datatypes.ret;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSJSONLDU;
import at.kc.tugraz.sss.app.datatypes.SSApp;
import at.tugraz.sss.serv.SSEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSAppsGetRet extends SSServRetI{
  
  public List<SSEntity> apps = new ArrayList<>();
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld         = new HashMap<>();
    final Map<String, Object> appsObj    = new HashMap<>();
    
    appsObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSApp.class.getName());
    appsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.apps, appsObj);
    
    return ld;
  }
  
  public static SSAppsGetRet get(
    final List<SSEntity>    apps){
    
    return new SSAppsGetRet(apps);
  }
  
  private SSAppsGetRet(
    final List<SSEntity>     apps) {
    
    super(SSServOpE.appsGet);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.apps, apps);
  }
}
