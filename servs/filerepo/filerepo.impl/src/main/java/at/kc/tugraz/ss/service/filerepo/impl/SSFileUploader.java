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
import at.tugraz.sss.serv.SSHTMLU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.tugraz.sss.serv.SSServImplStartA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileUploadRet;
import at.kc.tugraz.ss.service.filerepo.impl.fct.SSFileServCaller;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImageAddPar;
import com.googlecode.sardine.SardineFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSFileUploader extends SSServImplStartA{
  
  private final SSFileUploadPar       par;
  private       SSFileExtE            fileExt;
  private       FileOutputStream      fileOutputStream  = null;
  private       FileInputStream       fileInputStream   = null;
  private       String                fileId            = null;
  private       byte[]                fileChunk         = null;
  private       SSUri                 fileUri           = null;
  private       SSUri                 thumbUri          = null;
  private       String                localWorkPath     = null;
  private       SSEvalServerI         evalServ          = null;
  
  public SSFileUploader(
    final SSFileRepoConf     fileRepoConf, 
    final SSFileUploadPar    par) throws Exception{
    
    super(fileRepoConf, null);
    
    this.par               = par;
    this.localWorkPath     = SSCoreConf.instGet().getSss().getLocalWorkPath();
    
    try{
      this.fileExt           = SSMimeTypeE.fileExtForMimeType             (this.par.mimeType);
      this.fileUri           = SSServCaller.vocURICreate                  (this.fileExt);
      this.fileId            = SSVocConf.fileIDFromSSSURI(fileUri);
      this.fileOutputStream  = SSFileU.openOrCreateFileWithPathForWrite   (localWorkPath + fileId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void run(){
    
    try{
      
      this.dbSQL = (SSDBSQLI) SSDBSQL.inst.serv();
      
      sendAnswer();
      
      while(true){
        
        fileChunk = par.sSCon.readFileChunkFromClient();
        
        if(fileChunk.length != 0){
          
          fileOutputStream.write        (fileChunk);
          fileOutputStream.flush        ();
          continue;
        }
        
        fileOutputStream.close();
        
        switch(((SSFileRepoConf)conf).fileRepoType){
          case fileSys: moveFileToLocalRepo(); break;
          case webdav:  uploadFileToWebDav();  break;
          case i5Cloud: uploadFileToI5Cloud(); break;
        }
        
        dbSQL.startTrans(par.shouldCommit);
        
        ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).fileAdd(
          new SSEntityFileAddPar(
            par.user,
            fileUri, 
            SSEntityE.uploadedFile, //type
            par.label,
            null, 
            par.withUserRestriction, 
            false));
        
        SSFileServCaller.addFileContentsToSolr   (par, fileId);

        removeFileFromLocalWorkFolder();
        
        createFileThumb();
        
        dbSQL.commit(par.shouldCommit);
        
        evalServ = (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class);
        
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
        
        sendAnswer();
        return;
      }
    }catch(Exception error1){
      
      SSServErrReg.regErr(error1);
      
      try{
        par.sSCon.writeErrorFullToClient(SSServErrReg.getServiceImplErrors(), par.op);
      }catch(Exception error2){
        SSServErrReg.regErr(error2);
      }
    }finally{
      
      if(fileOutputStream != null){
        
        try{
          fileOutputStream.close();
        }catch(IOException ex){
          SSLogU.err(ex);
        }
      }
      
      try{
        finalizeImpl();
      }catch(Exception error3){
        SSLogU.err(error3);
      }
    }
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread(true);
  }
  
  private void sendAnswer() throws Exception{
    par.sSCon.writeRetFullToClient(SSFileUploadRet.get(fileUri, thumbUri));
  }
  
  private void removeFileFromLocalWorkFolder() throws Exception{
    
    if(SSStrU.equals(localWorkPath, ((SSFileRepoConf)conf).getPath())){
      return;
    }
    
    try{
      SSFileU.delFile(localWorkPath + fileId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void moveFileToLocalRepo() throws Exception{
    
    if(SSStrU.equals(localWorkPath, ((SSFileRepoConf)conf).getPath())){
      return;
    }
    
    try{
      final File file = new File(localWorkPath + fileId);
      
      if(!file.renameTo(new File(((SSFileRepoConf)conf).getPath() + fileId))){
        throw new Exception("couldnt move file to local file repo");
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void uploadFileToWebDav() throws Exception{
    
    try{
      fileInputStream = SSFileU.openFileForRead(localWorkPath + fileId);
      
      SardineFactory.begin(
        ((SSFileRepoConf)conf).user,
        ((SSFileRepoConf)conf).password).put(((SSFileRepoConf)conf).getPath() + fileId, fileInputStream);
      
      fileInputStream.close();
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }
  }
  
  private void uploadFileToI5Cloud() throws Exception{

    try{
      SSServCaller.i5CloudFileUpload(this.fileId, "private", SSServCaller.i5CloudAuth().get(SSHTMLU.xAuthToken));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  //TODO dtheiler: currently works with local file repository only (not web dav or any remote stuff; even dont if localWorkPath != local file repo path)
  private void createFileThumb(){
    
    try{
      
      thumbUri = SSServCaller.vocURICreate                  (SSFileExtE.png);
      
      final String filePath          = localWorkPath + fileId;
      final String pngFilePath       = localWorkPath + SSVocConf.fileIDFromSSSURI (thumbUri);
      Boolean      thumbCreated      = false;
      
      if(SSStrU.contains(SSFileExtE.imageFileExts, fileExt)){
        SSFileU.scalePNGAndWrite(ImageIO.read(new File(filePath)), pngFilePath, 500, 500);
        thumbCreated = true;
      }
      
      switch(fileExt){
        
        case pdf:{
          SSFileU.writeScaledPNGFromPDF(filePath, pngFilePath, 500, 500, false);
          thumbCreated = true;
          break;
        }
        
        case doc:{
          
          final String pdfFilePath = localWorkPath + SSVocConf.fileIDFromSSSURI(SSServCaller.vocURICreate     (SSFileExtE.pdf));
          
          SSFileU.writePDFFromDoc       (filePath,    pdfFilePath);
          SSFileU.writeScaledPNGFromPDF (pdfFilePath, pngFilePath, 500, 500, false);
          thumbCreated = true;          
        }
      }
      
      if(thumbCreated){
        
        ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).fileAdd(
          new SSEntityFileAddPar(
            par.user,
            thumbUri, //file
            SSEntityE.file, //type
            null, //label
            null, //entity
            false, //withUserRestriction
            false));//shouldCommit
        
        ((SSImageServerI) SSServReg.getServ(SSImageServerI.class)).imageAdd(
          new SSImageAddPar(
            par.user,
            null, //uuid
            null, //link
            SSImageE.thumb, //imageType,
            fileUri, //entity
            thumbUri, //file
            false, //withUserRestriction,
            false)); //shouldCommit
      }
      
    }catch(Exception error){
      SSLogU.warn("thumb couldnt be created from file with ext :" + fileExt);
    }
  }
}