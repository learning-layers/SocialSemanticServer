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

import at.tugraz.sss.serv.SSEntityResultPages;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.datatypes.SSResourceLikelihood;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommResourcesPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.service.rating.api.SSRatingServerI;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingEntityURIsGetPar;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSIDU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.search.api.*;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchCleanUpPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import at.kc.tugraz.ss.service.search.impl.fct.SSSearchFct;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagEntitiesForTagsGetPar;
import at.tugraz.sss.serv.SSClientE;
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
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.servs.common.impl.tagcategory.SSEntityQueryCacheU;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityURIsGetPar;
import sss.servs.entity.sql.SSEntitySQL;

public class SSSearchImpl
extends SSServImplWithDBA
implements
  SSSearchClientI,
  SSSearchServerI{
  
  protected static final Map<String, SSEntityResultPages> searchResultPagesCache = new HashMap<>();
  private          final SSEntitySQL                      sql;
  private          final SSSearchNoSQLFct                 noSQLFct;
  
  public SSSearchImpl(final SSConfA conf) throws SSErr{
    super(conf, (SSDBSQLI) SSDBSQL.inst.getServImpl(), (SSDBNoSQLI) SSDBNoSQL.inst.getServImpl());
    
    this.sql      = new SSEntitySQL     (dbSQL, SSVocConf.systemUserUri);
    this.noSQLFct = new SSSearchNoSQLFct(dbNoSQL);
  }
  
  @Override
  public SSServRetI search(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSSearchPar par = (SSSearchPar) parA.getFromJSON(SSSearchPar.class);
      
      return search(par);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
    
//    SSSearchActivityFct.search(new SSSearchPar((parA)));
  }
  
  @Override
  public SSSearchRet search(final SSSearchPar par) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      if(
        par.pagesID    != null &&
        par.pageNumber != null){
        
        return handleSearchPageRequest(entityServ, par);
      }
      
      final List<SSUri>        uris                   = new ArrayList<>();
      final SSEntityURIsGetPar entityURIsGetPar       =
        new SSEntityURIsGetPar(
          par.user,
          null, //entities
          true, //getAccessible
          par.typesToSearchOnlyFor,
          par.authorsToSearchFor,
          par.startTime,
          par.endTime);
      
      final List<SSUri>             accessibleEntityURIs  = entityServ.entityURIsGet(entityURIsGetPar);
      final List<SSUri>             tagResults            = new ArrayList<>();
      final List<SSUri>             labelResults          = new ArrayList<>();
      final List<SSUri>             descriptionResults    = new ArrayList<>();
      final List<SSUri>             contentResults        = new ArrayList<>();
      final List<SSUri>             recommendedResults    = new ArrayList<>();
      
      if(
        par.documentContentsToSearchFor.isEmpty()  &&
        par.tagsToSearchFor.isEmpty()              &&
        par.labelsToSearchFor.isEmpty()            &&
        par.descriptionsToSearchFor.isEmpty()      &&
        !par.includeRecommendedResults){
        
        SSLogU.info("no search query input given");
        
        uris.addAll(accessibleEntityURIs);
      }else{
        tagResults.addAll         (getTagResults            (par, accessibleEntityURIs));
        labelResults.addAll       (getLabelResults          (par, accessibleEntityURIs));
        descriptionResults.addAll (getDescriptionResults    (par, accessibleEntityURIs));
        contentResults.addAll     (getTextualContentResults (par));
        recommendedResults.addAll (SSUri.getDistinctNotNullFromEntities(getRecommendedResults    (par)));
        
        uris.addAll                  (tagResults);
        uris.addAll                  (labelResults);
        uris.addAll                  (descriptionResults);
        uris.addAll                  (contentResults);
        
        SSStrU.distinctWithoutNull2(uris);
      }
      
      final List<SSUri>             ratingResultUris              = new ArrayList<>();
      final List<SSUri>             resultsAfterGlobalSearchOp    = new ArrayList<>();
      final String                  pagesID                       = SSIDU.uniqueID();
      final List<List<SSUri>>       pages                         = new ArrayList<>();
      final List<SSUri>             page                          = new ArrayList<>();
      Integer                       recommendedEntityCounter      = 0;
      
      ratingResultUris.addAll(getRatingResults(par, uris));
      
      resultsAfterGlobalSearchOp.addAll(
        filterSearchResultsRegardingGlobalSearchOp(
          par,
          uris,
          tagResults,
          contentResults,
          labelResults,
          descriptionResults,
          ratingResultUris));
      
      if(!resultsAfterGlobalSearchOp.isEmpty()){
        
        final List<String> entities =
          SSStrU.retainAll(
            SSStrU.toStr(resultsAfterGlobalSearchOp),
            SSStrU.toStr(accessibleEntityURIs));
        
        final List<SSUri> entityURIs = SSUri.get(entities);
        
        for(SSUri entityURI : entityURIs){//entityServ.entitiesGet(entitiesGetPar)){
          
          if(page.size() == par.pageSize - 1){
            pages.add(new ArrayList<>(page));
            page.clear();
          }
          
          page.add(entityURI);
          
          recommendedEntityCounter =
            SSSearchFct.addRecommendedResult(
              page,
              entityURIs,
              par,
              recommendedResults,
              recommendedEntityCounter);
        }
      }
      
      if(!page.isEmpty()){
        pages.add(page);
      }
      
      SSSearchFct.fillPagesIfEmpty(
        par,
        pages,
        recommendedResults);
      
      final List<SSEntity>     results                = new ArrayList<>();
      
      if(!pages.isEmpty()){
        
        if(par.orderByLabel){
          
          final List<List<SSUri>> newPages =
            orderPagesByLabel(
              entityServ,
              par.pageSize,
              par.user,
              pages);
          
          pages.clear();
          pages.addAll(newPages);
          
        }else{
          
          if(par.orderByCreationTime){
            
            final List<List<SSUri>> newPages =
              orderPagesByCreationTime(
                entityServ,
                par.pageSize,
                par.user,
                pages);
            
            pages.clear();
            pages.addAll(newPages);
          }
        }
        
        searchResultPagesCache.put(
          pagesID,
          SSEntityResultPages.get(
            pages,
            SSDateU.dateAsLong(),
            pagesID));
        
        results.addAll(
          fillSearchResults(
            entityServ,
            par,
            pages.get(0)));
      }
      
      if(results.isEmpty()){
        
        return SSSearchRet.get(
          results,
          null,
          0,
          0);
        
      }else{
        
        return SSSearchRet.get(
          results,
          pagesID,
          1,
          pages.size());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void searchCleanUp(final SSSearchCleanUpPar par) throws SSErr{
    
    try{
      SSEntityQueryCacheU.entityQueryCacheClean(searchResultPagesCache);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private List<SSUri> filterSearchResultsRegardingGlobalSearchOp(
    final SSSearchPar par,
    final List<SSUri> currentResults,
    final List<SSUri> tagResults,
    final List<SSUri> documentResults,
    final List<SSUri> labelResuls,
    final List<SSUri> descriptionResults,
    final List<SSUri> ratingResults) throws SSErr{
    
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
          Boolean           ratingsThere;
          
          tmpUris.addAll(currentResults);
          
          for(SSUri uri : tmpUris){
            
            tagsThere           = false;
            labelsThere         = false;
            descriptionsThere   = false;
            contentThere        = false;
            ratingsThere        = false;
            
            if(
              par.minRating == null &&
              par.maxRating == null){
              ratingsThere = true;
            }else{
              if(SSStrU.contains(ratingResults, uri)){
                ratingsThere = true;
              }
            }
            
            if(par.tagsToSearchFor.isEmpty()){
              tagsThere = true;
            }else{
              if(SSStrU.contains(tagResults,uri)){
                tagsThere = true;
              }
            }
            
            if(par.labelsToSearchFor.isEmpty()){
              labelsThere = true;
            }else{
              if(SSStrU.contains(labelResuls, uri)){
                labelsThere = true;
              }
            }
            
            if(par.descriptionsToSearchFor.isEmpty()){
              descriptionsThere = true;
            }else{
              if(SSStrU.contains(descriptionResults, uri)){
                descriptionsThere = true;
              }
            }
            
            if(par.documentContentsToSearchFor.isEmpty()){
              contentThere        = true;
            }else{
              if(SSStrU.contains(documentResults, uri)){
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
            
            if(tagsThere && labelsThere && descriptionsThere && contentThere && ratingsThere){
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
    final SSSearchPar     par) throws SSErr{
    
    try{
      final SSEntityResultPages     pages;
      final List<SSUri>             page;
      
      pages = searchResultPagesCache.get(par.pagesID);
      
      if(pages == null){
        throw SSErr.get(SSErrE.queryResultOutDated);
      }
      
      try{
        page = pages.pages.get(par.pageNumber - 1);
      }catch(Exception error){
        throw SSErr.get(SSErrE.queryPageUnavailable);
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
  
  private List<SSUri> getTagResults(
    final SSSearchPar par,
    final List<SSUri> filteredResults) throws SSErr{
    
    try{
      
      if(par.tagsToSearchFor.isEmpty()){
        return new ArrayList<>();
      }
      
      final SSTagServerI tagServ = (SSTagServerI) SSServReg.getServ(SSTagServerI.class);
      
      return tagServ.tagEntitiesForTagsGet(
        new SSTagEntitiesForTagsGetPar(
          par.user, //user
          null, //forUser
          filteredResults, //entities
          SSTagLabel.get(par.tagsToSearchFor), //labels
          par.localSearchOp, //labelSearchOp
          null, //spaces
          null, //circles
          null, //startTime
          false)); //withUserRestriction
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.servServerNotAvailable)){
        SSLogU.warn(SSErrE.servServerNotAvailable.toString());
      }else{
        SSLogU.warn(error.getMessage());
      }
      
      SSServErrReg.reset();
      return new ArrayList<>();
    }
  }
  
  private List<SSUri> getDescriptionResults(
    final SSSearchPar par,
    final List<SSUri> filteredResults) throws SSErr{
    
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
            sql.getEntitiesForDescriptionsWithMatch(
              filteredResults,
              new ArrayList<>(), //requireds
              new ArrayList<>(), //absents
              SSStrU.toStr(par.descriptionsToSearchFor))); //eithers
          
          if(!orResults.isEmpty()){
            return orResults;
          }
          
          return sql.getEntitiesForDescriptionsWithLike(SSStrU.toStr(par.descriptionsToSearchFor));
        }
        
        case and:{
          
          return sql.getEntitiesForDescriptionsWithMatch(
            filteredResults,
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
  
  private List<SSUri> getLabelResults(
    final SSSearchPar par,
    final List<SSUri> filteredResults) throws SSErr{
    
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
            sql.getEntitiesForLabelsWithMatch(
              filteredResults,
              new ArrayList<>(), //requireds
              new ArrayList<>(), //absents
              SSStrU.toStr(par.labelsToSearchFor))); //eithers
          
          if(!orResults.isEmpty()){
            return orResults;
          }
          
          return sql.getEntitiesForLabelsWithLike(SSStrU.toStr(par.labelsToSearchFor));
        }
        
        case and:{
          
          return sql.getEntitiesForLabelsWithMatch(
            filteredResults,
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
    final SSSearchPar par) throws SSErr{
    
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
            50, //maxResources
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
      
      if(SSServErrReg.containsErr(SSErrE.servServerNotAvailable)){
        SSLogU.warn(SSErrE.servServerNotAvailable.toString());
      }else{
        SSLogU.warn(error.getMessage());
      }
      
      SSServErrReg.reset();
      return new ArrayList<>();
    }
  }
  
  private List<SSUri> getTextualContentResults(
    final SSSearchPar par) throws SSErr{
    
    try{
      
      if(par.documentContentsToSearchFor.isEmpty()){
        return new ArrayList<>();
      }
      
      return noSQLFct.search(
        par.globalSearchOp,
        par.localSearchOp,
        par.documentContentsToSearchFor);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.servServerNotAvailable)){
        SSLogU.warn(SSErrE.servServerNotAvailable.toString());
      }else{
        SSLogU.warn(error.getMessage());
      }
      
      SSServErrReg.reset();
      return new ArrayList<>();
    }
  }
  
  private List<SSUri> getRatingResults(
    final SSSearchPar par,
    final List<SSUri> searchResults) throws SSErr{
    
    try{
      
      if(
        par.minRating == null &&
        par.maxRating == null){
        
        return searchResults;
      }
      
      final SSRatingServerI ratingServ = (SSRatingServerI) SSServReg.getServ(SSRatingServerI.class);
      
      return ratingServ.ratingEntityURIsGet(
        new SSRatingEntityURIsGetPar(
          par.user,
          searchResults,
          par.minRating,
          par.maxRating,
          false));
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.servServerNotAvailable)){
        SSLogU.warn(SSErrE.servServerNotAvailable.toString());
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
    final List<SSUri>        results) throws SSErr{
    
    try{
      
      final SSEntityDescriberPar descPar = new SSEntityDescriberPar(null);
      
      descPar.setTags          = true;
      descPar.setOverallRating = true;
      descPar.setCircleTypes   = true;
      
      return entityServ.entitiesGet(
        new SSEntitiesGetPar(
          par.user,
          results, //entities
          null, //descPar,
          par.withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<List<SSUri>> orderPagesByLabel(
    final SSEntityServerI   entityServ,
    final Integer           pageSize,
    final SSUri             user,
    final List<List<SSUri>> originalPages) throws SSErr{
    
    try{
      final List<List<SSUri>>           pages             = new ArrayList<>();
      final Map<String, List<SSEntity>> entitiesForLabels = new HashMap<>();
      final List<SSUri>                 page              = new ArrayList<>();
      final List<String>                labels            = new ArrayList<>();
      final SSEntitiesGetPar            entitiesGetPar    =
        new SSEntitiesGetPar(
          user,
          null, //entities
          null, //descPar,
          false); //withUserRestriction)
      
      for(List<SSUri> origPage : originalPages){
        entitiesGetPar.entities.addAll(origPage);
      }
      
      for(SSEntity entity : entityServ.entitiesGet(entitiesGetPar)){
        
        if(!SSStrU.containsKey(entitiesForLabels, entity.label)){
          entitiesForLabels.put(entity.label.toString(), new ArrayList<>());
        }
        
        entitiesForLabels.get(entity.label.toString()).add(entity);
      }
      
      labels.addAll(entitiesForLabels.keySet());
      
      Collections.sort(labels);
      
      for(String label : labels){
        
        for(SSEntity entity : entitiesForLabels.get(label)){
          
          if(page.size() == pageSize - 1){
            pages.add(new ArrayList<>(page));
            page.clear();
          }
          
          page.add(entity.id);
        }
      }
      
      if(!page.isEmpty()){
        pages.add(page);
      }
      
      return pages;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<List<SSUri>> orderPagesByCreationTime(
    final SSEntityServerI   entityServ,
    final Integer           pageSize,
    final SSUri             user,
    final List<List<SSUri>> originalPages) throws SSErr{
    
    try{
      final List<List<SSUri>>             pages             = new ArrayList<>();
      final Map<Long, List<SSEntity>>     entitiesForTimes  = new HashMap<>();
      final List<SSUri>                   page              = new ArrayList<>();
      final List<Long>                    times             = new ArrayList<>();
      final SSEntitiesGetPar              entitiesGetPar    =
        new SSEntitiesGetPar(
          user,
          null, //entities
          null, //descPar,
          false); //withUserRestriction)
      
      for(List<SSUri> origPage : originalPages){
        entitiesGetPar.entities.addAll(origPage);
      }
      
      for(SSEntity entity : entityServ.entitiesGet(entitiesGetPar)){
        
        if(!SSStrU.containsKey(entitiesForTimes, entity.creationTime)){
          entitiesForTimes.put(entity.creationTime, new ArrayList<>());
        }
        
        entitiesForTimes.get(entity.creationTime).add(entity);
      }
      
      times.addAll(entitiesForTimes.keySet());
      
      Collections.sort(times);
      
      for(Long time : times){
        
        for(SSEntity entity : entitiesForTimes.get(time)){
          
          if(page.size() == pageSize - 1){
            pages.add(new ArrayList<>(page));
            page.clear();
          }
          
          page.add(entity.id);
        }
      }
      
      if(!page.isEmpty()){
        pages.add(page);
      }
      
      return pages;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//private List<SSUri> getSubEntities(
//    final SSUri       user,
//    final List<SSUri> entities) throws SSErr{
//
//    try{
//
//      final List<SSUri> subEntities  = new ArrayList<>();
//
//      for(SSUri entity : entities){
//        subEntities.addAll(SSServCaller.entityUserSubEntitiesGet(user, entity));
//      }
//
//      SSStrU.distinctWithoutNull2(subEntities);
//
//      return subEntities;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
//
//  private List<SSEntity> filterSearchResultsForSubEntitySearch(
//    final List<SSEntity> searchResults,
//    final Boolean        onlySubEntities,
//    final List<SSUri>    subEntities) throws SSErr{
//
//    try{
//
//      if(!onlySubEntities){
//        return searchResults;
//      }
//
//      final List<SSEntity> filteredResults = new ArrayList<>();
//
//      for(SSEntity mIResult : searchResults){
//
//        if(!SSStrU.contains(subEntities, mIResult.id)){
//          continue;
//        }
//
//        filteredResults.add(mIResult);
//      }
//
//      return filteredResults;
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
//
//private List<SSUri> filterForSubEntities(
//    final SSSearchPar    par,
//    final List<SSUri>    results) throws SSErr{
//
//    if(
//      !par.includeOnlySubEntities ||
//      par.entitiesToSearchWithin.isEmpty()){
//
//      return results;
//    }
//
//    return
//      SSUri.get(
//        SSStrU.retainAll(
//          SSStrU.toStrWithoutEmptyAndNull(
//            results),
//          SSStrU.toStrWithoutEmptyAndNull(
//            getSubEntities(
//              par.user,
//              par.entitiesToSearchWithin))));
//  }
//
//  private List<SSUri> extendToParentEntities(
//    final SSSearchPar par,
//    final List<SSUri> results) throws SSErr{
//
//    if(!par.extendToParents){
//      return results;
//    }
//
//    try{
//
//      final List<SSUri> parentEntities  = new ArrayList<>();
//
//      for(SSUri entity : results){
//
//        try{
//          parentEntities.addAll(SSServCaller.entityUserParentEntitiesGet(par.user, entity));
//        }catch(Exception error){
//          SSServErrReg.reset();
//        }
//      }
//
//      results.addAll(parentEntities);
//
//      SSStrU.distinctWithoutNull2(results);
//
//      return results;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }