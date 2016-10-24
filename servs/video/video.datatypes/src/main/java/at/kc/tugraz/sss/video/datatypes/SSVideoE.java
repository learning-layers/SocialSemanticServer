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
package at.kc.tugraz.sss.video.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSJSONLDPropI;
import java.util.ArrayList;
import java.util.List;

public enum SSVideoE implements SSJSONLDPropI{

  hugo1,
  hugo2;

   public static List<SSVideoE> get(final List<String> values){
  
    final List<SSVideoE> result = new ArrayList<>();
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
  
  public static SSVideoE get(final String value) {
    return SSVideoE.valueOf(value);
  }

  public static List<SSVideoE> asListWithoutNullAndEmpty(final SSVideoE... appStackLayouts){
   
    final List<SSVideoE> result = new ArrayList<>();
    
    if(appStackLayouts == null){
      return result;
    }
    
    for(SSVideoE appStackLayout : appStackLayouts){
      
      if(SSStrU.isEmpty(appStackLayout)){
        continue;
      }
      
      result.add(appStackLayout);
    }
    
    return result;
  }
  
  @Override
  public Object jsonLDDesc(){
    return SSVarNames.xsd + SSStrU.colon + SSStrU.valueString;
  }
}