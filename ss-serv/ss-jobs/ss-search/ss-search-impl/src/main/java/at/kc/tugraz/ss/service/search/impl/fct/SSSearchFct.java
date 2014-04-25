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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSSearchFct {

  public static List<SSSearchResult> selectAndFillSearchResults(
    final SSUri                             userUri, 
    final String                            searchOp,
    final Map<String, List<SSSearchResult>> searchResultsPerKeyword) throws Exception{
  
    final List<SSSearchResult> searchResults = selectSearchResultsWithRegardToSearchOp(searchOp, searchResultsPerKeyword);
    
    for(SSSearchResult searchResult : searchResults){
      searchResult.type          = SSServCaller.entityTypeGet                 (searchResult.uri);
      searchResult.label         = SSStrU.toString(SSServCaller.entityLabelGet(searchResult.uri));
    }
    
    return searchResults;
  }
  
  private static List<SSSearchResult> selectSearchResultsWithRegardToSearchOp(
    final String                            searchOp,
    final Map<String, List<SSSearchResult>> searchResultsPerKeyword){
    
    final List<SSSearchResult>      searchResults               = new ArrayList<SSSearchResult>();
    final List<SSUri>               checkEntityUris             = new ArrayList<SSUri>();
    Boolean                         resourceExistsForEachTag;
    
    if(SSStrU.equals(searchOp.toLowerCase(), SSStrU.valueAnd)) {
      
      for(List<SSSearchResult> outerSearchResultForOneKeyword : searchResultsPerKeyword.values()) {
        
        for(SSSearchResult outerSearchResult : outerSearchResultForOneKeyword) {
          
          if(SSUri.containsNot(checkEntityUris, outerSearchResult.uri)){
            
            checkEntityUris.add(outerSearchResult.uri);
            
            resourceExistsForEachTag = true;
            
            for(List<SSSearchResult> innerSearchResultForOneKeyword : searchResultsPerKeyword.values()) {
              
              for(SSSearchResult innerSearchResult : innerSearchResultForOneKeyword) {
                
                if(!SSUri.isSame(innerSearchResult.uri, outerSearchResult.uri)) {
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
      
      for(List<SSSearchResult> searchResultsForOneKeyword : searchResultsPerKeyword.values()) {
        searchResults.addAll(searchResultsForOneKeyword);
      }
    }
    
    return searchResults;
  }
}
