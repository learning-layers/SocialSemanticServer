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
package at.tugraz.sss.servs.entity.impl;

import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.serv.datatype.par.SSCircleGetPar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSEntityCircle;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import java.util.ArrayList;
import java.util.List;

public class SSEntityShareWithCircles {
  
  private final SSEntityServerI circleServ;
  
  public SSEntityShareWithCircles(final SSEntityServerI circleServ){
    this.circleServ = circleServ;
  }
  
  public void handle(
    final SSUri       user,
    final SSEntity    entity,
    final List<SSUri> circles,
    final Boolean     withUserRestriction) throws Exception{
    
    try{
      
      final List<SSEntity> entities                = new ArrayList<>();
      SSEntityCircle       circle;
      
      entities.add(entity);
      
      for(SSUri circleURI : circles){
        
        circleServ.circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            user,
            circleURI,  //circle
            SSUri.asListNotNull(entity.id),  //entities
            withUserRestriction, //withUserRestriction
            false)); //shouldCommit
        
        circle =
          circleServ.circleGet(
            new SSCircleGetPar(
              user,
              circleURI, //circle
              null, //entityTypesToIncludeOnly
              false,  //setTags
              null, //tagSpace
              false, //setEntities,
              true, //setUsers
              withUserRestriction, //withUserRestriction
              false)); //invokeEntityHandlers
        
        SSServReg.inst.circleEntitiesAdded(
          user,
          circle,
          entities,
          withUserRestriction);
      }
    }catch(Exception error){
      SSServErrReg.reset();
    }
  }
}