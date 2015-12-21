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
package at.tugraz.sss.adapter.socket;

import at.tugraz.sss.serv.util.SSSocketU;
import at.kc.tugraz.ss.activity.serv.SSActivityServ;
import at.kc.tugraz.ss.category.ss.category.serv.SSCategoryServ;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.friend.serv.SSFriendServ;
import at.kc.tugraz.ss.like.serv.SSLikeServ;
import at.kc.tugraz.ss.message.serv.SSMessageServ;
import at.kc.tugraz.ss.recomm.serv.SSRecommServ;
import at.kc.tugraz.ss.serv.auth.serv.SSAuthServ;
import at.kc.tugraz.ss.serv.dataimport.serv.SSDataImportServ;
import at.kc.tugraz.ss.serv.datatypes.entity.serv.SSEntityServ;
import at.kc.tugraz.ss.serv.datatypes.learnep.serv.SSLearnEpServ;
import at.kc.tugraz.ss.serv.job.dataexport.serv.SSDataExportServ;
import at.kc.tugraz.ss.serv.jobs.evernote.serv.SSEvernoteServ;
import at.kc.tugraz.ss.serv.jsonld.serv.SSJSONLD;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.service.coll.service.SSCollServ;
import at.kc.tugraz.ss.service.disc.service.SSDiscServ;
import at.kc.tugraz.ss.service.filerepo.service.SSFilerepoServ;
import at.kc.tugraz.ss.service.rating.service.SSRatingServ;
import at.kc.tugraz.ss.service.search.service.SSSearchServ;
import at.kc.tugraz.ss.service.tag.service.SSTagServ;
import at.kc.tugraz.ss.service.user.service.SSUserServ;
import at.kc.tugraz.ss.service.userevent.service.SSUEServ;
import at.kc.tugraz.sss.app.serv.SSAppServ;
import at.kc.tugraz.sss.appstacklayout.serv.SSAppStackLayoutServ;
import at.kc.tugraz.sss.comment.serv.SSCommentServ;
import at.kc.tugraz.sss.flag.serv.SSFlagServ;
import at.kc.tugraz.sss.video.serv.SSVideoServ;


import at.tugraz.sss.serv.util.SSEncodingU;
import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.SSJSONLDU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.SSMimeTypeE;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.impl.api.SSServImplStartA;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.db.serv.SSDBNoSQL;
import at.tugraz.sss.serv.db.serv.SSDBSQL;
import at.tugraz.sss.servs.image.serv.SSImageServ;
import at.tugraz.sss.servs.integrationtest.SSIntegrationTestServ;
import at.tugraz.sss.servs.kcprojwiki.serv.SSKCProjWikiServ;
import at.tugraz.sss.servs.livingdocument.serv.SSLivingDocServ;
import at.tugraz.sss.servs.location.serv.SSLocationServ;
import at.tugraz.sss.servs.mail.serv.SSMailServ;
import at.tugraz.sss.servs.ocd.service.SSOCDServ;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import sss.serv.eval.serv.SSEvalServ;

public class SSSocketMainV2{
  
  public SSSocketMainV2() {}
  
  public static void main(String[] args) throws Exception {
    
//    System.getProperties().list(System.out);
    new SSSocketMainV2().start(args);
  }
  
  public void start(String[] args) throws Exception {
    
//    addShutDownHookThread ();
    
//    initJmx               ();
    
    final Thread initializer = new Thread(new SSSInitializer());
    
    initializer.start();
    initializer.join();
    
    /* socket adapter */
    if(SSCoreConf.instGet().getSss().use){
      
      try {
        SSLogU.info("Starting server on port " + SSCoreConf.instGet().getSss().port);
        
        final ServerSocket server = new ServerSocket(SSCoreConf.instGet().getSss().port);
        
        while(true){
          new Thread(new SSSocketMain(server.accept())).start();    //SSCoreConf.instGet().getCloud().use));
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  public class SSSocketMain extends SSServImplStartA implements Runnable{
    
    private final Socket             clientSocket;
    private final OutputStreamWriter outputStreamWriter;
    private final InputStreamReader  inputStreamReader;
    private final SSSocketAdapterU   socketAdapterU;
    private SSServContainerI         serv        = null;
    private SSServImplA              servImpl    = null;
    private SSServPar                par         = null;
    private SSServRetI               ret         = null;
    
    public SSSocketMain(
      final Socket  clientSocket) throws Exception{
      
      super(null);
      
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
              
        par = new SSServPar(clientSocket,clientMsg);
        
        SSLogU.info(par.op + " start with " + par.clientJSONRequ);
        
        serv     = SSServReg.inst.getClientServ(par.op);
        servImpl = serv.getServImpl();
        
        SSServReg.inst.regClientRequest(par.user, servImpl, par.op);
        
        ret = servImpl.invokeClientServOp(serv.servImplClientInteraceClass, par);
              
        if(ret != null){
          socketAdapterU.writeRetFullToClient(outputStreamWriter, ret); 
        }
        
      }catch(Exception error){
        
        SSServErrReg.regErr(error, false);
        
        if(par == null){
          SSServErrReg.regErr(new Exception("couldnt get serv par"), true);
        }
        
        try{
          socketAdapterU.writeError(
            outputStreamWriter, 
            par.op);
          
        }catch(Exception error2){
          SSServErrReg.regErr(error2, true);
        }
      }finally{
        
        try{
          finalizeImpl();
          SSLogU.info(par.op + " end ");
        }catch(Exception error3){
          SSLogU.err(error3);
        }
      }
    }
    
    @Override
    protected void finalizeImpl() throws Exception{
      
      finalizeThread(false);
      
      SSServReg.inst.unregClientRequest(par.op, par.user, servImpl);
    }
    
//    public static void regServImplUsedByThread(final SSServImplA servImpl){
//      
//      List<SSServImplA> servImplUsedList = servImplsUsedByThread.get();
//      
//      if(servImplUsedList.contains(servImpl)){
//        return;
//      }
//      
//      servImplUsedList.add(servImpl);
//    }
    
    @Override
    protected void finalizeThread(final Boolean log){
      super.finalizeThread(true);
    }
  }
  
  public class SSSInitializer extends SSServImplStartA{
    
    public SSSInitializer() throws Exception{
      super(null);
    }
    
    @Override
    public void run(){
      
      try{
        
        SSCoreConf.instSet(SSVocConf.fileNameSSSConf);
        
        try{
          SSFileExtE.init    ();
          SSMimeTypeE.init   ();
          SSJSONLDU.init(SSCoreConf.instGet().getJsonLD().uri);
          
        }catch(Exception error1){
          SSServErrReg.regErr(error1);
          return;
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
          
        }catch(Exception error1){
          SSServErrReg.regErr(error1);
          return;
        }
        
        try{ //initializing
          SSAuthServ.inst.initServ();
          SSEntityServ.inst.initServ();
          SSDataImportServ.inst.initServ();
          SSCategoryServ.inst.initServ();
          SSRecommServ.inst.initServ();
        }catch(Exception error1){
          SSServErrReg.regErr(error1);
          return;
        }
        
        try{ //integration tests
          SSIntegrationTestServ.inst.initServ();
        }catch(Exception error1){
          SSServErrReg.regErr(error1);
          return;
        }
        
        try{ //scheduling
          SSEntityServ.inst.schedule         ();
          SSSearchServ.inst.schedule         ();
          SSLearnEpServ.inst.schedule        ();
          SSDataImportServ.inst.schedule     ();
          SSRecommServ.inst.schedule         ();
          SSKCProjWikiServ.inst.schedule     ();
          SSEvalServ.inst.schedule           ();
        }catch(Exception error1){
          SSServErrReg.regErr(error1);
          return;
        }
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
      }finally{
        try{
          finalizeImpl();
        }catch(Exception error2){
          SSLogU.err(error2);
        }
      }
    }
    
    @Override
    protected void finalizeImpl() throws Exception{
      finalizeThread(true);
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