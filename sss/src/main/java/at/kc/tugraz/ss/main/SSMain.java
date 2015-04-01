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
package at.kc.tugraz.ss.main;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;

public class SSMain{

  public SSMain() {}
  
  public static void main(String[] args) throws Exception {
    
//    System.getProperties().list(System.out);
    new SSMain().start(args);
  }
  
  public void start(String[] args) throws Exception {

//    addShutDownHookThread ();
    //TODO dtheiler: create conf via service as well
    SSCoreConf.instSet(SSVocConf.fileNameSSSConf);
    
//    initJmx               ();
    
    final Thread initializer = new Thread(new SSSInitializer());
    
    initializer.start();
    initializer.join();
    
    if(!SSSInitializer.isFinished()){
      return;
    }
    
    new Thread(new SSTester()).start();
    
    /* socket adapter */
    if(SSCoreConf.instGet().getSs().use){
      new SSServerSocket().run();
    }
  }
  
//  private void initJmx() throws Exception {
//    
//    MBeanServer  mBeanServer  = ManagementFactory.getPlatformMBeanServer();
//    
//    try{
//      mBeanServer.registerMBean(SSMainConf.inst().getLogConf(),           new ObjectName("SS:log=conflog"));
//      mBeanServer.registerMBean(SSMainConf.inst().getModelConf(),         new ObjectName("SS:conf=confmodel"));
//      mBeanServer.registerMBean(SSMainConf.inst().getsSConf(),            new ObjectName("SS:conf=confss"));
//      mBeanServer.registerMBean(SSMainConf.inst().getDbGraphConf(),       new ObjectName("SS:conf=confts"));
//      mBeanServer.registerMBean(SSMainConf.inst().getFilerepoConf(),      new ObjectName("SS:conf=confrepo"));
//      mBeanServer.registerMBean(SSMainConf.inst().getSolrConf(),          new ObjectName("SS:conf=confsolr"));
//      mBeanServer.registerMBean(SSMainConf.inst().getBroadcasterConf(),   new ObjectName("SS:conf=confbroadcaster"));
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
  //    if(SSStrU.equals(args[0], SSServOpE.scaffRecommTags)){
  //
  //      SSUri user;
  //      SSUri resource;
  //
  //      opPars = new HashMap<>();
  //      opPars.put(SSVarU.userLabel,  SSLabelStr.get("833086"));
  //
  //      user     = (SSUri) SSServReg.callServServer(new SSServPar(SSServOpE.userCreateUri, opPars));
  //      resource = SSUri.get(SSStrU.addAtBegin("Technological_singularity", SSLinkU.wikipediaEn.toString()));
  //
  //      opPars = new HashMap<>();
  //      opPars.put(SSVarU.user,     user);
  //      opPars.put(SSVarU.resource, resource);
  //      opPars.put(SSVarU.maxTags,  10);
  //
  //      System.out.println(SSServReg.callServServer(new SSServPar(SSServOpE.scaffRecommTags, opPars)));
  //    }
  //  }
  //
  //  private void checkAndExecDataImportServTests(String[] args) throws Exception{
  //
  //    if(SSStrU.equals(args[0], SSServOpE.dataImportUserResourceTagFromWikipedia)){
  //
  //      opPars = new HashMap<>();
  //
  //      SSServReg.callServServer(new SSServPar(SSServOpE.dataImportUserResourceTagFromWikipedia, opPars));
  //    }
  //  }
  //
  //  private void checkAndExecLomExtractFromDirServTests(String[] args) throws Exception{
  //
  //    if(SSStrU.equals(args[0], SSServOpE.lomExtractFromDir)){
  //
  //      opPars = new HashMap<>();
  //
  //      SSServReg.callServServer(new SSServPar(SSServOpE.lomExtractFromDir, opPars));
  //    }
  //  }
  //
  //  private void checkAndExecSolrServTests(String[] args) throws Exception{
  //
  //    if(SSStrU.equals(args[0], SSServOpE.solrRemoveDocsAll)){
  //
  //      opPars = new HashMap<>();
  //
  //      SSServReg.callServServer(new SSServPar(SSServOpE.solrRemoveDocsAll, opPars));
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
}

//    if(args.length > 0){
//      
//      //      checkAndExecSolrServTests                  (args);
//      //      checkAndExecLomExtractFromDirServTests     (args);
//      //      checkAndExecDataImportServTests            (args);
//      //      checkAndExecScaffServTests                 (args);
//    }