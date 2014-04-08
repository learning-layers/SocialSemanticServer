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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.HashMap;
import java.util.Map;

public class SSLearnEpEntity extends SSEntityA {
  
  public SSUri    learnEpEntityUri  = null;
  public SSUri    entityUri         = null;
  public Float  x                 = null;
  public Float  y                 = null;
  
  public static SSLearnEpEntity get(SSUri learnEpEntityUri, SSUri entityUri, Float x, Float y){
    return new SSLearnEpEntity(learnEpEntityUri, entityUri, x, y);
  }
  
  private SSLearnEpEntity(SSUri learnEpEntityUri, SSUri entityUri, Float x, Float y){
    super(learnEpEntityUri);
    
    this.learnEpEntityUri   = learnEpEntityUri;
    this.entityUri          = entityUri;
    this.x                  = x;
    this.y                  = y;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld         = new HashMap<String, Object>();
    
    ld.put(SSVarU.learnEpEntityUri, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.entityUri,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.x,                SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.y,                SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public String getLearnEpEntityUri() throws Exception {
    return SSUri.toStrWithoutSlash(learnEpEntityUri);
  }
  
  public String getEntityUri() throws Exception {
    return SSUri.toStrWithoutSlash(entityUri);
  }
  
  public Float getX() {
    return x;
  }

  public Float getY() {
    return y;
  }
}
