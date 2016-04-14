/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.adapter.rest.v3;

import at.tugraz.sss.servs.common.impl.*;
import at.tugraz.sss.servs.util.*;
import at.tugraz.sss.servs.activity.impl.*;
import at.tugraz.sss.servs.app.impl.*;
import at.tugraz.sss.servs.appstacklayout.impl.*;
import at.tugraz.sss.servs.auth.impl.*;
import at.tugraz.sss.servs.category.impl.*;
import at.tugraz.sss.servs.coll.impl.*;
import at.tugraz.sss.servs.comment.impl.*;
import at.tugraz.sss.servs.common.api.*;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.dataimport.impl.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.disc.impl.*;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.eval.impl.*;
import at.tugraz.sss.servs.evernote.impl.*;
import at.tugraz.sss.servs.file.impl.*;
import at.tugraz.sss.servs.flag.impl.*;
import at.tugraz.sss.servs.friend.impl.*;
import at.tugraz.sss.servs.image.impl.*;
import at.tugraz.sss.servs.kcprojwiki.impl.*;
import at.tugraz.sss.servs.learnep.impl.*;
import at.tugraz.sss.servs.like.impl.*;
import at.tugraz.sss.servs.link.impl.*;
import at.tugraz.sss.servs.livingdoc.impl.*;
import at.tugraz.sss.servs.location.impl.*;
import at.tugraz.sss.servs.mail.impl.*;
import at.tugraz.sss.servs.message.impl.*;
import at.tugraz.sss.servs.rating.impl.*;
import at.tugraz.sss.servs.recomm.impl.*;
import at.tugraz.sss.servs.search.impl.*;
import at.tugraz.sss.servs.tag.impl.*;
import at.tugraz.sss.servs.user.impl.*;
import at.tugraz.sss.servs.video.impl.*;
import javax.servlet.*;
import javax.servlet.annotation.*;

@WebListener
public class SSInitializer 
  extends SSServImplA
  implements ServletContextListener{
  
  private final SSDescribeEntity                describeEntity                = new SSDescribeEntity();
  private final SSEntityCopied                  entityCopied                  = new SSEntityCopied();
  private final SSCircleContentRemoved          circleContentRemoved          = new SSCircleContentRemoved();
  private final SSPushEntitiesToUsers           pushEntitiesToUsers           = new SSPushEntitiesToUsers();
  private final SSGetUsersResources             getUsersResources             = new SSGetUsersResources();
  private final SSCopyEntity                    copyEntity                    = new SSCopyEntity();
  private final SSAddAffiliatedEntitiesToCircle addAffiliatedEntitiesToCircle = new SSAddAffiliatedEntitiesToCircle();
  private final SSEntitiesSharedWithUsers       entitiesSharedWithUsers       = new SSEntitiesSharedWithUsers();
  private final SSGetUserRelations              getUserRelations              = new SSGetUserRelations();
      
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
        //register services for common duties
        describeEntity.regServ                (new SSEntityImpl(), SSCoreConf.instGet().getEntity());
        describeEntity.regServ                (new SSUserImpl(), SSCoreConf.instGet().getUser());
        describeEntity.regServ                (new SSCollImpl(), SSCoreConf.instGet().getColl());
        describeEntity.regServ                (new SSTagImpl(), SSCoreConf.instGet().getTag());
        describeEntity.regServ                (new SSEvernoteImpl(), SSCoreConf.instGet().getEvernote());
        describeEntity.regServ                (new SSFileImpl(), SSCoreConf.instGet().getFile());
        describeEntity.regServ                (new SSRatingImpl(), SSCoreConf.instGet().getRating());
        describeEntity.regServ                (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        describeEntity.regServ                (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        describeEntity.regServ                (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        describeEntity.regServ                (new SSActivityImpl(), SSCoreConf.instGet().getActivity());
        describeEntity.regServ                (new SSFlagImpl(), SSCoreConf.instGet().getFlag());
        describeEntity.regServ                (new SSCommentImpl(), SSCoreConf.instGet().getComment());
        describeEntity.regServ                (new SSMessageImpl(), SSCoreConf.instGet().getMessage());
        describeEntity.regServ                (new SSAppImpl(), SSCoreConf.instGet().getApp());
        describeEntity.regServ                (new SSFriendImpl(), SSCoreConf.instGet().getFriend());
        describeEntity.regServ                (new SSAppStackLayoutImpl(), SSCoreConf.instGet().getAppStackLayout());
        describeEntity.regServ                (new SSVideoImpl(), SSCoreConf.instGet().getVideo());
        describeEntity.regServ                (new SSLikeImpl(), SSCoreConf.instGet().getLike());
        describeEntity.regServ                (new SSImageImpl(), SSCoreConf.instGet().getImage());
        describeEntity.regServ                (new SSLocationImpl(), SSCoreConf.instGet().getLocation());
        describeEntity.regServ                (new SSLivingDocImpl(), SSCoreConf.instGet().getLivingDocument());
        
        getUsersResources.regServ             (new SSTagImpl(), SSCoreConf.instGet().getTag());
        getUsersResources.regServ             (new SSCollImpl(), SSCoreConf.instGet().getColl());
        getUsersResources.regServ             (new SSEntityImpl(), SSCoreConf.instGet().getEntity());
        getUsersResources.regServ             (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        getUsersResources.regServ             (new SSActivityImpl(), SSCoreConf.instGet().getActivity());
        getUsersResources.regServ             (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        getUsersResources.regServ             (new SSRatingImpl(), SSCoreConf.instGet().getRating());
        getUsersResources.regServ             (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        getUsersResources.regServ             (new SSFlagImpl(), SSCoreConf.instGet().getFlag());
        getUsersResources.regServ             (new SSLivingDocImpl(), SSCoreConf.instGet().getLivingDocument());
        getUsersResources.regServ             (new SSImageImpl(), SSCoreConf.instGet().getImage());
        getUsersResources.regServ             (new SSVideoImpl(), SSCoreConf.instGet().getVideo());
        getUsersResources.regServ             (new SSLinkImpl(), SSCoreConf.instGet().getLink());
        
        addAffiliatedEntitiesToCircle.regServ (new SSEntityImpl(), SSCoreConf.instGet().getEntity());
        addAffiliatedEntitiesToCircle.regServ (new SSVideoImpl(), SSCoreConf.instGet().getVideo());
        addAffiliatedEntitiesToCircle.regServ (new SSCollImpl(), SSCoreConf.instGet().getColl());
        addAffiliatedEntitiesToCircle.regServ (new SSFileImpl(), SSCoreConf.instGet().getFile());
        addAffiliatedEntitiesToCircle.regServ (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        addAffiliatedEntitiesToCircle.regServ (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        addAffiliatedEntitiesToCircle.regServ (new SSImageImpl(), SSCoreConf.instGet().getImage());
        
        getUserRelations.regServ              (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        getUserRelations.regServ              (new SSTagImpl(), SSCoreConf.instGet().getTag());
        getUserRelations.regServ              (new SSCollImpl(), SSCoreConf.instGet().getColl());
        getUserRelations.regServ              (new SSRatingImpl(), SSCoreConf.instGet().getRating());
        getUserRelations.regServ              (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        getUserRelations.regServ              (new SSCommentImpl(), SSCoreConf.instGet().getComment());
        
        copyEntity.regServ                    (new SSEntityImpl(), SSCoreConf.instGet().getEntity());
        copyEntity.regServ                    (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        
        pushEntitiesToUsers.regServ           (new SSCollImpl(), SSCoreConf.instGet().getColl());
        pushEntitiesToUsers.regServ           (new SSVideoImpl(), SSCoreConf.instGet().getVideo());
        pushEntitiesToUsers.regServ           (new SSDiscImpl(), SSCoreConf.instGet().getDisc());
        pushEntitiesToUsers.regServ           (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        pushEntitiesToUsers.regServ           (new SSLivingDocImpl(), SSCoreConf.instGet().getLivingDocument());
        
        entityCopied.regServ                  (new SSTagImpl(), SSCoreConf.instGet().getTag());
        entityCopied.regServ                  (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        
        circleContentRemoved.regServ          (new SSCategoryImpl(), SSCoreConf.instGet().getCategory());
        circleContentRemoved.regServ          (new SSTagImpl(), SSCoreConf.instGet().getTag());
        
        entitiesSharedWithUsers.regServ       (new SSLearnEpImpl(), SSCoreConf.instGet().getLearnEp());
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
      try{ //initializing
        
        new SSAuthImpl().initServ();
        new SSEntityImpl().initServ();
        new SSCategoryImpl().initServ();
        new SSRecommImpl().initServ();
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
      try{ //scheduling
        new SSEntityImpl().schedule();
        new SSSearchImpl().schedule();
        new SSLearnEpImpl().schedule();
        new SSDataImportImpl().schedule();
        new SSRecommImpl().schedule();
        new SSKCProjWikiImpl().schedule();
        new SSEvalImpl().schedule();
        new SSFileImpl().schedule();
        new SSMailImpl().schedule();
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
}