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
package at.tugraz.sss.adapter.socket.v3;

import at.kc.tugraz.ss.activity.serv.*;
import at.kc.tugraz.ss.category.ss.category.serv.*;
import at.kc.tugraz.ss.friend.serv.*;
import at.kc.tugraz.ss.like.serv.*;
import at.kc.tugraz.ss.message.serv.*;
import at.kc.tugraz.ss.recomm.serv.*;
import at.kc.tugraz.ss.serv.auth.serv.*;
import at.kc.tugraz.ss.serv.dataimport.serv.*;
import at.kc.tugraz.ss.serv.datatypes.entity.serv.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.serv.*;
import at.kc.tugraz.ss.serv.job.dataexport.serv.*;
import at.kc.tugraz.ss.serv.jobs.evernote.serv.*;
import at.kc.tugraz.ss.serv.jsonld.serv.*;
import at.kc.tugraz.ss.service.coll.service.*;
import at.kc.tugraz.ss.service.disc.service.*;
import at.kc.tugraz.ss.service.filerepo.service.*;
import at.kc.tugraz.ss.service.rating.service.*;
import at.kc.tugraz.ss.service.search.service.*;
import at.kc.tugraz.ss.service.tag.service.*;
import at.kc.tugraz.ss.service.user.service.*;
import at.kc.tugraz.ss.service.userevent.service.*;
import at.kc.tugraz.sss.app.serv.*;
import at.kc.tugraz.sss.appstacklayout.serv.*;
import at.kc.tugraz.sss.comment.serv.*;
import at.kc.tugraz.sss.flag.serv.*;
import at.kc.tugraz.sss.video.serv.*;
import at.tugraz.sss.adapter.socket.SSSocketAdapterU;
import at.tugraz.sss.conf.*;
import at.tugraz.sss.serv.util.SSSocketU;
import at.tugraz.sss.serv.util.SSEncodingU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.impl.api.SSServImplStartA;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.db.serv.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.image.serv.*;
import at.tugraz.sss.servs.integrationtest.*;
import at.tugraz.sss.servs.kcprojwiki.serv.*;
import at.tugraz.sss.servs.livingdocument.serv.*;
import at.tugraz.sss.servs.location.serv.*;
import at.tugraz.sss.servs.mail.serv.*;
import at.tugraz.sss.servs.ocd.service.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import sss.serv.eval.serv.*;

public class SSSocketMain extends SSServImplStartA{
  
  public SSSocketMain() {
    super(null);
  }
  
  public static void main(String[] args) throws Exception {
    
//    System.getProperties().list(System.out);
    
    new SSSocketMain().start(args);
  }
  
  public void start(String[] args) throws Exception {
    
//    addShutDownHookThread ();
//    initJmx               ();
    
//    final Thread initializer = new Thread(new SSInitializer());
//    initializer.start();
//    initializer.join();
    try{
      
      SSCoreConf.instSet("C:\\workspace_git\\tomcat\\SocialSemanticServer\\sssWorkDir\\" + SSFileU.fileNameSSSConf);
      
      try{
        SSLogU.init(SSCoreConf.instGet().getSss().getSssWorkDir());
        SSFileExtE.init    ();
        SSMimeTypeE.init   ();
        SSJSONLDU.init(SSCoreConf.instGet().getJsonLD().uri);
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
//      CLASS.FORNAME("ASDF").NEWINSTANCE()
//      SSServContainerI.class.getMethod("regServ").invoke(SSDBSQL.inst);
      
      try{
        SSDBSQL.inst.regServ               ();
        SSDBNoSQL.inst.regServ             ();
        SSEntityServ.inst.regServ          ();
        SSUserServ.inst.regServ            ();
        SSCollServ.inst.regServ            ();
        SSUEServ.inst.regServ              ();
        SSTagServ.inst.regServ             ();
        SSAuthServ.inst.regServ            ();
        SSEvernoteServ.inst.regServ        ();
        SSFilerepoServ.inst.regServ        ();
        SSDataImportServ.inst.regServ      ();
        SSJSONLD.inst.regServ              ();
        SSRatingServ.inst.regServ          ();
        SSCategoryServ.inst.regServ        ();
        SSDiscServ.inst.regServ            ();
        SSLearnEpServ.inst.regServ         ();
        SSActivityServ.inst.regServ        ();
        SSSearchServ.inst.regServ          ();
        SSDataExportServ.inst.regServ      ();
        SSRecommServ.inst.regServ          ();
        SSFlagServ.inst.regServ            ();
        SSCommentServ.inst.regServ         ();
        SSMessageServ.inst.regServ         ();
        SSAppServ.inst.regServ             ();
        SSFriendServ.inst.regServ          ();
        SSAppStackLayoutServ.inst.regServ  ();
        SSVideoServ.inst.regServ           ();
        SSLikeServ.inst.regServ            ();
        SSEvalServ.inst.regServ            ();
        SSOCDServ.inst.regServ             ();
        SSImageServ.inst.regServ           ();
        SSLocationServ.inst.regServ        ();
        SSIntegrationTestServ.inst.regServ ();
        SSLivingDocServ.inst.regServ       ();
        SSMailServ.inst.regServ            ();
        SSKCProjWikiServ.inst.regServ      ();
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
      try{ //initializing
        SSAuthServ.inst.initServ();
        SSEntityServ.inst.initServ();
        SSDataImportServ.inst.initServ();
        SSCategoryServ.inst.initServ();
        SSRecommServ.inst.initServ();
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
      try{ //integration tests
        SSIntegrationTestServ.inst.initServ();
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
      try{ //scheduling
        SSEntityServ.inst.schedule         ();
        SSSearchServ.inst.schedule         ();
        SSLearnEpServ.inst.schedule        ();
        SSDataImportServ.inst.schedule     ();
        SSRecommServ.inst.schedule         ();
        SSKCProjWikiServ.inst.schedule     ();
        SSEvalServ.inst.schedule           ();
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
      if(SSCoreConf.instGet().getSss().use){
        
        SSLogU.info("Starting server on port " + SSCoreConf.instGet().getSss().port);
        
        final ServerSocket server = new ServerSocket(SSCoreConf.instGet().getSss().port);
        
        while(true){
          new Thread(new SSSocket(server.accept())).start();    //SSCoreConf.instGet().getCloud().use));
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      SSServReg.destroy();
    }
  }
  
  public class SSSocket implements Runnable{
    
    private final Socket             clientSocket;
    private final OutputStreamWriter outputStreamWriter;
    private final InputStreamReader  inputStreamReader;
    private final SSSocketAdapterU   socketAdapterU;
    
    private SSServContainerI         serv        = null;
    private SSServImplA              servImpl    = null;
    private SSServPar                par         = null;
    private SSServRetI               ret         = null;
    
    public SSSocket(
      final Socket  clientSocket) throws Exception{
      
      this.clientSocket      = clientSocket;
      
      this.inputStreamReader =
        new InputStreamReader(
          clientSocket.getInputStream(),
          SSEncodingU.utf8.toString());
      
      this.outputStreamWriter =
        new OutputStreamWriter(
          clientSocket.getOutputStream(),
          SSEncodingU.utf8.toString());
      
      this.socketAdapterU = new SSSocketAdapterU();
      
    }
    
    @Override
    public void run(){
      
      try{
        
        final String clientMsg = SSSocketU.readFullString(inputStreamReader);
        
        par = new SSServPar(clientSocket, clientMsg);
        
        SSLogU.info(par.op + " start with " + par.clientJSONRequ);
        
        serv     = SSServReg.inst.getClientServContainer(par.op);
        servImpl = serv.getServImpl();
        
        SSServReg.inst.regClientRequest(par.user, servImpl, par.op);
        
        ret = servImpl.invokeClientServOp(serv.servImplClientInteraceClass, par);
        
        socketAdapterU.writeRetFullToClient(outputStreamWriter, ret);
        
      }catch(Exception error){
        
        SSLogU.err(error);
        
        if(par == null){
          SSLogU.err(new Exception("couldnt get serv par"));
        }
        
        try{
          socketAdapterU.writeError(outputStreamWriter, par.op);
        }catch(Exception error1){
          SSLogU.err(error1);
        }
      }finally{
        
        try {
          SSServReg.inst.unregClientRequest(par.op, par.user, servImpl);
        }catch(Exception error) {
          SSLogU.err(error);
        }
        
//        SSServErrReg.logAndReset(true);
      }
    }
  }
}
//  private void initJmx() throws Exception {
//
//    MBeanServer  mBeanServer  = ManagementFactory.getPlatformMBeanServer();
//
//    try{
//      mBeanServer.registerMBean(SSMainConf.inst().getLogConf(),           new ObjectName("SS:log=conflog"));
//      mBeanServer.registerMBean(SSMainConf.inst().getsSConf(),            new ObjectName("SS:conf=confss"));
//      mBeanServer.registerMBean(SSMainConf.inst().getDbGraphConf(),       new ObjectName("SS:conf=confts"));
//      mBeanServer.registerMBean(SSMainConf.inst().getFilerepoConf(),      new ObjectName("SS:conf=confrepo"));
//      mBeanServer.registerMBean(SSMainConf.inst().getSolrConf(),          new ObjectName("SS:conf=confsolr"));
//      mBeanServer.registerMBean(SSMainConf.inst().getAuthConf(),          new ObjectName("SS:conf=confauth"));
//      mBeanServer.registerMBean(SSMainConf.inst().getLomExtractorConf(),  new ObjectName("SS:conf=conflomextractor"));
//    }catch (Exception error){
//      SSLogU.logAndThrow(error);
//    }
//  }

//  private void addShutDownHookThread(){
//
//    Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
//  }

//  private void waitForEnterPressed()throws Exception{
//
//    try {
//      SSLogU.logInfo("waiting info");
//
//      System.out.println("Press to continue...");
//      System.in.read();
//
//      SSLogU.logInfo("waiting info");
//    }catch(Exception error){
//      SSLogU.logAndThrow(error);
//    }
//  }

//  private void checkAndExecScaffServTests(String[] args) throws Exception{
//
//    if(SSStrU.equals(args[0], SSVarNames.scaffRecommTags)){
//
//      SSUri user;
//      SSUri resource;
//
//      opPars = new HashMap<>();
//      opPars.put(SSVarU.userLabel,  SSLabelStr.get("833086"));
//
//      user     = (SSUri) SSServReg.callServServer(new SSServPar(SSVarNames.userCreateUri, opPars));
//      resource = SSUri.get(SSStrU.addAtBegin("Technological_singularity", SSLinkU.wikipediaEn.toString()));
//
//      opPars = new HashMap<>();
//      opPars.put(SSVarU.user,     user);
//      opPars.put(SSVarU.resource, resource);
//      opPars.put(SSVarU.maxTags,  10);
//
//      System.out.println(SSServReg.callServServer(new SSServPar(SSVarNames.scaffRecommTags, opPars)));
//    }
//  }
//
//  private void checkAndExecDataImportServTests(String[] args) throws Exception{
//
//    if(SSStrU.equals(args[0], SSVarNames.dataImportUserResourceTagFromWikipedia)){
//
//      opPars = new HashMap<>();
//
//      SSServReg.callServServer(new SSServPar(SSVarNames.dataImportUserResourceTagFromWikipedia, opPars));
//    }
//  }
//
//  private void checkAndExecLomExtractFromDirServTests(String[] args) throws Exception{
//
//    if(SSStrU.equals(args[0], SSVarNames.lomExtractFromDir)){
//
//      opPars = new HashMap<>();
//
//      SSServReg.callServServer(new SSServPar(SSVarNames.lomExtractFromDir, opPars));
//    }
//  }
//
//  private void checkAndExecSolrServTests(String[] args) throws Exception{
//
//    if(SSStrU.equals(args[0], SSVarNames.solrRemoveDocsAll)){
//
//      opPars = new HashMap<>();
//
//      SSServReg.callServServer(new SSServPar(SSVarNames.solrRemoveDocsAll, opPars));
//    }
//  }

/**
 * introduced for having control over shutting down the server. e.g. catches Strg+C
 */
//  public class ShutdownHookThread extends Thread {
//
//    @Override
//    public void run() {
//
//      SSLogU.logInfo("shutting down server...");
//
//      if(SSObjU.isNull(socketServer)){
//        return;
//      }
//
//      socketServer.setRun(false);
//    }
//  } // end of class ShutdownHook

//    if(args.length > 0){
//
//      //      checkAndExecSolrServTests                  (args);
//      //      checkAndExecLomExtractFromDirServTests     (args);
//      //      checkAndExecDataImportServTests            (args);
//      //      checkAndExecScaffServTests                 (args);
//    }