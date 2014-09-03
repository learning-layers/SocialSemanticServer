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
package at.kc.tugraz.ss.service.search.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;

public class SSSearchMiscFct{
 
  public static List<SSUri> getSubEntities(
    final SSUri       user, 
    final List<SSUri> entities) throws Exception{
    
    try{
    
      final List<SSUri> subEntities  = new ArrayList<>();
      
      for(SSUri entity : entities){
        subEntities.addAll(SSServCaller.entityUserSubEntitiesGet(user, entity));
      }
      
      SSStrU.distinctWithoutNull2(subEntities);
      
      return subEntities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static List<SSEntity> filterSearchResultsForSubEntitySearch(
    final List<SSEntity> searchResults,
    final Boolean        onlySubEntities,
    final List<SSUri>    subEntities) throws Exception{
    
    try{
      
      if(!onlySubEntities){
        return searchResults;
      }
      
      final List<SSEntity> filteredResults = new ArrayList<>();
      
      for(SSEntity mIResult : searchResults){
        
        if(!SSStrU.contains(subEntities, mIResult.id)){
          continue;
        }
        
        filteredResults.add(mIResult);
      }
      
      return filteredResults;
    
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static List<SSEntity> searchForLabelAndDescription(
    final List<String> keywords,
    final Boolean      includeDescription,
    final Boolean      includeLabel) throws Exception{
    
    try{
      final List<SSEntity> labelAndDescSearchResults = new ArrayList<>();
      
      if(
        includeDescription &&
        includeLabel){
        
        labelAndDescSearchResults.addAll(SSServCaller.entitiesForLabelsAndDescriptionsGet(keywords));
      }
      
      if(
        includeDescription &&
        !includeLabel){
        
        labelAndDescSearchResults.addAll(SSServCaller.entitiesForDescriptionsGet(keywords));
      }
      
      if(
        !includeDescription &&
        includeLabel){
        
        labelAndDescSearchResults.addAll(SSServCaller.entitiesForLabelsGet(keywords));
      }
      
      return labelAndDescSearchResults;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}