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
package at.tugraz.sss.servs.livingdocument.impl;

import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.livingdocument.api.SSLivingDocumentClientI;
import at.tugraz.sss.servs.livingdocument.api.SSLivingDocumentServerI;
import at.tugraz.sss.servs.livingdocument.conf.SSLivingDocumentConf;
import at.tugraz.sss.servs.livingdocument.datatype.SSLivingDocument;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocumentAddPar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocumentGetPar;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;

public class SSLivingDocumentImpl 
extends SSServImplWithDBA
implements
  SSLivingDocumentClientI,
  SSLivingDocumentServerI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI,
  SSPushEntitiesToUsersI{
  
  private final SSLivingDocumentSQLFct  sqlFct;
  private final SSLivingDocumentConf    livingDocumentConf;
  
  public SSLivingDocumentImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.livingDocumentConf  = (SSLivingDocumentConf) conf;
    this.sqlFct               = new SSLivingDocumentSQLFct(this);
//    this.entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
//    this.circleServ   = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    switch(entity.type){
    
      case livingDocument:{
        
        return SSLivingDocument.get(
          livingDocumentGet(
            new SSLivingDocumentGetPar(
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
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws Exception{
    
    try{
      final List<SSUri>    affiliatedURIs     = new ArrayList<>();
      final List<SSEntity> affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        switch(entityAdded.type){
          
          case livingDocument:{
            
            if(SSStrU.contains(par.recursiveEntities, entityAdded)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityAdded.id);
            }
            
            affiliatedURIs.clear();
            
//            for(SSUri learnEpContentURI : 
//              miscFct.getLearnEpContentURIs(
//                par.user, 
//                sqlFct, 
//                entityAdded.id, 
//                par.withUserRestriction)){
//            
//              if(SSStrU.contains(par.recursiveEntities, learnEpContentURI)){
//                continue;
//              }
//              
//              SSUri.addDistinctWithoutNull(
//                affiliatedURIs,
//                learnEpContentURI);
//            }
//            
//            SSEntity.addEntitiesDistinctWithoutNull(
//              affiliatedEntities,
//              entityServ.entitiesGet(
//                new SSEntitiesGetPar(
//                  par.user,
//                  affiliatedURIs,
//                  null, //types,
//                  null, //descPar
//                  par.withUserRestriction)));
//            
//            circleServ.circleEntitiesAdd(
//              new SSCircleEntitiesAddPar(
//                par.user,
//                par.circle,
//                affiliatedURIs,
//                false, //withUserRestriction
//                false)); //shouldCommit
            
            break;
          }
        }
      }
      
      if(affiliatedEntities.isEmpty()){
        return affiliatedEntities;
      }
      
      par.entities.clear();
      par.entities.addAll(affiliatedEntities);
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingAddAffiliatedEntitiesToCircle()){
        ((SSAddAffiliatedEntitiesToCircleI) serv.serv()).addAffiliatedEntitiesToCircle(par);
      }
      
      return affiliatedEntities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void pushEntitiesToUsers(
    final SSPushEntitiesToUsersPar par) throws Exception {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          case learnEp: {
            
            for(SSUri userToPushTo : par.users){
              
//              if(sqlFct.ownsUserLivingDocument(userToPushTo, entityToPush.id)){
//                continue;
//              }
//              
//              sqlFct.addLivingDocument(entityToPush.id, userToPushTo);
            }
            
            break;
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
//  @Override
//  public void livingDocumentsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
//    
//    SSServCallerU.checkKey(parA);
//
//    final SSLivingDocumentsGetPar par = (SSLivingDocumentsGetPar) parA.getFromJSON(SSLivingDocumentsGetPar.class);
//    
//    sSCon.writeRetFullToClient(SSLivingDocumentsGetRet.get(livingDocumentsGet(par)));
//  }
  
//  @Override
//  public List<SSEntity> livingDocumentsGet(final SSLearnEpsGetPar par) throws Exception{
//
//    try{
//      
//      if(par.withUserRestriction){
//        
//        if(par.user == null){
//          throw new SSErr(SSErrE.parameterMissing);
//        }
//      }
//      
//      final List<SSEntity>     livingDocuments = new ArrayList<>();
//      SSLivingDocumentGetPar   livingDocumentGetPar;
//        
//      for(SSUri livingDocumentURI : sqlFct.getLivingDocumentURIsForUser(par.user)){
//
//        livingDocumentGetPar =
//          new SSLivingDocumentGetPar(
//            par.user,
//            livingDocumentURI,
//            par.withUserRestriction,
//            par.invokeEntityHandlers);
//        
//        SSEntity.addEntitiesDistinctWithoutNull(
//          livingDocuments, 
//          livingDocumentGet(livingDocumentGetPar));          
//      }
//
//      return livingDocuments;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
  
  
  @Override
  public void livingDocumentAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSLivingDocumentAddPar par = (SSLivingDocumentAddPar) parA.getFromJSON(SSLivingDocumentAddPar.class);
    
//    sSCon.writeRetFullToClient(SSLivingDocumentAddRet.get(livingDocumentAdd(par)));
  }
  
  @Override
  public SSLivingDocument livingDocumentGet(final SSLivingDocumentGetPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
       
        if(!SSServCallerU.canUserRead(par.user, par.livingDocument)){
          return null;
        }
      }
      
      final SSLivingDocument livingDocument = null; 
      SSEntityDescriberPar   descPar;
      
      if(par.invokeEntityHandlers){
        descPar                  = new SSEntityDescriberPar(par.livingDocument);
      }else{
        descPar = null;
      }
        
//      livingDocument =
//        SSLivingDocument.get(
//          sqlFct.getLearnEp(par.learnEp),
//          entityServ.entityGet(
//            new SSEntityGetPar(
//              par.user,
//              par.learnEp,
//              par.withUserRestriction,
//              descPar)));
//      
//      if(par.invokeEntityHandlers){
//        descPar = new SSEntityDescriberPar(null);
//      }else{
//        descPar = null;
//      }

      return livingDocument;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
