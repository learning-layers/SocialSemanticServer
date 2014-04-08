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
package at.kc.tugraz.ss.serv.datatypes.entity.impl;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAddAtCreationTimePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAuthorGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCreationTimeGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityLabelGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityLabelSetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityTypeGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityLabelGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityLabelSetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityTypeGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserDirectlyAdjoinedEntitiesRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.graph.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SSEntityImpl extends SSServImplWithDBA implements SSEntityClientI, SSEntityServerI{
  
//  private final SSEntityGraphFct       graphFct;
  private final SSEntitySQLFct         sqlFct;
  
  public SSEntityImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct  = new SSEntityGraphFct (this);
    sqlFct    = new SSEntitySQLFct   (this);
  }
  
  /****** SSServRegisterableImplI ******/
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
      
    Method[] methods = SSEntityClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSEntityServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSEntityClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSEntityServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  /****** SSEntityClientI ******/
  @Override
  public void entityTypeGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityTypeGetRet.get(entityTypeGet(par), par.op));
  }
  
  @Override
  public void entityDescGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityDescGetRet.get(entityDescGet(par), par.op));
  }
  
  @Override
  public void entityLabelGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityLabelGetRet.get(entityLabelGet(par), par.op));
  }
 
  @Override
  public void entityLabelSet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityLabelSetRet.get(entityLabelSet(par), par.op));
  }
  
  @Override
  public void entityUserDirectlyAdjoinedEntitiesRemove(final SSSocketCon sSCon, final SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityUserDirectlyAdjoinedEntitiesRemoveRet.get(entityUserDirectlyAdjoinedEntitiesRemove(par), par.op));
  }
  
  /* SSEntityServerI */
  
  @Override
  public SSUri entityLabelSet(SSServPar parI) throws Exception{
    
    SSEntityLabelSetPar par = new SSEntityLabelSetPar(parI);
    
    try{
      
      if(!sqlFct.existsEntity(par.entityUri)){
        return par.entityUri;
      }
          
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.entityLabelSet(par.entityUri, par.label);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entityUri;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityRemove(SSServPar parI) throws Exception{
    
    SSEntityRemovePar par = new SSEntityRemovePar(parI);
    
    try{
      
      if(!sqlFct.existsEntity(par.entityUri)){
        return par.entityUri;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.entityRemove(par.entityUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entityUri;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityUserDirectlyAdjoinedEntitiesRemove(final SSServPar parA) throws Exception{
    
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par = new SSEntityUserDirectlyAdjoinedEntitiesRemovePar(parA);
    
    try{
      final SSEntity entity = sqlFct.getEntity(par.entityUri);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        ((SSEntityHandlerImplI) serv.serv()).removeDirectlyAdjoinedEntitiesForUser(
          entity.type,
          par,
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entityUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  @Override
  public SSUri entityAdd(SSServPar parA) throws Exception {
    
    SSEntityAddPar par = new SSEntityAddPar(parA);
    
    try{
      
      if(sqlFct.existsEntity(par.entityUri)){
        return par.entityUri;
      }
          
      sqlFct.addEntity(par.entityUri, par.label, par.entityType, par.user);
      
      return par.entityUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityAddAtCreationTime(SSServPar parI) throws Exception {
    
    SSEntityAddAtCreationTimePar par = new SSEntityAddAtCreationTimePar(parI);
    
    try{

      if(sqlFct.existsEntity(par.entityUri)){
        return par.entityUri;
      }
      
      sqlFct.addEntityAtCreationTime(par.entityUri, par.label, par.creationTime, par.entityType, par.user);
      
      return par.entityUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  //TODO dtheiler: to re-implement (because of table layout changes)
  @Override
  public void entityRemoveAll(SSServPar parA) throws Exception{
    
    throw new UnsupportedOperationException("entityRemoveAll has to be reimplemented");
    
//    SSEntityRemoveAllPar par = new SSEntityRemoveAllPar(parA);
//    
//    try{
//      
//      dbSQL.dbSQLStartTrans(par.shouldCommit);
//      
//      sqlFct.removeAllEntities();
//      
//      dbSQL.dbSQLCommit(par.shouldCommit);
//    }catch(Exception error){
//      dbSQL.dbSQLRollBack(par.shouldCommit);
//      SSServReg.addAndThrow(error);
//    }
  }
  
  @Override
  public SSLabelStr entityLabelGet(SSServPar parI) throws Exception{
    
    SSEntityLabelGetPar par = new SSEntityLabelGetPar(parI);
    
    try{
          
      if(!sqlFct.existsEntity(par.entityUri)){
        return SSLabelStr.get(SSStrU.empty);
      }
      
      return sqlFct.entityLabelGet(par.entityUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityAuthorGet(SSServPar parA) throws Exception{
    
    SSEntityAuthorGetPar par = new SSEntityAuthorGetPar(parA);
    
    try{
          
      if(!sqlFct.existsEntity(par.entityUri)){
        return null;
      }
      
      return sqlFct.getEntityAuthor(par.entityUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Long entityCreationTimeGet(SSServPar parI) throws Exception {
    
    SSEntityCreationTimeGetPar par    = new SSEntityCreationTimeGetPar(parI);
    
    try{
      
      if(!sqlFct.existsEntity(par.entityUri)){
        return 0L;
      }
      
      return sqlFct.getEntityCreationTime(par.entityUri);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntityDescA entityDescGet(final SSServPar parA) throws Exception{
    
    final SSEntityDescGetPar  par           = new SSEntityDescGetPar(parA);
    List<SSTag>               tags          = null;
    List<SSUri>               discUris      = null;
    SSRatingOverall           overallRating = null;
    SSEntityDescA             result        = null;
    final SSEntity            entity;
    
    try{
      
      if(!sqlFct.existsEntity(par.entityUri)){
        return null;
      }
      
      entity = sqlFct.getEntity(par.entityUri);
      
      if(par.getOverallRating){
        overallRating     = SSServCaller.ratingOverallGet   (par.user, par.entityUri);
      }
      
      if(par.getDiscUris){
        discUris          = SSServCaller.discUrisUserForTargetGet  (par.user, par.entityUri);
      }
      
      if(par.getTags){
        tags = SSServCaller.tagsUserGet(par.user,     par.entityUri, null, null);
      }
      
      if(SSEntityEnum.equals(entity.type, SSEntityEnum.entity)){
        
        result = SSEntityDesc.get(
          par.entityUri,
          entity.label,
          entity.creationTime,
          tags, 
          overallRating,
          discUris,
          entity.author);
        
      }else{
        
        for(SSServA serv : SSServA.getServsManagingEntities()){
        
          result = ((SSEntityHandlerImplI) serv.serv()).getDescForEntity(
            entity.type,
            par.user, 
            par.entityUri, 
            entity.label, 
            entity.creationTime, 
            tags,
            overallRating, 
            discUris, 
            entity.author);
        
          if(!SSEntityEnum.equals(result.entityDescType, SSEntityEnum.entityDesc)){
            break;
          }
        }
      }
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntityEnum entityTypeGet(SSServPar parI) throws Exception{
    
    SSEntityTypeGetPar par            = new SSEntityTypeGetPar (parI);
    SSEntityEnum       entityType;
    
    try{
      
      if(!sqlFct.existsEntity(par.entityUri)){
        return null;
      }
      
      entityType = sqlFct.getEntityType   (par.entityUri);
      
      if(entityType == null){
        return SSEntityEnum.entity;
      }
      
      return entityType;
      
//      if(!SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
//        return entityType;
//      }
//      
//      for(SSServA serv : SSServA.getServsManagingEntities()){
//        
//        entityType = ((SSEntityHandlerImplI) serv.serv()).getTypeForEntity(par.entityUri);
//        
//        if(!SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
//          return entityType;
//        }
//      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.coll.toString());
//   
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//      return SSEntityEnum.coll.toString();
//    }

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.disc.toString());
//   
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//      return SSEntityEnum.disc.toString();
//    }
    
//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.file.toString());
//    
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//      return SSEntityEnum.file.toString();
//    }

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.user.toString());
//    
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//
//			List<User> allUsers = ModelGC.SOCKET.getAllUsers();
//			
//			for(User user : allUsers){
//				
//				if(GM.isSame(uri, user.getId())){
//					
//					return SSEntityEnum.user;
//				}
//			}
//
//      return SSEntityEnum.user.toString();
//    }

//    return SSEntityEnum.entity.toString();