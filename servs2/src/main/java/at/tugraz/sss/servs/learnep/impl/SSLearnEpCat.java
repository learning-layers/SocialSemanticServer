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
package at.tugraz.sss.servs.learnep.impl;

import at.tugraz.sss.servs.category.datatype.SSCategoryAddPar;
import at.tugraz.sss.servs.category.datatype.SSCategoryLabel;
import at.tugraz.sss.servs.category.datatype.SSCategoriesRemovePar;
import at.tugraz.sss.servs.category.api.SSCategoryServerI;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.entity.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.learnep.datatype.*;
import java.util.*;

public class SSLearnEpCat {
  
  private final SSLearnEpCommons commons = new SSLearnEpCommons();
  
  public void removeCategories(
    final SSServPar       servPar,
    final SSUri           user,
    final List<SSUri>     entities,
    final SSCategoryLabel categoryLabel,
    final boolean         shouldCommit) throws SSErr{
    
    try{
      
      final SSCategoryServerI categoryServ = (SSCategoryServerI) SSServReg.getServ(SSCategoryServerI.class);
      
      for(SSUri entityURI : entities){
        
        categoryServ.categoriesRemove(
          new SSCategoriesRemovePar(
            servPar,
            user,
            null, //forUser,
            entityURI, //entity
            categoryLabel, //label
            SSSpaceE.sharedSpace, //space,
            null, //circle,
            false, //withUserRestriction,
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break; }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addCategory(
    final SSServPar       servPar,
    final SSUri           user,
    final List<SSUri>     entities,
    final SSCategoryLabel categoryLabel,
    final boolean         shouldCommit) throws Exception{
    
    try{
      
      final SSCategoryServerI categoryServ = (SSCategoryServerI) SSServReg.getServ(SSCategoryServerI.class);
      
      for(SSUri entity : entities){
        
        categoryServ.categoryAdd(
          new SSCategoryAddPar(
            servPar,
            user,
            entity, //entity
            categoryLabel,  //label
            SSSpaceE.sharedSpace,  //space
            null, //circle,
            null, //creationTime,
            false, //withUserRestriction,
            shouldCommit)); //shouldCommit
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break; }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void handleLearnEpCircleLabelUpdate(
    final SSServPar      servPar,
    final SSUri          user,
    final SSUri          learnEpCircle,
    final List<SSEntity> learnEpCirclesBefore,
    final SSLabel        originalLabel,
    final SSLabel        newLabel,
    final boolean        shouldCommit) throws SSErr{
    
    try{
      
      if(
        newLabel == null ||
        SSStrU.isEqual(originalLabel, newLabel)){
        return;
      }
      
      List<SSUri> entityURIs;
      
      for(SSEntity learnEpCircleBefore : learnEpCirclesBefore){
        
        if(!SSStrU.isEqual(learnEpCircle, learnEpCircleBefore)){
          continue;
        }
        
        entityURIs = commons.getEntityURIsFromLearnEpCircle((SSLearnEpCircle) learnEpCircleBefore);
        
        removeCategories(
          servPar,
          user,
          entityURIs,
          SSCategoryLabel.get(SSStrU.toStr(originalLabel)),
          shouldCommit);
        
        addCategory(
          servPar,
          user,
          entityURIs,
          SSCategoryLabel.get(SSStrU.toStr(newLabel)),
          shouldCommit);
        
        break;
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break; }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void handleRemoveLearnEpEntity(
    final SSServPar   servPar,
    final SSUri       user, 
    final SSUri       entity,
    final List<SSUri> learnEpEntityCircleURIsBefore,
    final List<SSUri> learnEpEntityCircleURIsAfter,
    final boolean     shouldCommit) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      SSEntity              circle;
      
      for(SSUri learnEpCircleBefore : learnEpEntityCircleURIsBefore){
        
        if(SSStrU.contains(learnEpEntityCircleURIsAfter, learnEpCircleBefore)){
          continue;
        }
        
        circle = 
          entityServ.entityGet(
            new SSEntityGetPar(
              servPar,
              user,
              learnEpCircleBefore, //entity
              false, //withUserRestriction
              null)); //descPar
        
        removeCategories(
          servPar,
          user,
          SSUri.asListNotNull(entity), //entities
          SSCategoryLabel.get(SSStrU.toStr(circle.label)),
          shouldCommit);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void handleAddLearnEpEntity(
    final SSServPar      servPar,
    final SSUri          user,
    final SSUri          entity,
    final List<SSUri>    learnEpEntityCircleURIsBefore,
    final List<SSUri>    learnEpEntityCircleURIsAfter,
    final boolean        shouldCommit) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      SSEntity              circle;
      
      for(SSUri learnEpCircleAfter : learnEpEntityCircleURIsAfter){
        
        if(SSStrU.contains(learnEpEntityCircleURIsBefore, learnEpCircleAfter)){
          continue;
        }
        
        circle = 
          entityServ.entityGet(
            new SSEntityGetPar(
              servPar,
              user,
              learnEpCircleAfter, //entity
              false, //withUserRestriction
              null)); //descPar
          
        addCategory(
          servPar,
          user,
          SSUri.asListNotNull(entity), //entities
          SSCategoryLabel.get(SSStrU.toStr(circle.label)),
          shouldCommit);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}