/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.adapter.rest.v3;

import at.tugraz.sss.serv.conf.*;
import at.tugraz.sss.servs.file.serv.SSFileServ;
import at.tugraz.sss.servs.activity.serv.*;
import at.tugraz.sss.servs.friend.serv.*;
import at.tugraz.sss.servs.like.serv.*;
import at.tugraz.sss.servs.message.serv.*;
import at.tugraz.sss.servs.recomm.serv.*;
import at.tugraz.sss.servs.dataexport.serv.*;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.requestlimit.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.app.serv.*;
import at.tugraz.sss.servs.appstacklayout.serv.*;
import at.tugraz.sss.servs.auth.serv.*;
import at.tugraz.sss.servs.category.serv.*;
import at.tugraz.sss.servs.coll.serv.*;
import at.tugraz.sss.servs.comment.serv.*;
import at.tugraz.sss.servs.conf.SSCoreConf;
import at.tugraz.sss.servs.dataimport.serv.*;
import at.tugraz.sss.servs.db.impl.*;
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
import at.tugraz.sss.servs.link.serv.SSLinkServ;
import at.tugraz.sss.servs.livingdoc.serv.*;
import at.tugraz.sss.servs.location.serv.*;
import at.tugraz.sss.servs.mail.serv.*;
import at.tugraz.sss.servs.ocd.serv.*;
import at.tugraz.sss.servs.rating.serv.*;
import at.tugraz.sss.servs.search.serv.*;
import at.tugraz.sss.servs.tag.serv.*;
import at.tugraz.sss.servs.user.serv.*;
import at.tugraz.sss.servs.video.serv.*;
import javax.servlet.*;
import javax.servlet.annotation.*;

@WebListener
public class SSInitializer 
  extends SSServImplA
  implements ServletContextListener{
  
  public SSInitializer() {
    super(null);
  }
  
  public SSInitializer(final SSConfA conf) {
    super(conf);
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    
    try{
      
      new SSSchedules().clear();
      new SSClientRequestLimit().clear();
      
      SSDBSQLMySQLImpl.closePool();
    } catch (Exception error) {
      SSLogU.err(error);
    }
  }
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    
    try{
      
      try{
//        SSCoreConf.instSet("/sssWorkDir/" + SSFileU.fileNameSSSConf);
SSCoreConf.instSet("C:\\workspace_git\\master\\SocialSemanticServer\\sssWorkDir\\" + SSFileU.fileNameSSSConf);
//      SSCoreConf.instSet("/home/dtheiler/bp.demo/" + SSFileU.fileNameSSSConf);
//        SSCoreConf.instSet("/home/dtheiler/eval/" + SSFileU.fileNameSSSConf);
//        SSCoreConf.instSet("/home/dtheiler/internal/" + SSFileU.fileNameSSSConf);
//        SSCoreConf.instSet("/home/dtheiler/construction/" + SSFileU.fileNameSSSConf);
//        SSCoreConf.instSet("/home/dtheiler/test/" + SSFileU.fileNameSSSConf);
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
//      
////      CLASS.FORNAME("ASDF").NEWINSTANCE()
////      SSServContainerI.class.getMethod("regServ").invoke(SSDBSQL.inst);
//      
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
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
}