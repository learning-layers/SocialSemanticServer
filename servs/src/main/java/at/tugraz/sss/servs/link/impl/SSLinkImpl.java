 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

package at.tugraz.sss.servs.link.impl;

import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityContext;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSEntityUpdatePar;
import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.entity.datatype.SSServRetI;
import at.tugraz.sss.servs.db.impl.SSCoreSQL;

import at.tugraz.sss.servs.link.api.SSLinkClientI;
import at.tugraz.sss.servs.link.api.SSLinkServerI;
import at.tugraz.sss.servs.link.datatype.SSLinkAddPar;
import at.tugraz.sss.servs.link.datatype.SSLinkAddRet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.servs.common.api.SSGetUsersResourcesI;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;

public class SSLinkImpl
extends SSEntityImpl
implements
  SSLinkClientI,
  SSLinkServerI, 
  SSGetUsersResourcesI{
  
  private final SSLinkActAndLog                       actAndLog   = new SSLinkActAndLog();
  private final SSCoreSQL                             sql         = new SSCoreSQL(dbSQL);
  
  public SSLinkImpl(){
    super(SSCoreConf.instGet().getLink());
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
      
      entityUpdatePar.addUserToAdditionalAuthors = true;
        
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri link = entityUpdate(entityUpdatePar);
      
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