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
package at.tugraz.sss.servs.file.impl;

import at.tugraz.sss.servs.file.conf.SSFileConf;
import at.tugraz.sss.servs.file.datatype.ret.SSFileAddRet;
import at.tugraz.sss.servs.file.datatype.ret.SSFileDownloadRet;
import at.tugraz.sss.servs.file.datatype.ret.SSFileUploadRet;
import at.tugraz.sss.servs.file.datatype.par.SSFileDownloadPar;
import at.tugraz.sss.servs.file.datatype.par.SSFileUploadPar;
import at.tugraz.sss.servs.file.datatype.par.SSFilesDeleteNotRegisteredPar;
import at.tugraz.sss.servs.file.datatype.SSFile;
import at.tugraz.sss.servs.file.api.*;
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
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import at.tugraz.sss.adapter.socket.*;
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
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.servs.file.datatype.par.SSFileGetPar;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImageAddPar;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.ws.rs.core.*;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSFileImpl 
extends SSServImplWithDBA
implements 
  SSFileClientI, 
  SSFileServerI, 
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI{

  private final SSUserCommons   userCommons = new SSUserCommons();
  private final SSFileSQL       sql;
  
  public SSFileImpl(
    final SSFileConf conf) throws SSErr{

    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql = new SSFileSQL (dbSQL, SSConf.systemUserUri);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setFiles){
        
        final List<SSEntity> files =
          filesGet(
            new SSEntityFilesGetPar(
              servPar,
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
                servPar, 
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
  public List<SSEntity> addAffiliatedEntitiesToCircle(
    final SSServPar                          servPar, 
    final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      final List<SSUri>     affiliatedURIs  = new ArrayList<>();
      final SSEntityServerI circleServ      = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      for(SSEntity entityAdded : par.entities){
        
        for(SSEntity file :
          filesGet(
            new SSEntityFilesGetPar(
              servPar, 
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
          servPar, 
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
      
      final SSFileDownloadRet ret = fileDownload(par);
      
      if(!par.isPublicDownload){
        
        ((SSEvalServerI) SSServReg.getServ(SSEvalServerI.class)).evalLog(
          new SSEvalLogPar(
            par, 
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.downloadEntity,
            par.file,  //entity
            null, //content,
            null, //entities
            null, //users
            null, //creationTime
            par.shouldCommit));
      }
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSFileDownloadRet fileDownload(final SSFileDownloadPar par) throws SSErr{
    
    try{
      
      SSEntity file = null;
        
      if(
        par.withUserRestriction &&
        !par.isPublicDownload){
        
        file =
          sql.getEntityTest(
            par, 
            par.user,
            par.file,
            par.withUserRestriction);
       
        if(file == null){
          throw SSErr.get(SSErrE.userNotAllowedToAccessEntity);
        }
        
      }else{
        
        file =
          sql.getEntityTest(
            par, 
            par.user,
            par.file,
            false); //withUserRestriction
        
        if(file == null){
          throw SSErr.get(SSErrE.entityDoesNotExist);
        }
      }
      
      final String            fileId          = SSConf.fileIDFromSSSURI(par.file);
      final FileInputStream   fileInputStream = new FileInputStream(new File(conf.getLocalWorkPath() + fileId));
      final SSFileDownloadRet ret             = new SSFileDownloadRet(par.file, file.label, null);
      
      switch(par.clientType){
        
        case socket:{
          
          final DataInputStream fileReader = new DataInputStream (fileInputStream);
          
          try{
            final DataOutputStream    dataOutputStream   = new DataOutputStream   (par.clientSocket.getOutputStream());
            final InputStreamReader   inputStreamReader  = new InputStreamReader  (par.clientSocket.getInputStream(), SSEncodingU.utf8.toString());
            final OutputStreamWriter  outputStreamWriter = new OutputStreamWriter (par.clientSocket.getOutputStream());
            final SSSocketAdapterU    socketAdapterU     = new SSSocketAdapterU   ();
            byte[]                    chunk              = new byte[SSSocketU.socketTranmissionSize];
            int                       fileChunkLength    = -1;
            
            socketAdapterU.writeRetFullToClient(outputStreamWriter, ret);
            
            //      switch(((SSFileRepoConf)conf).fileRepoType){
//        case i5Cloud: downloadFromI5Cloud(); break;
//    if(SSFileRepoTypeEnum.isSame(fileRepoConf.fileRepoType, SSFileRepoTypeEnum.webdav)){
//      this.webdavInputStream = SardineFactory.begin(fileRepoConf.getUser(), fileRepoConf.getPassword()).getInputStream(fileRepoConf.getPath() + fileId);
////      fileReader = new DataInputStream(SardineFactory.begin(fileRepoConf.getUser(), fileRepoConf.getPassword()).getInputStream(fileRepoConf.getPath() + fileId));
//    }
//      }
            
            SSSocketU.readFullString(inputStreamReader);
            
            while(true){
              
              fileChunkLength = fileReader.read(chunk);
              
              if(fileChunkLength == -1){
                
                SSSocketU.writeByteChunk(dataOutputStream, new byte[0], fileChunkLength);
                
                fileReader.close();
                
                break;
              }
              
              SSSocketU.writeByteChunk(dataOutputStream, chunk, fileChunkLength);
            }
            
            return ret;
            
          }catch(Exception error){
            SSServErrReg.regErrThrow(error);
            return null;
          }finally{
            
            try{
              fileReader.close();
            }catch(IOException ex){
              SSLogU.err(ex);
            }
          }          
        }
        
        case rest:{
          
          final StreamingOutput stream = new StreamingOutput(){
            
            @Override
            public void write(OutputStream out) throws IOException{
              
              try{
                byte[] bytes     = new byte[SSSocketU.socketTranmissionSize];
                int    read;
                
                while((read = fileInputStream.read(bytes)) != -1) {
                  out.write        (bytes, 0, read);
                  out.flush        ();
                }
                
                out.close();
              }finally{
                fileInputStream.close();
              }
            }
          };
          
          ret.outputStream = stream;
          
          break;
        }
      }
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSServRetI fileUpload(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSFileUploadPar par = (SSFileUploadPar) parA.getFromClient(clientType, parA, SSFileUploadPar.class);
      final SSFileUploadRet ret = fileUpload(par);
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par, 
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.uploadFile,
          ret.file,  //entity
          null, //content,
          null, //entities
          null, //users
          null, //creationTime
          par.shouldCommit));
      
      return ret;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSFileUploadRet fileUpload(final SSFileUploadPar par) throws SSErr{
    
    try{
      
      final SSFileExtE      fileExt;
      final SSUri           fileUri;
      final String          fileId;
      final SSFileUploadRet result;
      SSUri                 thumbUri  = null;
      
      fileExt            = SSMimeTypeE.fileExtForMimeType       (par.mimeType);
      fileUri            = SSConf.vocURICreate                  (fileExt);
      fileId             = SSConf.fileIDFromSSSURI              (fileUri);
      result             = SSFileUploadRet.get(fileUri, thumbUri);
      
      switch(par.clientType){
        
        case socket:{
          final SSSocketAdapterU   socketAdapterU     = new SSSocketAdapterU();
          final DataInputStream    dataInputStream    = new DataInputStream    (par.clientSocket.getInputStream());
          final OutputStreamWriter outputStreamWriter = new OutputStreamWriter (par.clientSocket.getOutputStream());
          
          socketAdapterU.writeRetFullToClient(outputStreamWriter, result);
          
          readFileFromSocketStreamAndSave(dataInputStream, fileId);
          break;
        }
        
        case rest:{
          readFileFromRESTInputStreamAndSave(par.fileInputStream, fileId);
        }
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      result.thumb =
        registerFileAndCreateThumb(
          par, 
          par.user,
          fileUri,
          par.label,
          par.description,
          par.withUserRestriction);
      
      dbSQL.commit(par, par.shouldCommit);
      
      addFileContentsToNoSQLStore  (fileId);
//      removeFileFromLocalWorkFolder();
      
      return result;
      
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return null;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return null;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void readFileFromRESTInputStreamAndSave(
    final InputStream inputStream,
    final String      fileID) throws SSErr{
    
    FileOutputStream fileOutputStream = null;
      
    try{
      
      byte[] bytes     = new byte[SSSocketU.socketTranmissionSize];
      int    read;
      fileOutputStream = SSFileU.openOrCreateFileWithPathForWrite   (conf.getLocalWorkPath() + fileID);

      while((read = inputStream.read(bytes)) != -1) {
        fileOutputStream.write        (bytes, 0, read);
        fileOutputStream.flush        ();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(fileOutputStream != null){
        
        try{
          fileOutputStream.close();
        }catch(IOException ex){
          SSLogU.warn("closing file output stream failed", ex);
        }
      }
    }
  }
  
  private void readFileFromSocketStreamAndSave(
    final DataInputStream dataInputStream,
    final String          fileID) throws SSErr{
    
    FileOutputStream fileOutputStream = null;
      
    try{
      
      byte[] fileChunk = null;
      
      fileOutputStream = SSFileU.openOrCreateFileWithPathForWrite   (conf.getLocalWorkPath() + fileID);

      while(true){
        
        fileChunk = SSSocketU.readByteChunk(dataInputStream);
        
        if(fileChunk.length != 0){
          
          fileOutputStream.write        (fileChunk);
          fileOutputStream.flush        ();
          continue;
        }
        
        fileOutputStream.close();
        break;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(fileOutputStream != null){
        
        try{
          fileOutputStream.close();
        }catch(IOException ex){
          SSLogU.warn("closing file output stream failed", ex);
        }
      }
    }
  }
  
  private void addFileContentsToNoSQLStore(final String fileID){
    
    try{
      
      dbNoSQL.addDoc(new SSDBNoSQLAddDocPar(fileID));
      
    }catch(Exception error){
      
//      if(SSServErrReg.containsErr(SSErrE.servInvalid)){
        SSLogU.warn(SSErrE.servInvalid.toString(), error);
//      }else{
//        SSLogU.warn(error.getMessage());
//      }
    }
  }
  
  private SSUri registerFileAndCreateThumb(
    final SSServPar     servPar, 
    final SSUri         user,
    final SSUri         fileUri,
    final SSLabel       label,
    final SSTextComment description,
    final boolean       withUserRestriction) throws SSErr{
    
    try{
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSFileAddRet    result =
        fileAdd(
          new SSEntityFileAddPar(
            servPar, 
            user,
            null,
            null, //fileLength
            null, //fileExt,
            fileUri, //file,
            SSEntityE.uploadedFile, //type,
            label, //label,
            null, //entity
            true, //createThumb,
            fileUri, //entityToAddThumbTo,
            false, //removeExistingFilesForEntity,
            withUserRestriction,
            false));

      if(result == null){
        return null;
      }
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          servPar,
          user,
          fileUri,
          null,
          null, //label,
          description, //description,
          null, //creationTime,
          null, //read,
          false, //setPublic,
          false, //createIfNotExists,
          withUserRestriction,
          false)); //shouldCommit)
      
      return result.thumb;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSFileAddRet fileAdd(final SSEntityFileAddPar par) throws SSErr{
    
    try{
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      if(
        par.file == null &&
        par.type == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.type == null){
        par.type = SSEntityE.file;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
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
              par,
              par.user,
              par.entity, //entity
              par.withUserRestriction, //withUserRestriction
              false))){  //invokeEntityHandlers
          
          entityServ.entityRemove(
            new SSEntityRemovePar(
              par, 
              par.user, 
              file.id));
          
          try{
            SSFileU.delFile(conf.getLocalWorkPath() + SSConf.fileIDFromSSSURI(file.id));
          }catch(Exception error){
            SSLogU.warn("file couldnt be removed from file system", error);
          }
        }
      }
        
      par.file =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            par.file,  //entity
            par.type,  //type
            par.label, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic,
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(par.file == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.addFile(par, par.file);
      
      if(par.entity != null){
        
        par.entity = 
          entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
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
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
        
        sql.addFileToEntity(
          par, 
          par.file, 
          par.entity);
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
              par, 
              par.user,
              null, //uuid,
              null, //link,
              SSImageE.thumb, //imageType,
              entityToAddThumbTo, //entity
              par.file, //file
              par.label, //label
              true, //createThumb
              true, //isImageToAddTheThumb,
              true, //removeThumbsFromEntity
              par.withUserRestriction, //withUserRestriction,
              false)).thumb; //shouldCommit
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return new SSFileAddRet(par.file, thumbURI);
      
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return null;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return null;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSFile fileGet(final SSFileGetPar par) throws SSErr{
    
    try{
      final SSEntityServerI entityServ    = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSFileExtE      fileExt       = SSFileExtE.ext(par.file);
      final SSMimeTypeE     mimeType      = SSMimeTypeE.mimeTypeForFileExt (fileExt);
      final SSUri           downloadLink  =
        SSUri.get(
          SSFileU.correctDirPath(SSCoreConf.instGet().getSss().restAPIPath) +
            SSFileU.correctDirPath(SSConf.restAPIResourceFile)           +
            SSFileU.correctDirPath(SSConf.fileIDFromSSSURI(par.file))    +
            SSConf.restAPIPathFileDownloadPublic);
      
      final SSEntityE fileType = 
        sql.getFileType(
          par, 
          par.file);
      
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
            par, 
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
            par, 
            par.user, 
            par.entity, 
            par.withUserRestriction);
          
        if(entity == null){
          return files;
        }
      }
      
      final SSFileGetPar fileGetPar =
        new SSFileGetPar(
          par, 
          par.user,
          null, //file
          par.withUserRestriction,
          par.invokeEntityHandlers);

      for(SSUri file : sql.getEntityFiles(par, par.entity)){
        
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
  
  @Override
  public boolean filesDeleteNotRegistered(final SSFilesDeleteNotRegisteredPar par) throws SSErr{
    
    try{
      
      if(SSStrU.isEmpty(par.dirPath)){
        par.dirPath = SSCoreConf.instGet().getSss().getSssWorkDirTmp();
      }
      
      final File[] filesForDirPath = SSFileU.filesForDirPath(par.dirPath);
      SSUri        fileURI;
      SSEntity     fileEntity;
      int          counter = 1;
      
      for(File file : filesForDirPath){
        
        try{
          
          fileURI = SSUri.get(file.getName(), SSConf.sssUri);

          if(fileURI == null){
            continue;
          }

          fileEntity = 
            sql.getEntityTest(
              par, 
              par.user, 
              fileURI, //entity
              false); //withUserRestriction

          if(fileEntity == null){
            SSFileU.delFile(file.getAbsolutePath());
//            SSLogU.info("delete file " + file.getName() + " " + counter++);
          }else{
//            SSLogU.info("file exists " + file.getName());
          }
          
        }catch(Exception error){
          SSLogU.err(error);
        }
      }
      
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
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


//private void uploadFileToWebDav() throws SSErr{
//    
//    try{
//      fileInputStream = SSFileU.openFileForRead(localWorkPath + fileId);
//      
//      SardineFactory.begin(
//        fileConf.user,
//        fileConf.password).put(fileConf.getPath() + fileId, fileInputStream);
//      
//      fileInputStream.close();
//    }catch(Exception error){
//      SSServErrReg.regErr(error);
//    }
//  }

//  private void uploadFileToI5Cloud() throws SSErr{
//
//    try{
//      SSServCaller.i5CloudFileUpload(this.fileId, "private", SSServCaller.i5CloudAuth().get(SSHTMLU.xAuthToken));
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }

//private void disposeUploadedFile() throws SSErr{
//    
//    try{
//      
//      switch(fileConf.fileRepoType){
//        case fileSys: moveFileToLocalRepo(); break;
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }

//  private void removeFileFromLocalWorkFolder() throws SSErr{
//    
//    if(SSStrU.equals(localWorkPath, fileConf.getPath())){
//      return;
//    }
//    
//    try{
//      SSFileU.delFile(localWorkPath + fileId);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
  
//  private void moveFileToLocalRepo() throws SSErr{
//    
//    if(SSStrU.equals(localWorkPath, fileConf.getPath())){
//      return;
//    }
//    
//    try{
//      final File file = new File(localWorkPath + fileId);
//      
//      if(!file.renameTo(new File(fileConf.getPath() + fileId))){
//        throw new Exception("couldnt move file to local file repo");
//      }
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }

//  private void saveActivity() throws SSErr{
//    
//    try{
//      
//      SSServCaller.activityAdd(
//        par.user,
//        SSActivityE.downloadFile,
//        SSUri.asListWithoutNullAndEmpty(),
//        SSUri.asListWithoutNullAndEmpty(this.par.file),
//        SSTextComment.asListWithoutNullAndEmpty(),
//        null,
//        false);
//      
//    }catch(SSErr error){
//      
//      switch(error.code){
//        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
//        default: SSServErrReg.regErrThrow(error);
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
//  private void downloadFromI5Cloud() throws SSErr{
//    
//    try{
//      SSServCaller.i5CloudFileDownload(this.fileId, "private", SSServCaller.i5CloudAuth().get(SSHTMLU.xAuthToken));
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }