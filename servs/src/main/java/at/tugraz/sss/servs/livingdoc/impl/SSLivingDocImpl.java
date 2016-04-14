/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.livingdoc.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.disc.api.SSDiscServerI;
import at.tugraz.sss.servs.disc.datatype.SSDiscTargetsAddPar;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.common.api.SSDescribeEntityI;
import at.tugraz.sss.servs.entity.datatype.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSEntityContext;
import at.tugraz.sss.servs.common.datatype.SSEntityDescriberPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityUpdatePar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.common.api.SSPushEntitiesToUsersI;
import at.tugraz.sss.servs.common.datatype.SSPushEntitiesToUsersPar;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.entity.datatype.SSServRetI; 
import at.tugraz.sss.servs.livingdoc.api.SSLivingDocClientI;
import at.tugraz.sss.servs.livingdoc.api.SSLivingDocServerI;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocument;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocAddPar;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocGetPar;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocRemovePar;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocUpdatePar;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocsGetPar;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocAddRet;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocGetRet;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocRemoveRet;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocUpdateRet;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocsGetRet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.servs.common.api.SSGetUsersResourcesI;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.disc.impl.*;
import at.tugraz.sss.servs.entity.impl.*;

public class SSLivingDocImpl 
extends SSEntityImpl
implements
  SSLivingDocClientI,
  SSLivingDocServerI,
  SSDescribeEntityI,
  SSPushEntitiesToUsersI, 
  SSGetUsersResourcesI{
  
  private final SSLivingDocSQLFct                     sql          = new SSLivingDocSQLFct(dbSQL);
  private final SSLivingDocActAndLogFct               actAndLogFct = new SSLivingDocActAndLogFct();
  
  public SSLivingDocImpl(){
    super(SSCoreConf.instGet().getLivingDocument());
  }
  
  @Override
  public void getUsersResources(
    final SSServPar                          servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSLivingDocsGetPar ldsGetPar =
        new SSLivingDocsGetPar(
          servPar,
          null, //user
          null, //forUser
          false, //withUserRestriction
          false); //invokeEntityHandlers
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        ldsGetPar.user    = userID;
        ldsGetPar.forUser = userID;
        
        for(SSEntity doc : livingDocsGet(ldsGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              doc.id,
              SSEntityE.livingDoc,
              null,
              null));
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    switch(entity.type){
      
      case livingDoc:{
        
        return SSLivingDocument.get(
          livingDocGet(
            new SSLivingDocGetPar(
              servPar,
              par.user,
              entity.id,
              par.withUserRestriction,
              false)),
          entity);
      }
      
      default: return entity;
    }
  }
  
  @Override
  public void pushEntitiesToUsers(
    final SSServPar                servPar,
    final SSPushEntitiesToUsersPar par) throws SSErr {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          
          case livingDoc: {
            
            for(SSUri userToPushTo : par.users){
              sql.addLivingDoc(servPar, entityToPush.id, userToPushTo);
            }
            
            break;
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI livingDocAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLivingDocAddPar par          = (SSLivingDocAddPar) parA.getFromClient(clientType, parA, SSLivingDocAddPar.class);
      final SSUri             livingDocURI = livingDocAdd(par);
      
      if(livingDocURI != null){
        
        if(par.discussion != null){
          
          final SSDiscServerI discServ = new SSDiscImpl();
          final List<SSUri>   targets  = new ArrayList<>();
          
          targets.add(livingDocURI);
          
          discServ.discTargetsAdd(
            new SSDiscTargetsAddPar(
              par,
              par.user,
              par.discussion,
              targets,
              par.withUserRestriction,
              par.shouldCommit));
        }
      }
      
      return SSLivingDocAddRet.get(livingDocURI);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri livingDocAdd(final SSLivingDocAddPar par) throws SSErr{
    
    try{
      
      if(par.uri == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSUri            livingDocUri;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      livingDocUri =
        entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            par.uri,
            SSEntityE.livingDoc, //type,
            par.label, //label
            par.description,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(livingDocUri == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.createLivingDoc(par, par.uri, par.user);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLogFct.createLivingDoc(
        par,
        livingDocUri,
        par.shouldCommit);
      
      return livingDocUri;
      
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
  
  @Override
  public SSServRetI livingDocUpdate(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLivingDocUpdatePar par          = (SSLivingDocUpdatePar) parA.getFromClient(clientType, parA, SSLivingDocUpdatePar.class);
      final SSUri                livingDocURI = livingDocUpdate(par);
      
      if(livingDocURI != null){
        
        if(par.discussion != null){
          
          final SSDiscServerI discServ = new SSDiscImpl();
          final List<SSUri>   targets  = new ArrayList<>();
          
          targets.add(livingDocURI);
          
          discServ.discTargetsAdd(
            new SSDiscTargetsAddPar(
              par,
              par.user,
              par.discussion,
              targets,
              par.withUserRestriction,
              par.shouldCommit));
        }
      }
      
      return SSLivingDocUpdateRet.get(livingDocURI);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri livingDocUpdate(final SSLivingDocUpdatePar par) throws SSErr{
    
    try{
      
      final SSUri            livingDocURI;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      livingDocURI =
        entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            par.livingDoc,
            SSEntityE.livingDoc, //type,
            par.label, //label
            par.description,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            false, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLogFct.updateLivingDoc(
        par, 
        par.shouldCommit);
      
      return livingDocURI;
      
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
  
  @Override
  public SSServRetI livingDocRemove(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLivingDocRemovePar par       = (SSLivingDocRemovePar) parA.getFromClient(clientType, parA, SSLivingDocRemovePar.class);
      final SSUri                livingDoc = livingDocRemove(par);
      
      return SSLivingDocRemoveRet.get(livingDoc);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri livingDocRemove(final SSLivingDocRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity livingDoc = 
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user, 
          par.livingDoc, 
          par.withUserRestriction);

      if(livingDoc == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.removeLivingDoc(par, par.livingDoc);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLogFct.removeLivingDoc(
        par,
        par.livingDoc,
        par.shouldCommit);
      
      return par.livingDoc;
      
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
  
  @Override
  public SSServRetI livingDocGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    userCommons.checkKeyAndSetUser(parA);
    
    final SSLivingDocGetPar par = (SSLivingDocGetPar) parA.getFromClient(clientType, parA, SSLivingDocGetPar.class);
    
    return SSLivingDocGetRet.get(livingDocGet(par));
  }
  
  @Override
  public SSLivingDocument livingDocGet(final SSLivingDocGetPar par) throws SSErr{

    try{
      
      SSLivingDocument livingDoc = sql.getLivingDoc(par, par.livingDoc);
      
      if(livingDoc == null){
        return null;
      }
      
      SSEntityDescriberPar   descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.livingDoc);
        
        descPar.setDiscs             = par.setDiscs;
        descPar.setAttachedEntities  = par.setAttachedEntities;
      }else{
        descPar = null;
      }

      final SSEntity livingDocEntity  =
        entityGet(
          new SSEntityGetPar(
            par,
            par.user,
            par.livingDoc,
            par.withUserRestriction,
            descPar));
        
      if(livingDocEntity == null){
        return null;
      }
        
      livingDoc = 
        SSLivingDocument.get(
          livingDoc, 
          livingDocEntity);
      
      if(!par.setUsers){
        return livingDoc;
      }

      final List<SSUri> userURIs = sql.getLivingDocUserURIs(par, livingDoc.id);
      
      descPar = new SSEntityDescriberPar(livingDoc.id);
      
      livingDoc.users.addAll(
        entitiesGet(
          new SSEntitiesGetPar(
            par,
            par.user,
            userURIs, //entities
            descPar, //descPar
            par.withUserRestriction)));
      
      return livingDoc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI livingDocsGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    userCommons.checkKeyAndSetUser(parA);
    
    final SSLivingDocsGetPar par = (SSLivingDocsGetPar) parA.getFromClient(clientType, parA, SSLivingDocsGetPar.class);
    
    return SSLivingDocsGetRet.get(livingDocsGet(par));
  }
  
  @Override
  public List<SSEntity> livingDocsGet(final SSLivingDocsGetPar par) throws SSErr{

    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(!SSStrU.isEqual(par.forUser, par.user)){
          par.forUser = par.user;
        }
      }
      
      final List<SSUri> livinDocURIs;
      
      if(par.forUser == null){
        livinDocURIs = sql.getLivingDocURIs(par);
      }else{
        livinDocURIs = sql.getLivingDocURIsForUser(par, par.forUser);
      }
      
      final List<SSEntity>    docs            = new ArrayList<>();
      final SSLivingDocGetPar livingDocGetPar =
        new SSLivingDocGetPar(
          par,
          par.user,
          null, //livingDoc
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      livingDocGetPar.setUsers            = par.setUsers;
      livingDocGetPar.setDiscs            = par.setDiscs;
      livingDocGetPar.setAttachedEntities = par.setAttachedEntities;
      
      for(SSUri livingDocUri : livinDocURIs){
      
        livingDocGetPar.livingDoc = livingDocUri;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          docs,
          livingDocGet(livingDocGetPar));
      }
      
      return docs;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
