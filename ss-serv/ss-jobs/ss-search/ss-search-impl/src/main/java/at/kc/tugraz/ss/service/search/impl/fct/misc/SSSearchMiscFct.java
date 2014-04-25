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

import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircle;
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
      
      final List<SSUri> privateAddedUris = new ArrayList<SSUri>();
      final List<SSUri> publicAddedUris  = new ArrayList<SSUri>();
      
      for(SSCircle circle : SSServCaller.entityUserEntityCirclesGet(userUri, searchResultUri)){
        
        switch(circle.circleType){
          case priv:{
            
            if(!SSUri.contains(privateAddedUris, searchResultUri)){
              privateAddedUris.add(searchResultUri);
            }
            
            break;
          }
          
          case pub:
          default:{
            
            if(!SSUri.contains(publicAddedUris, searchResultUri)){
              publicAddedUris.add(searchResultUri);
            }
          }
        }
      }
      
      for(SSUri entityUri : privateAddedUris){
        searchResultsForOneKeyword.add(new SSSearchResult(entityUri, SSSpaceEnum.privateSpace));
      }
      
      for(SSUri entityUri : publicAddedUris){
        searchResultsForOneKeyword.add(new SSSearchResult(entityUri, SSSpaceEnum.sharedSpace));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
