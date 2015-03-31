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
package at.kc.tugraz.ss.cloud.impl.fct.op;

import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSIDU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.cloud.conf.SSCloudConf;
import at.tugraz.sss.serv.SSCoreConfA;
import at.kc.tugraz.ss.conf.conf.SSConf;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;

import at.tugraz.sss.serv.SSServA;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSServErrReg;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

public class SSCloudPublishServiceFct{

  public static String getLocalWorkTmpDirPath() throws Exception{
    return SSCoreConf.instGet().getSs().getLocalWorkPath() + SSFileU.correctDirPath (SSIDU.uniqueID());
  }
  
  public static void publishServiceFromWindowsToWindowsLocally(
    final SSServA serv,
    final String  localWorkTmpDirPath) throws Exception{
    
    try{
      
      setAndSaveServiceConf(
        serv,
        localWorkTmpDirPath,
        "F:\\SSSInstance\\tmp\\",
        SSVocConf.serverNameLocalhost,
        7001);
      
      copyFilesLocally(
        localWorkTmpDirPath,
        "F:\\SSSInstance\\");
      
      startServiceLocally();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void publishServiceFromWindowsToUnixRemotely(
    final SSServA serv, 
    final String  localWorkTmpDirPath) throws Exception{
    
    try{
      
      setAndSaveServiceConf(
        serv,
        localWorkTmpDirPath,
        "/home/dtheiler/SSSService/tmp/",
        "1234", /* TODO dtheiler: add server name to conf */ 
        9000);
      
      copyFilesRemotely(
        localWorkTmpDirPath,
        "/home/dtheiler/SSSService/",
        "1234", /* TODO dtheiler: add server name to conf */ 
        "adf",
        "asdf");
      
      startServiceRemotely(
        "/home/dtheiler/SSSService/",
        "1234", /* TODO dtheiler: add server name to conf */ 
        "adsf",
        "adsf");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private static void startServiceRemotely(
    final String  serviceDestDirPath,
    final String  host,
    final String  userName, 
    final String  password) throws Exception{
    
    Session     session     = null;
    Channel     channel     = null;
    
    try{

      final ChannelExec channelExec;
      JSch              jsch = new JSch();
      
      session = jsch.getSession(userName, host, 22);
      
      java.util.Properties config = new java.util.Properties();
      config.put         ("StrictHostKeyChecking", "no");
      
      session.setConfig  (config);
      session.setPassword(password);
      session.connect();
      
      String command = 
        "cd " + serviceDestDirPath + SSStrU.semiColon + 
        "java -jar -Dlog4j.configuration=file:log4j.properties " + serviceDestDirPath + SSVocConf.fileNameSSSJar;
      
      channel = session.openChannel("exec");
      
      channelExec = (ChannelExec) channel;
      channelExec.setCommand(command);
      
      channel.setInputStream(null);
      
      ((ChannelExec) channel).setErrStream(System.err);
      
      channel.connect();
      
      Thread.sleep(4000);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{

      if(channel != null){
        channel.disconnect();
      }
      
      if(session != null){
        session.disconnect();
      }
    }
  }
  
  private static void startServiceLocally() throws Exception{
    
    try{
      
      Runtime.getRuntime().exec("cmd.exe /c cd \"" + "F:\\SSSInstance\\" + "\" & start cmd.exe /k \"runit.bat\"");
      
      Thread.sleep(4000);
      
    }catch(IOException error){
      SSServErrReg.regErrThrow(error);
    }catch(InterruptedException error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private static void copyFilesRemotely(
    final String  localWorkTmpDirPath,
    final String  serviceDestDirPath,
    final String  host,
    final String  userName, 
    final String  password) throws Exception{
    
    Session           session   = null;
    Channel           channel   = null;
    FileInputStream   in        = null;
    
    try{
      
      final Properties  config   = new java.util.Properties();
      final JSch        jsch     = new JSch();
      final ChannelSftp channelSftp;
      
      session = jsch.getSession(userName, host, 22);

      config.put          ("StrictHostKeyChecking", "no");
      session.setPassword (password);
      session.setConfig   (config);
      session.connect     ();
      
      channel = session.openChannel("sftp");
      channel.connect();
      
      channelSftp = (ChannelSftp)channel;
      
      try{
        channelSftp.mkdir(serviceDestDirPath);
      }catch(SftpException error){
        SSLogU.warn("service folder already exists");
      }
      
      channelSftp.cd(serviceDestDirPath);
      
      in = SSFileU.openFileForRead(localWorkTmpDirPath + SSVocConf.fileNameSSSConf);
        
      channelSftp.put(
        in, 
        SSVocConf.fileNameSSSConf);
      
      in.close();
      
      in = SSFileU.openFileForRead(SSFileU.dirWorking() + SSVocConf.fileNameSSSJar);
        
      channelSftp.put(
        in, 
        SSVocConf.fileNameSSSJar);
      
      in.close();

      in = SSFileU.openFileForRead(SSFileU.dirWorking() + SSVocConf.fileNameLog4JProperties);
      
      channelSftp.put(
        in, 
        SSVocConf.fileNameLog4JProperties);
      
      in.close();
      
      in = SSFileU.openFileForRead(SSFileU.dirWorking() + SSVocConf.fileNameRunitSh);
      
      channelSftp.put(
        in,
        SSVocConf.fileNameRunitSh);
      
      in.close();
      
      try{
        channelSftp.mkdir (serviceDestDirPath + SSVocConf.dirNameLib);
      }catch(SftpException error){
        SSLogU.warn("service lib folder already exists");
      }
      
      channelSftp.cd (serviceDestDirPath + SSVocConf.dirNameLib);
      
      for(File file : SSFileU.filesForDirPath(SSFileU.dirWorking() + SSVocConf.dirNameLib)){
        
        in = new FileInputStream(file);
        
        channelSftp.put(
          in,
          file.getName());
        
        in.close();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{

      if(in != null){
        in.close();
      }
      
      if(channel != null){
        channel.disconnect();
      }
      
      if(session != null){
        session.disconnect();
      }
     
      FileUtils.deleteDirectory(new File(localWorkTmpDirPath));
    }
  }

  private static void copyFilesLocally(
    final String localWorkTmpDirPath,
    final String serviceDestDirPath) throws Exception{
    
    try{
      FileUtils.copyFile(
        new File(localWorkTmpDirPath  + SSVocConf.fileNameSSSConf),
        new File(serviceDestDirPath + SSVocConf.fileNameSSSConf));
      
      FileUtils.copyFile(
        new File(SSFileU.dirWorking() + SSVocConf.fileNameSSSJar),
        new File(serviceDestDirPath + SSVocConf.fileNameSSSJar));
      
      FileUtils.copyFile(
        new File(SSFileU.dirWorking() + SSVocConf.fileNameLog4JProperties),
        new File(serviceDestDirPath + SSVocConf.fileNameLog4JProperties));
      
      FileUtils.copyFile(
        new File(SSFileU.dirWorking() + SSVocConf.fileNameRunitBat),
        new File(serviceDestDirPath + SSVocConf.fileNameRunitBat));
      
      FileUtils.copyDirectory(
        new File(SSFileU.dirWorking() + SSVocConf.dirNameLib),
        new File(serviceDestDirPath   + SSVocConf.dirNameLib));

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      FileUtils.deleteDirectory(new File(localWorkTmpDirPath));
    }
  }

  private static void setAndSaveServiceConf(
    final SSServA     servToStart,
    final String      localWorkTmpDirPath,
    final String      serviceDestDirTmpPath,
    final String      host,
    final Integer     port) throws Exception{
    
    try{
      final SSCoreConfA              confForServ        = servToStart.getConfForCloudDeployment(SSCoreConf.copy(), new ArrayList<Class>());
      final SSCloudConf              cloudConf          = ((SSCoreConf)confForServ).getCloud();
      final SSConf                   ssConf             = ((SSCoreConf)confForServ).getSs();
      final SSAuthConf               authConf           = ((SSCoreConf)confForServ).getAuth();
//      final SSDBSQLConf              sqlConf            = ((SSCoreConf)confForServ).getDbSQLConf();
      
      cloudConf.use          = false;
      ssConf.host            = host;
      ssConf.port            = port;
      authConf.initAtStartUp = false;
      
//      sqlConf.host     = "localhost";
//      sqlConf.schema   = "sss_cloud";
      
      ssConf.setLocalWorkPath(serviceDestDirTmpPath);
      
      confForServ.save(localWorkTmpDirPath + SSVocConf.fileNameSSSConf);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
