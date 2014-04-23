 /**
  * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.List;

public class SSEntityMiscFct{
  
  public static void addEntitiesToCircleByEntityHandlers(
    final SSUri                              userUri,
    final List<SSUri>                        entityUris,
    final SSUri                              circleUri) throws Exception{
    
    if(SSObjU.isNull(entityUris, circleUri)){
      return;
    }
    
    try{
      
      Boolean handledEntity;
      
      for(SSUri entityUri : entityUris){
        
        final SSEntityEnum entityType = SSServCaller.entityTypeGet(entityUri);
        
        if(SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
          continue;
        }
        
        handledEntity = false;
        
        for(SSServA serv : SSServA.getServsManagingEntities()){
          
          if(((SSEntityHandlerImplI) serv.serv()).addEntityToCircle(userUri, circleUri, entityUri, entityType)){
            handledEntity = true;
            break;
          }
        }
        
        if(!handledEntity){
          throw new Exception("entity couldnt not be added to circle by entity handlers");
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
