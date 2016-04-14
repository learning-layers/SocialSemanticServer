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

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSObjU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
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
    }catch(IllegalArgumentException error){
      SSServErrReg.regErrThrow(SSErrE.spaceInvalid, error);
      return null;
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
  
  public static boolean isShared(
    final SSSpaceE space){
    
    return SSStrU.isEqual(space, SSSpaceE.sharedSpace);
  }
  
  public static boolean isPrivate(
    final SSSpaceE space) {
    
    return SSStrU.isEqual(space, SSSpaceE.privateSpace);
  }
  
  public static boolean isPrivateOrShared(
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