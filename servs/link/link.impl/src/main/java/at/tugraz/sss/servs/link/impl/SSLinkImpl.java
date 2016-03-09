 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.link.impl;

import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.SSUri;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.datatype.enums.SSEntityE;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.db.api.SSCoreSQL;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import at.tugraz.sss.servs.link.api.SSLinkClientI;
import at.tugraz.sss.servs.link.api.SSLinkServerI;
import at.tugraz.sss.servs.link.datatype.par.SSLinkAddPar;
import at.tugraz.sss.servs.link.datatype.ret.SSLinkAddRet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSLinkImpl
extends SSServImplWithDBA
implements
  SSLinkClientI,
  SSLinkServerI, 
  SSUsersResourcesGathererI{
  
  private final  SSLinkActAndLog actAndLog   = new SSLinkActAndLog();
  private final  SSUserCommons   userCommons = new SSUserCommons();
  private final  SSCoreSQL       sql;
  
  public SSLinkImpl(final SSConfA conf) throws SSErr{
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql = new SSCoreSQL(dbSQL);
  }
  
  @Override
  public void getUsersResources(
    final SSServPar                          servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      final List<SSEntityE> types = new ArrayList<>();
      
      types.add(SSEntityE.link);
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        for(SSUri entity :
          sql.getAccessibleURIs(
            servPar,
            SSConf.systemUserUri,
            userID, //user
            types, //types
            null,//authors
            null, //startTime
            null)){ //endTime
          
          usersEntities.get(user).add(
            new SSEntityContext(
              entity,
              SSEntityE.link,
              null,
              null));
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI linkAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLinkAddPar par = (SSLinkAddPar) parA.getFromClient(clientType, parA, SSLinkAddPar.class);
      
      return new SSLinkAddRet(linkAdd(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri linkAdd(final SSLinkAddPar par) throws SSErr{
    
    try{
      
      final SSEntityServerI   entityServ      = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntityUpdatePar entityUpdatePar =
        new SSEntityUpdatePar(
          par,  //servPaR
          par.user, //user
          par.link, //entity
          SSEntityE.link, //type
          par.label, //label
          par.description, //descriptiion
          null, //creationTime,
          null, //read,
          false, //setPublic,
          true, //createIfNotExists,
          par.withUserRestriction,
          false); //shouldCommit
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri link = entityServ.entityUpdate(entityUpdatePar);
      
      if(link == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.addLink(
        par,
        par.user,
        link,
        par.shouldCommit);
      
      return link;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}