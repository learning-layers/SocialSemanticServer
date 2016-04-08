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
package at.tugraz.sss.servs.learnep.impl;

import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpEntity;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpVersion;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.misc.SSEntityFiller;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.servs.learnep.datatype.*;
import java.util.List;

public class SSLearnEpVersionsGetFct {

  private final SSEntityFiller        entityFiller =  new SSEntityFiller();
  private final SSEntityServerI       entityServ;
  private final SSLearnEpSQL          sqlFct;
  
  public SSLearnEpVersionsGetFct(
    final SSEntityServerI  entityServ, 
    final SSLearnEpSQL  sqlFct){
    
    this.entityServ = entityServ;
    this.sqlFct     = sqlFct;
  }
  
  public void setLearnEpVersionCircles(
    final SSServPar servPar, 
    final SSLearnEpVersion        learnEpVersion) throws SSErr{
    
    try{
      learnEpVersion.learnEpCircles.addAll(sqlFct.getLearnEpVersionCircles(servPar, learnEpVersion.id));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setLearnEpVersionEntities(
    final SSLearnEpVersionsGetPar par,
    final SSLearnEpVersion        learnEpVersion) throws SSErr{
    
    try{
      
      final SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(learnEpVersion.id);
        
        descPar.setOverallRating = true;
        descPar.setTags          = true;
      }else{
        descPar = null;
      }
      
      final SSEntityGetPar entityGetPar =
        new SSEntityGetPar(
          par,
          par.user,
          null, //entity
          par.withUserRestriction, //withUserRestriction
          descPar);
      
      SSLearnEpEntity learnEpEntity;
      SSEntity        filledEntity;
      
      for(SSEntity entity : sqlFct.getLearnEpVersionEntities(par, learnEpVersion.id)){
        
        learnEpEntity = (SSLearnEpEntity) entity;
        
        if(learnEpEntity.entity == null){
          continue;
        }
        
        if(entityFiller.containsFilledEntity(learnEpEntity.entity)){
          
          learnEpEntity.entity = entityFiller.getFilledEntity(learnEpEntity.entity);
          
          SSEntity.addEntitiesDistinctWithoutNull(learnEpVersion.learnEpEntities, learnEpEntity);
          continue;
        }
        
        entityGetPar.entity   = learnEpEntity.entity.id;
        filledEntity          = entityServ.entityGet(entityGetPar);

        entityFiller.addFilledEntity(learnEpEntity.entity.id, filledEntity);

        learnEpEntity.entity = filledEntity;
        
        SSEntity.addEntitiesDistinctWithoutNull(learnEpVersion.learnEpEntities, learnEpEntity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public List<SSUri> getLearnEpVersionURIsToFill(
    final SSLearnEpVersionsGetPar par) throws SSErr {
    
    try{
      
      if(par.learnEp != null){
        return sqlFct.getLearnEpVersionURIs(par, par.learnEp);
      }
      
      return par.learnEpVersions;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
