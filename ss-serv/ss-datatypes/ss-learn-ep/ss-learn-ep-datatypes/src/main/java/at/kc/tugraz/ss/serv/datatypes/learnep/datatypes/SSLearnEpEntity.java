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
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import java.util.HashMap;
import java.util.Map;

public class SSLearnEpEntity extends SSEntityA {
  
  public SSUri  id       = null;
  public SSUri  entity   = null;
  public Float  x        = null;
  public Float  y        = null;
  
  public static SSLearnEpEntity get(SSUri learnEpEntityUri, SSUri entityUri, Float x, Float y)throws Exception{
    return new SSLearnEpEntity(learnEpEntityUri, entityUri, x, y);
  }
  
  private SSLearnEpEntity(SSUri learnEpEntityUri, SSUri entityUri, Float x, Float y)throws Exception{
    super(learnEpEntityUri);
    
    this.id   = learnEpEntityUri;
    this.entity          = entityUri;
    this.x                  = x;
    this.y                  = y;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld         = new HashMap<String, Object>();
    
    ld.put(SSVarU.id,      SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.entity,  SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.x,       SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.y,       SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    
    return ld;
  }
  
  /* getters to allow for json enconding */
  public String getId() throws Exception {
    return SSUri.toStrWithoutSlash(id);
  }
  
  public String getEntity() throws Exception {
    return SSUri.toStrWithoutSlash(entity);
  }
  
  public Float getX() {
    return x;
  }

  public Float getY() {
    return y;
  }
}
