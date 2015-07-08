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
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.servs.location.serv.SSLocationServ;
import at.tugraz.sss.servs.ocd.service.SSOCDServ;
import at.tugraz.sss.servs.image.serv.SSImageServ;
import sss.serv.eval.serv.SSEvalServ;

public class SSSInitializer extends SSServImplStartA{
  
  protected static Boolean finished = false;
  
  public SSSInitializer() throws Exception{
    super(null, null);
  }
  
  @Override
  public void run(){
    
    try{
      
      try{
        //registering
        SSVoc.inst.regServ               (SSCoreConf.instGet().getVoc());
        
        SSFileExtE.init    ();
        SSMimeTypeE.init   ();
        SSJSONLDU.init(
          SSCoreConf.instGet().getJsonLD().uri);
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        
        SSDBSQL.inst.regServ               (SSCoreConf.instGet().getDbSQL());
        SSDBNoSQL.inst.regServ             (SSCoreConf.instGet().getDbNoSQL());
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        SSEntityServ.inst.regServ          (SSCoreConf.instGet().getEntity());
        SSCircleServ.inst.regServ          (SSCoreConf.instGet().getCircle());
        SSUserServ.inst.regServ            (SSCoreConf.instGet().getUser());
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        SSCollServ.inst.regServ            (SSCoreConf.instGet().getColl());
        SSUEServ.inst.regServ              (SSCoreConf.instGet().getUe());
        SSTagServ.inst.regServ             (SSCoreConf.instGet().getTag());
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        SSAuthServ.inst.regServ            (SSCoreConf.instGet().getAuth());
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        SSEvernoteServ.inst.regServ        (SSCoreConf.instGet().getEvernote());
        SSFilerepoServ.inst.regServ        (SSCoreConf.instGet().getFilerepo());
        SSDataImportServ.inst.regServ      (SSCoreConf.instGet().getDataImport());
        SSJSONLD.inst.regServ              (SSCoreConf.instGet().getJsonLD());
        SSRatingServ.inst.regServ          (SSCoreConf.instGet().getRating());
        SSCategoryServ.inst.regServ        (SSCoreConf.instGet().getCategory());
        SSDiscServ.inst.regServ            (SSCoreConf.instGet().getDisc());
        SSLearnEpServ.inst.regServ         (SSCoreConf.instGet().getLearnEp());
        SSActivityServ.inst.regServ        (SSCoreConf.instGet().getActivity());
        SSSearchServ.inst.regServ          (SSCoreConf.instGet().getSearch());
        SSDataExportServ.inst.regServ      (SSCoreConf.instGet().getDataExport());
        SSSolrServ.inst.regServ            (SSCoreConf.instGet().getSolr());
        SSModelUEServ.inst.regServ         (SSCoreConf.instGet().getModel());
        SSBroadcasterServ.inst.regServ     (SSCoreConf.instGet().getBroadcaster());
        SSRecommServ.inst.regServ          (SSCoreConf.instGet().getRecomm());
        SSI5CloudServ.inst.regServ         (SSCoreConf.instGet().getI5Cloud());
        SSCloudServ.inst.regServ           (SSCoreConf.instGet().getCloud());
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
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
        return;
      }
      
      try{
        //initializing
        SSVoc.inst.initServ();
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
        SSSolrServ.inst.initServ();
        SSModelUEServ.inst.initServ();
        SSBroadcasterServ.inst.initServ();
        SSRecommServ.inst.initServ();
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
        SSEvalServ.inst.initServ();
        SSOCDServ.inst.initServ();
        SSImageServ.inst.initServ();
        
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
    finalizeThread(true);
  }
}
