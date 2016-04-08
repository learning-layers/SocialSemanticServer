/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
 * For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"();
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
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.SSFileU;
import at.tugraz.sss.serv.util.SSJSONLDU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.SSMimeTypeE;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import at.tugraz.sss.servs.file.serv.SSFileServ;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.servs.conf.SSCoreConf;
import at.tugraz.sss.servs.link.serv.SSLinkServ;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class SSRESTDropwizardMain extends Application<SSAdapterRestDropwizardConf> {
  
  public static void main(String[] args) throws Exception {
    new SSRESTDropwizardMain().run(args);
  }
  
  @Override
  public String getName() {
    return "SSRESTDropwizardMain";
  }
  
  @Override
  public void initialize(Bootstrap<SSAdapterRestDropwizardConf> bootstrap) {
    
    try{
      
      try{
//        SSCoreConf.instSet("/sssWorkDir/" + SSFileU.fileNameSSSConf);
SSCoreConf.instSet("C:\\worKspace_git\\master\\SocialSemanticServer\\sssWorkDir\\" + SSFileU.fileNameSSSConf);
//      SSCoreConf.instSet("/home/dtheiler/bp.demo/" + SSFileU.fileNameSSSConf);
//        SSCoreConf.instSet("/home/dtheiler/eval/" + SSFileU.fileNameSSSConf);
//SSCoreConf.iNstSet("/home/dtheiler/internal/" + SSFileU.fileNameSSSConf);
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
  
  @Override
  public void run(
    SSAdapterRestDropwizardConf configuration,
    Environment                 environment){
    
    final FilterRegistration.Dynamic cors = environment.servlets().addFilter("crossOriginRequsts", CrossOriginFilter.class);
    
    cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    
    cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
    cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");    
    cors.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, environment.getApplicationContext().getContextPath() + "*");
    
    final SSRESTAuth auth = new SSRESTAuth();
    final SSRESTActivity act = new SSRESTActivity();
    final SSRESTApp app = new SSRESTApp();
    final SSRESTAppStackLayout appStackLayout = new SSRESTAppStackLayout();
    final SSRESTCategory category = new SSRESTCategory();
    final SSRESTCircle circle = new SSRESTCircle();
    final SSRESTColl coll = new SSRESTColl();
    final SSRESTDisc disc = new SSRESTDisc();
    final SSRESTEntity entity = new SSRESTEntity();
    final SSRESTEval eval = new SSRESTEval();
    final SSRESTFile file = new SSRESTFile();
    final SSRESTFlag flag = new SSRESTFlag();
    final SSRESTFriend friend = new SSRESTFriend();
    final SSRESTImage img = new SSRESTImage();
    final SSRESTLearnEp learnEp = new SSRESTLearnEp();
    final SSRESTLike like = new SSRESTLike();
    final SSRESTLivingDoc livingDoc = new SSRESTLivingDoc();
    final SSRESTMessage message = new SSRESTMessage();
    final SSRESTRating rating = new SSRESTRating();
    final SSRESTRecomm recomm = new SSRESTRecomm();
    final SSRESTSearch search = new SSRESTSearch();
    final SSRESTTag tag = new SSRESTTag();
    final SSRESTUser user = new SSRESTUser();
    final SSRESTVideo video = new SSRESTVideo();
    final SSRESTLink link = new SSRESTLink();

    environment.jersey().register(new MultiPartFeature());
    environment.jersey().register(auth);
    environment.jersey().register(act);
    environment.jersey().register(app);
    environment.jersey().register(appStackLayout);
    environment.jersey().register(category);
    environment.jersey().register(circle);
    environment.jersey().register(coll);
    environment.jersey().register(disc);
    environment.jersey().register(entity);
    environment.jersey().register(eval);
    environment.jersey().register(file);
    environment.jersey().register(flag);
    environment.jersey().register(friend);
    environment.jersey().register(img);
    environment.jersey().register(learnEp);
    environment.jersey().register(like);
    environment.jersey().register(livingDoc);
    environment.jersey().register(message);
    environment.jersey().register(rating);
    environment.jersey().register(recomm);
    environment.jersey().register(search);
    environment.jersey().register(tag);
    environment.jersey().register(user);
    environment.jersey().register(video);
    environment.jersey().register(link);

  }
}