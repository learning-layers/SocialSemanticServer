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
package at.kc.tugraz.ss.service.search.impl.fct;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSSearchFct {

  public static List<SSEntity> selectSearchResultsWithRegardToSearchOp(
    final String                      searchOp,
    final Map<String, List<SSEntity>> searchResultsPerKeyword) throws Exception{
    
    final List<SSEntity>      searchResults               = new ArrayList<>();
    final List<SSUri>         checkEntityUris             = new ArrayList<>();
    Boolean                   resourceExistsForEachTag;
    
    if(SSStrU.equals(searchOp.toLowerCase(), SSStrU.valueAnd)) {
      
      for(List<SSEntity> outerSearchResultForOneKeyword : searchResultsPerKeyword.values()) {
        
        for(SSEntity outerSearchResult : outerSearchResultForOneKeyword) {
          
          if(!SSStrU.contains(checkEntityUris, outerSearchResult.id)){
            
            checkEntityUris.add(outerSearchResult.id);
            
            resourceExistsForEachTag = true;
            
            for(List<SSEntity> innerSearchResultForOneKeyword : searchResultsPerKeyword.values()) {
              
              for(SSEntity innerSearchResult : innerSearchResultForOneKeyword) {
                
                if(!SSStrU.equals(innerSearchResult.id, outerSearchResult.id)) {
                  resourceExistsForEachTag = false;
                  break;
                }
              }
            }
            
            if(resourceExistsForEachTag) {
              searchResults.add(outerSearchResult);
            }
          }
        }
      }
    }
    
    if(SSStrU.equals(searchOp.toLowerCase(), SSStrU.valueOr)) {
      
      for(List<SSEntity> searchResultsForOneKeyword : searchResultsPerKeyword.values()) {
        searchResults.addAll(searchResultsForOneKeyword);
      }
      
      SSStrU.distinctWithoutEmptyAndNull(searchResults);
    }
    
    return searchResults;
  }
  
  public static Boolean handleType(
    final SSSearchPar par, 
    final SSEntity    entity){
    
    if(
      !par.typesToSearchOnlyFor.isEmpty() &&
      !SSStrU.contains(par.typesToSearchOnlyFor, entity.type)){
      return false;
    }
    
    return true;
  }
  
  public static SSEntity handleAccess(
    final SSSearchPar par, 
    final SSUri                entityID) throws Exception{
    
    try{
      
      return SSServCaller.entityUserCanRead(
        par.user,
        entityID);
      
    }catch(Exception error){
      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
        SSServErrReg.reset();
        return null;
      }
      
      throw error;
    }
  }

  public static List<SSEntity> recommendEntities(
    final SSSearchPar par) throws Exception{
    
    final List<SSEntity> result = new ArrayList<>();
    
    if(!par.includeRecommendedResults){
      return result;
    }
    
    try{
      
      result.addAll(
        SSServCaller.recommResources(
          par.user,
          par.user,
          null,
          new ArrayList<>(),
          10,
          par.typesToSearchOnlyFor,
          false,
          true).keySet());
    }catch(Exception error){
      SSLogU.warn("reomm entities for search failed");
      SSServErrReg.reset();
    }
    
    return result;
  }

  public static Integer addRecommendedResult(
    final List<SSEntity> page,
    final List<SSUri>    uris,
    final SSSearchPar    par,
    final List<SSEntity> recommendedEntities,
    Integer              recommendedEntityCounter) throws Exception{
    
    if(
      !par.includeRecommendedResults                         ||
      recommendedEntities.isEmpty()                          ||
      recommendedEntityCounter >= recommendedEntities.size() ||
      page.size()              == 10 ||
      page.size() %2           != 0){
      
      return recommendedEntityCounter;
    }
      
    while(recommendedEntityCounter < recommendedEntities.size()){
      
      if(
        SSStrU.contains(uris, recommendedEntities.get(recommendedEntityCounter).id) || 
        !handleRating  (par,  recommendedEntities.get(recommendedEntityCounter))){
        
        recommendedEntityCounter++;
        continue;
      }
      
      page.add(recommendedEntities.get(recommendedEntityCounter));
      recommendedEntityCounter++;
      break;
    }
    
    return recommendedEntityCounter;
  }

  public static void fillPagesIfEmpty(
    final SSSearchPar          par, 
    final List<List<SSEntity>> pages, 
    final List<SSEntity>       recommendedEntities){
    
    if(
      !pages.isEmpty() ||
      !par.includeRecommendedResults ||
      recommendedEntities.isEmpty()){
      return;
    }
    
    if(recommendedEntities.size() < 10){
      pages.add(new ArrayList<>(recommendedEntities.subList(0, recommendedEntities.size() - 1)));
    }else{
      pages.add(new ArrayList<>(recommendedEntities.subList(0, 9)));
    }
  }

  public static boolean handleRating(
    final SSSearchPar par,
    final SSEntity    entity) throws Exception{
    
    if(
      par.minRating == null && 
      par.maxRating == null){
      return false;
    }
    
    try{
      
      final SSRatingOverall rating = SSServCaller.ratingOverallGet(par.user, entity.id);
       
      if(
        par.minRating != null &&
        par.minRating > rating.score){
        return false;
      }
      
      if(
        par.maxRating != null &&
        par.maxRating < rating.score){
        return false;
      }
      
      return true;
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return true;
  }
}