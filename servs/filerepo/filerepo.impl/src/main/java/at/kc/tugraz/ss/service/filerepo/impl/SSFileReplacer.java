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

import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSHTMLU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSServImplStartA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileReplacePar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileReplaceRet;
import at.kc.tugraz.ss.service.filerepo.impl.fct.SSFileServCaller;
import at.tugraz.sss.serv.SSServErrReg;
import com.googlecode.sardine.SardineFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SSFileReplacer extends SSServImplStartA{
  
  private FileOutputStream                                fileOutputStream  = null;
  private FileInputStream                                 fileInputStream   = null;
  private String                                          fileId            = null;
  private byte[]                                          fileChunk         = null;
  private SSFileReplacePar                                par               = null;
  private String                                          localWorkPath     = null;
  private SSFilerepoImpl                                  servImpl          = null;
  
  public SSFileReplacer(
    final SSFileRepoConf                            fileRepoConf,
    final SSFileReplacePar                          par, 
    final SSFilerepoImpl                            servImpl) throws Exception{
    
    super(fileRepoConf, null);
    
    this.par                  = par;
    this.servImpl             = servImpl;
    this.localWorkPath        = SSCoreConf.instGet().getSss().getLocalWorkPath();
    this.fileId               = SSVocConf.fileIDFromSSSURI(par.file);
    this.fileOutputStream     = SSFileU.openOrCreateFileWithPathForWrite (localWorkPath + fileId);
  }
  
  @Override
  public void run(){
    
    try{
      
      //check whether WebSocket connections need this:
      par.sSCon.writeRetFullToClient(new SSFileReplaceRet(par.file, par.user));
      
      while(true){
        
        fileChunk = par.sSCon.readFileChunkFromClient();
        
        if(fileChunk.length != 0){
          
          fileOutputStream.write        (fileChunk);
          fileOutputStream.flush        ();
          
          //check whether WebSocket connections need this: sSCon.writeRetFullToClient(new SSFileReplaceRet(par.uri, par.user, SSStrU.valueGot));
          continue;
        }
        
        fileOutputStream.close();
        
//        synchronized(fileAccessProperties){
          
//          if(SSServCaller.fileCanWrite(par.user, par.file).canWrite){
            
            switch(((SSFileRepoConf)conf).fileRepoType){
              case fileSys: moveFileToLocalRepo();  break;
              case webdav:  uploadFileToWebDav();   break;
              case i5Cloud: uploadFileToI5Cloud();  break;
            }
            
//            SSServCaller.fileRemoveReaderOrWriter(par.user, par.file, true, true);
            
            SSFileServCaller.replaceFileContentsInSolr(
              par.user, 
              fileId,  
              true);
//          }
//        }
        
        removeFileFromLocalWorkFolder();
        
        par.sSCon.writeRetFullToClient(new SSFileReplaceRet(par.file, par.user));
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
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void uploadFileToI5Cloud() throws Exception{

    try{
      SSServCaller.i5CloudFileUpload(fileId, "private", SSServCaller.i5CloudAuth().get(SSHTMLU.xAuthToken));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
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
}