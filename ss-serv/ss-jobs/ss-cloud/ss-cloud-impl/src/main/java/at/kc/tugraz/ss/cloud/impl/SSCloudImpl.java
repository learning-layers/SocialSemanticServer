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
package at.kc.tugraz.ss.cloud.impl;

import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.cloud.api.SSCloudClientI;
import at.kc.tugraz.ss.cloud.api.SSCloudServerI;
import at.kc.tugraz.ss.cloud.conf.SSCloudConf;
import at.kc.tugraz.ss.cloud.datatypes.par.SSCloudPublishServicePar;
import at.kc.tugraz.ss.cloud.datatypes.ret.SSCloudPublishServiceRet;
import at.kc.tugraz.ss.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.conf.conf.SSConf;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

public class SSCloudImpl extends SSServImplMiscA implements SSCloudClientI, SSCloudServerI{

  public SSCloudImpl(final SSConfA conf) throws Exception{

    super(conf);
  }
  
  /* SSCloudClientI */
//  @Override
//  public void collUserParentGet(SSSocketCon sSCon, SSServPar parA) throws Exception{
//
//    SSServCaller.checkKey(parA);
//
//    SSColl collParent = collUserParentGet(parA);
//
//    sSCon.writeRetFullToClient(SSCollUserParentGetRet.get(collParent, parA.op));
//  }

  /* SSCloudServerI */
  @Override
  public SSCloudPublishServiceRet cloudPublishService(final SSServPar parA) throws Exception{

    try{
      final SSCloudPublishServicePar par           = new SSCloudPublishServicePar(parA);
      final SSCoreConfA              confForServ   = par.serv.getConfForCloudDeployment(SSCoreConf.copy(), new ArrayList<Class>());
      final String                   localWorkPath = SSFileU.correctDirPath(SSCoreConf.instGet().getSsConf().getLocalWorkPath());
      final SSCloudConf              cloudConf     = ((SSCoreConf)confForServ).getCloudConf();
      final SSConf                   ssConf        = ((SSCoreConf)confForServ).getSsConf();
      final String                   confFileName  = "ss-conf" + SSStrU.dot + SSFileExtU.yaml;
      final String                   confFilePath  = localWorkPath + SSFileU.correctDirPath (SSIDU.uniqueID());
      final String                   destDirPath   = "F:\\SSSInstance\\";
      
      cloudConf.use  = false;
      ssConf.host    = "localhost";
      ssConf.port    = 7000;
      ssConf.setLocalWorkPath(destDirPath);
      
      confForServ.save(confFilePath + confFileName);

      FileUtils.copyFile(
        new File(confFilePath + confFileName), 
        SSFileU.openOrCreateFileWithPathForWrite(destDirPath + confFileName));
      
      FileUtils.copyFile(
        new File(SSFileU.dirWorking() + "ss.jar"), 
        SSFileU.openOrCreateFileWithPathForWrite(destDirPath + "ss.jar"));
      
      FileUtils.copyFile(
        new File(SSFileU.dirWorking() + "log4j.properties"), 
        SSFileU.openOrCreateFileWithPathForWrite(destDirPath + "log4j.properties"));
      
      FileUtils.copyFile(
        new File(SSFileU.dirWorking() + "runit.sh"), 
        SSFileU.openOrCreateFileWithPathForWrite(destDirPath + "runit.sh"));
      
      FileUtils.copyFile(
        new File(SSFileU.dirWorking() + "runit.bat"), 
        SSFileU.openOrCreateFileWithPathForWrite(destDirPath + "runit.bat"));
      
      FileUtils.copyDirectory(
        new File(SSFileU.dirWorking() + "lib\\"), 
        new File(destDirPath + "lib\\"));
      
      FileUtils.deleteDirectory(new File(confFilePath));
     
      System.out.println("");

//      Runtime.getRuntime().exec(command)

//      moveFileToDir();
      
      return SSCloudPublishServiceRet.get("host", 1);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private Boolean moveFileToDir() throws Exception{
    
    Session     session     = null;
    Channel     channel     = null;
    ChannelSftp channelSftp = null;
    JSch        jsch;
    
    try{
      jsch    = new JSch();
      session = jsch.getSession("dtheiler", "kedemo.know-center.tugraz.at", 22);
     
      session.setPassword("somepassword");
      
      java.util.Properties config = new java.util.Properties();
      config.put         ("StrictHostKeyChecking", "no");
      session.setConfig  (config);
      session.connect    ();
      
      channel = session.openChannel("sftp");
      channel.connect();
      
      channelSftp = (ChannelSftp)channel;
      channelSftp.cd("/home/dtheiler/");
      
      File   file     = new File("F:\\20140425 Attacher.pdf");
      String fileName = file.getName();
      
      channelSftp.put(new FileInputStream(file), fileName);
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErr(error);
      return null;
    }finally{

      if(channel != null){
        channel.disconnect();
      }
      
      if(session != null){
        session.disconnect();
      }
    }
  }
}
