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
package at.tugraz.sss.servs.thumb.impl;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileIDFromURIPar;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.thumb.api.SSThumbClientI;
import at.tugraz.sss.servs.thumb.api.SSThumbServerI;
import at.tugraz.sss.servs.thumb.datatype.par.SSFileThumbBase64GetPar;
import at.tugraz.sss.servs.thumb.datatype.par.SSThumbAddPar;
import at.tugraz.sss.servs.thumb.datatype.par.SSThumbsGetPar;
import at.tugraz.sss.servs.thumb.datatype.ret.SSThumbsGetRet;
import at.tugraz.sss.servs.thumb.impl.sql.SSThumbSQLFct;

public class SSThumbImpl 
extends SSServImplWithDBA 
implements 
  SSThumbClientI, 
  SSThumbServerI,
  SSEntityHandlerImplI{

  private final SSThumbSQLFct         sqlFct;
  
  public SSThumbImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
     sqlFct = new SSThumbSQLFct   (this);
  }

  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    if(!par.setThumb){
      return entity;
    }
    
    switch(entity.type){
      case file:{
        
        entity.thumb =
          ((SSThumbServerI) SSServReg.getServ(SSThumbServerI.class)).thumbBase64Get(
            new SSFileThumbBase64GetPar(
              null,
              null,
              par.user,
              entity.id,
              false)); //withUserRestriction));
        break;
      }
    }
    
    return entity;
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
    
  @Override
  public List<SSUri> getSubEntities(
    final SSUri     user,
    final SSUri     entity,
    final SSEntityE type) throws Exception{

    return null;
  }
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri userUri,
    final SSEntityE entityType,
    final SSUri entityUri,
    final Boolean removeUserTags,
    final Boolean removeUserRatings,
    final Boolean removeFromUserColls,
    final Boolean removeUserLocations) throws Exception{
  }

  @Override
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
  }
  
  @Override
  public void copyEntity(
    final SSUri       user,
    final List<SSUri> users,
    final SSUri       entity,
    final List<SSUri> entitiesToExclude,
    final SSEntityE   entityType) throws Exception{

  }

  @Override
  public void thumbsGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSThumbsGetPar par = (SSThumbsGetPar) parA.getFromJSON(SSThumbsGetPar.class);
    
    sSCon.writeRetFullToClient(SSThumbsGetRet.get(thumbsGet(par)));
  }

  @Override
  public List<SSUri> thumbsGet(final SSThumbsGetPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
        SSServCallerU.canUserReadEntity(par.user, par.entity);
      }
      
      return sqlFct.getThumbs(par.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public String thumbBase64Get(final SSFileThumbBase64GetPar par) throws Exception{
    
    try{
      final List<SSUri>  thumbs =
        thumbsGet(
          new SSThumbsGetPar(
            null,
            null,
            par.user,
            par.entity,
            par.withUserRestriction));
      
      if(thumbs.isEmpty()){
        SSLogU.warn("thumb(s) couldnt be retrieved from file " + par.entity);
        return null;
      }
      
      final String pngFilePath = 
        SSCoreConf.instGet().getSss().getLocalWorkPath() + 
       ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).fileIDFromURI(
         new SSFileIDFromURIPar(
           null, 
           null, 
           par.user, 
           thumbs.get(0)));
      
      return SSFileU.readPNGToBase64Str(pngFilePath);
      
    }catch(Exception error){
      SSLogU.warn("base 64 file thumb couldnt be retrieved");
      SSServErrReg.reset();
      return null;
    }
  }
  
  @Override
  public SSUri thumbAdd(final SSThumbAddPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
        SSServCallerU.canUserReadEntity(par.user, par.entity);
      }
      
      dbSQL.startTrans(par.shouldCommit);

      if(par.removeExistingThumbs){
        sqlFct.removeThumbs(par.entity);
      }
      
      sqlFct.addThumb(par.entity, par.thumb);
      
      dbSQL.commit(par.shouldCommit);

      return par.thumb;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return thumbAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}