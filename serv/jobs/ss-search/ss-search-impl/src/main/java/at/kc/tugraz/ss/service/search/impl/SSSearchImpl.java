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

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchTagsPar;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
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
import at.kc.tugraz.ss.service.search.impl.fct.activity.SSSearchActivityFct;
import at.kc.tugraz.ss.service.search.impl.fct.misc.SSSearchMiscFct;
import java.util.*;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSSearchImpl extends SSServImplMiscA implements SSSearchClientI, SSSearchServerI{
  
  protected static final Map<String, SSSearchResultPages> searchResultPagesCache = new HashMap<>();
  
  public SSSearchImpl(final SSConfA conf) throws Exception{
    super(conf);
  }
  
  @Deprecated
  @Override
  public List<SSEntity> searchTags(final SSServPar parA) throws Exception {
    return searchTags(new SSSearchTagsPar(parA));
  }
  
  @Deprecated
  protected List<SSEntity> searchTags(final SSSearchTagsPar par) throws Exception {
    
    try{
      
      final Map<String, List<SSEntity>> searchResultsPerTag = new HashMap<>();
      List<SSEntity>                    searchResultsForTagOneTag;
      
      for(String tagLabel : par.tags){
        
        searchResultsForTagOneTag = new ArrayList<>();
        
        for(SSUri foundEntity :
          
          SSServCaller.tagUserEntitiesForTagsGet(
            par.user,
            null,
            SSStrU.toStrWithoutEmptyAndNull(tagLabel),
            SSSpaceE.sharedSpace,
            null)){
          
          searchResultsForTagOneTag.add(SSServCaller.entityGet(foundEntity));
        }
        
        for(SSUri foundEntity :
          
          SSServCaller.tagUserEntitiesForTagsGet(
            par.user,
            null,
            SSStrU.toStrWithoutEmptyAndNull(tagLabel),
            SSSpaceE.privateSpace,
            null)){
          
          searchResultsForTagOneTag.add(SSServCaller.entityGet(foundEntity));
        }
        
        searchResultsPerTag.put(tagLabel, searchResultsForTagOneTag);
      }
      
      return SSSearchFct.selectSearchResultsWithRegardToSearchOp(
        par.searchOp, 
        searchResultsPerTag);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Deprecated
  @Override
  public List<SSEntity> searchMIs(final SSServPar parA) throws Exception {
    return searchMIs(new SSSearchMIsPar(parA));
  }
  
  @Deprecated
  protected List<SSEntity> searchMIs(final SSSearchMIsPar par) throws Exception{
    
    try{
      
      final Map<String, List<SSEntity>> searchResultsPerKeyword    = new HashMap<>();
      final List<SSEntity>              searchResultsForOneKeyword = new ArrayList<>();
      SSEntity                          entityObj;
     
      for(String mi : par.mIs){
        
        searchResultsForOneKeyword.clear();
        
        for(SSUri entityUri : SSServCaller.modelUEEntitiesForMiGet(par.user, mi)){
        
          entityObj = SSServCaller.entityGet(entityUri);
          
          entityObj.circleTypes.addAll(
            SSServCaller.entityUserEntityCircleTypesGet(
              par.user, 
              entityUri));
            
          searchResultsForOneKeyword.add(entityObj);
        }
        
        searchResultsPerKeyword.put(mi, new ArrayList<>(searchResultsForOneKeyword));
      }
      
      return SSSearchFct.selectSearchResultsWithRegardToSearchOp(
        par.searchOp,  
        searchResultsPerKeyword);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Deprecated
  @Override
  public List<SSEntity> searchSolr(final SSServPar parA) throws Exception {
    return searchSolr(new SSSearchSolrPar(parA));
  }
  
  @Deprecated
  protected List<SSEntity> searchSolr(final SSSearchSolrPar par) throws Exception{

    try{
      
      final Map<String, List<SSEntity>> searchResultsPerKeyword    = new HashMap<>();
      final List<SSEntity>              searchResultsForOneKeyword = new ArrayList<>();
      SSEntity                          entityObj;
      
      for(String keyword : par.keywords){
        
        searchResultsForOneKeyword.clear();
        
        for(String entityId : SSServCaller.solrSearch(keyword, 20)){
          
          entityObj =
            SSServCaller.entityGet(
              SSServCaller.fileUriFromID(
                par.user,
                entityId));
            
          entityObj.circleTypes.addAll(
            SSServCaller.entityUserEntityCircleTypesGet(
              par.user, 
              entityObj.id));
          
          searchResultsForOneKeyword.add(entityObj);
        }

        searchResultsPerKeyword.put(keyword, new ArrayList<>(searchResultsForOneKeyword));
      }
      
      return SSSearchFct.selectSearchResultsWithRegardToSearchOp(
        par.searchOp, 
        searchResultsPerKeyword);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Deprecated
  @Override
  public List<SSEntity> searchTagsWithinEntity(final SSServPar parA) throws Exception{
    return searchTagsWithinEntity(new SSSearchTagsWithinEntityPar(parA));
  }
  
  @Deprecated
  protected List<SSEntity> searchTagsWithinEntity(final SSSearchTagsWithinEntityPar par) throws Exception{
    
    try{
      
      final Map<String, List<SSEntity>> searchResultsPerTag        = new HashMap<>();
      final List<SSEntity>              searchResultsForOneKeyword = new ArrayList<>();
      SSEntity                          entityObj;
      
      for(String tag : par.tags){
        
        searchResultsForOneKeyword.clear();
        
        for(SSUri entityUri : SSSearchMiscFct.getSubEntities(par.user, SSUri.asListWithoutNullAndEmpty(par.entity))){
          
          if(SSServCaller.tagsUserGet(par.user, null, SSUri.asListWithoutNullAndEmpty(entityUri), SSStrU.toStrWithoutEmptyAndNull(tag), null, null).isEmpty()){
            continue;
          }
          
          entityObj = SSServCaller.entityGet(entityUri);
          
          entityObj.circleTypes.addAll(
            SSServCaller.entityUserEntityCircleTypesGet(
              par.user, 
              entityObj.id));
          
          searchResultsForOneKeyword.add(entityObj);
        }
        
        searchResultsPerTag.put(tag, new ArrayList<>(searchResultsForOneKeyword));
      }
      
      return SSSearchFct.selectSearchResultsWithRegardToSearchOp(
        SSStrU.valueOr, 
        searchResultsPerTag);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Deprecated
  protected List<SSEntity> searchCombined(final SSSearchCombinedPar par) throws Exception{
    
    try{
      final List<SSEntity> searchResults        = new ArrayList<>();
      final List<SSEntity> searchResultsForUser = new ArrayList<>();
      final List<SSUri>    subEntities          = new ArrayList<>();
      SSEntity             entityToCheckType;
      
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
      
      for(SSEntity searchResult : searchResults){
        
        try{
          SSServCaller.entityUserCanRead(par.user, searchResult.id);
        }catch(Exception error){
          SSServErrReg.reset();
          continue;
        }
        
        if(!par.types.isEmpty()){
          
          entityToCheckType = SSServCaller.entityGet(searchResult.id);
        
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
  public void searchResultPagesCacheClean(final SSServPar parA) throws Exception{
    
    try{
      
      final Long                             now            = SSDateU.dateAsLong();
      final Long                             fiveMinutesAgo = now - SSDateU.minuteInMilliSeconds * 5;
      Map.Entry<String, SSSearchResultPages> entry;
      
      for(Iterator<Map.Entry<String, SSSearchResultPages>> it = searchResultPagesCache.entrySet().iterator(); it.hasNext();){
        
        entry = it.next();
        
        if(fiveMinutesAgo > entry.getValue().creationTime){
          it.remove();
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void search(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(search(parA));
    
    SSSearchActivityFct.search(new SSSearchPar((parA)));
  }

  @Override
  public SSSearchRet search(final SSServPar parA) throws Exception{
    
    try{
      
      final SSSearchPar             par           = new SSSearchPar(parA);
      final List<SSEntity>          results       = new ArrayList<>();
      final List<SSUri>             uris          = new ArrayList<>();
      SSEntity                      entity;
      final List<List<SSEntity>>    pages;
      final List<SSEntity>          page;
      
      if(
        par.pagesID    != null &&
        par.pageNumber != null){
        
        return handleSearchPageRequest(parA.op, par);
      }
      
      uris.addAll (getTagResults(par));
      uris.addAll (getMIResults(par));
      uris.addAll (getTextualContentResults(par));
      uris.addAll (getLabelAndDescriptionResults(par));
      
      SSStrU.distinctWithoutNull2(uris);
      
      final String pagesID = SSIDU.uniqueID();
      
      pages = new ArrayList<>();
      page  = new ArrayList<>();
      
      for(SSUri result : extendToParentEntities(par, filterForSubEntities(par, uris))){
        
        try{
          entity = SSServCaller.entityGet(result);
        }catch(Exception error){
          SSServErrReg.reset();
          continue;
        }
        
        if(
          !par.typesToSearchOnlyFor.isEmpty() &&
          !SSStrU.contains(par.typesToSearchOnlyFor, entity.type)){
          continue;
        }

        switch(entity.type){
          case entity: break;
          default: {
            try{
              SSServCaller.entityUserCanRead(par.user, entity.id);
            }catch(Exception error){
              SSServErrReg.reset();
              continue;
            }
          }
        }
        
        if(page.size() == 10){
          pages.add(new ArrayList<>(page));
          page.clear();
        }
        
        page.add(entity);
      }
      
      if(!page.isEmpty()){
        pages.add(page);
      }
      
      if(!pages.isEmpty()){
        
        searchResultPagesCache.put(
          pagesID, 
          SSSearchResultPages.get(
            pages, 
            SSDateU.dateAsLong(), 
            pagesID));
        
        for(SSEntity result : pages.get(0)){
          results.add(fillSearchResult(par, result.id));
        }
      }

      return SSSearchRet.get(
        results, 
        pagesID,
        1,
        pages.size(), 
        parA.op);
      
//      par.includeRecommendedResults;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSEntity fillSearchResult(
    final SSSearchPar par,
    final SSUri       result) throws Exception{
    
    try{
      final SSEntity entity =
        SSServCaller.entityDescGet(
          par.user,
          result,
          true,
          true,
          false,
          false,
          false,
          false,
          false);
      
      entity.entries.addAll(
        SSStrU.removeTrailingSlash(
          getEntries(
            par,
            entity)));
      
      entity.circleTypes =
        SSServCaller.entityUserEntityCircleTypesGet(
          par.user,
          entity.id);
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSSearchRet handleSearchPageRequest(
    final SSMethU     op,
    final SSSearchPar par) throws Exception{
    
    try{
      final List<SSEntity>          results = new ArrayList<>();
      final SSSearchResultPages     pages;
      final List<SSEntity>          page;
      
      pages = searchResultPagesCache.get(par.pagesID);
      
      if(pages == null){
        throw new SSErr(SSErrE.searchResultOutDated);
      }
      
      try{
        page = pages.pages.get(par.pageNumber - 1);
      }catch(Exception error){
        throw new SSErr(SSErrE.searchResultPageUnavailable);
      }
      
      for(SSEntity result : page){
        results.add(fillSearchResult(par, result.id));
      }
      
      return SSSearchRet.get(
        results,
        par.pagesID,
        par.pageNumber,
        pages.pages.size(),
        op);
      
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
      
      for(SSEntity result : searchTags(searchTagsPar)){
        tagResults.add(result.id);
      }
    }
    
    if(!par.keywordsToSearchFor.isEmpty()){
      
      searchTagsPar =
        SSSearchTagsPar.get(
          par.user,
          par.keywordsToSearchFor,
          SSStrU.valueOr,
          10);
      
      for(SSEntity result : searchTags(searchTagsPar)){
        tagResults.add(result.id);
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
      
      for(SSEntity solrResult : searchSolr(searchSolrPar)){
        textualContentResults.add(solrResult.id);
      }
    }
    
    if(!par.keywordsToSearchFor.isEmpty()){
      
      searchSolrPar =
        SSSearchSolrPar.get(
          par.user,
          par.keywordsToSearchFor,
          SSStrU.valueOr);
      
      for(SSEntity solrResult : searchSolr(searchSolrPar)){
        textualContentResults.add(solrResult.id);
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