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
package at.tugraz.sss.servs.flag.datatype;

import at.tugraz.sss.servs.util.SSObjU;
import at.tugraz.sss.servs.util.SSStrU;
import java.util.ArrayList;
import java.util.List;

public enum SSFlagE{

  importance,
  deadline;

   public static List<SSFlagE> get(final List<String> values){
  
    final List<SSFlagE> result = new ArrayList<>();
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
  
  public static SSFlagE get(final String value) {
    return SSFlagE.valueOf(value);
  }

  public static List<SSFlagE> asListWithoutNullAndEmpty(final SSFlagE... flags){
   
    final List<SSFlagE> result = new ArrayList<>();
    
    if(flags == null){
      return result;
    }
    
    for(SSFlagE flag : flags){
      
      if(SSStrU.isEmpty(flag)){
        continue;
      }
      
      result.add(flag);
    }
    
    return result;
  }
  
  public static void addDistinctWithoutNull(
    final List<SSFlagE>  flags,
    final List<SSFlagE>  toAddFlags){
    
    if(SSObjU.isNull(flags, toAddFlags)){
      return;
    }
    
    for(SSFlagE flag : toAddFlags){
      
      if(flag == null){
        continue;
      }
      
      if(!SSStrU.contains(flags, flag)){
        flags.add(flag);
      }
    }
  }
}
