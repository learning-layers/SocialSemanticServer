/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.category.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.category.datatype.SSCategoriesRemovePar;
import at.tugraz.sss.servs.category.datatype.SSCategoryLabel;
import at.tugraz.sss.servs.category.datatype.SSCategoryAddPar;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.servs.activity.datatype.*;
import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSToolContextE;
import at.tugraz.sss.servs.activity.impl.*;
import at.tugraz.sss.servs.eval.api.*;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogE;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogPar;
import at.tugraz.sss.servs.eval.impl.*;

public class SSCategoryActAndLogFct {
  
  public void addCategory(
    final SSCategoryAddPar par, 
    final SSUri            categoryURI,
    final SSCategoryLabel  categoryLabel, 
    final boolean          shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = new SSActivityImpl();
      final SSEvalServerI     evalServ     = new SSEvalImpl();
      
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
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCategories(
    final SSCategoriesRemovePar par,
    final boolean               shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = new SSActivityImpl();
      final SSEvalServerI     evalServ     = new SSEvalImpl();
      
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
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}