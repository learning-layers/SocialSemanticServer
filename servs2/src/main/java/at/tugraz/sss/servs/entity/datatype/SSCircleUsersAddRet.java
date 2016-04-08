/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 

public class SSCircleUsersAddRet extends SSServRetI{

  public SSUri circle = null;

  public String getCircle() throws SSErr{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public static SSCircleUsersAddRet get(
    final SSUri circleUri){
    
    return new SSCircleUsersAddRet(circleUri);
  }
  
  private SSCircleUsersAddRet(
    final SSUri   circleUri){

    super(SSVarNames.circleUsersAdd);
    
    this.circle = circleUri;
  }
}
