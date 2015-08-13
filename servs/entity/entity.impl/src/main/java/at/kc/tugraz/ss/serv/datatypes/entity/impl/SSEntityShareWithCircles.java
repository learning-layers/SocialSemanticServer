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
package at.kc.tugraz.ss.serv.datatypes.entity.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;

public class SSEntityShareWithCircles {
  
  private final SSCircleServerI circleServ;
  
  public SSEntityShareWithCircles(final SSCircleServerI circleServ){
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
            SSUri.asListWithoutNullAndEmpty(entity.id),  //entities
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
              withUserRestriction, //withUserRestriction
              false)); //invokeEntityHandlers
        
        SSServCallerU.handleCircleEntitiesAdd(
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