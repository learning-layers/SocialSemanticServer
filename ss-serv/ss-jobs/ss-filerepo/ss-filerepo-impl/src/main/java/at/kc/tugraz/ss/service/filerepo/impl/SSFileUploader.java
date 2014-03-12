/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.localwork.serv.SSLocalWorkServ;
import at.kc.tugraz.ss.serv.serv.api.SSServImplStartA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import at.kc.tugraz.ss.service.filerepo.datatypes.enums.SSFileRepoTypeE;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileUploadRet;
import com.googlecode.sardine.SardineFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SSFileUploader extends SSServImplStartA{
  
  private final SSFileUploadPar       par;
  private final SSFileRepoConf        localWorkConf;
  private       FileOutputStream      fileOutputStream  = null;
  private       FileInputStream       fileInputStream   = null;
  private       String                fileId            = null;
  private       byte[]                fileChunk         = null;
  private       SSUri                 uri               = null;
  private       SSSocketCon           sSCon             = null;
  
  public SSFileUploader(
    final SSFileRepoConf     fileRepoConf, 
    final SSSocketCon        sSCon, 
    final SSServPar         par) throws Exception{
    
    super(fileRepoConf);
    
    this.sSCon             = sSCon;
    this.par               = new SSFileUploadPar(par);
    this.localWorkConf     = (SSFileRepoConf) SSLocalWorkServ.inst.servConf;
    
    try{
      this.uri = SSServCaller.fileCreateUri(par.user, SSMimeTypeU.fileExtForMimeType(this.par.mimeType));
      
      switch(fileRepoConf.fileRepoType){
        case fileSys:
          this.fileId            = SSServCaller.fileIDFromURI (par.user, uri);
          this.fileOutputStream  = SSFileU.openOrCreateFileWithPathForWrite   (localWorkConf.getPath() + fileId);
          break;
        default:
          throw new UnsupportedOperationException("impl. currently not supported");
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void run(){
    
    try{
      
//    check whether WebSocket connections need this: 
      sendAnswer(null);
      
      while(true){
        
        fileChunk = sSCon.readFileChunkFromClient();
        
        if(fileChunk.length != 0){
          
          fileOutputStream.write        (fileChunk);
          fileOutputStream.flush        ();
          
//     check whether WebSocket connections need this: sendAnswer(SSStrU.valueGot);
          continue;
        }
        
        fileOutputStream.close();
        
        moveFileToLocalRepo();
        uploadFileToWebDav();
        addFileToSolr();
        addFileEntity();
        
        removeFileFromLocalWorkFolder();
        
        sendAnswer(SSStrU.valueFinished);
        return;
      }
    }catch(Exception error1){
      
      SSServErrReg.regErr(error1);
      
      try{
        sSCon.writeErrorFullToClient(SSServErrReg.getServiceImplErrors(), par.op);
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
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread();
  }
  
  private void sendAnswer(String status) throws Exception{
    sSCon.writeRetFullToClient(SSFileUploadRet.get(uri, status, par.op));
  }
  
  private void removeFileFromLocalWorkFolder() throws Exception{
    
    if(SSStrU.equals(localWorkConf.getPath(), ((SSFileRepoConf)conf).getPath())){
      return;
    }
    
    try{
      SSFileU.delFile(localWorkConf.getPath() + fileId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void moveFileToLocalRepo() throws Exception{
    
    if(!SSFileRepoTypeE.isSame(((SSFileRepoConf)conf).fileRepoType, SSFileRepoTypeE.fileSys)){
      return;
    }
    
    if(SSStrU.equals(localWorkConf.getPath(), ((SSFileRepoConf)conf).getPath())){
      return;
    }
    
    try{
      final File file = new File(localWorkConf.getPath() + fileId);
      
      if(!file.renameTo(new File(((SSFileRepoConf)conf).getPath() + fileId))){
        throw new Exception("couldnt move file to local file repo");
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void uploadFileToWebDav() throws Exception{
    
    if(!SSFileRepoTypeE.isSame(((SSFileRepoConf)conf).fileRepoType, SSFileRepoTypeE.webdav)){
      return;
    }
    
    fileInputStream = SSFileU.openFileForRead(localWorkConf.getPath() + fileId);
    
    SardineFactory.begin(
      ((SSFileRepoConf)conf).user,
      ((SSFileRepoConf)conf).password).put(((SSFileRepoConf)conf).getPath() + fileId, fileInputStream);
    
    fileInputStream.close();
  }

  private void addFileToSolr(){
    
    try{
      SSServCaller.solrAddDoc(par.user, fileId, true);
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }
  }

  private void addFileEntity() throws Exception{
    SSServCaller.addEntity(par.user, uri, par.fileName, SSEntityEnum.file);
  }
}
