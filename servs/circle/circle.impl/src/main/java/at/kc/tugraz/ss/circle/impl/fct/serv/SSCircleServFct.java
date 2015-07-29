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
package at.kc.tugraz.ss.circle.impl.fct.serv;

import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddPar;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import java.util.List;

public class SSCircleServFct{

  public static void addTags(
    final SSUri           user,
    final List<String>    tags, 
    final List<SSUri>     entities,
    final SSUri           circleAsSpace) throws Exception{
    
    try{
      
      if(tags.isEmpty()){
        return;
      }
      
      for(SSUri entity : entities){
        
        ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsAdd(
          new SSTagsAddPar(
            null,
            null,
            user,
            SSTagLabel.get(tags), //labels
            entity, //entity
            null, //space
            circleAsSpace, //circle
            null, //creationTime,
            true, //withUserRestriction
            false)); //shouldCommit)
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addCategories(
    final SSUri           user,
    final List<String>    categories, 
    final List<SSUri>     entities,
    final SSUri           circleAsSpace) throws Exception{
    
    try{
      
      if(categories.isEmpty()){
        return;
      }
      
      for(SSUri entity : entities){
        
        ((SSCategoryServerI) SSServReg.getServ(SSCategoryServerI.class)).categoriesAdd(
          new SSCategoriesAddPar(
            null,
            null,
            user,
            SSCategoryLabel.get(categories), //labels
            entity, //file
            null, //space
            circleAsSpace, //circle
            null, //creationTime,
            true, //withUserRestriction
            false)); //shouldCommit)
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}