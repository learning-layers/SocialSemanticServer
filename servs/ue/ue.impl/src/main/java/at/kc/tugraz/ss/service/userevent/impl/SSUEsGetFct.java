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
package at.kc.tugraz.ss.service.userevent.impl;

import at.tugraz.sss.serv.impl.api.SSEntityServerI;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.kc.tugraz.ss.service.userevent.impl.fct.sql.SSUESQLFct;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.misc.SSEntityFiller;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import java.util.ArrayList;
import java.util.List;

public class SSUEsGetFct {
  
  private final SSEntityFiller      entityFiller = new SSEntityFiller();
  private final SSEntityServerI     entityServ;
  private final SSUESQLFct          sqlFct;
  
  public SSUEsGetFct(
    final SSEntityServerI  entityServ,
    final SSUESQLFct       sqlFct){
    
    this.entityServ = entityServ;
    this.sqlFct     = sqlFct;
  }

  public void setUEEntity(
    final SSUEsGetPar par,
    final SSUE        ue) throws Exception{
    
    try{
      
      if(ue.entity == null){
        return;
      }

      if(entityFiller.containsFilledEntity(ue.entity)){
        ue.entity = entityFiller.getFilledEntity(ue.entity);
      }else{

        final SSEntityDescriberPar descPar;
        
        if(par.invokeEntityHandlers){
          
          descPar          = new SSEntityDescriberPar(ue.id);
          descPar.setFlags = par.setFlags;
          descPar.setTags  = par.setTags;
        }else{
          descPar = null;
        }
        
        final SSEntityGetPar entityGetPar =
          new SSEntityGetPar(
            par.user,
            ue.entity.id, //entity,
            par.withUserRestriction, //withUserRestriction,
            descPar); //descPar
        
        final SSEntity filledEntity = entityServ.entityGet(entityGetPar);
        
        entityFiller.addFilledEntity(ue.entity.id, filledEntity);
        
        ue.entity = filledEntity;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void setUEUser(
    final SSUEsGetPar par, 
    final SSUE        ue) throws Exception{
    
    try{
      
      if(ue.user == null){
        return;
      }

      if(entityFiller.containsFilledEntity(ue.user)){
        ue.user = entityFiller.getFilledEntity(ue.user);
      }else{
        
        final SSEntityDescriberPar descPar = new SSEntityDescriberPar(ue.id);
        
        final SSEntityGetPar entityGetPar =
          new SSEntityGetPar(
            par.user,
            ue.user.id, //entity,
            par.withUserRestriction, //withUserRestriction,
            descPar); //descPar
        
        final SSEntity filledUser = entityServ.entityGet(entityGetPar);
        
        entityFiller.addFilledEntity(ue.user.id, filledUser);
        
        ue.user = filledUser;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public List<SSUri> getUEsToFill(
    final SSUEsGetPar par) throws Exception{
    
    try{
  
      if(!par.userEvents.isEmpty()){
        return par.userEvents;
      }
      
      if(par.withUserRestriction){
        
        if(par.entity != null){
          
          final SSEntity entity =
            sqlFct.getEntityTest(
              par.user,
              par.entity,
              par.withUserRestriction);
          
          if(entity == null){
            return new ArrayList<>();
          }
        }
      }
      
      return sqlFct.getUEURIs(
        par.forUser,
        par.entity,
        par.types,
        par.startTime,
        par.endTime);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}