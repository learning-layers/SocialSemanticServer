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

import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.conf.SSCoreConf;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.SSMimeTypeE;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.kc.tugraz.ss.service.filerepo.api.*;
import at.kc.tugraz.ss.service.filerepo.conf.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileDownloadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileAddRet;
import at.kc.tugraz.ss.service.filerepo.impl.fct.SSFileSQLFct;
import at.tugraz.sss.serv.entity.api.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.datatype.par.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.util.SSFileU;
import at.tugraz.sss.serv.datatype.enums.SSImageE;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.datatype.par.SSEntityRemovePar;
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

  private final SSFileSQLFct    sql;
  private final SSEntityServerI entityServ;
  private final SSUserCommons  userCommons;
  
  public SSFilerepoImpl(
    final SSFileRepoConf conf) throws SSErr{

    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql            = new SSFileSQLFct   (dbSQL, SSConf.systemUserUri);
    this.entityServ     = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.userCommons    = new SSUserCommons();
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{
    
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
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      final List<SSUri>     affiliatedURIs  = new ArrayList<>();
      final SSEntityServerI circleServ      = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
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
  public SSServRetI fileDownload(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      SSFileDownloadPar par = (SSFileDownloadPar) parA.getFromClient(clientType, parA, SSFileDownloadPar.class);
      
      //TODO fix this public download: get user and check whether he can read
      if(!par.isPublicDownload){
        userCommons.checkKeyAndSetUser(parA);
      }
      
      par = (SSFileDownloadPar) parA.getFromClient(clientType, parA, SSFileDownloadPar.class);
      
      fileDownload(par);
      
      if(!par.isPublicDownload){
        
        ((SSEvalServerI) SSServReg.getServ(SSEvalServerI.class)).evalLog(
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
      
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void fileDownload(final SSFileDownloadPar par) throws SSErr{
    
    try{
      
      if(
        !par.isPublicDownload &&
        par.withUserRestriction){
        
        final SSEntity file =
          sql.getEntityTest(
            par.user,
            par.file,
            par.withUserRestriction);
        
        if(file == null){
          throw SSErr.get(SSErrE.userNotAllowedToAccessEntity);
        }
      }
      
      new Thread(new SSFileDownloader((SSFileRepoConf) conf, par)).start();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public SSServRetI fileUpload(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSFileUploadPar par = (SSFileUploadPar) parA.getFromClient(clientType, parA, SSFileUploadPar.class);
      
      fileUpload(par);
      
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void fileUpload(final SSFileUploadPar par) throws SSErr{
    
    try{
      
      new Thread(new SSFileUploader((SSFileRepoConf) conf, par)).start();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public SSFileAddRet fileAdd(final SSEntityFileAddPar par) throws SSErr{
    
    try{
      
      if(
        par.file == null &&
        par.type == null){
        throw SSErr.get(SSErrE.parameterMissing);
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

        par.file                       = SSConf.vocURICreate  (par.fileExt);
        final String     fileId        = SSConf.fileIDFromSSSURI (par.file);
        
        SSFileU.writeFileBytes(
          new FileOutputStream(conf.getLocalWorkPath() + fileId),
          par.fileBytes,
          par.fileLength);
      }
      
      if(par.file == null){
        throw SSErr.get(SSErrE.parameterMissing);
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
          
          entityServ.entityRemove(new SSEntityRemovePar(par.user, file.id));
          
          try{
            SSFileU.delFile(conf.getLocalWorkPath() + SSConf.fileIDFromSSSURI(file.id));
          }catch(Exception error){
            SSLogU.warn("file couldnt be removed from file system");
          }
        }
      }
        
      par.file = 
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
          true, //createIfNotExists
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      if(par.file == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.addFile(par.file);
      
      if(par.entity != null){
        
        par.entity = 
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
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
        
        if(par.file == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
        
        sql.addFileToEntity(par.file, par.entity);
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
  public SSFile fileGet(final SSFileGetPar par) throws SSErr{
    
    try{
    
      final SSFileExtE  fileExt       = SSFileExtE.ext(SSStrU.removeTrailingSlash(par.file));
      final SSMimeTypeE mimeType      = SSMimeTypeE.mimeTypeForFileExt (fileExt);
      final SSUri       downloadLink  =
        SSUri.get(
          SSFileU.correctDirPath(SSCoreConf.instGet().getSss().restAPIPath) +
            SSFileU.correctDirPath(SSConf.restAPIResourceFile)           +
            SSFileU.correctDirPath(SSConf.fileIDFromSSSURI(par.file))    +
            SSConf.restAPIPathFileDownloadPublic);
      final SSEntityE fileType = sql.getFileType(par.file);
      
      final SSFile file = 
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
      
      final SSEntity fileEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.file,
            par.withUserRestriction,
            descPar));
      
      if(fileEntity == null){
        return null;
      }
      
      return SSFile.get(
        file,
        fileEntity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> filesGet(final SSEntityFilesGetPar par) throws SSErr{
    
    try{
    
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<SSEntity> files = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        final SSEntity entity = 
          sql.getEntityTest(
            par.user, 
            par.entity, 
            par.withUserRestriction);
          
        if(entity == null){
          return files;
        }
      }
      
      final SSFileGetPar fileGetPar =
        new SSFileGetPar(
          par.user,
          null, //file
          par.withUserRestriction,
          par.invokeEntityHandlers);

      for(SSUri file : sql.getEntityFiles(par.entity)){
        
        fileGetPar.file = file;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          files, 
          fileGet(fileGetPar));
      }

      return files;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
  
//  public static SSUri getFileDefaultUri(
//     String fileId) throws SSErr{
//    
//    if(SSObjectUtils.isNull (fileId)){
//      throw new MalformedURLException("Cannot get default file uri for null id");
//    }
//   
//    return SSUri.get(SSVocServ.inst().serv().getUriDefaultFile() + fileId);
//  }
//  public static SSUri getFileDefaultUri(
//     SSUri fileUri) throws SSErr{
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
//     SSUri fileUri) throws SSErr{
//    
//    return SSUri.get(getFileUri() + getIdFromFileUri(fileUri));
//  }