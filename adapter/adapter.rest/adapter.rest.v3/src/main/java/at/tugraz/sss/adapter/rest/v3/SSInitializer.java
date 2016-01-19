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
import at.tugraz.sss.conf.*;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.db.serv.*;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.image.serv.*;
import at.tugraz.sss.servs.integrationtest.*;
import at.tugraz.sss.servs.kcprojwiki.serv.*;
import at.tugraz.sss.servs.livingdocument.serv.*;
import at.tugraz.sss.servs.location.serv.*;
import at.tugraz.sss.servs.mail.serv.*;
import at.tugraz.sss.servs.ocd.service.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import sss.serv.eval.serv.*;

@WebListener
public class SSInitializer extends SSServImplStartA implements ServletContextListener{
  
  public SSInitializer() {
    super(null);
  }
  
  public SSInitializer(final SSConfA conf) {
    super(conf);
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    
    try {
      
      destroy();
      
      SSDBSQLMySQLImpl.closePool();
    } catch (Exception error) {
      SSLogU.err(error);
    }
  }
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    
    try{
      
      SSCoreConf.instSet("C:\\workspace_git\\tomcat\\SocialSemanticServer\\sssWorkDir\\" + SSFileU.fileNameSSSConf);
//      SSCoreConf.instSet("/home/dtheiler/bp.preparation/" + SSFileU.fileNameSSSConf);
      
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
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
}