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
package at.kc.tugraz.sss.flag.impl;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.sss.flag.api.SSFlagClientI;
import at.kc.tugraz.sss.flag.api.SSFlagServerI;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsUserGetRet;
import at.kc.tugraz.sss.flag.datatypes.SSFlag;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsUserGetPar;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsUserSetPar;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsUserSetRet;
import at.kc.tugraz.sss.flag.impl.fct.sql.SSFlagSQLFct;
import java.util.List;

public class SSFlagImpl extends SSServImplWithDBA implements SSFlagClientI, SSFlagServerI, SSEntityDescriberI{
  
  private final SSFlagSQLFct sqlFct;
  
  public SSFlagImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    this.sqlFct = new SSFlagSQLFct(dbSQL);
  }
  
  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityDescGetPar par,
    final SSEntityDescA      entityDesc) throws Exception{
    
    if(par.getFlags){
      
      entityDesc.flags.addAll(
        SSServCaller.flagsUserGet(
          par.user,
          SSUri.asListWithoutNullAndEmpty(par.entity),
          SSStrU.toStrWithoutEmptyAndNull(),
          null,
          null));
    }
    
    return entityDesc;
  }
  
  @Override
  public void flagsSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSFlagsUserSetRet.get(flagsUserSet(parA), parA.op));
  }

  @Override
  public Boolean flagsUserSet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSFlagsUserSetPar par = new SSFlagsUserSetPar(parA);
      
      for(SSUri entity : par.entities){
       
        SSServCaller.entityAdd(
          par.user, 
          entity, 
          null, 
          SSEntityE.entity,
          null,
          false);
        
        if(!SSServCaller.entityUserCanEdit(par.user, entity)){
         throw new Exception("user cannot edit entity");
        }
      }
      
      for(SSUri entity : par.entities){
       
        for(SSFlagE flag : par.types){
          
          SSUri flagUri = SSServCaller.vocURICreate();
          
          SSServCaller.entityAdd(
            par.user,
            flagUri,
            null,
            SSEntityE.flag,
            null,
            false);
          
          sqlFct.createFlag(
            flagUri, 
            flag, 
            par.endTime, 
            par.value);
          
          sqlFct.addFlagAssIfNotExists(
            par.user,
            flagUri,
            entity);
        }
      }
      
      return true;
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return flagsUserSet(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void flagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSFlagsUserGetRet.get(flagsUserGet(parA), parA.op));
  }

  @Override
  public List<SSFlag> flagsUserGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSFlagsUserGetPar par = new SSFlagsUserGetPar(parA);
      
      for(SSUri entity : par.entities){
       
        if(!SSServCaller.entityUserCanRead(par.user, entity)){
         throw new Exception("user cannot read entity");
        }
      }
      
      return sqlFct.getFlags(
        SSUri.asListWithoutNullAndEmpty(par.user),
        par.entities,
        par.types, 
        par.startTime,
        par.endTime);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}