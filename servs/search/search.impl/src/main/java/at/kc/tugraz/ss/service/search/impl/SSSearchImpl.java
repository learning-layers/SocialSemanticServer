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

import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.datatypes.SSResourceLikelihood;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommResourcesPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSIDU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.search.api.*;
import at.kc.tugraz.ss.service.search.datatypes.*;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import at.kc.tugraz.ss.service.search.impl.fct.SSSearchFct;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagEntitiesForTagsGetPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBNoSQLSearchPar;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.*;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSSolrKeywordLabel;
import at.tugraz.sss.serv.SSSolrSearchFieldE;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;

public class SSSearchImpl 
extends SSServImplWithDBA
implements 
  SSSearchClientI, 
  SSSearchServerI{
  
  protected static final Map<String, SSSearchResultPages> searchResultPagesCache = new HashMap<>();
  private          final SSSearchSQLFct                   sqlFct;
  
  public SSSearchImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSSearchSQLFct(dbSQL);
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
    
    //TODO take into account typesToSearchOnlyFor, authorsToSearchFor in e.g., getTagResults, getTextualContentResults, etc. 
    //TODO clean up this method regarding single entityGet calls in e.g., getTagResults, getTextualContentResults, etc. 
    try{
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      if(
        par.pagesID    != null &&
        par.pageNumber != null){
        
        return handleSearchPageRequest(entityServ, par);
      }
      
      if(
        par.wordsToSearchFor.isEmpty()        &&
        par.tagsToSearchFor.isEmpty()         &&
        par.labelsToSearchFor.isEmpty()       &&
        par.descriptionsToSearchFor.isEmpty() &&
        !par.includeRecommendedResults){
       
        //TODO
        //1) use circle service to retrieve all entities the user can access (take into account typesToSearchOnlyFor)
        //2) use solr service to retrieve a ranked list of entities taking into account the author, min / max rating, typesToSearchOnlyFor
        //3) mix results from solr and circle service
        SSLogU.warn("no search query input given");
      }
      
      final List<SSEntity>          results                       = new ArrayList<>();
      final List<SSUri>             uris                          = new ArrayList<>();
      final List<SSUri>             tagResultUris                 = getTagResults(par);
      final List<SSUri>             contentResultUris             = getTextualContentResults(par);
      final List<SSUri>             labelResultUris               = getLabelResults(par);
      final List<SSUri>             descriptionResultUris         = getDescriptionResults(par);
      final List<SSEntity>          recommendedEntities           = getRecommendedResults(par);
      final String                  pagesID                       = SSIDU.uniqueID();
      final List<List<SSEntity>>    pages                         = new ArrayList<>();
      final List<SSEntity>          page                          = new ArrayList<>();
      Integer                       recommendedEntityCounter      = 0;
      SSEntityDescriberPar          descPar;
      
      uris.addAll                  (tagResultUris);
      uris.addAll                  (contentResultUris);
      uris.addAll                  (labelResultUris);
      uris.addAll                  (descriptionResultUris);
      
      SSStrU.distinctWithoutNull2(uris);
      
      final List<SSUri> filteredResults = 
        filterSearchResultsRegardingGlobalSearchOp(
          par,
          uris,
          tagResultUris, 
          contentResultUris,
          labelResultUris,
          descriptionResultUris);

//TODO extend entitiesGet with types, authors
//TODO use search with Query Expansion
      for(SSEntity entity :
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            extendToParentEntities(
              par,
              filterForSubEntities(
                par,
                filteredResults)),
            null, //types
            null, //descPar,
            par.withUserRestriction))){
        
        if(
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
            filteredResults,
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
      
        results.addAll(
          fillSearchResults(
            entityServ, 
            par, 
            pages.get(0)));
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
  
  private List<SSUri> filterSearchResultsRegardingGlobalSearchOp(
    final SSSearchPar par,
    final List<SSUri> currentResults,
    final List<SSUri> tagResultUris, 
    final List<SSUri> contentResultUris,
    final List<SSUri> labelResultUris, 
    final List<SSUri> descriptionResultUris) throws Exception{
    
    try{
      
      switch(par.globalSearchOp){
        
        case or:{
          return currentResults;
        }
        
        case and:{
          
          final List<SSUri> filteredResults = new ArrayList<>();
          final List<SSUri> tmpUris         = new ArrayList<>();
          Boolean           tagsThere;
          Boolean           contentThere;
          Boolean           labelsThere;
          Boolean           descriptionsThere;
          
          tmpUris.addAll(currentResults);
          
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
            
            if(par.applyGlobalSearchOpBetweenLabelAndDescription){
              
              if(
                !par.labelsToSearchFor.isEmpty() &&
                !par.descriptionsToSearchFor.isEmpty()){
                
                if(
                  !labelsThere ||
                  !descriptionsThere){
                  continue;
                }
              }
            }else{
              
              if(
                labelsThere ||
                descriptionsThere){
                
                labelsThere       = true;
                descriptionsThere = true;
              }
            }
            
            if(tagsThere && labelsThere && descriptionsThere && contentThere){
              filteredResults.add(uri);
            }
          }
          
          return filteredResults;
        }
        
        default: throw new UnsupportedOperationException();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSSearchRet handleSearchPageRequest(
    final SSEntityServerI entityServ, 
    final SSSearchPar     par) throws Exception{
    
    try{
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
      
      return SSSearchRet.get(
        fillSearchResults(
          entityServ,
          par,
          page),
        par.pagesID,
        par.pageNumber,
        pages.pages.size());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSUri> getTagResults(final SSSearchPar par) throws Exception{
    
    try{
      
      if(par.tagsToSearchFor.isEmpty()){
        return new ArrayList<>();
      }
      
      final SSTagServerI tagServ = (SSTagServerI) SSServReg.getServ(SSTagServerI.class);
      
      return tagServ.tagEntitiesForTagsGet(
        new SSTagEntitiesForTagsGetPar(
          par.user, //user
          null, //forUser
          null, //entities
          SSTagLabel.get(par.tagsToSearchFor), //labels
          par.localSearchOp, //labelSearchOp
          null, //spaces
          null, //circles
          null, //startTime
          false)); //withUserRestriction
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.notServerServiceForOpAvailable)){
        SSLogU.warn(SSErrE.notServerServiceForOpAvailable.toString());
      }else{
        SSLogU.warn(error.getMessage());
      }
      
      SSServErrReg.reset();
      return new ArrayList<>();
    }
  }
  
  private List<SSUri> getDescriptionResults(final SSSearchPar par) throws Exception{
    
    try{
      
        //search with "match against" depends on the innodb_ft_min_token_size setting for InnoDB MYSQL tables, stopwords, etc.
        //http://dba.stackexchange.com/questions/51144/mysql-match-against-boolean-mode and http://dev.mysql.com/doc/refman/5.6/en/fulltext-stopwords.html
        //please consider that MYSQL LIKE doesnt exploit MYSQL indexes on labels and descriptions
//        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.requireds, new ArrayList<>(), SSSearchOpE.and);
//        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.eithers, new ArrayList<>(), SSSearchOpE.or);
      
      for(int counter = par.descriptionsToSearchFor.size() - 1; counter >= 0; counter--){
        
        if(SSStrU.isEmpty(par.descriptionsToSearchFor.get(counter))){
          par.descriptionsToSearchFor.remove(counter);
        }
      }
      
      if(par.descriptionsToSearchFor.isEmpty()){
        return new ArrayList<>();
      }
      
      switch(par.localSearchOp){
        
        case or:{
          
          final List<SSUri> orResults = new ArrayList<>();
          
          orResults.addAll(
            sqlFct.getEntitiesForDescriptionsWithMatch(
              new ArrayList<>(), //requireds
              new ArrayList<>(), //absents
              SSStrU.toStr(par.descriptionsToSearchFor))); //eithers
          
          if(!orResults.isEmpty()){
            return orResults;
          }
          
          return sqlFct.getEntitiesForDescriptionsWithLike(SSStrU.toStr(par.descriptionsToSearchFor));
        }
        
        case and:{
          
          return sqlFct.getEntitiesForDescriptionsWithMatch(
            SSStrU.toStr(par.descriptionsToSearchFor), //requireds
            new ArrayList<>(), //absents
            new ArrayList<>()); //eithers
        }
        
        default: throw new UnsupportedOperationException();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSUri> getLabelResults(final SSSearchPar par) throws Exception{
    
    try{
      
      for(int counter = par.labelsToSearchFor.size() - 1; counter >= 0; counter--){
        
        if(SSStrU.isEmpty(par.labelsToSearchFor.get(counter))){
          par.labelsToSearchFor.remove(counter);
        }
      }
      
      if(par.labelsToSearchFor.isEmpty()){
        return new ArrayList<>();
      }
      
      switch(par.localSearchOp){
        
        case or:{
          
          final List<SSUri> orResults = new ArrayList<>();
          
          orResults.addAll(
            sqlFct.getEntitiesForLabelsWithMatch(
              new ArrayList<>(), //requireds
              new ArrayList<>(), //absents
              SSStrU.toStr(par.labelsToSearchFor))); //eithers
          
          if(!orResults.isEmpty()){
            return orResults;
          }
          
          return sqlFct.getEntitiesForLabelsWithLike(SSStrU.toStr(par.labelsToSearchFor));
        }
        
        case and:{
          
          return sqlFct.getEntitiesForLabelsWithMatch(
            SSStrU.toStr(par.labelsToSearchFor), //requireds
            new ArrayList<>(), //absents
            new ArrayList<>()); //eithers
        }
        
        default: throw new UnsupportedOperationException();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSEntity> getRecommendedResults(
    final SSSearchPar par) throws Exception{
    
    try{
      
      if(!par.includeRecommendedResults){
        return new ArrayList<>();
      }

      final List<SSEntity>             result               = new ArrayList<>();
      final SSRecommServerI            recommServ           = (SSRecommServerI) SSServReg.getServ(SSRecommServerI.class);
      final List<SSResourceLikelihood> recommendedResources =
        recommServ.recommResources(
          new SSRecommResourcesPar(
            par.user,
            null, //realm
            par.user, //forUser
            null, //entity
            null, //categories
            10, //maxResources
            par.typesToSearchOnlyFor, //typesToRecommOnly
            false, //setCircleTypes
            true, //includeOwn
            false, //ignoreAccessRights
            false, //withUserRestriction
            false)); //invokeEntityHandlers
          
      for(SSResourceLikelihood reommendedResource : recommendedResources){
        result.add(reommendedResource.resource);
      }
      
      return result;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.notServerServiceForOpAvailable)){
        SSLogU.warn(SSErrE.notServerServiceForOpAvailable.toString());
      }else{
        SSLogU.warn(error.getMessage());
      }
      
      SSServErrReg.reset();
      return new ArrayList<>();
    }
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
            getSubEntities(
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
  
  private List<SSUri> getTextualContentResults(
    final SSSearchPar par) throws Exception{
    
    try{
      
      if(par.wordsToSearchFor.isEmpty()){
        return new ArrayList<>();
      }
      
      final Map<SSSolrSearchFieldE, List<SSSolrKeywordLabel>> wheres = new HashMap<>();
      
      wheres.put(SSSolrSearchFieldE.docText, SSSolrKeywordLabel.get(par.wordsToSearchFor));
      
      return SSUri.get(
        dbNoSQL.search(
          new SSDBNoSQLSearchPar(
            par.globalSearchOp.toString(),
            par.localSearchOp.toString(),
            wheres,
            100)),
        SSVocConf.sssUri);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.notServerServiceForOpAvailable)){
        SSLogU.warn(SSErrE.notServerServiceForOpAvailable.toString());
      }else{
        SSLogU.warn(error.getMessage());
      }
      
       SSServErrReg.reset();
       return new ArrayList<>();
    }
  }
  
  private List<SSEntity> fillSearchResults(
    final SSEntityServerI    entityServ,
    final SSSearchPar        par,
    final List<SSEntity>     results) throws Exception{
    
    try{
      
      final SSEntityDescriberPar descPar = new SSEntityDescriberPar(null);
      
      descPar.setTags          = true;
      descPar.setOverallRating = true;
      descPar.setCircleTypes   = true;
      
      return entityServ.entitiesGet(
        new SSEntitiesGetPar(
          par.user,
          SSUri.getDistinctNotNullFromEntities(results), //entities
          null, //types
          null, //descPar,
          par.withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSUri> getSubEntities(
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

  private List<SSEntity> filterSearchResultsForSubEntitySearch(
    final List<SSEntity> searchResults,
    final Boolean        onlySubEntities,
    final List<SSUri>    subEntities) throws Exception{
    
    try{
      
      if(!onlySubEntities){
        return searchResults;
      }
      
      final List<SSEntity> filteredResults = new ArrayList<>();
      
      for(SSEntity mIResult : searchResults){
        
        if(!SSStrU.contains(subEntities, mIResult.id)){
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
}