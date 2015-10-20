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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.List;

public enum SSImageE implements SSJSONLDPropI{
  
  image,
  thumb,
  screenShot;

  @Override
  public Object jsonLDDesc(){
    return SSVarNames.xsd + SSStrU.colon + SSStrU.valueString;
  }
  
  public static List<SSImageE> get(final List<String> values) throws Exception{
  
    final List<SSImageE> result = new ArrayList<>();
    
    if(values == null){
      return result;
    }
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
  
  public static SSImageE get(final String value) throws Exception{
    
    try{
    
      if(value == null){
        return null;
      }
      
      return SSImageE.valueOf(value);
    }catch(Exception error){
      throw new Exception("image type not available: " + value);
    }
  }
  
  public static void addDistinctNotNull(
    final List<SSImageE>     entities,
    final SSImageE           entity){
    
    if(
      SSObjU.isNull  (entities, entity) ||
      SSStrU.contains(entities, entity)){
      return;
    }
    
    entities.add(entity);
  }
  
  public static void addDistinctNotNull(
    final List<SSImageE>  entities,
    final List<SSImageE>  toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSImageE entity : toAddEntities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(entities, entity)){
        entities.add(entity);
      }
    }
  }
  
  public static List<SSImageE> asListNotNull(final SSImageE... types){
   
    final List<SSImageE> result = new ArrayList<>();
    
    if(types == null){
      return result;
    }
    
    for(SSImageE type : types){
      
      if(type == null){
        continue;
      }
      
      result.add(type);
    }
    
    return result;
  }
  
  public static List<SSImageE> asListNotNull(final List<SSImageE> types){
   
    final List<SSImageE> result = new ArrayList<>();
    
    if(types == null){
      return result;
    }
    
    for(SSImageE type : types){
      
      if(type == null){
        continue;
      }
      
      result.add(type);
    }
    
    return result;
  }
}