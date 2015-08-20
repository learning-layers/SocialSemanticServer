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
package at.tugraz.sss.servs.image.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSImage;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
import at.tugraz.sss.servs.image.api.SSImageClientI;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImageBase64GetPar;
import at.tugraz.sss.servs.image.datatype.par.SSImageAddPar;
import at.tugraz.sss.servs.image.datatype.par.SSImageGetPar;
import at.tugraz.sss.servs.image.datatype.par.SSImagesGetPar;
import at.tugraz.sss.servs.image.datatype.ret.SSImagesGetRet;
import at.tugraz.sss.servs.image.impl.sql.SSImageSQLFct;

public class SSImageImpl 
extends SSServImplWithDBA 
implements 
  SSImageClientI, 
  SSImageServerI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI{

  private final SSImageSQLFct         sqlFct;
  private final SSEntityServerI       entityServ;
  
  public SSImageImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
     this.sqlFct     = new SSImageSQLFct   (this);
     this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setThumb){
        
//        switch(entity.type){
//
//          case file:
//          case evernoteNote:
//          case evernoteResource:{
        
        entity.thumb =
          imageBase64Get(
            new SSImageBase64GetPar(
              par.user,
              entity.id,
              SSImageE.thumb,
              false)); //withUserRestriction));
//            break;
//          }
//        }
      }
      
      switch(entity.type){
        
        case image:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSImage.get(
            imageGet(
              new SSImageGetPar(
                par.user,
                entity.id,
                par.withUserRestriction)),
            entity);
        }
        
        default: return entity;
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws Exception{
    
    try{
      final List<SSUri>    affiliatedURIs     = new ArrayList<>();
      final List<SSEntity> affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        switch(entityAdded.type){
          case image:{
            
            if(SSStrU.contains(par.recursiveEntities, entityAdded)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityAdded.id);
            }
            
            affiliatedURIs.clear();
            
             //replace with method addAffiliatedEntitiesToCircle in file repo service
            for(SSEntity file :
              ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).filesGet(
                new SSEntityFilesGetPar(
                  par.user,
                  entityAdded.id,
                  par.withUserRestriction,
                  false))){ //invokeEntityHandlers
              
              if(SSStrU.contains(par.recursiveEntities, file)){
                continue;
              }
              
              SSUri.addDistinctWithoutNull(
                affiliatedURIs,
                file.id);
              
              SSEntity.addEntitiesDistinctWithoutNull(
                affiliatedEntities,
                file);
            }
            
            ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
              new SSCircleEntitiesAddPar(
                par.user,
                par.circle,
                affiliatedURIs,
                false, //withUserRestriction
                false)); //shouldCommit
            
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
  public SSImage imageGet(final SSImageGetPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.image)){
          return null;
        }
      }
      
      final SSImage image = sqlFct.getImage(par.image);
      
      for(SSEntity file :
        ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).filesGet(
          new SSEntityFilesGetPar(
            par.user,
            par.image,
            par.withUserRestriction,
            false))){ //invokeEntityHandlers
        
        image.file = file;
        break;
      }
      
      return image;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void imagesGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSImagesGetPar par = (SSImagesGetPar) parA.getFromJSON(SSImagesGetPar.class);
      
    sSCon.writeRetFullToClient(SSImagesGetRet.get(imagesGet(par)));
  }

  @Override
  public List<SSEntity> imagesGet(final SSImagesGetPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        if(par.entity != null){
          
          if(SSServCallerU.canUserRead(par.user, par.entity)){
            return new ArrayList<>();
          }
        }
      }
      
      final List<SSUri>    imageURIs = sqlFct.getImages(par.entity, par.imageType);
      final List<SSEntity> images    = new ArrayList<>();
        
      for(SSUri imageURI : imageURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          images, 
          imageGet(
            new SSImageGetPar(
              par.user, 
              imageURI, 
              par.withUserRestriction)));
      }
      
      return images;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public String imageBase64Get(final SSImageBase64GetPar par) throws Exception{
    
    try{
      final List<SSEntity> images =
        imagesGet(
          new SSImagesGetPar(
            par.user,
            par.entity,
            par.imageType,
            par.withUserRestriction));
      
      if(
        images.isEmpty() ||
        images.get(0).file == null){
        return null;
      }
      
      final String pngFilePath =
        SSCoreConf.instGet().getSss().getLocalWorkPath() +
        SSVocConf.fileIDFromSSSURI(images.get(0).file.id);
      
      return SSFileU.readImageToBase64Str(pngFilePath);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri imageAdd(final SSImageAddPar par) throws Exception{

    try{
      
      if(par.imageType == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final SSUri imageUri;
      
      if(par.uuid != null){
        imageUri = SSServCaller.vocURICreateFromId(par.uuid);
      }else{
        
        if(par.link != null){
          imageUri = par.link;
        }else{
          imageUri = SSServCaller.vocURICreate();
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          par.user, 
          imageUri,  //entity
          SSEntityE.image,  //type
          null, //label, 
          null, //description, 
          null, //creationTime, 
          null, //read, 
          false, //setPublic, 
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.addImage(
        imageUri, 
        par.imageType,
        par.link);
      
      if(par.entity != null){
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity,  //entity
            null,  //type
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic,
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
        
        sqlFct.addImageToEntity(imageUri, par.entity);
      }
      
      if(par.file != null){
        
        ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).fileAdd(
          new SSEntityFileAddPar(
            par.user,
            par.file,
            imageUri,
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      dbSQL.commit(par.shouldCommit);

      return imageUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return imageAdd(par);
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