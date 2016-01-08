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
package at.tugraz.sss.serv.datatype.enums;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.*;
import java.util.List;

public enum SSCircleRightE{

  all,
  read,
  edit;
  
  public static SSCircleRightE get(final String value) throws Exception{
    return SSCircleRightE.valueOf(value);
  }
  
  public static String toStr(final SSCircleRightE accessRight){
    return SSStrU.toStr(accessRight);
  }
  
  public static boolean equals(
    final SSCircleRightE right1,
    final SSCircleRightE right2){
    
    if(SSObjU.isNull(right1, right2)){
      return false;
    }
    
    return right1.toString().equals(right2.toString());
  }
  
  public static boolean contains(
    final List<SSCircleRightE> rights, 
    final SSCircleRightE       certainRight){
    
    if(SSObjU.isNull(rights)){
      return false;
    }
    
    for(SSCircleRightE right : rights){
      
      if(equals(right, certainRight)){
        return true;
      }
    } 
    
    return false;
  }
  
  private SSCircleRightE(){}
}
