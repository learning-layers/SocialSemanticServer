/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.recomm.impl;

import at.tugraz.sss.servs.category.datatype.SSCategoriesGetPar;
import at.tugraz.sss.servs.category.api.SSCategoryServerI;
import at.tugraz.sss.servs.recomm.conf.*;
import at.tugraz.sss.servs.recomm.datatype.*;
import at.tugraz.sss.servs.tag.datatype.SSTagLikelihood;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import engine.*;
import java.util.*;
import at.tugraz.sss.servs.eval.api.SSEvalServerI;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogE;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogPar;
import at.tugraz.sss.servs.user.datatype.*;

public class SSRecommTagCommons {
  
  public boolean checkAccessRights(
    final SSRecommTagsPar par, 
    final SSRecommConf    conf,
    final String          realmToUse) throws SSErr{ 
    
    try{
      
      if(par.ignoreAccessRights){
        
        if(SSStrU.isEqual(realmToUse, conf.fileNameForRec)){
          SSServErrReg.regErrThrow(SSErrE.parameterMissing);
          return false;
        }
        
        return true;
      }
      
      if(!par.withUserRestriction){
        return true;
      }
          
      if(
        par.forUser != null &&
        !SSStrU.isEqual(par.user, par.forUser)){
        
        SSServErrReg.regErrThrow(SSErrE.userNotAllowedToRetrieveForOtherUser);
        return false;
      }
          
      if(par.entities.isEmpty()){
        return true;
      }
      
      final SSEntityServerI   entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final List<SSEntity>    entities;
      final SSEntitiesGetPar  entitiesGetPar = 
        new SSEntitiesGetPar(
          par, 
          par.user, 
          par.entities, 
          null, 
          par.withUserRestriction);
        
      entities = entityServ.entitiesGet(entitiesGetPar);
        
      if(entities.size() == par.entities.size()){
        return true;
      }
      
      return false;
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  public Algorithm getRecommTagsAlgo(
    final SSRecommConf conf, 
    final SSUser       user) throws SSErr{
    
    try{
      
      if(
        conf.recommTagsAlgoPerUser != null &&
        !conf.recommTagsAlgoPerUser.isEmpty()){
        
        String userEmail;
        String algo;
        
        for(String userAndAlgo : conf.recommTagsAlgoPerUser){
          
          userEmail  = SSStrU.split(userAndAlgo, SSStrU.colon).get(0);
          algo       = SSStrU.split(userAndAlgo, SSStrU.colon).get(1);
          
          if(SSStrU.isEqual(user.email, userEmail)){
            return Algorithm.valueOf(algo);
          }
        }
      }
      
      if(
        conf.recommTagsRandomAlgos != null &&
        !conf.recommTagsRandomAlgos.isEmpty()){
        
        return Algorithm.valueOf(((SSRecommConf) conf).recommTagsRandomAlgos.get(new Random().nextInt(conf.recommTagsRandomAlgos.size())));
      }
      
      throw new UnsupportedOperationException();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<String> provideCategoryInputForRecommTags(
    final SSRecommTagsPar par,
    final SSUri           entity,
    final Algorithm       algo) throws SSErr{
    
    try{
      
      if(!par.categories.isEmpty()){
        return par.categories;
      }
      
      if(entity == null){
        return new ArrayList<>();
      }
      
      switch(algo){
        
        case THREELcoll:{
          
          final SSCategoryServerI categoryServ = (SSCategoryServerI) SSServReg.getServ(SSCategoryServerI.class);
          final List<String>      categories   = new ArrayList<>();
          
          for(SSEntity category :
            categoryServ.categoriesGet(
              new SSCategoriesGetPar(
                par,
                par.user,
                null, //forUser,
                SSUri.asListNotNull(entity), //entities
                null, //labels,
                SSSearchOpE.or, //labelSearchOp,
                null, //spaces
                null, //circles,
                null, //startTime,
                false))){ //withUserRestriction
            
            SSStrU.addDistinctNotNull(categories, category.getLabel());
          }
          
          return categories;
        }
        
        default:{
          return new ArrayList<>();
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void evalLog(
    final SSRecommTagsPar       par, 
    final List<SSTagLikelihood> tags,
    final Algorithm             algo,
    final Set<String>           entitiesCategories,
    final boolean               shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ   = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      final SSEvalLogPar  evalLogPar =
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.recommTags,
          null, // entity
          null, //content
          null, //entities
          SSUri.asListNotNull(par.forUser), //users
          new Date().getTime(), //creationTime
          shouldCommit);
      
      if(par.entities.size() == 1){
        evalLogPar.entity = par.entities.get(0);
      }
      
      if(par.entities.size() > 1){
        evalLogPar.entities.addAll(par.entities);
      }
      
      evalLogPar.query  = SSStrU.empty;
      evalLogPar.result = SSStrU.empty;
      
      final String formattedCategories =
        SSStrU.toCommaSeparatedStrNotNull(
          SSStrU.escapeColonSemiColonComma(entitiesCategories));
      
      final String realm = 
        SSStrU.escapeColonSemiColonComma(par.realm);
      
      evalLogPar.query += SSVarNames.algo + SSStrU.colon + algo + evalLogPar.creationTime;
      
      if(evalLogPar.entity != null){
        evalLogPar.query += SSVarNames.forEntity + SSStrU.colon + evalLogPar.entity + evalLogPar.creationTime;
      }else{
        
        if(!evalLogPar.entities.isEmpty()){
          evalLogPar.query += SSVarNames.forEntity + SSStrU.colon + SSStrU.toCommaSeparatedStrNotNull(evalLogPar.entities) + evalLogPar.creationTime;
        }else{
          evalLogPar.query += SSVarNames.forEntity + SSStrU.colon + SSStrU.empty + evalLogPar.creationTime;
        } 
      }
      
      evalLogPar.query += SSVarNames.forUser                           + SSStrU.colon + par.forUser                                            + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.categories                        + SSStrU.colon + formattedCategories                                    + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.includeOwn                        + SSStrU.colon + par.includeOwn                                         + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.maxTags                           + SSStrU.colon + par.maxTags                                            + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.realm                             + SSStrU.colon + realm                                                  + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.ignoreAccessRights                + SSStrU.colon + par.ignoreAccessRights                                 + evalLogPar.creationTime;
      
      for(SSTagLikelihood tag : tags){
        evalLogPar.result += SSStrU.escapeColonSemiColonComma(tag.getLabel()) + SSStrU.colon + tag.getLikelihood() + SSStrU.comma;
      }
      
      evalServ.evalLog(evalLogPar);
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default: {
          SSServErrReg.regErrThrow(error);
          break;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}