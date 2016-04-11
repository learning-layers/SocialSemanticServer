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

import at.tugraz.sss.adapter.socket.*;
import at.tugraz.sss.serv.conf.*;
import at.tugraz.sss.servs.activity.serv.*;
import at.tugraz.sss.servs.friend.serv.*;
import at.tugraz.sss.servs.like.serv.*;
import at.tugraz.sss.servs.message.serv.*;
import at.tugraz.sss.servs.recomm.serv.*;
import at.tugraz.sss.servs.dataexport.serv.*;
import at.tugraz.sss.servs.app.serv.*;
import at.tugraz.sss.servs.appstacklayout.serv.*;
import at.tugraz.sss.servs.auth.serv.*;
import at.tugraz.sss.servs.category.serv.*;
import at.tugraz.sss.servs.coll.serv.*;
import at.tugraz.sss.servs.comment.serv.*;
import at.tugraz.sss.servs.dataimport.serv.*;
import at.tugraz.sss.servs.db.serv.*;
import at.tugraz.sss.servs.disc.serv.*;
import at.tugraz.sss.servs.entity.serv.*;
import at.tugraz.sss.servs.eval.serv.*;
import at.tugraz.sss.servs.evernote.serv.*;
import at.tugraz.sss.servs.flag.serv.*;
import at.tugraz.sss.servs.image.serv.*;
import at.tugraz.sss.servs.jsonld.serv.*;
import at.tugraz.sss.servs.kcprojwiki.serv.*;
import at.tugraz.sss.servs.learnep.serv.*;
import at.tugraz.sss.servs.livingdoc.serv.*;
import at.tugraz.sss.servs.location.serv.*;
import at.tugraz.sss.servs.mail.serv.*;
import at.tugraz.sss.servs.ocd.serv.*;
import at.tugraz.sss.servs.rating.serv.*;
import at.tugraz.sss.servs.search.serv.*;
import at.tugraz.sss.servs.tag.serv.*;
import at.tugraz.sss.servs.user.serv.*;
import at.tugraz.sss.servs.video.serv.*;
import at.tugraz.sss.serv.util.SSSocketU;
import at.tugraz.sss.serv.util.SSEncodingU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.requestlimit.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.file.serv.*;
import at.tugraz.sss.servs.link.serv.*;
import java.io.*;
import java.net.*;
import java.sql.*;

public class SSSocketMain extends SSServImplA{
  
  private final SSClientRequestLimit clientRequestLimit = new SSClientRequestLimit();
  
  public SSSocketMain() {
    super(null);
  }
  
  public static void main(String[] args) throws SSErr {
    
//    System.getProperties().list(System.out);
    
    new SSSocketMain().start(args);
  }
  
  public void start(String[] args) throws SSErr {
    
//    addShutDownHookThread ();
//    initJmx               ();
    
//    final Thread initializer = new Thread(new SSInitializer());
//    initializer.start();
//    initializer.join();
    try{
      
      try{
        SSCoreConf.instSet("/sssWorkDir/" + SSFileU.fileNameSSSConf);
//        SSCoreConf.instSet("C:\\workspace_git\\master\\SocialSemanticServer\\sssWorkDir\\" + SSFileU.fileNameSSSConf);
      }catch(Exception error){
        System.err.println("conf couldnt be set");
        SSServErrReg.regErrThrow(error);
      }
      
      try{
        SSLogU.init(SSConf.getSssWorkDir());
      }catch(Exception error){
        System.err.println("logUtil couldnt be set");
        SSServErrReg.regErrThrow(error);
      }
      
      try{
        
        SSFileExtE.init    ();
        SSMimeTypeE.init   ();
        SSJSONLDU.init(SSCoreConf.instGet().getJsonLD().uri);
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
//      CLASS.FORNAME("ASDF").NEWINSTANCE()
//      SSServContainerI.class.getMethod("regServ").invoke(SSDBSQL.inst);
      
      try{
        SSDBSQL.inst.regServ               (SSCoreConf.instGet().getDbSQL());
        SSDBNoSQL.inst.regServ             (SSCoreConf.instGet().getDbNoSQL());
        SSEntityServ.inst.regServ          (SSCoreConf.instGet().getEntity());
        SSUserServ.inst.regServ            (SSCoreConf.instGet().getUser());
        SSCollServ.inst.regServ            (SSCoreConf.instGet().getColl());
        SSTagServ.inst.regServ             (SSCoreConf.instGet().getTag());
        SSAuthServ.inst.regServ            (SSCoreConf.instGet().getAuth());
        SSEvernoteServ.inst.regServ        (SSCoreConf.instGet().getEvernote());
        SSFileServ.inst.regServ            (SSCoreConf.instGet().getFile());
        SSDataImportServ.inst.regServ      (SSCoreConf.instGet().getDataImport());
        SSJSONLD.inst.regServ              (SSCoreConf.instGet().getJsonLD());
        SSRatingServ.inst.regServ          (SSCoreConf.instGet().getRating());
        SSCategoryServ.inst.regServ        (SSCoreConf.instGet().getCategory());
        SSDiscServ.inst.regServ            (SSCoreConf.instGet().getDisc());
        SSLearnEpServ.inst.regServ         (SSCoreConf.instGet().getLearnEp());
        SSActivityServ.inst.regServ        (SSCoreConf.instGet().getActivity());
        SSSearchServ.inst.regServ          (SSCoreConf.instGet().getSearch());
        SSDataExportServ.inst.regServ      (SSCoreConf.instGet().getDataExport());
        SSRecommServ.inst.regServ          (SSCoreConf.instGet().getRecomm());
        SSFlagServ.inst.regServ            (SSCoreConf.instGet().getFlag());
        SSCommentServ.inst.regServ         (SSCoreConf.instGet().getComment());
        SSMessageServ.inst.regServ         (SSCoreConf.instGet().getMessage());
        SSAppServ.inst.regServ             (SSCoreConf.instGet().getApp());
        SSFriendServ.inst.regServ          (SSCoreConf.instGet().getFriend());
        SSAppStackLayoutServ.inst.regServ  (SSCoreConf.instGet().getAppStackLayout());
        SSVideoServ.inst.regServ           (SSCoreConf.instGet().getVideo());
        SSLikeServ.inst.regServ            (SSCoreConf.instGet().getLike());
        SSEvalServ.inst.regServ            (SSCoreConf.instGet().getEval());
        SSOCDServ.inst.regServ             (SSCoreConf.instGet().getOcd());
        SSImageServ.inst.regServ           (SSCoreConf.instGet().getImage());
        SSLocationServ.inst.regServ        (SSCoreConf.instGet().getLocation());
        SSLivingDocServ.inst.regServ       (SSCoreConf.instGet().getLivingDocument());
        SSMailServ.inst.regServ            (SSCoreConf.instGet().getMail());
        SSKCProjWikiServ.inst.regServ      (SSCoreConf.instGet().getKcprojwiki());
        SSLinkServ.inst.regServ            (SSCoreConf.instGet().getLink());
        
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
      
      try{ //scheduling
        SSEntityServ.inst.schedule         ();
        SSSearchServ.inst.schedule         ();
        SSLearnEpServ.inst.schedule        ();
        SSDataImportServ.inst.schedule     ();
        SSRecommServ.inst.schedule         ();
        SSKCProjWikiServ.inst.schedule     ();
        SSEvalServ.inst.schedule           ();
        SSFileServ.inst.schedule           ();
        SSMailServ.inst.schedule           ();
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
      new SSSchedules().clear();
      clientRequestLimit.clear();
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
      final Socket  clientSocket) throws UnsupportedEncodingException, IOException{
      
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
      
      Connection sqlCon = null;
      
      try{
        
        final String     clientMsg = SSSocketU.readFullString(inputStreamReader);
        
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
        
        par = 
          new SSServPar(
            clientSocket, 
            sqlCon,
            clientMsg);
        
        SSLogU.info(par.op + " start with " + par.clientJSONRequ);
        
        serv     = SSServReg.inst.getClientServContainer(par.op);
        servImpl = serv.getServImpl();
        
        clientRequestLimit.regClientRequest(par.user, servImpl, par.op);
        
        ret = (SSServRetI) serv.servImplClientInteraceClass.getMethod(SSStrU.toStr(par.op), SSClientE.class, SSServPar.class).invoke(this, SSClientE.socket, par);
        
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
        
        try{
          if(sqlCon != null){
            sqlCon.close();
          }
          
        }catch(Exception error){
          SSLogU.err(error);
        }
        
        try {
          clientRequestLimit.unregClientRequest(par.op, par.user, servImpl);
        }catch(Exception error) {
          SSLogU.err(error);
        }
        
//        SSServErrReg.logAndReset(true);
      }
    }
  }
}
//  private void initJmx() throws SSErr {
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

//  private void waitForEnterPressed()throws SSErr{
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

//  private void checkAndExecScaffServTests(String[] args) throws SSErr{
//
//    if(SSStrU.isEqual(args[0], SSVarNames.scaffRecommTags)){
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
//  private void checkAndExecDataImportServTests(String[] args) throws SSErr{
//
//    if(SSStrU.isEqual(args[0], SSVarNames.dataImportUserResourceTagFromWikipedia)){
//
//      opPars = new HashMap<>();
//
//      SSServReg.callServServer(new SSServPar(SSVarNames.dataImportUserResourceTagFromWikipedia, opPars));
//    }
//  }
//
//  private void checkAndExecLomExtractFromDirServTests(String[] args) throws SSErr{
//
//    if(SSStrU.isEqual(args[0], SSVarNames.lomExtractFromDir)){
//
//      opPars = new HashMap<>();
//
//      SSServReg.callServServer(new SSServPar(SSVarNames.lomExtractFromDir, opPars));
//    }
//  }
//
//  private void checkAndExecSolrServTests(String[] args) throws SSErr{
//
//    if(SSStrU.isEqual(args[0], SSVarNames.solrRemoveDocsAll)){
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