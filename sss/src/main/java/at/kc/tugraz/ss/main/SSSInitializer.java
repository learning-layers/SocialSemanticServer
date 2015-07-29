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

import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.kc.tugraz.ss.activity.serv.SSActivityServ;
import at.kc.tugraz.ss.category.ss.category.serv.SSCategoryServ;
import at.kc.tugraz.ss.circle.serv.SSCircleServ;
import at.kc.tugraz.ss.cloud.serv.SSCloudServ;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.friend.serv.SSFriendServ;
import at.kc.tugraz.ss.like.serv.SSLikeServ;
import at.kc.tugraz.ss.message.serv.SSMessageServ;
import at.tugraz.sss.serv.SSDBSQL;
import at.kc.tugraz.ss.serv.jsonld.serv.SSJSONLD;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.serv.auth.serv.SSAuthServ;
import at.kc.tugraz.ss.serv.dataimport.serv.SSDataImportServ;
import at.kc.tugraz.ss.serv.job.dataexport.serv.SSDataExportServ;
import at.kc.tugraz.ss.serv.job.i5cloud.serv.SSI5CloudServ;
import at.kc.tugraz.ss.recomm.serv.SSRecommServ;
import at.kc.tugraz.ss.serv.datatypes.entity.serv.SSEntityServ;
import at.kc.tugraz.ss.serv.datatypes.learnep.serv.SSLearnEpServ;
import at.kc.tugraz.ss.serv.jobs.evernote.serv.SSEvernoteServ;
import at.tugraz.sss.serv.SSJSONLDU;
import at.kc.tugraz.ss.serv.modeling.ue.serv.SSModelUEServ;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSServImplStartA;
import at.kc.tugraz.ss.service.broadcast.service.SSBroadcasterServ;
import at.kc.tugraz.ss.service.coll.service.SSCollServ;
import at.kc.tugraz.ss.service.disc.service.SSDiscServ;
import at.kc.tugraz.ss.service.filerepo.service.SSFilerepoServ;
import at.kc.tugraz.ss.service.rating.service.SSRatingServ;
import at.kc.tugraz.ss.service.search.service.SSSearchServ;
import at.kc.tugraz.ss.service.solr.service.SSSolrServ;
import at.kc.tugraz.ss.service.tag.service.SSTagServ;
import at.kc.tugraz.ss.service.user.service.SSUserServ;
import at.kc.tugraz.ss.service.userevent.service.SSUEServ;
import at.kc.tugraz.sss.app.serv.SSAppServ;
import at.kc.tugraz.sss.appstacklayout.serv.SSAppStackLayoutServ;
import at.kc.tugraz.sss.comment.serv.SSCommentServ;
import at.kc.tugraz.sss.flag.serv.SSFlagServ;
import at.kc.tugraz.sss.video.serv.SSVideoServ;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.servs.location.serv.SSLocationServ;
import at.tugraz.sss.servs.ocd.service.SSOCDServ;
import at.tugraz.sss.servs.image.serv.SSImageServ;
import at.tugraz.sss.servs.integrationtest.SSIntegrationTestServ;
import sss.serv.eval.serv.SSEvalServ;

public class SSSInitializer extends SSServImplStartA{
  
  protected static Boolean finished = false;
  
  public SSSInitializer() throws Exception{
    super(null, null);
  }
  
  @Override
  public void run(){
    
    try{
      
      SSCoreConf.instSet(SSVocConf.fileNameSSSConf);
      
      try{
        SSVoc.inst.regServ               ();
        
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
        SSCircleServ.inst.regServ          ();
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
        SSSolrServ.inst.regServ            ();
        SSModelUEServ.inst.regServ         ();
        SSBroadcasterServ.inst.regServ     ();
        SSRecommServ.inst.regServ          ();
        SSI5CloudServ.inst.regServ         ();
        SSCloudServ.inst.regServ           ();
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
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{ //initializing
        SSAuthServ.inst.initServ();
        SSCircleServ.inst.initServ();
        SSDataImportServ.inst.initServ();
        SSCategoryServ.inst.initServ();
        SSSolrServ.inst.initServ();
        SSModelUEServ.inst.initServ();
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
        SSSearchServ.inst.schedule         ();
        SSLearnEpServ.inst.schedule        ();
        SSBroadcasterServ.inst.schedule    ();
        SSDataImportServ.inst.schedule     ();
        SSModelUEServ.inst.schedule        ();
        SSRecommServ.inst.schedule         ();
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      finished = true;
      
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
  
  public static Boolean isFinished(){
    return finished;
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread(true);
  }
}
