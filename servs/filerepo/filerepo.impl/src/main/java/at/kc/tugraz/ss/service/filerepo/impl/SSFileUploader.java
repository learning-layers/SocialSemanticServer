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
package at.kc.tugraz.ss.service.filerepo.impl;

import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.tugraz.sss.serv.SSServImplStartA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileAddRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileUploadRet;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLAddDocPar;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import java.io.FileOutputStream;
import java.io.IOException;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSFileUploader extends SSServImplStartA{
  
  private final SSFileUploadPar       par;
  private final SSFileExtE            fileExt;
  private final SSUri                 fileUri;
  private final String                fileId;
  private       SSUri                 thumbUri          = null;

  public SSFileUploader(
    final SSFileRepoConf     fileConf, 
    final SSFileUploadPar    par) throws Exception{
    
    super(fileConf);
    
    this.par               = par;
    this.fileExt           = SSMimeTypeE.fileExtForMimeType             (par.mimeType);
    this.fileUri           = SSServCaller.vocURICreate                  (fileExt);
    this.fileId            = SSVocConf.fileIDFromSSSURI                 (fileUri);
  }
  
  @Override
  public void run(){
    
    try{
      
      this.dbSQL   = (SSDBSQLI)   SSDBSQL.inst.serv();
      this.dbNoSQL = (SSDBNoSQLI) SSDBNoSQL.inst.serv();
      
      sendAnswer();
      readFileFromStreamAndSave();
//      disposeUploadedFile      ();
      
      dbSQL.startTrans(par.shouldCommit);
      
      registerFileAndCreateThumb();
      
      dbSQL.commit(par.shouldCommit);

      sendAnswer();
      
      addFileContentsToNoSQLStore  ();
//      removeFileFromLocalWorkFolder();
      
      evalLogFileUpload();
      
    }catch(Exception error1){
      
      SSServErrReg.regErr(error1);
      
      try{
        par.sSCon.writeErrorFullToClient(SSServErrReg.getServiceImplErrors(), par.op);
      }catch(Exception error2){
        SSServErrReg.regErr(error2);
      }
    }finally{
      
      try{
        finalizeImpl();
      }catch(Exception error3){
        SSLogU.err(error3);
      }
    }
  }
  
  private void readFileFromStreamAndSave() throws Exception{
    
    FileOutputStream fileOutputStream = null;
      
    try{
      
      byte[] fileChunk = null;
      
      fileOutputStream = SSFileU.openOrCreateFileWithPathForWrite   (SSFileRepoConf.getLocalWorkPath() + fileId);

      while(true){
        
        fileChunk = par.sSCon.readFileChunkFromClient();
        
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
          SSLogU.warn("closing file output stream failed");
        }
      }
    }
  }
  
  private void addFileContentsToNoSQLStore(){
    
    try{
      
      dbNoSQL.addDoc(new SSDBNoSQLAddDocPar(fileId));
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.notServerServiceForOpAvailable)){
        SSLogU.warn(SSErrE.notServerServiceForOpAvailable.toString());
      }else{
        SSLogU.warn(error.getMessage());
      }
      
       SSServErrReg.reset();
    }
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread(true);
  }
  
  private void sendAnswer() throws Exception{
    par.sSCon.writeRetFullToClient(SSFileUploadRet.get(fileUri, thumbUri));
  }
  
  private void registerFileAndCreateThumb() throws Exception{
    
    try{
      
      final SSFileRepoServerI fileServ = (SSFileRepoServerI) SSServReg.getServ (SSFileRepoServerI.class);
      final SSFileAddRet      result   = 
        fileServ.fileAdd(
          new SSEntityFileAddPar(
            par.user,
            null,
            null, //fileLength
            null, //fileExt,
            fileUri, //file,
            SSEntityE.uploadedFile, //type,
            par.label, //label,
            null, //entity
            true, //createThumb,
            fileUri, //entityToAddThumbTo,
            false, //removeExistingFilesForEntity,
            par.withUserRestriction,
            false));
      
      thumbUri = result.thumb;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private void evalLogFileUpload() {
    
    try{
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.fileUpload,
          fileUri,  //entity
          null, //content,
          null, //entities
          null, //users
          par.shouldCommit));
    }catch(Exception error){
      SSLogU.warn(error);
      SSServErrReg.reset();
    }
  }
}

//private void uploadFileToWebDav() throws Exception{
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

//  private void uploadFileToI5Cloud() throws Exception{
//
//    try{
//      SSServCaller.i5CloudFileUpload(this.fileId, "private", SSServCaller.i5CloudAuth().get(SSHTMLU.xAuthToken));
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }

//private void disposeUploadedFile() throws Exception{
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

//  private void removeFileFromLocalWorkFolder() throws Exception{
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
  
//  private void moveFileToLocalRepo() throws Exception{
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