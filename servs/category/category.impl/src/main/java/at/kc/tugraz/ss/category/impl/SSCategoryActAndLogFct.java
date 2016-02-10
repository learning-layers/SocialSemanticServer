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
package at.kc.tugraz.ss.category.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.enums.*;
import at.kc.tugraz.ss.activity.datatypes.par.*;
import at.kc.tugraz.ss.category.datatypes.*;
import at.kc.tugraz.ss.category.datatypes.par.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.util.*;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSCategoryActAndLogFct {
  
  private final SSActivityServerI activityServ;
  private final SSEvalServerI     evalServ;
  
  public SSCategoryActAndLogFct(
    final SSActivityServerI activityServ,
    final SSEvalServerI     evalServ){
    
    this.activityServ = activityServ;
    this.evalServ     = evalServ;
  }
  
  public void addCategory(
    final SSCategoryAddPar par, 
    final SSUri            categoryURI,
    final SSCategoryLabel  categoryLabel, 
    final boolean          shouldCommit) throws SSErr{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par, 
          par.user,
          SSActivityE.addCategory,
          par.entity,
          null, //users
          SSUri.asListNotNull(categoryURI), //entities
          null, //comments
          null, //creationTime
          par.shouldCommit));
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.addCategory,
          par.entity, //entity
          SSStrU.toStr(categoryLabel), //content
          null, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCategories(
    final SSCategoriesRemovePar par,
    final boolean               shouldCommit) throws SSErr{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par,
          par.user,
          SSActivityE.removeCategories,
          par.entity,
          null,
          null,
          null,
          null,
          par.shouldCommit));
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.removeCategory,
          par.entity, //entity
          SSStrU.toStr(par.label), //content
          null, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}