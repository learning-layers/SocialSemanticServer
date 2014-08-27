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
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchSolrPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchTagsWithinEntityPar;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import at.kc.tugraz.ss.service.search.impl.fct.SSSearchFct;
import at.kc.tugraz.ss.service.search.impl.fct.misc.SSSearchMiscFct;
import java.util.*;

public class SSSearchImpl extends SSServImplMiscA implements SSSearchClientI, SSSearchServerI{
  
  public SSSearchImpl(final SSConfA conf) throws Exception{
    super(conf);
  }
  
  @Deprecated
  @Override
  public List<SSSearchResult> searchTags(final SSServPar parA) throws Exception {
    return searchTags(new SSSearchTagsPar(parA));
  }
  
  @Deprecated
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
            null,
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
            null,
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
  
  @Deprecated
  @Override
  public List<SSSearchResult> searchMIs(final SSServPar parA) throws Exception {
    return searchMIs(new SSSearchMIsPar(parA));
  }
  
  @Deprecated
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

  @Deprecated
  @Override
  public List<SSSearchResult> searchSolr(final SSServPar parA) throws Exception {
    return searchSolr(new SSSearchSolrPar(parA));
  }
  
  @Deprecated
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
  
  @Deprecated
  @Override
  public List<SSSearchResult> searchTagsWithinEntity(final SSServPar parA) throws Exception{
    return searchTagsWithinEntity(new SSSearchTagsWithinEntityPar(parA));
  }
  
  @Deprecated
  protected List<SSSearchResult> searchTagsWithinEntity(final SSSearchTagsWithinEntityPar par) throws Exception{
    
    try{
      
      final Map<String, List<SSSearchResult>> searchResultsPerTag        = new HashMap<>();
      final List<SSSearchResult>              searchResultsForOneKeyword = new ArrayList<>();
      
      for(String tag : par.tags){
        
        searchResultsForOneKeyword.clear();
        
        for(SSUri entityUri : SSSearchMiscFct.getSubEntities(par.user, SSUri.asListWithoutNullAndEmpty(par.entity))){
          
          if(SSServCaller.tagsUserGet(par.user, null, SSUri.asListWithoutNullAndEmpty(entityUri), SSStrU.toStrWithoutEmptyAndNull(tag), null, null).isEmpty()){
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
  
  @Deprecated
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
        
//        searchResults.addAll(
//          SSSearchMiscFct.filterSearchResultsForSubEntitySearch(
//            searchMIs(
//              SSSearchMIsPar.get(
//                par.keywords,
//                SSStrU.valueOr)),
//            par.onlySubEntities,
//            subEntities));
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
        
        try{
          SSServCaller.entityUserCanRead(par.user, searchResult.entity);
        }catch(Exception error){
          SSServErrReg.reset();
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
    
  @Override
  public void search(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSSearchRet.get(search(parA), parA.op));
  }
  
  @Override
  public List<SSEntity> search(final SSServPar parA) throws Exception{
    
    try{
      final SSSearchPar             par           = new SSSearchPar(parA);
      final List<SSEntity>          results       = new ArrayList<>();
      final List<SSUri>             uris          = new ArrayList<>();
      SSEntity                      entity;
      
      uris.addAll (getTagResults(par));
      uris.addAll (getMIResults(par));
      uris.addAll (getTextualContentResults(par));
      uris.addAll (getLabelAndDescriptionResults(par));
      
      SSStrU.distinctWithoutNull2(uris);
      
      for(SSUri result : extendToParentEntities(par, filterForSubEntities(par, uris))){
        
        entity = SSServCaller.entityGet(result);
        
        if(
          !par.typesToSearchOnlyFor.isEmpty() &&
          !SSStrU.contains(par.typesToSearchOnlyFor, entity.type)){
          continue;
        }

        try{
          SSServCaller.entityUserCanRead(par.user, result);
        }catch(Exception error){
          SSServErrReg.reset();
          continue;
        }
        
        results.add(entity);
        
        entity.circleTypes = SSServCaller.entityUserEntityCircleTypesGet(par.user, result);
        entity.entries.addAll(SSStrU.removeTrailingSlash(getEntries(par, entity)));
      }
      
//      par.includeRecommendedResults;
      
      return results;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSUri> getEntries(
    final SSSearchPar par, 
    final SSEntity    entity) throws Exception{
    
    final List<SSUri> entries = new ArrayList<>();
    
    if(!par.provideEntries){
      return entries;
    }
          
    switch(entity.type){
      case chat:
      case disc:
      case qa:
        entries.addAll(SSServCaller.discEntryURIsGet(par.user, entity.id));
        break;
    }
    
    return entries;
  }
  
  private List<SSUri> getMIResults(final SSSearchPar par) throws Exception{
    return new ArrayList<>();
  }
  
  private List<SSUri> getTagResults(final SSSearchPar par) throws Exception{
    
    final List<SSUri> tagResults = new ArrayList<>();
    SSSearchTagsPar   searchTagsPar;
      
    if(!par.includeTags){
      return tagResults;
    }
    
    if(!par.tagsToSearchFor.isEmpty()){
      
      searchTagsPar =
        SSSearchTagsPar.get(
          par.user,
          par.tagsToSearchFor,
          SSStrU.valueOr,
          10);
      
      for(SSSearchResult result : searchTags(searchTagsPar)){
        tagResults.add(result.entity);
      }
    }
    
    if(!par.keywordsToSearchFor.isEmpty()){
      
      searchTagsPar =
        SSSearchTagsPar.get(
          par.user,
          par.keywordsToSearchFor,
          SSStrU.valueOr,
          10);
      
      for(SSSearchResult result : searchTags(searchTagsPar)){
        tagResults.add(result.entity);
      }
    }

    return tagResults;
  }
  
  private List<SSUri> getTextualContentResults(final SSSearchPar par) throws Exception{
    
    final List<SSUri> textualContentResults = new ArrayList<>();
    SSSearchSolrPar   searchSolrPar;
    
    if(!par.includeTextualContent){
      return textualContentResults;
    }
    
    if(!par.wordsToSearchFor.isEmpty()){
      
      searchSolrPar =
        SSSearchSolrPar.get(
          par.user,
          par.wordsToSearchFor,
          SSStrU.valueOr);
      
      for(SSSearchResult solrResult : searchSolr(searchSolrPar)){
        textualContentResults.add(solrResult.entity);
      }
    }
    
    if(!par.keywordsToSearchFor.isEmpty()){
      
      searchSolrPar =
        SSSearchSolrPar.get(
          par.user,
          par.keywordsToSearchFor,
          SSStrU.valueOr);
      
      for(SSSearchResult solrResult : searchSolr(searchSolrPar)){
        textualContentResults.add(solrResult.entity);
      }    
    }
    
    return textualContentResults;
  }
  
  private List<SSUri> getLabelAndDescriptionResults(final SSSearchPar par) throws Exception{
    
    final List<SSUri>         results  = new ArrayList<>();
    final List<String>        keywords = SSStrU.toStr(SSSearchLabel.get(par.keywordsToSearchFor));
    
    if(
      par.includeDescription &&
      par.includeLabel){
      
      if(!keywords.isEmpty()){
        
        for(SSEntity entity : SSServCaller.entitiesForLabelsAndDescriptionsGet(keywords)){
          results.add(entity.id);
        }
      }
      
      if(!par.labelsToSearchFor.isEmpty()){
        
        for(SSEntity entity : SSServCaller.entitiesForLabelsAndDescriptionsGet(SSStrU.toStrWithoutEmptyAndNull(par.labelsToSearchFor))){
          results.add(entity.id);
        }
      }
      
      if(!par.descriptionsToSearchFor.isEmpty()){
        
        for(SSEntity entity : SSServCaller.entitiesForLabelsAndDescriptionsGet(SSStrU.toStrWithoutEmptyAndNull(par.descriptionsToSearchFor))){
          results.add(entity.id);
        }
      }
    }
    
    if(
      par.includeDescription &&
      !par.includeLabel){
      
      if(!keywords.isEmpty()){
       
        for(SSEntity entity : SSServCaller.entitiesForDescriptionsGet(keywords)){
          results.add(entity.id);
        }
      }
      
      if(!par.descriptionsToSearchFor.isEmpty()){
        
        for(SSEntity entity : SSServCaller.entitiesForDescriptionsGet(SSStrU.toStrWithoutEmptyAndNull(par.descriptionsToSearchFor))){
          results.add(entity.id);
        }
      }
    }
    
    if(
      !par.includeDescription &&
      par.includeLabel){
      
      if(!keywords.isEmpty()){
       
        for(SSEntity entity : SSServCaller.entitiesForLabelsGet(keywords)){
          results.add(entity.id);
        }
      }
      
      if(!par.labelsToSearchFor.isEmpty()){
        
        for(SSEntity entity : SSServCaller.entitiesForLabelsGet(SSStrU.toStrWithoutEmptyAndNull(par.labelsToSearchFor))){
          results.add(entity.id);
        }
      }
    }
    
    return results;
  }
    
  private List<SSUri> filterForSubEntities(
    final SSSearchPar    par,
    final List<SSUri>    results) throws Exception{
    
    if(
      !par.includeOnlySubEntities ||
      par.entitiesToSearchWithin.isEmpty()){

      return results;
    }
    
    return
      SSUri.get(
        SSStrU.retainAll(
          SSStrU.toStrWithoutEmptyAndNull(
            results),
          SSStrU.toStrWithoutEmptyAndNull(
            SSSearchMiscFct.getSubEntities(
              par.user,
              par.entitiesToSearchWithin))));
  }
  
  private List<SSUri> extendToParentEntities(
    final SSSearchPar par,
    final List<SSUri> results) throws Exception{
    
    if(!par.extendToParents){
      return results;
    }
    
    try{
    
      final List<SSUri> parentEntities  = new ArrayList<>();
      
      for(SSUri entity : results){
        
        try{
          parentEntities.addAll(SSServCaller.entityUserParentEntitiesGet(par.user, entity));
        }catch(Exception error){
          SSServErrReg.reset();
        }
      }
      
      results.addAll(parentEntities);
      
      SSStrU.distinctWithoutNull2(results);
      
      return results;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}