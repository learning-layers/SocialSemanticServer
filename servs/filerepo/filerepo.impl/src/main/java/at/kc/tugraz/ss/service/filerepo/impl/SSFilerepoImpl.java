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
package at.kc.tugraz.ss.service.filerepo.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.service.filerepo.api.*;
import at.kc.tugraz.ss.service.filerepo.conf.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.*;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileDownloadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileAddRet;
import at.kc.tugraz.ss.service.filerepo.impl.fct.SSFileSQLFct;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.file.datatype.par.SSFileGetPar;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImageAddPar;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSFilerepoImpl 
extends SSServImplWithDBA
implements 
  SSFileRepoClientI, 
  SSFileRepoServerI, 
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI{

  private final SSFileSQLFct    sqlFct;
  private final SSEntityServerI entityServ;
  private final String          localWorkPath;
  
  public SSFilerepoImpl(
    final SSFileRepoConf conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct         = new SSFileSQLFct   (this);
    this.entityServ     = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.localWorkPath  = SSCoreConf.instGet().getSss().getLocalWorkPath();
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setFiles){
        
        final List<SSEntity> files =
          filesGet(
            new SSEntityFilesGetPar(
              par.user, 
              entity.id, 
              par.withUserRestriction,
              false)); //invokeEntityHandlers

        if(!files.isEmpty()){
          entity.file = files.get(0);
        }
      }
      
      switch(entity.type){
        
        case uploadedFile:
        case file:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSFile.get(
            fileGet(
              new SSFileGetPar(
                par.user,
                entity.id,
                par.withUserRestriction,
                false)),
            entity);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws Exception{
    
    try{
      final List<SSUri>     affiliatedURIs  = new ArrayList<>();
      final SSCircleServerI circleServ      = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
      
      for(SSEntity entityAdded : par.entities){
        
        for(SSEntity file :
          filesGet(
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
        }
      }
      
      if(affiliatedURIs.isEmpty()){
        return new ArrayList<>();
      }
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          par.user,
          par.circle,
          affiliatedURIs,
          false, //withUserRestriction
          false)); //shouldCommit
      
      return new ArrayList<>();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void fileDownload(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSFileDownloadPar par = (SSFileDownloadPar) parA.getFromJSON(SSFileDownloadPar.class);
    
    if(!par.isPublicDownload){
      SSServCallerU.checkKey(parA);
    }
    
    par = (SSFileDownloadPar) parA.getFromJSON(SSFileDownloadPar.class);
    
    par.sSCon = sSCon;
    
    fileDownload(par);
    
    if(!par.isPublicDownload){
      
      ((SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class)).evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.fileDowload,
          par.file,  //entity
          null, //content,
          null, //entities
          null, //users
          par.shouldCommit));
    }
  }

  @Override
  public void fileDownload(final SSFileDownloadPar par) throws Exception{
    
    try{

      if(!par.isPublicDownload){
        
        if(!SSServCallerU.canUserRead(par.user, par.file)){
          throw new SSErr(SSErrE.userNotAllowedToAccessEntity);
        }
      }
      
      new Thread(new SSFileDownloader((SSFileRepoConf)conf, par, this)).start();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public void fileUpload(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSFileUploadPar par = (SSFileUploadPar) parA.getFromJSON(SSFileUploadPar.class);
    
    par.sSCon = sSCon;
    
    fileUpload(par);
  }
  
  @Override
  public void fileUpload(final SSFileUploadPar par) throws Exception{
    
    try{
      new Thread(new SSFileUploader((SSFileRepoConf) conf, par)).start();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public SSFileAddRet fileAdd(final SSEntityFileAddPar par) throws Exception{
    
    try{
      
      if(
        par.file == null &&
        par.type == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.type == null){
        par.type = SSEntityE.file;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(
        par.file       == null &&
        par.fileBytes  != null &&
        par.fileLength != null &&
        par.fileExt    != null){

        par.file                       = SSServCaller.vocURICreate  (par.fileExt);
        final String     fileId        = SSVocConf.fileIDFromSSSURI (par.file);
        
        SSFileU.writeFileBytes(
          new FileOutputStream(localWorkPath + fileId),
          par.fileBytes,
          par.fileLength);
      }
      
      if(par.file == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(
        par.removeExistingFilesForEntity &&
        par.entity != null){
        
        for(SSEntity file :
          filesGet(
            new SSEntityFilesGetPar(
              par.user,
              par.entity, //entity
              par.withUserRestriction, //withUserRestriction
              false))){  //invokeEntityHandlers
          
          SSServCaller.entityRemove(file.id, false);
          
          try{
            SSFileU.delFile(localWorkPath + SSVocConf.fileIDFromSSSURI(file.id));
          }catch(Exception error){
            SSLogU.warn("file couldnt be removed from file system");
          }
        }
      }
        
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          par.user, 
          par.file,  //entity
          par.type,  //type
          par.label, //label,
          null, //description, 
          null, //creationTime, 
          null, //read, 
          null, //setPublic, 
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.addFile(par.file);
      
      if(par.entity != null){
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity,
            null, //type,
            null, //label
            null,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
        
        sqlFct.addFileToEntity(par.file, par.entity);
      }
      
      SSUri thumbURI = null;
      
      if(par.createThumb){
        
        final SSImageServerI imageServ = (SSImageServerI) SSServReg.getServ(SSImageServerI.class);
        final SSUri          entityToAddThumbTo;
        
        if(par.entityToAddThumbTo != null){
          entityToAddThumbTo = par.entityToAddThumbTo;
        }else{
          
          if(par.file != null){
            entityToAddThumbTo = par.file;
          }else{
            entityToAddThumbTo =  null;
          }
        }
        
        thumbURI =
          imageServ.imageAdd(
            new SSImageAddPar(
              par.user,
              null, //uuid,
              null, //link,
              SSImageE.thumb, //imageType,
              entityToAddThumbTo, //entity
              par.file, //file
              true, //createThumb
              true, //isImageToAddTheThumb,
              true, //removeThumbsFromEntity
              par.withUserRestriction, //withUserRestriction,
              false)).thumb; //shouldCommit
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return new SSFileAddRet(par.file, thumbURI);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return fileAdd(par);
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

  @Override
  public SSFile fileGet(final SSFileGetPar par) throws Exception{
    
    try{
    
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.file)){
          return null;
        }
      }
      
      final SSFileExtE  fileExt       = SSFileExtE.ext(SSStrU.removeTrailingSlash(par.file));
      final SSMimeTypeE mimeType      = SSMimeTypeE.mimeTypeForFileExt (fileExt);
      final SSUri       downloadLink  =
        SSUri.get(
          SSFileU.correctDirPath(SSCoreConf.instGet().getSss().restAPIPath) +
            SSFileU.correctDirPath(SSVocConf.restAPIResourceFile)           +
            SSFileU.correctDirPath(SSVocConf.fileIDFromSSSURI(par.file))    +
            SSVocConf.restAPIPathFileDownloadPublic);
      final SSEntityE fileType = sqlFct.getFileType(par.file);
      
      final SSFile      file  = 
        SSFile.get(
          par.file,
          fileType,
          fileExt, 
          mimeType,
          downloadLink);
      
      final SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.file);
        
        switch(fileType){
          case uploadedFile:{
            descPar.setThumb = true;
            break;
          }
        }
        
      }else{
        descPar = null;
      }
      
      return SSFile.get(
        file,
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.file,
            par.withUserRestriction,
            descPar)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> filesGet(final SSEntityFilesGetPar par) throws Exception{
    
    try{
    
      if(par.entity == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.entity)){
          return new ArrayList<>();
        }
      }

      final List<SSEntity> files = new ArrayList<>();
      
      for(SSUri file : sqlFct.getEntityFiles(par.entity)){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          files, 
          fileGet(
            new SSFileGetPar(
            par.user, 
            file, 
            par.withUserRestriction, 
            par.invokeEntityHandlers)));
      }

      return files;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
  
//  public static SSUri getFileDefaultUri(
//     String fileId) throws Exception{
//    
//    if(SSObjectUtils.isNull (fileId)){
//      throw new MalformedURLException("Cannot get default file uri for null id");
//    }
//   
//    return SSUri.get(SSVocServ.inst().serv().getUriDefaultFile() + fileId);
//  }
//  public static SSUri getFileDefaultUri(
//     SSUri fileUri) throws Exception{
//    
//    String fileAsString;
//    
//    if(SSObjectUtils.isNull (fileUri)){
//      return fileUri;
//    }
//    
//    fileAsString = SSStrU.removeTrailingSlash(fileUri.toString());
//    
//    return SSUri.get(SSVocServ.inst().serv().getUriDefaultFile() + fileAsString.substring(fileAsString.lastIndexOf(strU.slash) + 1));
//  }
//  public static SSUri getFileUriInDomain(
//     SSUri fileUri) throws Exception{
//    
//    return SSUri.get(getFileUri() + getIdFromFileUri(fileUri));
//  }