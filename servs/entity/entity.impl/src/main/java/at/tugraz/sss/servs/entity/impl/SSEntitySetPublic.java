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
import at.tugraz.sss.serv.datatype.par.SSCirclePubURIGetPar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSEntityCircle;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.*;
import java.util.ArrayList;
import java.util.List;

public class SSEntitySetPublic {
  
  private final SSEntityServerI circleServ;
  
  public SSEntitySetPublic(final SSEntityServerI circleServ){
    this.circleServ = circleServ;
  }
  
  public void handle(
    final SSUri       user,
    final SSEntity    entity,
    final boolean     withUserRestriction) throws Exception{
    
    try{
      
      final List<SSEntity> entities  = new ArrayList<>();
      
      entities.add(entity);
      
      final SSUri pubCircleURI =
        circleServ.circlePubURIGet(
          new SSCirclePubURIGetPar(
            user,
            false));
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          user,
          pubCircleURI,  //circle
          SSUri.asListNotNull(entity.id),  //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      final SSEntityCircle circle =
        circleServ.circleGet(
          new SSCircleGetPar(
            user,
            pubCircleURI,
            null, //entityTypesToIncludeOnly,
            false, //setTags,
            null, //tagSpace,
            false, //setEntities,
            true, //setUsers
            false, //withUserRestriction
            false)); //invokeEntityHandlers));
      
      SSServReg.inst.circleEntitiesAdded(
        user,
        circle,
        entities,
        withUserRestriction);
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
}
