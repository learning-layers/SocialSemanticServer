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

import at.kc.tugraz.ss.category.api.*;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.*;
import at.kc.tugraz.ss.recomm.conf.*;
import at.kc.tugraz.ss.recomm.datatypes.par.*;
import at.kc.tugraz.ss.service.user.datatypes.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import engine.*;
import java.util.*;

public class SSRecommTagCommons {
  
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
    final Algorithm       algo) throws SSErr{
    
    try{
      
      if(!par.categories.isEmpty()){
        return par.categories;
      }
      
      if(par.entity == null){
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
                SSUri.asListNotNull(par.entity), //entities
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
}