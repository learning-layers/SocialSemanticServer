/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.serv.entity.api;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import java.util.List;

public interface SSGetParentEntitiesI{
    
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception;
}


//  public List<SSUri> getParentEntities(final SSEntityUserParentEntitiesGetPar par) throws SSErr{
//    
//    try{
//      final List<SSUri>  entities     = new ArrayList<>();
//      final SSEntity     entity       = 
//        sql.getEntityTest(
//          par.user, 
//          par.entity, 
//          par.withUserRestriction);
//      
//      if(entity == null){
//        return entities;
//      }
//      
//      for(SSServContainerI serv : SSServReg.inst.getServsHandlingGetParentEntities()){
//        entities.addAll(((SSGetParentEntitiesI) serv.serv()).getParentEntities(par.user, par.entity, entity.type));
//      }
//      
//      SSStrU.distinctWithoutNull2(entities);
//      
//      return entities;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }