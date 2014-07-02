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
package at.kc.tugraz.ss.service.search.impl;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchTagsPar;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.search.api.*;
import at.kc.tugraz.ss.service.search.datatypes.*;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchCombinedPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchMIsPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchSolrPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchTagsWithinEntityPar;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchCombinedRet;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchMIsRet;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchSolrRet;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchTagsRet;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchTagsWithinEntityRet;
import at.kc.tugraz.ss.service.search.impl.fct.SSSearchFct;
import at.kc.tugraz.ss.service.search.impl.fct.misc.SSSearchMiscFct;
import java.util.*;

public class SSSearchImpl extends SSServImplMiscA implements SSSearchClientI, SSSearchServerI{
  
  public SSSearchImpl(final SSConfA conf) throws Exception{
    super(conf);
  }
  
  /* SSSearchClientI */
  
  @Override
  public void searchCombined(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSSearchCombinedRet.get(searchCombined(parA), parA.op));
  }
  
  @Override
  public void searchTags(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSSearchTagsRet.get(searchTags(parA), parA.op));
  }
  
  @Override
  public void searchMIs(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSSearchMIsRet.get(searchMIs(parA), parA.op));
  }
  
  @Override
  public void searchSolr(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSSearchSolrRet.get(searchSolr(parA), parA.op));
  }
  
  @Override
  public void searchTagsWithinEntity(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSSearchTagsWithinEntityRet.get(searchTagsWithinEntity(parA), parA.op));
  }
  
  /* SSSearchServerI */
  
  @Override
  public List<SSSearchResult> searchTags(final SSServPar parA) throws Exception {
    return searchTags(new SSSearchTagsPar(parA));
  }
  
  @Override
  public List<SSSearchResult> searchSolr(final SSServPar parA) throws Exception {
    return searchSolr(new SSSearchSolrPar(parA));
  }
  
  @Override
  public List<SSSearchResult> searchMIs(final SSServPar parA) throws Exception {
    return searchMIs(new SSSearchMIsPar(parA));
  }
  
  @Override
  public List<SSSearchResult> searchTagsWithinEntity(final SSServPar parA) throws Exception{
    return searchTagsWithinEntity(new SSSearchTagsWithinEntityPar(parA));
  }
  
  @Override
  public List<SSSearchResult> searchCombined(final SSServPar parA) throws Exception {
    return searchCombined(new SSSearchCombinedPar(parA));
  }
  
  /* protected */

  protected List<SSSearchResult> searchTags(final SSSearchTagsPar par) throws Exception {
    
    try{
      
      final Map<String, List<SSSearchResult>> searchResultsPerTag = new HashMap<>();
      final List<SSSearchResult>              result;
      List<SSSearchResult>                    searchResultsForTagOneTag;
      
      for(String tagLabel : par.tags){
        
        searchResultsForTagOneTag = new ArrayList<>();
        
        for(SSUri foundEntity :
          
          SSServCaller.tagUserEntitiesForTagsGet(
            par.user,
            SSStrU.toStrWithoutEmptyAndNull(tagLabel),
            SSSpaceE.sharedSpace,
            null)){
          
          searchResultsForTagOneTag.add(
            SSSearchResult.get(
              foundEntity,
              SSSpaceE.sharedSpace));
        }
        
        for(SSUri foundEntity :
          
          SSServCaller.tagUserEntitiesForTagsGet(
            par.user,
            SSStrU.toStrWithoutEmptyAndNull(tagLabel),
            SSSpaceE.privateSpace,
            null)){
          
          searchResultsForTagOneTag.add(
            SSSearchResult.get(
              foundEntity,
              SSSpaceE.privateSpace));
        }
        
        searchResultsPerTag.put(tagLabel, searchResultsForTagOneTag);
      }
      
      result = SSSearchFct.selectAndFillSearchResults(par.user, par.searchOp, searchResultsPerTag);
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  protected List<SSSearchResult> searchSolr(final SSSearchSolrPar par) throws Exception{

    try{
      
      final Map<String, List<SSSearchResult>> searchResultsPerKeyword    = new HashMap<>();
      final List<SSSearchResult>              searchResultsForOneKeyword = new ArrayList<>();
      
      for(String keyword : par.keywords){
        
        searchResultsForOneKeyword.clear();
        
        for(String entityId : SSServCaller.solrSearch(keyword, 20)){

          SSSearchMiscFct.getPublicAndPrivateResults(
            par.user, 
            SSServCaller.fileUriFromID(par.user, entityId),
            searchResultsForOneKeyword);
        }

        searchResultsPerKeyword.put(keyword, new ArrayList<>(searchResultsForOneKeyword));
      }
      
      return SSSearchFct.selectAndFillSearchResults(par.user, par.searchOp, searchResultsPerKeyword);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  protected List<SSSearchResult> searchMIs(final SSSearchMIsPar par) throws Exception{
    
    try{
      
      final Map<String, List<SSSearchResult>> searchResultsPerKeyword    = new HashMap<>();
      final List<SSSearchResult>              searchResultsForOneKeyword = new ArrayList<>();
     
      for(String mi : par.mIs){
        
        searchResultsForOneKeyword.clear();
        
        for(SSUri entityUri : SSServCaller.modelUEEntitiesForMiGet(par.user, mi)){
          
          SSSearchMiscFct.getPublicAndPrivateResults(
            par.user,
            entityUri,
            searchResultsForOneKeyword);
        }
        
        searchResultsPerKeyword.put(mi, new ArrayList<>(searchResultsForOneKeyword));
      }
      
      return SSSearchFct.selectAndFillSearchResults(par.user, par.searchOp, searchResultsPerKeyword);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  protected List<SSSearchResult> searchTagsWithinEntity(final SSSearchTagsWithinEntityPar par) throws Exception{
    
    try{
      
      final Map<String, List<SSSearchResult>> searchResultsPerTag        = new HashMap<>();
      final List<SSSearchResult>              searchResultsForOneKeyword = new ArrayList<>();
      
      for(String tag : par.tags){
        
        searchResultsForOneKeyword.clear();
        
        for(SSUri entityUri : SSSearchMiscFct.getSubEntities(par.user, SSUri.asListWithoutNullAndEmpty(par.entity))){
          
          if(SSServCaller.tagsUserGet(par.user, SSUri.asListWithoutNullAndEmpty(entityUri), SSStrU.toStrWithoutEmptyAndNull(tag), null, null).isEmpty()){
            continue;
          }
          
          SSSearchMiscFct.getPublicAndPrivateResults(
            par.user,
            entityUri,
            searchResultsForOneKeyword);
        }
        
        searchResultsPerTag.put(tag, new ArrayList<>(searchResultsForOneKeyword));
      }
      
      return SSSearchFct.selectAndFillSearchResults(par.user, SSStrU.valueOr, searchResultsPerTag);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  protected List<SSSearchResult> searchCombined(final SSSearchCombinedPar par) throws Exception{
    
    try{
      final List<SSSearchResult> searchResults        = new ArrayList<>();
      final List<SSSearchResult> searchResultsForUser = new ArrayList<>();
      final List<SSUri>          subEntities          = new ArrayList<>();
      SSEntity                   entityToCheckType;
      
      if(par.onlySubEntities){
      
        subEntities.addAll(
          SSSearchMiscFct.getSubEntities(
            par.user, 
            par.entities));

        if(par.includeTags){
      
          for(SSUri entity : subEntities){
            
            searchResults.addAll(
              searchTagsWithinEntity(
                SSSearchTagsWithinEntityPar.get(
                  par.user,
                  entity,
                  par.keywords)));
          }
        }
      }
      
      if(
        par.includeTags &&
        !par.onlySubEntities){
        
        searchResults.addAll(
          searchTags(
            SSSearchTagsPar.get(
              par.user,
              par.keywords,
              SSStrU.valueOr,
              10)));
      }
      
      if(par.includeTextualContent){
        
        searchResults.addAll(
          SSSearchMiscFct.filterSearchResultsForSubEntitySearch(
            searchSolr(
              SSSearchSolrPar.get(
                par.user,
                par.keywords,
                SSStrU.valueOr)),
            par.onlySubEntities,
            subEntities));
      }
      
      if(par.includeMIs){
        
        searchResults.addAll(
          SSSearchMiscFct.filterSearchResultsForSubEntitySearch(
            searchMIs(
              SSSearchMIsPar.get(
                par.keywords,
                SSStrU.valueOr)),
            par.onlySubEntities,
            subEntities));
      }
      
      searchResults.addAll(
        SSSearchMiscFct.filterSearchResultsForSubEntitySearch(
          SSSearchMiscFct.searchForLabelAndDescription(
            par.keywords,
            par.includeDescription,
            par.includeLabel),
          par.onlySubEntities,
          subEntities));
      
      SSStrU.distinctWithoutEmptyAndNull2(searchResults);
      
      for(SSSearchResult searchResult : searchResults){
        
        if(!SSServCaller.entityUserCanRead(par.user, searchResult.entity)){
          continue;
        }
        
        if(!par.types.isEmpty()){
          
          entityToCheckType = SSServCaller.entityGet(searchResult.entity);
        
          if(!SSStrU.contains(par.types, entityToCheckType.type)){
            continue;
          }
        }

        searchResultsForUser.add(searchResult);
      }
      
      return searchResultsForUser;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}