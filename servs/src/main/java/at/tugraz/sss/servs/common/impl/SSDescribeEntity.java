/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.common.datatype.SSEntityDescriberPar;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.conf.SSConfA;
import at.tugraz.sss.servs.common.api.*;
import java.util.*;

public class SSDescribeEntity {
  
  protected static final List<SSDescribeEntityI> servsForDescribeEntity = new ArrayList<>();
  
  public void regServ(
    final SSDescribeEntityI serv, 
    final SSConfA           conf) throws SSErr{
    
    try{
      
      if(!conf.use){
        return;
      }
      
      synchronized(servsForDescribeEntity){
        
        if(servsForDescribeEntity.contains(serv)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForDescribeEntity.add(serv);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSUri                user,
    final SSEntity             entity,
    final SSEntityDescriberPar descPar,
    final boolean              withUserRestriction) throws SSErr{
    
    try{
      
      if(entity == null){
        return null;
      }
      
      if(descPar == null){
        return entity;
      }
        
      descPar.user                = user;
      descPar.withUserRestriction = withUserRestriction;
      
      SSEntity describedEntity = entity;
      
      for(SSDescribeEntityI serv : servsForDescribeEntity){
        describedEntity = serv.describeEntity(servPar, describedEntity, descPar);
      }

      return describedEntity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}