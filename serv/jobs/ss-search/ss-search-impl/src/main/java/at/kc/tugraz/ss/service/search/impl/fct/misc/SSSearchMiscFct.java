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
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchResult;
import java.util.ArrayList;
import java.util.List;

public class SSSearchMiscFct{
 
  public static void getPublicAndPrivateResults(
    final SSUri                userUri,
    final SSUri                searchResultUri,
    final List<SSSearchResult> searchResultsForOneKeyword) throws Exception{
    
    try{
      
      final List<SSUri> privateAddedUris = new ArrayList<>();
      final List<SSUri> publicAddedUris  = new ArrayList<>();
      
      for(SSEntityCircle circle : SSServCaller.entityUserEntityCirclesGet(userUri, searchResultUri, true)){
        
        switch(circle.type){
          case priv:{
            
            if(!SSStrU.contains(publicAddedUris, searchResultUri)){
              SSUri.addDistinctWithoutNull(privateAddedUris, searchResultUri);
            }

            break;
          }
          
          case pub:
          default:{
            
            SSStrU.remove(privateAddedUris, searchResultUri);
            SSUri.addDistinctWithoutNull(publicAddedUris,  searchResultUri);
          }
        }
      }
      
      for(SSUri entityUri : privateAddedUris){
        searchResultsForOneKeyword.add(SSSearchResult.get(entityUri, SSSpaceE.privateSpace));
      }
      
      for(SSUri entityUri : publicAddedUris){
        searchResultsForOneKeyword.add(SSSearchResult.get(entityUri, SSSpaceE.sharedSpace));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
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

  public static List<SSSearchResult> filterSearchResultsForSubEntitySearch(
    final List<SSSearchResult> searchResults,
    final Boolean              onlySubEntities,
    final List<SSUri>          subEntities) throws Exception{
    
    try{
      
      if(!onlySubEntities){
        return searchResults;
      }
      
      final List<SSSearchResult> filteredResults = new ArrayList<>();
      
      for(SSSearchResult mIResult : searchResults){
        
        if(!SSStrU.contains(subEntities, mIResult.entity)){
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

  public static List<SSSearchResult> searchForLabelAndDescription(
    final List<String> keywords,
    final Boolean      includeDescription,
    final Boolean      includeLabel) throws Exception{
    
    try{
      final List<SSSearchResult> labelAndDescSearchResults = new ArrayList<>();
      
      if(
        includeDescription &&
        includeLabel){
        
        labelAndDescSearchResults.addAll(SSSearchResult.get(SSServCaller.entitiesForLabelsAndDescriptionsGet(keywords)));
      }
      
      if(
        includeDescription &&
        !includeLabel){
        
        labelAndDescSearchResults.addAll(SSSearchResult.get(SSServCaller.entitiesForDescriptionsGet(keywords)));
      }
      
      if(
        !includeDescription &&
        includeLabel){
        
        labelAndDescSearchResults.addAll(SSSearchResult.get(SSServCaller.entitiesForLabelsGet(keywords)));
      }
      
      return labelAndDescSearchResults;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}