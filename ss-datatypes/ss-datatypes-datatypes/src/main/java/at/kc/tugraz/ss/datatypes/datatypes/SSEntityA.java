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
package at.kc.tugraz.ss.datatypes.datatypes;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SSEntityA implements SSJSONLDPropI{
  
  protected String val = null;

  @Override 
  public String toString(){
    return val;
  }
  
  protected SSEntityA(final String value){
    this.val = value;
  }
  
  protected SSEntityA(final SSEntityA entity){
    
    if(SSStrU.isNotEmpty(SSStrU.toString(entity))){
     this.val = entity.toString();
    }
  }
  
  protected SSEntityA(int value){
    
    if(SSStrU.isNotEmpty(SSStrU.toString(value))){
     this.val = SSStrU.toString(value);
    }
  }
  
//  public static List<SSEntityA> distinctForUri(List<? extends SSEntityA> entities) throws Exception{
//    
//    List<String>     foundEntities = new ArrayList<String>();
//    List<SSEntityA>  result        = new ArrayList<SSEntityA>();
//    
//    for (SSEntityA entity : entities){
//      
//      if(SSStrU.contains(foundEntities, entity.toString())) {
//        continue;
//      }
//      
//      result.add        (entity);
//      foundEntities.add (entity.toString());
//    }
//    
//    return result;
//  }
  
  public static Boolean contains(List<? extends SSEntityA> entities, SSEntityA entityToContain) {
    
    for(SSEntityA entity : entities){
      if(isSame(entity, entityToContain)){
        return true;
      }
    }
    
    return false;
  }
  
  public static Boolean containsNot(List<? extends SSEntityA> entities, SSEntityA entityToContain) {
    return !contains(entities, entityToContain);
  }
  
  public static void remove(List<? extends SSEntityA> entities, SSEntityA entityToRemove) {
    
    for(SSEntityA entity : entities) {
      
      if(isSame(entity, entityToRemove)){
        
        entities.remove (entity);
        remove          (entities, entityToRemove);
        return;
      }
    }
  }
  
  public static SSEntityA[] toArray(Collection<? extends SSEntityA> entities) {
    return (SSEntityA[]) entities.toArray(new SSEntityA[entities.size()]);
  }
  
  public static List<String> toDistinctStringArray(final List<? extends SSEntityA> entities){
    
    final List<String> result = new ArrayList<String>();
    
    for (SSEntityA entity : entities) {
      
      if(
        entity != null &&
        !result.contains(entity.toString())){
        
        result.add(entity.toString());
      }
    }
    
    return result;
  }
  
  public static Boolean isNotSame(final SSEntityA entity1, SSEntityA entity2){
    return !isSame(entity1, entity2);
  }
  
  public static Boolean isSame(final SSEntityA entity1, final SSEntityA entity2){
    
    if(SSObjU.isNull(entity1, entity2)){
      return false;
    }
    
    return entity1.toString().equals(entity2.toString());
  }
}
