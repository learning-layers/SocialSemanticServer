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
package at.kc.tugraz.ss.serv.job.accessrights.impl;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.job.accessrights.api.SSAccessRightsClientI;
import at.kc.tugraz.ss.serv.job.accessrights.api.SSAccessRightsServerI;
import at.kc.tugraz.ss.serv.job.accessrights.impl.fct.sql.SSAccessRightsFct;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import java.lang.reflect.Method;
import java.util.*;

public class SSAccessRightsImpl extends SSServImplWithDBA implements SSAccessRightsClientI, SSAccessRightsServerI{

  private final SSAccessRightsFct sqlFct;

  public SSAccessRightsImpl(final SSServConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    sqlFct = new SSAccessRightsFct(dbSQL);
  }
  
  /* SSServRegisterableImplI */
  @Override
  public List<SSMethU> publishClientOps() throws Exception{

    List<SSMethU> clientOps = new ArrayList<SSMethU>();

    Method[] methods = SSAccessRightsClientI.class.getMethods();

    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }

    return clientOps;
  }

  @Override
  public List<SSMethU> publishServerOps() throws Exception{

    List<SSMethU> serverOps = new ArrayList<SSMethU>();

    Method[] methods = SSAccessRightsServerI.class.getMethods();

    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }

    return serverOps;
  }

  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSAccessRightsClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }

  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSAccessRightsServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }

  /* SSAccessRightsClientI */
//  @Override
//  public void collUserParentGet(SSSocketCon sSCon, SSServPar par) throws Exception{
//
//    SSServCaller.checkKey(par);
//
//    SSColl collParent = collUserParentGet(par);
//
//    sSCon.writeRetFullToClient(SSCollUserParentGetRet.get(collParent, par.op));
//  }

   /*  SSAccessRightsServerI */
   
//  @Override
//  public Boolean collUserRootAdd(final SSServPar parA) throws Exception{
//
//    final SSCollUserRootAddPar par = new SSCollUserRootAddPar(parA);
//    final SSUri rootCollUri;
//
//    try{
//
//      if(sqlFct.existsUserRootColl(par.user)){
//        return true;
//      }
//
//      rootCollUri = sqlFct.createCollURI();
//
//      SSServCaller.addEntity(
//        par.user,
//        rootCollUri,
//        SSLabelStr.get(SSStrU.valueRoot),
//        SSEntityEnum.coll);
//
//      dbSQL.startTrans(par.shouldCommit);
//
//      sqlFct.createColl     (rootCollUri);
//      sqlFct.addUserRootColl(rootCollUri, par.user);
//
//      dbSQL.commit(par.shouldCommit);
//
//      return true;
//    }catch(Exception error){
//      dbSQL.rollBack(par.shouldCommit);
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
}