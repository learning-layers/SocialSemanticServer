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

import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSHTMLU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSSocketU;
import at.tugraz.sss.serv.SSServImplStartA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileDownloadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileDownloadRet;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSToolContextE;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSFileDownloader extends SSServImplStartA{
  
  private DataInputStream          fileReader;
  private byte[]                   chunk             = new byte[SSSocketU.socketTranmissionSize];
  private String                   fileId            = null;
  private int                      fileChunkLength   = -1;
  private SSFileDownloadPar        par               = null;
  private SSFilerepoImpl           servImpl          = null;
  private SSEvalServerI            evalServ          = null;
//  private InputStream webdavInputStream;
  
  public SSFileDownloader(
    final SSFileRepoConf    fileRepoConf, 
    final SSFileDownloadPar par,
    final SSFilerepoImpl    servImpl) throws Exception{
    
    super(fileRepoConf, null);
    
    this.par               = par;
    this.servImpl          = servImpl;
    this.fileId            = SSVocConf.fileIDFromSSSURI(this.par.file);
  }
  
  @Override
  public void run(){
    
    try{
      
      par.sSCon.writeRetFullToClient(new SSFileDownloadRet(par.file));
      
      switch(((SSFileRepoConf)conf).fileRepoType){
        case i5Cloud: downloadFromI5Cloud(); break;    
//    if(SSFileRepoTypeEnum.isSame(fileRepoConf.fileRepoType, SSFileRepoTypeEnum.webdav)){
//      this.webdavInputStream = SardineFactory.begin(fileRepoConf.getUser(), fileRepoConf.getPassword()).getInputStream(fileRepoConf.getPath() + fileId);
////      fileReader = new DataInputStream(SardineFactory.begin(fileRepoConf.getUser(), fileRepoConf.getPassword()).getInputStream(fileRepoConf.getPath() + fileId));
//    }
      }

      fileReader = new DataInputStream (new FileInputStream(new File(((SSFileRepoConf)conf).getPath() + fileId)));
      
      par.sSCon.readMsgFullFromClient();
      
      while(true){
        
//        sSCon.readMsgFullFromClient();
        
        fileChunkLength = fileReader.read(chunk);
        
        if(fileChunkLength == -1){
          par.sSCon.writeFileChunkToClient(new byte[0], fileChunkLength);
          fileReader.close();
          
//          saveActivity();
          
          evalServ = (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class);
          
          evalServ.evalLog(
            new SSEvalLogPar(
              par.user,
              SSToolContextE.sss,
              SSEvalLogE.fileDowload,
              par.file,  //entity
              null, //content,
              null, //entities
              null, //users
              par.shouldCommit));
          
          return;
        }
        
        par.sSCon.writeFileChunkToClient(chunk, fileChunkLength);
      }
      
    }catch(Exception error1){
      
      SSServErrReg.regErr(error1);
      
      try{
        par.sSCon.writeErrorFullToClient(SSServErrReg.getServiceImplErrors(), par.op);
      }catch(Exception error2){
        SSServErrReg.regErr(error2);
      }
    }finally{
      
      if(fileReader != null){
        
        try{
          fileReader.close();
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
  
  private void downloadFromI5Cloud() throws Exception{
    
    try{
      SSServCaller.i5CloudFileDownload(this.fileId, "private", SSServCaller.i5CloudAuth().get(SSHTMLU.xAuthToken));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread(true);
  }

//  private void saveActivity() throws Exception{
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
}