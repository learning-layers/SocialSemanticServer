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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSSearchOpE;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSSearchFct {
  
  public static List<SSEntity> selectSearchResultsWithRegardToSearchOp(
    final SSSearchOpE                 searchOp,
    final Map<String, List<SSEntity>> searchResultsPerKeyword) throws Exception{
    
    final List<SSEntity>      searchResults               = new ArrayList<>();
    final List<SSUri>         checkEntityUris             = new ArrayList<>();
    Boolean                   resourceExistsForEachTag;
    
    switch(searchOp){
      
      case and:{
        for(List<SSEntity> outerSearchResultsForOneKeyword : searchResultsPerKeyword.values()) {
          
          for(SSEntity outerSearchResult : outerSearchResultsForOneKeyword){
            
            if(SSStrU.contains(checkEntityUris, outerSearchResult)){
              continue;
            }
            
            checkEntityUris.add(outerSearchResult.id);
            
            resourceExistsForEachTag = true;
            
            for(List<SSEntity> innerSearchResultsForOneKeyword : searchResultsPerKeyword.values()) {
              
              if(!SSStrU.contains(innerSearchResultsForOneKeyword, outerSearchResult)){
                resourceExistsForEachTag = false;
                break;
              }
            }
            
            if(resourceExistsForEachTag) {
              searchResults.add(outerSearchResult);
            }
          }
        }
        
        break;
      }
      
      case or:{
        for(List<SSEntity> searchResultsForOneKeyword : searchResultsPerKeyword.values()) {
          searchResults.addAll(searchResultsForOneKeyword);
        }
        
        SSStrU.distinctWithoutEmptyAndNull(searchResults);
        
        break;
      }
    }
    
    return searchResults;
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
      
      if(SSStrU.contains(uris, recommendedEntities.get(recommendedEntityCounter).id)){
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
}