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
package at.tugraz.sss.servs.integrationtest.knowbraintaggingstudy2015;

import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddPar;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkUserRealmsFromCirclesPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIsGetPar;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;

public class SSIntegrationTestKnowBrainTaggingStudy2015 {
  
  private final SSCircleServerI    circleServ;
  private final SSUserServerI      userServ;
  private final SSEntityServerI    entityServ;
  private final SSTagServerI       tagServ;
  private final SSCategoryServerI  categoryServ;
  private final SSRecommServerI    recommServ;
  
  private SSUri circle5Uri = null;
  
  public SSIntegrationTestKnowBrainTaggingStudy2015() throws Exception{
    circleServ      = (SSCircleServerI)   SSServReg.getServ (SSCircleServerI.class);
    userServ        = (SSUserServerI)     SSServReg.getServ (SSUserServerI.class);
    entityServ      = (SSEntityServerI)   SSServReg.getServ (SSEntityServerI.class);
    tagServ         = (SSTagServerI)      SSServReg.getServ (SSTagServerI.class);
    categoryServ    = (SSCategoryServerI) SSServReg.getServ (SSCategoryServerI.class);
    recommServ      = (SSRecommServerI)   SSServReg.getServ (SSRecommServerI.class);
  }
  
  public void getUsersCircles(
    final List<SSUri> userUris) throws Exception{
    
    try{
      
      circle5Uri =
        SSUri.getDistinctNotNullFromEntities(
          circleServ.circlesGet(
            new SSCirclesGetPar(
              userUris.get(0), //user
              null, //entity,
              null, //entityTypesToIncludeOnly,
              true, //withUserRestriction,
              false, //withSystemCircles,
              true))).get(0); //invokeEntityHandlers
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeUsersFromOldCircles(
    final SSUri       adminUri,
    final SSUri       circle1Uri,
    final SSUri       circle2Uri,
    final SSUri       circle3Uri,
    final List<SSUri> userUris) throws Exception{
    
    try{
      
      circleServ.circleUsersRemove(
        new SSCircleUsersRemovePar(
          adminUri,
          circle1Uri,
          userUris.subList(0, 2),
          true, //withUserRestriction
          false)); //shouldCommit
      
      circleServ.circleUsersRemove(
        new SSCircleUsersRemovePar(
          adminUri,
          circle2Uri,
          SSUri.asListWithoutNullAndEmpty(userUris.get(2)),
          true, //withUserRestriction
          false)); //shouldCommit
      
      circleServ.circleUsersRemove(
        new SSCircleUsersRemovePar(
          adminUri,
          circle3Uri,
          SSUri.asListWithoutNullAndEmpty(userUris.get(3)),
          true, //withUserRestriction
          false)); //shouldCommit
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void mergeCircle2And3(
    final SSUri       adminUri,
    final SSUri       circle2Uri,
    final SSUri       circle3Uri) throws Exception{
    
    try{
      
      final SSUri targetCircle =
        circleServ.circleCreate(
          new SSCircleCreatePar(
            adminUri,
            SSCircleE.group,
            SSLabel.get("target circle"),
            null, //description,
            false, //isSystemCircle,
            true, //withUserRestriction,
            false)); //shouldCommit);
      
      entityServ.entityCopy(
        new SSEntityCopyPar(
          adminUri,
          circle2Uri,
          targetCircle, //targetEntity,
          null, //forUsers
          null, //label
          true, //includeUsers,
          true, //includeEntities,
          true, //includeMetadataSpecificToEntityAndItsEntities,
          true, //includeOriginUser
          null, //entitiesToExclude,
          null, //comment,
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      entityServ.entityCopy(
        new SSEntityCopyPar(
          adminUri,
          circle3Uri,
          targetCircle, //targetEntity,
          null, //forUsers
          null, //label
          true, //includeUsers,
          true, //includeEntities,
          true, //includeMetadataSpecificToEntityAndItsEntities,
          true, //includeOriginUser
          null, //entitiesToExclude,
          null, //comment,
          true, //withUserRestriction,
          false)); //shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void splitCircle1(
    final SSUri       adminUri,
    final SSUri       circle1Uri,
    final List<SSUri> userUris) throws Exception{
    
    try{
      
      entityServ.entityCopy(
        new SSEntityCopyPar(
          adminUri,
          circle1Uri,
          null, //targetEntity,
          userUris.subList(0, 2), //forUsers
          SSLabel.get("circle 1 split for user 1 / 2"), //label
          false, //includeUsers,
          true, //includeEntities,
          true, //includeMetadataSpecificToEntityAndItsEntities,
          true, //includeOriginUser
          null, //entitiesToExclude,
          null, //comment,
          true, //withUserRestriction,
          false)); //shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeTags(
    final List<SSUri> userUris) throws Exception{
    
    try{
      
      final SSUri          link1 = SSUri.get("http://link1");
      final SSUri          link2 = SSUri.get("http://link2");
      final SSTagLabel     tag1  = SSTagLabel.get("tag1");
      final SSTagLabel     tag2  = SSTagLabel.get("tag2");
      
      tagServ.tagsRemove(
        new SSTagsRemovePar(
          userUris.get(1),
          null, //forUser,
          link1, //entity
          tag1, //label
          null, //space,
          null, //circle
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      tagServ.tagsRemove(
        new SSTagsRemovePar(
          userUris.get(1),
          null, //forUser,
          link2, //entity
          tag2, //label
          null, //space,
          null, //circle
          true, //withUserRestriction,
          false)); //shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void getTagRecommsAfterSplitAndMerge(
    final List<SSUri> userUris) throws Exception{
    
    try{
      System.out.println(
        recommServ.recommTags(
          new SSRecommTagsPar(
            userUris.get(0),
            SSStrU.toStr(circle5Uri),
            userUris.get(0),
            null,  //entity
            null, //categories,
            10, //maxTags,
            true, //includeOwn,
            false, //ignoreAccessRights,
            true))); //withUserRestriction);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void getTagRecomms(
    final SSUri       circle1Uri,
    final SSUri       circle2Uri,
    final SSUri       circle3Uri,
    final List<SSUri> userUris) throws Exception{
    
    try{
      System.out.println(
        recommServ.recommTags(
          new SSRecommTagsPar(
            userUris.get(0),
            SSStrU.toStr(circle1Uri),
            userUris.get(0),
            null,  //entity
            null, //categories,
            10, //maxTags,
            true, //includeOwn,
            false, //ignoreAccessRights,
            true))); //withUserRestriction);
      
      System.out.println(
        recommServ.recommTags(
          new SSRecommTagsPar(
            userUris.get(1),
            SSStrU.toStr(circle1Uri),
            userUris.get(1),
            null,  //entity
            null, //categories,
            10, //maxTags,
            true, //includeOwn,
            false, //ignoreAccessRights,
            true))); //withUserRestriction);
      
      System.out.println(
        recommServ.recommTags(
          new SSRecommTagsPar(
            userUris.get(2),
            SSStrU.toStr(circle2Uri),
            userUris.get(2),
            null,  //entity
            null, //categories,
            10, //maxTags,
            true, //includeOwn,
            false, //ignoreAccessRights,
            true))); //withUserRestriction);
      
      System.out.println(
        recommServ.recommTags(
          new SSRecommTagsPar(
            userUris.get(3),
            SSStrU.toStr(circle3Uri),
            userUris.get(3),
            null,  //entity
            null, //categories,
            10, //maxTags,
            true, //includeOwn,
            false, //ignoreAccessRights,
            true))); //withUserRestriction);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateTagRecomm() throws Exception{
    
    try{
      
      recommServ.recommUpdateBulkUserRealmsFromCircles(
        new SSRecommUpdateBulkUserRealmsFromCirclesPar(
          SSVocConf.systemUserUri));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addCircle3Entities(
    final List<SSUri>    userUris,
    final SSUri          circle3Uri) throws Exception{
    
    try{
      
      final SSUri          user4 = userUris.get(3);
      final SSUri          link4 = SSUri.get("http://link4");
      final SSEntityCircle circle3;
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          user4,
          circle3Uri,
          SSUri.asListWithoutNullAndEmpty(link4),
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      circle3 =
        circleServ.circleGet(
          new SSCircleGetPar(
            user4,
            circle3Uri, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //withUserRestriction,
            true)); //invokeEntityHandlers))
      
      tagServ.tagsAdd(
        new SSTagsAddPar(
          user4,
          SSTagLabel.asListWithoutNullAndEmpty(SSTagLabel.get("tag4")), //labels
          link4, //entity
          null, //space
          circle3Uri, //circle
          null, //creationTime,
          true, //withUserRestriction
          false)); //shouldCommit)
      
      categoryServ.categoriesAdd(
        new SSCategoriesAddPar(
          user4,
          SSCategoryLabel.asListWithoutNullAndEmpty(SSCategoryLabel.get("category4")), //labels
          link4, //file
          null, //space
          circle3.id, //circle
          null, //creationTime,
          true, //withUserRestriction
          false)); //shouldCommit)
      
      final List<SSEntity> entities =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            user4,
            SSUri.asListWithoutNullAndEmpty(link4),
            null, //types,
            null, //descPar,
            true)); //withUserRestriction,
      
      SSServCallerU.handleCircleEntitiesAdd(
        user4,
        circle3,
        entities,
        true);
            
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addCircle2Entities(
    final List<SSUri>    userUris,
    final SSUri          circle2Uri) throws Exception{
    
    try{
      
      final SSUri          user3 = userUris.get(2);
      final SSUri          link3 = SSUri.get("http://link3");
      final SSEntityCircle circle2;
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          user3,
          circle2Uri,
          SSUri.asListWithoutNullAndEmpty(link3),
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      circle2 =
        circleServ.circleGet(
          new SSCircleGetPar(
            user3,
            circle2Uri, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //withUserRestriction,
            true)); //invokeEntityHandlers))
      
      categoryServ.categoriesAdd(
        new SSCategoriesAddPar(
          user3,
          SSCategoryLabel.asListWithoutNullAndEmpty(SSCategoryLabel.get("category3")), //labels
          link3, //file
          null, //space
          circle2.id, //circle
          null, //creationTime,
          true, //withUserRestriction
          false)); //shouldCommit)
      
      final List<SSEntity> entities =
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            user3,
            SSUri.asListWithoutNullAndEmpty(link3),
            null, //types,
            null, //descPar,
            true)); //withUserRestriction,
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addCircle1Entities(
    final List<SSUri>    userUris,
    final SSUri          circle1Uri) throws Exception{
    
    try{
      
      final SSUri          user1 = userUris.get(0);
      final SSUri          user2 = userUris.get(1);
      final SSUri          link1 = SSUri.get("http://link1");
      final SSUri          link2 = SSUri.get("http://link2");
      SSEntityCircle circle1;
      
      //user1
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          user1,
          circle1Uri,
          SSUri.asListWithoutNullAndEmpty(link1),
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      circle1 =
        circleServ.circleGet(
          new SSCircleGetPar(
            user1,
            circle1Uri, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //withUserRestriction,
            true)); //invokeEntityHandlers))
      
      tagServ.tagsAdd(
        new SSTagsAddPar(
          user1,
          SSTagLabel.asListWithoutNullAndEmpty(SSTagLabel.get("tag1")), //labels
          link1, //entity
          null, //space
          circle1Uri, //circle
          null, //creationTime,
          true, //withUserRestriction
          false)); //shouldCommit)
      
      categoryServ.categoriesAdd(
        new SSCategoriesAddPar(
          user1,
          SSCategoryLabel.asListWithoutNullAndEmpty(SSCategoryLabel.get("category1")), //labels
          link1, //file
          null, //space
          circle1.id, //circle
          null, //creationTime,
          true, //withUserRestriction
          false)); //shouldCommit)
      
      final List<SSEntity> entities =
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            user1,
            SSUri.asListWithoutNullAndEmpty(link1),
            null, //types,
            null, //descPar,
            true)); //withUserRestriction,
      
      SSServCallerU.handleCircleEntitiesAdd(
        user1,
        circle1,
        entities,
        true);
      
      //user2
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          user2,
          circle1Uri,
          SSUri.asListWithoutNullAndEmpty(link2),
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      circle1 =
        circleServ.circleGet(
          new SSCircleGetPar(
            user2,
            circle1Uri, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //withUserRestriction,
            true)); //invokeEntityHandlers))
      
      tagServ.tagsAdd(
        new SSTagsAddPar(
          user2,
          SSTagLabel.asListWithoutNullAndEmpty(SSTagLabel.get("tag2")), //labels
          link2, //entity
          null, //space
          circle1Uri, //circle
          null, //creationTime,
          true, //withUserRestriction
          false)); //shouldCommit)
      
      categoryServ.categoriesAdd(
        new SSCategoriesAddPar(
          user2,
          SSCategoryLabel.asListWithoutNullAndEmpty(SSCategoryLabel.get("category2")), //labels
          link2, //file
          null, //space
          circle1.id, //circle
          null, //creationTime,
          true, //withUserRestriction
          false)); //shouldCommit)
      
      entities.clear();
      entities.addAll(
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            user2,
            SSUri.asListWithoutNullAndEmpty(link2),
            null, //types,
            null, //descPar,
            true))); //withUserRestriction,
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void deleteCircle4(
    final SSUri          adminUri,
    final SSEntityCircle circle4) throws Exception{
    
    try{
      
      circleServ.circleRemove(
        new SSCircleRemovePar(
          adminUri,
          circle4.id,
          true, //withUserRestriction,
          false)); //shouldCommit)
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCircle1User3(
    final SSUri          adminUri,
    final SSEntityCircle circle1,
    final SSUri          userUriToRemove) throws Exception{
    
    try{
      
      circleServ.circleUsersRemove(
        new SSCircleUsersRemovePar(
          adminUri,
          circle1.id,
          SSUri.asListWithoutNullAndEmpty(userUriToRemove),
          true, //withUserRestriction
          false)); //shouldCommit
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void changeCircle1Label(
    final SSUri          adminUri,
    final SSEntityCircle circle1) throws Exception{
    
    try{
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          adminUri,
          circle1.id,
          null, //type
          SSLabel.get("circle new 1"), //label
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic,
          true, //withUserRestriction,
          false)); //shouldCommit
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSEntityCircle createCircle4(
    final SSUri       adminUri,
    final List<SSUri> userUris) throws Exception{
    
    try{
      
      final SSUri          circle4Uri;
      final SSEntityCircle circle4;
      
      circle4Uri =
        circleServ.circleCreate(
          new SSCircleCreatePar(
            adminUri,
            SSCircleE.group,  //circleType
            SSLabel.get("circle 4"), //label
            SSTextComment.get("description 4"),  //description
            false,    //isSystemCircle,
            true,    //withUserRestriction,
            false)); //shouldCommit
      
      circleServ.circleUsersAdd(
        new SSCircleUsersAddPar(
          adminUri,
          circle4Uri,
          userUris,
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      circle4 =
        circleServ.circleGet(
          new SSCircleGetPar(
            adminUri,
            circle4Uri, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //withUserRestriction,
            true)); //invokeEntityHandlers
      
      SSServCallerU.handleCircleUsersAdd(
        adminUri,
        circle4,
        userUris,
        true);
            
      return circle4;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEntityCircle createCircle3(
    final SSUri       adminUri,
    final List<SSUri> userUris) throws Exception{
    
    try{
      
      final SSUri          circle3Uri;
      final SSEntityCircle circle3;
      
      circle3Uri =
        circleServ.circleCreate(
          new SSCircleCreatePar(
            adminUri,
            SSCircleE.group,  //circleType
            SSLabel.get("circle 3"), //label
            SSTextComment.get("description 3"),  //description
            false,    //isSystemCircle,
            true,    //withUserRestriction,
            false)); //shouldCommit
      
      circleServ.circleUsersAdd(
        new SSCircleUsersAddPar(
          adminUri,
          circle3Uri,
          SSUri.asListWithoutNullAndEmpty(userUris.get(3)),
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      circle3 =
        circleServ.circleGet(
          new SSCircleGetPar(
            adminUri,
            circle3Uri, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //withUserRestriction,
            true)); //invokeEntityHandlers
      
      SSServCallerU.handleCircleUsersAdd(
        adminUri,
        circle3,
        SSUri.asListWithoutNullAndEmpty(userUris.get(3)),
        true);
      
      return circle3;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEntityCircle createCircle2(
    final SSUri       adminUri,
    final List<SSUri> userUris) throws Exception{
    
    try{
      
      final SSUri          circle2Uri;
      final SSEntityCircle circle2;
      
      circle2Uri =
        circleServ.circleCreate(
          new SSCircleCreatePar(
            adminUri,
            SSCircleE.group,  //circleType
            SSLabel.get("circle 2"), //label
            SSTextComment.get("description 2"),  //description
            false,    //isSystemCircle,
            true,    //withUserRestriction,
            false)); //shouldCommit
      
      circleServ.circleUsersAdd(
        new SSCircleUsersAddPar(
          adminUri,
          circle2Uri,
          SSUri.asListWithoutNullAndEmpty(userUris.get(2)),
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      circle2 =
        circleServ.circleGet(
          new SSCircleGetPar(
            adminUri,
            circle2Uri, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //withUserRestriction,
            true)); //invokeEntityHandlers
      
      SSServCallerU.handleCircleUsersAdd(
        adminUri,
        circle2,
        SSUri.asListWithoutNullAndEmpty(userUris.get(2)),
        true);
      
      return circle2;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEntityCircle createCircle1(
    final SSUri       adminUri,
    final List<SSUri> userUris) throws Exception{
    
    try{
      
      final List<SSUri>    circle1UserURIs = new ArrayList<>();
      final SSUri          circle1Uri;
      final SSEntityCircle circle1;
      
      circle1Uri =
        circleServ.circleCreate(
          new SSCircleCreatePar(
            adminUri,
            SSCircleE.group,  //circleType
            SSLabel.get("circle 1"), //label
            SSTextComment.get("description 1"),  //description
            false,    //isSystemCircle,
            true,    //withUserRestriction,
            false)); //shouldCommit
      
      circle1UserURIs.addAll(userUris.subList(0, 2));
      circle1UserURIs.add(userUris.get(2));
      
      circleServ.circleUsersAdd(
        new SSCircleUsersAddPar(
          adminUri,
          circle1Uri,
          circle1UserURIs,
          true, //withUserRestriction,
          false)); //shouldCommit));
      
      circle1 =
        circleServ.circleGet(
          new SSCircleGetPar(
            adminUri,
            circle1Uri, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //withUserRestriction,
            true)); //invokeEntityHandlers
      
      SSServCallerU.handleCircleUsersAdd(
        adminUri,
        circle1,
        circle1UserURIs,
        true);
      
      return circle1;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSUri> getUserUris(final SSRecommConf recommConf) throws Exception{
    
    try{
      
      if(
        SSObjU.isNull(recommConf, recommConf.recommTagsUsersToSetRealmsFromCircles) ||
        recommConf.recommTagsUsersToSetRealmsFromCircles.size() < 4){
        
        throw new Exception("recommConf set incorrectly for test");
      }
      
      return userServ.userURIsGet(
        new SSUserURIsGetPar(
          SSVocConf.systemUserUri,
          recommConf.recommTagsUsersToSetRealmsFromCircles));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
}
