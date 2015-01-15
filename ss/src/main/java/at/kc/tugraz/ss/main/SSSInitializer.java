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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.ss.activity.serv.SSActivityServ;
import at.kc.tugraz.ss.category.ss.category.serv.SSCategoryServ;
import at.kc.tugraz.ss.circle.serv.SSCircleServ;
import at.kc.tugraz.ss.cloud.serv.SSCloudServ;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.friend.serv.SSFriendServ;
import at.kc.tugraz.ss.like.serv.SSLikeServ;
import at.kc.tugraz.ss.message.serv.SSMessageServ;
import at.kc.tugraz.ss.serv.db.serv.SSDBGraph;
import at.kc.tugraz.ss.serv.db.serv.SSDBSQL;
import at.kc.tugraz.ss.serv.jsonld.serv.SSJSONLD;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.serv.auth.serv.SSAuthServ;
import at.kc.tugraz.ss.serv.dataimport.serv.SSDataImportServ;
import at.kc.tugraz.ss.serv.datatypes.entity.serv.SSEntityServ;
import at.kc.tugraz.ss.serv.datatypes.learnep.serv.SSLearnEpServ;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.dataexport.serv.SSDataExportServ;
import at.kc.tugraz.ss.serv.job.file.sys.local.serv.SSFileSysLocalServ;
import at.kc.tugraz.ss.serv.job.i5cloud.serv.SSI5CloudServ;
import at.kc.tugraz.ss.recomm.serv.SSRecommServ;
import at.kc.tugraz.ss.serv.jobs.evernote.serv.SSEvernoteServ;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.ss.serv.lomextractor.serv.SSLOMExtractorServ;
import at.kc.tugraz.ss.serv.modeling.ue.serv.SSModelUEServ;
import at.kc.tugraz.ss.serv.serv.api.SSServImplStartA;
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

public class SSSInitializer extends SSServImplStartA{
  
  protected static Boolean finished = false;
  
  public SSSInitializer() throws Exception{
    super(null);
  }
  
  @Override
  public void run(){
    
    try{
      
      try{
        //registering
        SSVoc.inst.regServ               (SSCoreConf.instGet().getVocConf());
        
        SSMimeTypeU.init   ();
        SSJSONLDU.init(
          SSCoreConf.instGet().getJsonLDConf().uri);
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        
        SSDBGraph.inst.regServ             (SSCoreConf.instGet().getDbGraphConf());
        SSDBSQL.inst.regServ               (SSCoreConf.instGet().getDbSQLConf());
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        SSEntityServ.inst.regServ          (SSCoreConf.instGet().getEntityConf());
        SSCircleServ.inst.regServ          (SSCoreConf.instGet().getCircleConf());
        SSUserServ.inst.regServ            (SSCoreConf.instGet().getUserConf());
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        SSCollServ.inst.regServ            (SSCoreConf.instGet().getCollConf());
        SSUEServ.inst.regServ              (SSCoreConf.instGet().getUeConf());
        SSTagServ.inst.regServ             (SSCoreConf.instGet().getTagConf());
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        SSAuthServ.inst.regServ            (SSCoreConf.instGet().getAuthConf());
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        SSEvernoteServ.inst.regServ        (SSCoreConf.instGet().getEvernoteConf());
        SSFilerepoServ.inst.regServ        (SSCoreConf.instGet().getFilerepoConf());
        SSDataImportServ.inst.regServ      (SSCoreConf.instGet().getDataImportConf());
        SSJSONLD.inst.regServ              (SSCoreConf.instGet().getJsonLDConf());
        SSRatingServ.inst.regServ          (SSCoreConf.instGet().getRatingConf());
        SSCategoryServ.inst.regServ        (SSCoreConf.instGet().getCategoryConf());
        SSDiscServ.inst.regServ            (SSCoreConf.instGet().getDiscConf());
        SSLearnEpServ.inst.regServ         (SSCoreConf.instGet().getLearnEpConf());
        SSActivityServ.inst.regServ        (SSCoreConf.instGet().getActivityConf());
        SSSearchServ.inst.regServ          (SSCoreConf.instGet().getSearchConf());
        SSDataExportServ.inst.regServ      (SSCoreConf.instGet().getDataExportConf());
        SSLOMExtractorServ.inst.regServ    (SSCoreConf.instGet().getLomExtractorConf());
        SSSolrServ.inst.regServ            (SSCoreConf.instGet().getSolrConf());
        SSModelUEServ.inst.regServ         (SSCoreConf.instGet().getModelConf());
        SSBroadcasterServ.inst.regServ     (SSCoreConf.instGet().getBroadcasterConf());
        SSRecommServ.inst.regServ          (SSCoreConf.instGet().getRecommConf());
        SSFileSysLocalServ.inst.regServ    (SSCoreConf.instGet().getFileSysLocalConf());
        SSI5CloudServ.inst.regServ         (SSCoreConf.instGet().getI5CloudConf());
        SSCloudServ.inst.regServ           (SSCoreConf.instGet().getCloudConf());
        SSFlagServ.inst.regServ            (SSCoreConf.instGet().getFlagConf());
        SSCommentServ.inst.regServ         (SSCoreConf.instGet().getCommentConf());
        SSMessageServ.inst.regServ         (SSCoreConf.instGet().getMessageConf());
        SSAppServ.inst.regServ             (SSCoreConf.instGet().getAppConf());
        SSFriendServ.inst.regServ          (SSCoreConf.instGet().getFriendConf());
        SSAppStackLayoutServ.inst.regServ  (SSCoreConf.instGet().getAppStackLayoutConf());
        SSVideoServ.inst.regServ           (SSCoreConf.instGet().getVideoConf());
        SSLikeServ.inst.regServ            (SSCoreConf.instGet().getLikeConf());
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        //initializing
        SSVoc.inst.initServ();
        SSDBGraph.inst.initServ();
        SSDBSQL.inst.initServ();
        SSEntityServ.inst.initServ();
        SSCircleServ.inst.initServ();
        SSUserServ.inst.initServ();
        SSCollServ.inst.initServ();
        SSUEServ.inst.initServ();
        SSTagServ.inst.initServ();
        SSAuthServ.inst.initServ();
        SSEvernoteServ.inst.initServ();
        SSFilerepoServ.inst.initServ();
        SSDataImportServ.inst.initServ();
        SSJSONLD.inst.initServ();
        SSRatingServ.inst.initServ();
        SSCategoryServ.inst.initServ();
        SSDiscServ.inst.initServ();
        SSLearnEpServ.inst.initServ();
        SSActivityServ.inst.initServ();
        SSSearchServ.inst.initServ();
        SSDataExportServ.inst.initServ();
        SSLOMExtractorServ.inst.initServ();
        SSSolrServ.inst.initServ();
        SSModelUEServ.inst.initServ();
        SSBroadcasterServ.inst.initServ();
        SSRecommServ.inst.initServ();
        SSFileSysLocalServ.inst.initServ();
        SSI5CloudServ.inst.initServ();
        SSCloudServ.inst.initServ();
        SSFlagServ.inst.initServ();
        SSCommentServ.inst.initServ();
        SSMessageServ.inst.initServ();
        SSAppServ.inst.initServ();
        SSFriendServ.inst.initServ();
        SSAppStackLayoutServ.inst.initServ();
        SSVideoServ.inst.initServ();
        SSLikeServ.inst.initServ();
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        //scheduling
        SSModelUEServ.inst.schedule        ();
        SSFilerepoServ.inst.schedule       ();
        SSRecommServ.inst.schedule         ();
        SSSearchServ.inst.schedule         ();
        SSDataImportServ.inst.schedule     ();
        SSLearnEpServ.inst.schedule        ();
        SSBroadcasterServ.inst.schedule    ();
        
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
    finalizeThread();
  }
}
