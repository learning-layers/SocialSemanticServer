/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.auth.datatype;

import at.tugraz.sss.servs.util.SSObjU;
import at.tugraz.sss.servs.util.SSStrU;

public enum SSAuthE {
  
  wikiAuth, 
  csvFileAuth,
  oidc;

  public static String toStr(final SSAuthE value) {
    return SSStrU.toStr(value);
  }
  
  public static SSAuthE get(final String type) {
    return SSAuthE.valueOf(type);
  }
    
  public static boolean isSame(
    final SSAuthE type1, 
    final SSAuthE type2){
    
    if(SSObjU.isNull(type1, type2)){
      return false;
    }
    
    return type1.toString().equals(type2.toString());
  }
}
