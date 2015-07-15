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

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
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
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.search.api.*;
import at.kc.tugraz.ss.service.search.datatypes.*;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchSolrPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchTagsWithinEntityPar;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import at.kc.tugraz.ss.service.search.impl.fct.SSSearchFct;
import at.kc.tugraz.ss.service.search.impl.fct.misc.SSSearchMiscFct;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsGetPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.*;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServReg;

public class SSSearchImpl extends SSServImplWithDBA implements SSSearchClientI, SSSearchServerI{
  
  protected static final Map<String, SSSearchResultPages> searchResultPagesCache = new HashMap<>();
  
  public SSSearchImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
  }
  
  @Override
  public void search(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSSearchPar par = (SSSearchPar) parA.getFromJSON(SSSearchPar.class);
      
    sSCon.writeRetFullToClient(search(par));
    
//    SSSearchActivityFct.search(new SSSearchPar((parA)));
  }

  @Override
  public SSSearchRet search(final SSSearchPar par) throws Exception{
    
    try{
      
      final List<SSEntity>          results                       = new ArrayList<>();
      final List<SSUri>             uris                          = new ArrayList<>();
      final List<SSUri>             tagResultUris                 = new ArrayList<>();
      final List<SSUri>             contentResultUris             = new ArrayList<>();
      final List<SSUri>             labelResultUris               = new ArrayList<>();
      final List<SSUri>             descriptionResultUris         = new ArrayList<>();
      final List<List<SSEntity>>    pages;
      final List<SSEntity>          page;
      final List<SSEntity>          recommendedEntities;
      Integer                       recommendedEntityCounter = 0;
      SSEntity                      entity;
      Boolean                       tagsThere;
      Boolean                       contentThere;
      Boolean                       labelsThere;
      Boolean                       descriptionsThere;
      
      if(
        par.pagesID    != null &&
        par.pageNumber != null){
        
        return handleSearchPageRequest(par.op, par);
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
      
      labelResultUris.addAll       (getLabelResults(par));
      uris.addAll                  (labelResultUris);
      
      descriptionResultUris.addAll (getDescriptionResults(par));
      uris.addAll                  (descriptionResultUris);
      
      SSStrU.distinctWithoutNull2(uris);
      
      switch(par.globalSearchOp){
        
        case and:{

          final List<SSUri> tmpUris = new ArrayList<>();
          
          tmpUris.addAll(uris);
          uris.clear();
          
          for(SSUri uri : tmpUris){
            
            tagsThere           = false;
            labelsThere         = false;
            descriptionsThere   = false;
            contentThere        = false;
            
            if(par.tagsToSearchFor.isEmpty()){
              tagsThere = true;
            }else{
              if(SSStrU.contains(tagResultUris,uri)){
                tagsThere = true;
              }
            }
                       
            if(par.labelsToSearchFor.isEmpty()){
              labelsThere = true;
            }else{
              if(SSStrU.contains(labelResultUris, uri)){
                labelsThere = true;
              }
            }
            
            if(par.descriptionsToSearchFor.isEmpty()){
              descriptionsThere = true;
            }else{
              if(SSStrU.contains(descriptionResultUris, uri)){
                descriptionsThere = true;
              }
            }
            
            if(par.wordsToSearchFor.isEmpty()){
              contentThere        = true;
            }else{
              if(SSStrU.contains(contentResultUris, uri)){
                contentThere        = true;
              }
            }
            
            if(tagsThere && labelsThere && descriptionsThere && contentThere){
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

        entity = 
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null, 
            null, 
            par.user, 
            result, 
            par.withUserRestriction, 
            null)); //descPar))
        
        if(
          entity == null ||
          !SSSearchFct.handleType     (par, entity) ||
          !SSSearchFct.handleRating   (par, entity) ||
          !SSSearchFct.handleAuthors  (par, entity)){
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
        pages.size());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSEntity fillSearchResult(
    final SSSearchPar par,
    final SSUri       result) throws Exception{
    
    try{
      
      final SSEntityDescriberPar descPar = new SSEntityDescriberPar(null);
      
      descPar.setTags          = true;
      descPar.setOverallRating = true;
      descPar.setCircleTypes   = true;
      
      final SSEntity entity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            result,
            false, //withUserRestriction
            descPar)); //descPar
      
//      entity.entries.addAll(
//        SSStrU.removeTrailingSlash(
//          getEntries(
//            par,
//            entity)));
//      
//      private List<SSEntity> getEntries(
//    final SSSearchPar par, 
//    final SSEntity    entity) throws Exception{
//    
//    final List<SSEntity> entries = new ArrayList<>();
//    
//    if(!par.provideEntries){
//      return entries;
//    }
//          
//    switch(entity.type){
//      case chat:
//      case disc:
//      case qa:
//
//        entries.addAll(
//          ((SSDiscServerI) SSServReg.getServ(SSDiscServerI.class)).discEntryURIsGet(
//            new SSDiscEntryURIsGetPar(op, key, user, disc, withUserRestriction)
//              null, 
//              null, 
//              par.user, 
//              entity.id, 
//              )));
//          
//        break;
//    }
//    
//    return entries;
//  }
      
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
        pages.pages.size());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSUri> getMIResults(final SSSearchPar par) throws Exception{
    return new ArrayList<>();
  }
  
  private List<SSUri> getTagResults(final SSSearchPar par) throws Exception{
    
    final List<SSUri> tagResults = new ArrayList<>();
    SSSearchTagsPar   searchTagsPar;
    
    if(par.tagsToSearchFor.isEmpty()){
      return tagResults;
    }
    
    searchTagsPar =
      SSSearchTagsPar.get(
        par.user,
        par.tagsToSearchFor,
        par.localSearchOp,
        10);
    
    for(SSEntity result : searchTags(searchTagsPar)){
      tagResults.add(result.id);
    }
    
    return tagResults;
  }
  
  private List<SSUri> getTextualContentResults(final SSSearchPar par) throws Exception{
    
    final List<SSUri> textualContentResults = new ArrayList<>();
    SSSearchSolrPar   searchSolrPar;
    
    if(par.wordsToSearchFor.isEmpty()){
      return textualContentResults;
    }
    
    searchSolrPar =
      SSSearchSolrPar.get(
        par.user,
        par.wordsToSearchFor,
        par.localSearchOp);
    
    for(SSEntity solrResult : searchSolr(searchSolrPar)){
      textualContentResults.add(solrResult.id);
    }
    
    return textualContentResults;
  }
  
  private List<SSUri> getDescriptionResults(final SSSearchPar par) throws Exception{
    
    final List<SSUri>         results            = new ArrayList<>();
    
    for(int counter = par.descriptionsToSearchFor.size() - 1; counter >= 0; counter--){
      
      if(SSStrU.isEmpty(par.descriptionsToSearchFor.get(counter))){
        par.descriptionsToSearchFor.remove(counter);
      }
    }
    
    if(par.descriptionsToSearchFor.isEmpty()){
      return results;
    }
    
    switch(par.localSearchOp){
      
      case or:{
        for(SSEntity entity : SSServCaller.entitiesForDescriptionsGet(new ArrayList<>(), new ArrayList<>(), SSStrU.toStr(par.descriptionsToSearchFor))){
          results.add(entity.id);
        }
        
        break;
      }
      
      case and:{
        
        for(SSEntity entity : SSServCaller.entitiesForDescriptionsGet(SSStrU.toStr(par.descriptionsToSearchFor), new ArrayList<>(), new ArrayList<>())){
          results.add(entity.id);
        }
        
        break;
      }
    }
    return results;
  }
    
  private List<SSUri> getLabelResults(final SSSearchPar par) throws Exception{
    
    final List<SSUri>         results            = new ArrayList<>();
    
    for(int counter = par.labelsToSearchFor.size() - 1; counter >= 0; counter--){
      
      if(SSStrU.isEmpty(par.labelsToSearchFor.get(counter))){
        par.labelsToSearchFor.remove(counter);
      }
    }
  
    if(par.labelsToSearchFor.isEmpty()){
      return results;
    }
    
    switch(par.localSearchOp){
      
      case or:{
        
        for(SSEntity entity : SSServCaller.entitiesForLabelsGet(new ArrayList<>(), new ArrayList<>(), SSStrU.toStr(par.labelsToSearchFor))){
          results.add     (entity.id);
        }
        
        break;
      }
      
      case and:{
        
        for(SSEntity entity : SSServCaller.entitiesForLabelsGet(SSStrU.toStr(par.labelsToSearchFor), new ArrayList<>(), new ArrayList<>())){
          results.add     (entity.id);
        }
        
        break;
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
          ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagEntitiesForTagsGet(
            new SSTagEntitiesForTagsGetPar(
              null,
              null,
              par.user,
              null, 
              SSTagLabel.asListWithoutNullAndEmpty(SSTagLabel.get(tagLabel)), 
              SSSpaceE.sharedSpace, 
              null))){
          
          searchResultsForTagOneTag.add(
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                null,
                foundEntity,  //entity
                false, //withUserRestriction
                null))); //descPar
        }
        
        for(SSUri foundEntity :
          ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagEntitiesForTagsGet(
            new SSTagEntitiesForTagsGetPar(
              null,
              null,
              par.user,
              null, 
              SSTagLabel.asListWithoutNullAndEmpty(SSTagLabel.get(tagLabel)), 
              SSSpaceE.privateSpace, 
              null))){

          searchResultsForTagOneTag.add(
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                null,
                foundEntity,  //entity
                false, //withUserRestriction
                null))); //descPar
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
          
          try{
            entityObj =
              ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
                new SSEntityGetPar(
                  null,
                  null,
                  null,
                  SSServCaller.vocURICreateFromId(entityId),  //entity
                  false, //withUserRestriction
                  null)); //descPar
              
            searchResultsForOneKeyword.add(entityObj);
          }catch(Exception error){
            SSLogU.warn("solr result entity not found in sss");
            SSServErrReg.reset();
          }
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
          
          if(
            ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsGet(
              new SSTagsGetPar(
                null,
                null,
                par.user,
                null, //forUser
                SSUri.asListWithoutNullAndEmpty(entityUri), //entities
                SSTagLabel.asListWithoutNullAndEmpty(SSTagLabel.get(tag)),  //labels
                null, //space
                null, //circles
                null, //startTime
                false)).isEmpty()){ //withUserRestriction
            continue;
          }
          
          entityObj =
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                null,
                entityUri,  //entity
                false, //withUserRestriction
                null)); //descPar
          
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