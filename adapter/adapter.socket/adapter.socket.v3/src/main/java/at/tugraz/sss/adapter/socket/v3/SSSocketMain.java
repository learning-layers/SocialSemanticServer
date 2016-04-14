/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.adapter.socket.v3;

import at.tugraz.sss.servs.common.impl.*;
import at.tugraz.sss.servs.util.*;
import at.tugraz.sss.servs.entity.datatype.*;
import at.tugraz.sss.servs.db.api.*;
import at.tugraz.sss.servs.activity.api.*;
import at.tugraz.sss.servs.activity.impl.*;
import at.tugraz.sss.servs.app.api.*;
import at.tugraz.sss.servs.app.impl.*;
import at.tugraz.sss.servs.appstacklayout.api.*;
import at.tugraz.sss.servs.appstacklayout.impl.*;
import at.tugraz.sss.servs.auth.api.*;
import at.tugraz.sss.servs.auth.impl.*;
import at.tugraz.sss.servs.category.api.*;
import at.tugraz.sss.servs.category.impl.*;
import at.tugraz.sss.servs.coll.api.*;
import at.tugraz.sss.servs.coll.impl.*;
import at.tugraz.sss.servs.comment.api.*;
import at.tugraz.sss.servs.comment.impl.*;
import at.tugraz.sss.servs.common.api.*;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.dataimport.impl.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.disc.api.*;
import at.tugraz.sss.servs.disc.impl.*;
import at.tugraz.sss.servs.entity.api.*;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.eval.api.*;
import at.tugraz.sss.servs.eval.impl.*;
import at.tugraz.sss.servs.evernote.impl.*;
import at.tugraz.sss.servs.file.api.*;
import at.tugraz.sss.servs.file.impl.*;
import at.tugraz.sss.servs.flag.api.*;
import at.tugraz.sss.servs.flag.impl.*;
import at.tugraz.sss.servs.friend.api.*;
import at.tugraz.sss.servs.friend.impl.*;
import at.tugraz.sss.servs.image.api.*;
import at.tugraz.sss.servs.image.impl.*;
import at.tugraz.sss.servs.jsonld.api.*;
import at.tugraz.sss.servs.jsonld.impl.*;
import at.tugraz.sss.servs.kcprojwiki.impl.*;
import at.tugraz.sss.servs.learnep.impl.*;
import at.tugraz.sss.servs.like.api.*;
import at.tugraz.sss.servs.like.impl.*;
import at.tugraz.sss.servs.link.api.*;
import at.tugraz.sss.servs.link.impl.*;
import at.tugraz.sss.servs.livingdoc.api.*;
import at.tugraz.sss.servs.livingdoc.impl.*;
import at.tugraz.sss.servs.location.impl.*;
import at.tugraz.sss.servs.mail.impl.*;
import at.tugraz.sss.servs.message.api.*;
import at.tugraz.sss.servs.message.impl.*;
import at.tugraz.sss.servs.rating.api.*;
import at.tugraz.sss.servs.rating.impl.*;
import at.tugraz.sss.servs.search.api.*;
import at.tugraz.sss.servs.search.impl.*;
import at.tugraz.sss.servs.tag.api.*;
import at.tugraz.sss.servs.tag.impl.*;
import at.tugraz.sss.servs.user.api.*;
import at.tugraz.sss.servs.user.impl.*;
import at.tugraz.sss.servs.video.api.*;
import at.tugraz.sss.servs.video.impl.*;
import java.io.*;
import java.net.*;
import java.sql.*;

public class SSSocketMain extends SSServImplA{
  
  private final SSClientServs                   clientServs                   = new SSClientServs();
  private final SSEntityCopied                  entityCopied                  = new SSEntityCopied();
  private final SSCircleContentRemoved          circleContentRemoved          = new SSCircleContentRemoved();
  private final SSDescribeEntity                describeEntity                = new SSDescribeEntity();
  private final SSGetUsersResources             getUsersResources             = new SSGetUsersResources();
  private final SSCopyEntity                    copyEntity                    = new SSCopyEntity();
  private final SSAddAffiliatedEntitiesToCircle addAffiliatedEntitiesToCircle = new SSAddAffiliatedEntitiesToCircle();
  private final SSClientRequestLimit            clientRequestLimit            = new SSClientRequestLimit();
  private final SSPushEntitiesToUsers           pushEntitiesToUsers           = new SSPushEntitiesToUsers();
  private final SSGetUserRelations              getUserRelations              = new SSGetUserRelations();
  private final SSEntitiesSharedWithUsers       entitiesSharedWithUsers       = new SSEntitiesSharedWithUsers();
      
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
//        SSCoreConf.instSet("/sssWorkDir/" + SSFileU.fileNameSSSConf);
        SSCoreConf.instSet("C:\\workspace_git\\newish_1\\SocialSemanticServer\\sssWorkDir\\" + SSFileU.fileNameSSSConf);
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
        //entity
        clientServs.regServ                  (new SSEntityImpl(), SSEntityClientI.class);
        getUsersResources.regServ            (new SSEntityImpl(), SSCoreConf.instGet().getEntity());
        addAffiliatedEntitiesToCircle.regServ(new SSEntityImpl(), SSCoreConf.instGet().getEntity());
        describeEntity.regServ               (new SSEntityImpl(), SSCoreConf.instGet().getEntity());
        copyEntity.regServ                   (new SSEntityImpl(), SSCoreConf.instGet().getEntity());
        
        //user
        clientServs.regServ                  (new SSEntityImpl(), SSUserClientI.class);
        describeEntity.regServ               (new SSUserImpl(), SSCoreConf.instGet().getUser());
        
        //coll
        clientServs.regServ                   (new SSCollImpl(), SSCollClientI.class);
        describeEntity.regServ                (new SSCollImpl(), SSCoreConf.instGet().getColl());
        pushEntitiesToUsers.regServ           (new SSCollImpl(), SSCoreConf.instGet().getColl());
        addAffiliatedEntitiesToCircle.regServ (new SSCollImpl(), SSCoreConf.instGet().getColl());
        getUserRelations.regServ              (new SSCollImpl(), SSCoreConf.instGet().getColl());
        getUsersResources.regServ             (new SSCollImpl(), SSCoreConf.instGet().getColl());
        
        //tag
        clientServs.regServ                   (new SSTagImpl(), SSTagClientI.class);
        describeEntity.regServ                (new SSTagImpl(), SSCoreConf.instGet().getTag());
        entityCopied.regServ                  (new SSTagImpl(), SSCoreConf.instGet().getTag());
        circleContentRemoved.regServ          (new SSTagImpl(), SSCoreConf.instGet().getTag());
        getUserRelations.regServ              (new SSTagImpl(), SSCoreConf.instGet().getTag());
        getUsersResources.regServ             (new SSTagImpl(), SSCoreConf.instGet().getTag());
        
        //auth
        clientServs.regServ                   (new SSAuthImpl(), SSAuthClientI.class);
        
        //evernote
        describeEntity.regServ                (new SSEvernoteImpl(), SSCoreConf.instGet().getEvernote());
        
        //file
        clientServs.regServ                   (new SSFileImpl(), SSFileClientI.class);
        describeEntity.regServ                (new SSFileImpl(), SSCoreConf.instGet().getFile());
        addAffiliatedEntitiesToCircle.regServ (new SSFileImpl(), SSCoreConf.instGet().getFile());
        
        //jsonld
        clientServs.regServ                   (new SSJSONLDImpl(), SSJSONLDClientI.class);
        
        //rating
        clientServs.regServ                   (new SSRatingImpl(), SSRatingClientI.class);
        describeEntity.regServ                (new SSRatingImpl(), SSCoreConf.instGet().getRating());
        getUserRelations.regServ              (new SSRatingImpl(), SSCoreConf.instGet().getRating());
        getUsersResources.regServ             (new SSRatingImpl(), SSCoreConf.instGet().getRating());
        
        //category
        clientServs.regServ                   (new SSCategoryImpl(), SSCategoryClientI.class);
        describeEntity.regServ                (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        entityCopied.regServ                  (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        circleContentRemoved.regServ          (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        getUserRelations.regServ              (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        getUsersResources.regServ             (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        
        //disc
        clientServs.regServ                   (new SSDiscImpl(), SSDiscClientI.class);
        describeEntity.regServ                (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        pushEntitiesToUsers.regServ           (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        addAffiliatedEntitiesToCircle.regServ (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        getUserRelations.regServ              (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        getUsersResources.regServ             (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        
        //learnep
        describeEntity.regServ                (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        copyEntity.regServ                    (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        pushEntitiesToUsers.regServ           (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        addAffiliatedEntitiesToCircle.regServ (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        entitiesSharedWithUsers.regServ       (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        getUsersResources.regServ             (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        
        //activity
        clientServs.regServ                (new SSActivityImpl(), SSActivityClientI.class);
        describeEntity.regServ             (new SSActivityImpl(), SSCoreConf.instGet().getActivity());
        getUsersResources.regServ          (new SSActivityImpl(), SSCoreConf.instGet().getActivity());
        
        //search
        clientServs.regServ                (new SSSearchImpl(), SSSearchClientI.class);
        
        //flag
        clientServs.regServ                (new SSFlagImpl(), SSFlagClientI.class);
        describeEntity.regServ             (new SSFlagImpl(), SSCoreConf.instGet().getFlag());
        getUsersResources.regServ          (new SSFlagImpl(), SSCoreConf.instGet().getFlag());
        
        //comment
        clientServs.regServ                   (new SSCommentImpl(), SSCommentClientI.class);
        describeEntity.regServ                (new SSCommentImpl(), SSCoreConf.instGet().getComment());
        getUserRelations.regServ              (new SSCommentImpl(), SSCoreConf.instGet().getComment());
        
        //message
        clientServs.regServ                   (new SSMessageImpl(), SSMessageClientI.class);
        describeEntity.regServ                (new SSMessageImpl(), SSCoreConf.instGet().getMessage());
        
        //app
        clientServs.regServ                (new SSAppImpl(), SSAppClientI.class);
        describeEntity.regServ             (new SSAppImpl(), SSCoreConf.instGet().getApp());
        
        //friend
        clientServs.regServ                (new SSFriendImpl(), SSFriendClientI.class);
        describeEntity.regServ             (new SSFriendImpl(), SSCoreConf.instGet().getFriend());
        
        //appstacklayout
        clientServs.regServ                (new SSAppStackLayoutImpl(), SSAppStackLayoutClientI.class);
        describeEntity.regServ             (new SSAppStackLayoutImpl(), SSCoreConf.instGet().getAppStackLayout());
        
        //video
        clientServs.regServ                   (new SSVideoImpl(), SSVideoClientI.class);
        describeEntity.regServ                (new SSVideoImpl(), SSCoreConf.instGet().getVideo());
        pushEntitiesToUsers.regServ           (new SSVideoImpl(), SSCoreConf.instGet().getVideo());
        addAffiliatedEntitiesToCircle.regServ (new SSVideoImpl(), SSCoreConf.instGet().getVideo());
        getUsersResources.regServ             (new SSVideoImpl(), SSCoreConf.instGet().getVideo());
        
        //like        
        clientServs.regServ                   (new SSLikeImpl(), SSLikeClientI.class);
        describeEntity.regServ                (new SSLikeImpl(), SSCoreConf.instGet().getLike());
        
        //eval
        clientServs.regServ                (new SSEvalImpl(), SSEvalClientI.class);
        
        //image
        clientServs.regServ                   (new SSImageImpl(), SSImageClientI.class);
        describeEntity.regServ                (new SSImageImpl(), SSCoreConf.instGet().getImage());
        addAffiliatedEntitiesToCircle.regServ (new SSImageImpl(), SSCoreConf.instGet().getImage());
        getUsersResources.regServ             (new SSImageImpl(), SSCoreConf.instGet().getImage());
        
        //location
        describeEntity.regServ                (new SSLocationImpl(), SSCoreConf.instGet().getLocation());
        
        //living doc
        clientServs.regServ                   (new SSLivingDocImpl(), SSLivingDocClientI.class);
        describeEntity.regServ                (new SSLivingDocImpl(), SSCoreConf.instGet().getLivingDocument());
        pushEntitiesToUsers.regServ           (new SSLivingDocImpl(), SSCoreConf.instGet().getLivingDocument());
        getUsersResources.regServ             (new SSLivingDocImpl(), SSCoreConf.instGet().getLivingDocument());
        
        //link
        clientServs.regServ                   (new SSLinkImpl(), SSLinkClientI.class);
        getUsersResources.regServ             (new SSLinkImpl(), SSCoreConf.instGet().getLink());
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
      try{ //initializing
        new SSAuthImpl().initServ();
        new SSEntityImpl().initServ();
        new SSCategoryImpl().initServ();
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
      try{ //scheduling

        new SSEntityImpl().schedule();
        new SSSearchImpl().schedule();
        new SSLearnEpImpl().schedule();
        new SSDataImportImpl().schedule();
        new SSKCProjWikiImpl().schedule();
        new SSEvalImpl().schedule();
        new SSFileImpl().schedule();
        new SSMailImpl().schedule();
        
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
    
    private final SSDBSQLI           dbSQL = new SSDBSQLMySQLImpl();
    private final Socket             clientSocket;
    private final OutputStreamWriter outputStreamWriter;
    private final InputStreamReader  inputStreamReader;
    private final SSSocketAdapterU   socketAdapterU;
    
    private SSServImplA              serv        = null;
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
        
        sqlCon = dbSQL.createConnection();
        
        par = 
          new SSServPar(
            clientSocket, 
            sqlCon,
            clientMsg);
        
        SSLogU.info(par.op + " start with " + par.clientJSONRequ);
        
        serv = clientServs.getServ(par.op);
        
        clientRequestLimit.regClientRequest(par.user, serv, par.op);
        
        ret = (SSServRetI) clientServs.getClass(serv).getMethod(SSStrU.toStr(par.op), SSClientE.class, SSServPar.class).invoke(this, SSClientE.socket, par);
        
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
          clientRequestLimit.unregClientRequest(par.op, par.user, serv);
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