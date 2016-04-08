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

import at.tugraz.sss.serv.datatype.SSEntity;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import java.util.ArrayList;
import java.util.List;

public class SSCirclesGetRet extends SSServRetI{

  public List<SSEntity> circles = new ArrayList<>();

  public static SSCirclesGetRet get(
    final List<SSEntity> circles){
    
    return new SSCirclesGetRet(circles);
  }
  
  private SSCirclesGetRet(
    final List<SSEntity> circles){

    super(SSVarNames.circlesGet);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.circles, circles);
  }
}
