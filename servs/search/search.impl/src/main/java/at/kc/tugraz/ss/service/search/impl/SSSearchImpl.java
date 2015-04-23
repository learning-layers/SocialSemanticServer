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

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSIDU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchTagsPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.search.api.*;
import at.kc.tugraz.ss.service.search.datatypes.*;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchSolrPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchTagsWithinEntityPar;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import at.kc.tugraz.ss.service.search.impl.fct.SSSearchFct;
import at.kc.tugraz.ss.service.search.impl.fct.misc.SSSearchMiscFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import java.util.*;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;

public class SSSearchImpl extends SSServImplWithDBA implements SSSearchClientI, SSSearchServerI{
  
  protected static final Map<String, SSSearchResultPages> searchResultPagesCache = new HashMap<>();
  
  public SSSearchImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
  }
  
  @Override
  public void search(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(search(parA), parA.op);
    
//    SSSearchActivityFct.search(new SSSearchPar((parA)));
  }

  @Override
  public SSSearchRet search(final SSServPar parA) throws Exception{
    
    try{
      
      final SSSearchPar             par                           = SSSearchPar.get(parA);
      final List<SSEntity>          results                       = new ArrayList<>();
      final List<SSUri>             uris                          = new ArrayList<>();
      final List<SSUri>             tagResultUris                 = new ArrayList<>();
      final List<SSUri>             contentResultUris             = new ArrayList<>();
      final List<SSUri>             labelAndDescriptionResultUris = new ArrayList<>();
      final List<List<SSEntity>>    pages;
      final List<SSEntity>          page;
      final List<SSEntity>          recommendedEntities;
      Integer                       recommendedEntityCounter = 0;
      SSEntity                      entity;
      Boolean                       tagsThere;
      Boolean                       contentThere;
      Boolean                       labelsAndDescsThere;
      
      if(
        par.pagesID    != null &&
        par.pageNumber != null){
        
        return handleSearchPageRequest(parA.op, par);
      }
      
      tagResultUris.addAll                 (getTagResults(par));
      uris.addAll                          (tagResultUris);
      uris.addAll                          (getMIResults(par));
      
      try{
        contentResultUris.addAll             (getTextualContentResults(par));
        uris.addAll                          (contentResultUris);
      }catch(SSErr error){
        
        switch(error.code){
          case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
          default: SSServErrReg.regErrThrow(error);
        }
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
      labelAndDescriptionResultUris.addAll (getLabelAndDescriptionResults(par));
      uris.addAll                          (labelAndDescriptionResultUris);
      
      SSStrU.distinctWithoutNull2(uris);
      
      switch(par.globalSearchOp){
        
        case and:{

          final List<SSUri> tmpUris = new ArrayList<>();
          
          tmpUris.addAll(uris);
          uris.clear();
          
          for(SSUri uri : tmpUris){
            
            tagsThere           = false;
            labelsAndDescsThere = false;
            contentThere        = false;
            
            if(
              !par.includeTags ||
              par.tagsToSearchFor.isEmpty()){
              tagsThere = true;
            }else{
              if(SSStrU.contains(tagResultUris,uri)){
                tagsThere = true;
              }
            }
            
            if(
              (!par.includeLabel &&
              !par.includeDescription)
              ||
              (par.labelsToSearchFor.isEmpty() &&
              par.descriptionsToSearchFor.isEmpty())){
              labelsAndDescsThere = true;
            }else{
              if(SSStrU.contains(labelAndDescriptionResultUris, uri)){
                labelsAndDescsThere = true;
              }
            }
            
            if(
              !par.includeTextualContent ||
              par.wordsToSearchFor.isEmpty()){
              contentThere        = true;
            }else{
              if(SSStrU.contains(contentResultUris, uri)){
                contentThere        = true;
              }
            }
            
            if(tagsThere && labelsAndDescsThere && contentThere){
              uris.add(uri);
            }
          }
          
          break;
        }
      }
      
      recommendedEntities = SSSearchFct.recommendEntities(par);
      
      final String pagesID = SSIDU.uniqueID();
      
      pages = new ArrayList<>();
      page  = new ArrayList<>();
      
      for(SSUri result : extendToParentEntities(par, filterForSubEntities(par, uris))){
        
        entity = SSSearchFct.handleAccess(par, result);
        
        if(
          entity == null ||
          !SSSearchFct.handleType     (par, entity) ||
          !SSSearchFct.handleRating   (par, entity)){
          continue;
        }
        
        if(page.size() == 10){
          pages.add(new ArrayList<>(page));
          page.clear();
        }
        
        page.add(entity);
        
        recommendedEntityCounter =
          SSSearchFct.addRecommendedResult(
            page,
            uris,
            par,
            recommendedEntities,
            recommendedEntityCounter);
      }
      
      if(!page.isEmpty()){
        pages.add(page);
      }
      
      SSSearchFct.fillPagesIfEmpty(
        par, 
        pages, 
        recommendedEntities);
      
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
        SSServCaller.circleTypesGet(
          par.user, 
          par.user, 
          entity.id,
          false);
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSSearchRet handleSearchPageRequest(
    final SSServOpE     op,
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
          par.localSearchOp,
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
          par.localSearchOp);
      
      for(SSEntity solrResult : searchSolr(searchSolrPar)){
        textualContentResults.add(solrResult.id);
      }
    }
    
    return textualContentResults;
  }
  
  private List<SSUri> getLabelAndDescriptionResults(final SSSearchPar par) throws Exception{
    
    final List<SSUri>         results            = new ArrayList<>();
    final List<String>        combinedKeywords   = new ArrayList<>();
    
    for(int counter = par.labelsToSearchFor.size() - 1; counter >= 0; counter--){
      
      if(SSStrU.isEmpty(par.labelsToSearchFor.get(counter))){
        par.labelsToSearchFor.remove(counter);
      }
    }
  
    for(int counter = par.descriptionsToSearchFor.size() - 1; counter >= 0; counter--){
      
      if(SSStrU.isEmpty(par.descriptionsToSearchFor.get(counter))){
        par.descriptionsToSearchFor.remove(counter);
      }
    }
     
    if(
      par.includeDescription &&
      par.includeLabel){
      
      switch(par.localSearchOp){
        
        case or:{
          combinedKeywords.addAll(SSStrU.toStr(par.labelsToSearchFor));
          
          for(SSEntity entity : SSServCaller.entitiesForLabelsGet(new ArrayList<>(), new ArrayList<>(), combinedKeywords)){
            results.add     (entity.id);
          }
          
          combinedKeywords.clear();
          combinedKeywords.addAll(SSStrU.toStr(par.descriptionsToSearchFor));
          
          for(SSEntity entity : SSServCaller.entitiesForDescriptionsGet(new ArrayList<>(), new ArrayList<>(), combinedKeywords)){
            results.add           (entity.id);
          }
          
          break;
        }
        
        case and:{
          
          combinedKeywords.addAll(SSStrU.toStr(par.labelsToSearchFor));
          
          for(SSEntity entity : SSServCaller.entitiesForLabelsGet(combinedKeywords, new ArrayList<>(), new ArrayList<>())){
            results.add     (entity.id);
          }
          
          combinedKeywords.clear();
          combinedKeywords.addAll(SSStrU.toStr(par.descriptionsToSearchFor));
          
          for(SSEntity entity : SSServCaller.entitiesForDescriptionsGet(combinedKeywords, new ArrayList<>(), new ArrayList<>())){
            results.add           (entity.id);
          }
          break;
        }
      }
    }
    
    if(
      par.includeDescription &&
      !par.includeLabel){
      
      combinedKeywords.addAll(SSStrU.toStr(par.descriptionsToSearchFor));
      
      switch(par.localSearchOp){
        
        case or:{
          for(SSEntity entity : SSServCaller.entitiesForDescriptionsGet(new ArrayList<>(), new ArrayList<>(), combinedKeywords)){
            results.add(entity.id);
          }
          break;
        }
        
        case and:{
          for(SSEntity entity : SSServCaller.entitiesForDescriptionsGet(combinedKeywords, new ArrayList<>(), new ArrayList<>())){
            results.add(entity.id);
          }
          break;
        }
      }
    }
        
    if(
      !par.includeDescription &&
      par.includeLabel){
      
      combinedKeywords.addAll(SSStrU.toStr(par.labelsToSearchFor));
      
      switch(par.localSearchOp){
        
        case or:{
          
          for(SSEntity entity : SSServCaller.entitiesForLabelsGet(new ArrayList<>(), new ArrayList<>(), combinedKeywords)){
            results.add(entity.id);
          }
          break;
        }
        
        case and:{
          for(SSEntity entity : SSServCaller.entitiesForLabelsGet(combinedKeywords, new ArrayList<>(), new ArrayList<>())){
            results.add(entity.id);
          }
          break;
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
              SSServCaller.vocURICreateFromId(entityId));
            
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
          
          searchResultsForOneKeyword.add(entityObj);
        }
        
        searchResultsPerTag.put(tag, new ArrayList<>(searchResultsForOneKeyword));
      }
      
      return SSSearchFct.selectSearchResultsWithRegardToSearchOp(
        par.searchOp, 
        searchResultsPerTag);
      
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
}