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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSSocketU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplStartA;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import static at.kc.tugraz.ss.service.filerepo.datatypes.enums.SSFileRepoTypeE.fileSys;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileThumbGetPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileThumbGetRet;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class SSFileThumbGetter extends SSServImplStartA{
  
  private DataInputStream          fileReader;
  private byte[]                   chunk             = new byte[SSSocketU.socketTranmissionSize];
  private int                      fileChunkLength   = -1;
  private SSSocketCon              sSCon             = null;
  private SSFileThumbGetPar        par               = null;
  
  
  public SSFileThumbGetter(
    final SSFileRepoConf fileRepoConf, 
    final SSSocketCon    sSCon, 
    final SSServPar      parA) throws Exception{
    
    super(fileRepoConf);
    
    this.sSCon                = sSCon;
    this.par                  = new SSFileThumbGetPar(parA);
    
    switch(fileRepoConf.fileRepoType){
        case fileSys:
          fileReader = new DataInputStream (new FileInputStream(new File(fileRepoConf.getPath() + this.par.fileId)));
          break;
        default:
          throw new UnsupportedOperationException("impl. currently not supported");
    }
  }
  
  @Override
  public void run(){
    
    try{
      
      sSCon.writeRetFullToClient(new SSFileThumbGetRet(par.fileId, par.op));
      
      while(true){
        
        sSCon.readMsgFullFromClient();
        
        fileChunkLength = fileReader.read(chunk);
        
        if(fileChunkLength == -1){
          sSCon.writeFileChunkToClient(new byte[0], fileChunkLength);
          fileReader.close();
          return;
        }
        
        if(sSCon.writeFileChunkToClient(chunk, fileChunkLength)){
          continue;
        }
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
}


//      BufferedImage         imageBuff;
//    byte[]                imageData   = null;
//    ByteArrayOutputStream baos;
//    InputStream           imageIn;
//      imageIn      = new ByteArrayInputStream(ArrayUtils.toPrimitive(nonPrimBytes));
//      imageBuff    = ImageIO.read(imageIn);
//      baos         = new ByteArrayOutputStream();
//      ImageIO.write(imageBuff, SSFileExtU.png, baos);
//      imageData = baos.toByteArray();
//      imageData = com.sun.jersey.core.util.Base64.encode(imageData);