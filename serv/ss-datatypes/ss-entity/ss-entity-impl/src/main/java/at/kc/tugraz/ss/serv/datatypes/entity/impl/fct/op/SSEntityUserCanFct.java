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
package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleRightE;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSEntityUserCanFct{

  public static void checkWhetherUserCanForEntityType(
    final SSEntitySQLFct sqlFct,
    final SSUri          user, 
    final SSEntity       entity,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      switch(entity.type){
        case entity: return; //TODO dtheiler: break down general entity types so that checks on e.g. videos will be present
        case circle: {
          
          try{
            SSEntityMiscFct.checkWhetherUserIsInCircle       (sqlFct, user,      entity.id);
            SSEntityMiscFct.checkWhetherCircleOfTypeHasRight (sqlFct, entity.id, accessRight);
          }catch(Exception error){
            throw new SSErr(SSErrE.userNotAllowedToAccessEntity);
          }
          
          break;
        }
        
        default:{
          if(!SSEntityMiscFct.doesUserHaveRightInAnyCircleOfEntity(sqlFct, user, entity.id, accessRight)){
            throw new SSErr(SSErrE.userDoesntHaveRightInAnyCircleOfEntity);
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}