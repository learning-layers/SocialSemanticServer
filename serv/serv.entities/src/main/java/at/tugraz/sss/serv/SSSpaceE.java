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

public enum SSSpaceE{
  
  sharedSpace,
  circleSpace,
  privateSpace;
  
  public static SSSpaceE get(final String space) throws SSErr{
    
    try{
      
      if(space == null){
        return null;
      }
      
      return SSSpaceE.valueOf(space);
    }catch(Exception error){
      throw SSErr.get(SSErrE.spaceInvalid);
    }
  }
  
  public static List<SSSpaceE> get(final List<String> strings) throws SSErr {

    final List<SSSpaceE> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static Boolean isShared(
    final SSSpaceE space){
    
    return SSStrU.equals(space, SSSpaceE.sharedSpace);
  }
  
  public static Boolean isPrivate(
    final SSSpaceE space) {
    
    return SSStrU.equals(space, SSSpaceE.privateSpace);
  }
  
  public static Boolean isPrivateOrShared(
    final SSSpaceE space){
    
    return isPrivate(space) || isShared(space);
  }
  
  public static void addDistinctWithoutNull(
    final List<SSSpaceE>  entities,
    final List<SSSpaceE>  toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSSpaceE entity : toAddEntities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(entities, entity)){
        entities.add(entity);
      }
    }
  }
  
  public static List<SSSpaceE> asListWithoutNull(final SSSpaceE... spaces){
   
    final List<SSSpaceE> result = new ArrayList<>();
    
    if(spaces == null){
      return result;
    }
    
    for(SSSpaceE space : spaces){
      
      if(space == null){
        continue;
      }
      
      result.add(space);
    }
    
    return result;
  }
}