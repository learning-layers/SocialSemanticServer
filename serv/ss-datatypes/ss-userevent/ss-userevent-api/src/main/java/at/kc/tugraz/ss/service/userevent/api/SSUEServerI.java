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
 package at.kc.tugraz.ss.service.userevent.api;

import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import java.util.List;

public interface SSUEServerI {

  public Integer     uECountGet          (final SSServPar parA) throws Exception;
  public SSUE        uEGet               (final SSServPar parA) throws Exception;
  public List<SSUE>  uEsGet              (final SSServPar parA) throws Exception;
  public Boolean     uEAdd               (final SSServPar parA) throws Exception;
  public Boolean     uEAddAtCreationTime (final SSServPar parA) throws Exception;
  public Boolean     uEsRemove           (final SSServPar parA) throws Exception;
  
//  addUserEvent(shouldCommit, user, SSUserEventEnum.appearsInSearchResult, resource, strU.empty);
//  addUserEvent(shouldCommit, user, SSUserEventEnum.shareCollection, coll, strU.empty);
//  addUserEvent(shouldCommit, user, SSUserEventEnum.subscribeCollection, resource, strU.empty);
//  addUserEvent(shouldCommit, user, SSUserEventEnum.unSubscribeCollection, resource, strU.empty);
//      if (SSSpaceEnum.isShared(space)) {
//      addUserEvent(shouldCommit, user, SSUserEventEnum.createSharedCollection, resource, strU.empty);
//    } else {
//      addUserEvent(shouldCommit, user, SSUserEventEnum.createPrivateCollection, resource, strU.empty);
//    }
//      addUserEvent(shouldCommit, user,  SSUserEventEnum.rateEntity, resource, value);
//    addUserEvent(shouldCommit, user, SSUserEventEnum.discussEntity,                target, disc);   //together with newDiscussionByDiscussEntity
//    addUserEvent(shouldCommit, user, SSUserEventEnum.newDiscussionByDiscussEntity, disc,   target); //together with discussEntity
//  addUserEvent(shouldCommit, user, SSUserEventEnum.addDiscussionComment, disc, strU.empty);(

  // if (SSSpaceEnum.isShared(space)) {
//      addUserEvent(shouldCommit, user, SSUserEventEnum.removeSharedTag, resource, tagString);
//    } else {
//      addUserEvent(shouldCommit, user, SSUserEventEnum.removePrivateTag, resource, tagString);
//    }(
  
//  saveUERemoveCollectionItem: 
//  if (
//      SSSpaceEnum.isShared (space) ||
//      SSSpaceEnum.isFollow (space)) {
//      
//      addUserEvent(shouldCommit, user, SSUserEventEnum.removeSharedCollectionItem,                   resource,  coll);
//      addUserEvent(shouldCommit, user, SSUserEventEnum.changeCollectionByRemoveSharedCollectionItem, coll,      resource);
//    } else {
//      
//      addUserEvent(shouldCommit, user, SSUserEventEnum.removePrivateCollectionItem,                   resource, coll);
//      addUserEvent(shouldCommit, user, SSUserEventEnum.changeCollectionByRemovePrivateCollectionItem, coll,     resource);
//    }
//  saveUERemoveCollection:
//  if (
//      SSSpaceEnum.isShared (space) ||
//      SSSpaceEnum.isFollow (space)) {
//      
//      addUserEvent(shouldCommit, user, SSUserEventEnum.removeSharedCollection, coll, strU.empty);
//    } else {
//      addUserEvent(shouldCommit, user, SSUserEventEnum.removePrivateCollection, coll, strU.empty);
//    }
//  addUserEvent(shouldCommit, user, SSUserEventEnum.renameDiscussion, disc, strU.empty);
//  saveUEAddCollectionItem:
//      if (SSSpaceEnum.isSharedOrFollow(space)) {
//      
//      addUserEvent(shouldCommit, user, SSUserEventEnum.addSharedCollectionItem,                   collEntry, coll);
//      addUserEvent(shouldCommit, user, SSUserEventEnum.changeCollectionByAddSharedCollectionItem, coll,      collEntry);
//    } else {
//      
//      addUserEvent(shouldCommit, user, SSUserEventEnum.addPrivateCollectionItem,                   collEntry, coll);
//      addUserEvent(shouldCommit, user, SSUserEventEnum.changeCollectionByAddPrivateCollectionItem, coll,      collEntry);
//    }
//saveUEAddTag:
//  if (SSSpaceEnum.isShared(space)) {
//      addUserEvent(shouldCommit, user, SSUserEventEnum.addSharedTag,  resource, tagString);
//    } else {
//      addUserEvent(shouldCommit, user, SSUserEventEnum.addPrivateTag, resource, tagString);
//    }
}





//public List<SSUserEvent> getArbitraryResourceEvents(
//    Date  startTime) throws Exception;
//
//	public List<SSUserEvent> getRecentResourceEvents(
//    List<SSUri> resources,
//    Date        startTime) throws Exception;
//  
//  public List<SSUserEvent> getEventObjects(
//    SSUri     user, 
//    SSUserEventEnum actionType, 
//    SSUri     resource, 
//    long      startTimestamp) throws Exception;
//  
//  public boolean addUserEventArray(
//    boolean         shouldCommit, 
//    SSUri           userUri,
//    List<SSUserEventEnum> actionTypes,
//    List<SSUri>     resourceUris,
//    List<String>    contents) throws Exception;
//  
//  public boolean addUserEvent(
//    boolean    shouldCommit, 
//    SSUri      user,
//    SSUserEventEnum  actionType,
//    SSUri      resource,
//    String     content)throws Exception;
//  
//  public boolean addUserEvent(
//    boolean    shouldCommit, 
//    SSUri      user,
//    SSUserEventEnum  actionType,
//    SSUri      resource,
//    SSUri      content)throws Exception;
//  
//  public boolean addUserEvent(
//    boolean          shouldCommit, 
//    SSUri            user,
//    SSUserEventEnum  actionType,
//    SSUri            resource,
//    SSTagString      content)throws Exception;
//  
//  public void saveUEAppearsInSearchResult(
//    boolean               shouldCommit, 
//    SSUri                 user,
//    SSUri                 resource) throws Exception; 
//  
//  public void saveUEShareCollection(
//    boolean               shouldCommit, 
//    boolean               saveUserEvent,
//    SSUri                 user,
//    SSUri                 coll)throws Exception;
//  
//  public void saveUESubscribeToCollection(
//    boolean               shouldCommit, 
//    SSUri                 user,
//    SSUri                 resource)throws Exception;
//  
//  public void saveUEUnSubscribeToCollection(
//    boolean               shouldCommit, 
//    SSUri                 user,
//    SSUri                 resource)throws Exception;
//  
//  public void saveUECreateCollection(
//    boolean               shouldCommit, 
//    SSUri                 user,
//    SSUri                 resource,
//    SSSpaceEnum                space)throws Exception;
//
//  public void saveUERateEntity(
//    boolean    shouldCommit, 
//    SSUri      user,
//    SSUri      resource,
//    String     value)throws Exception;
//  
//  public void saveUEDiscussEntity(
//    boolean               shouldCommit, 
//    SSUri                 user,
//    SSUri                 target,
//    SSUri                 disc)throws Exception;
//  
//  public void saveUEAddDiscussionComment(
//    boolean               shouldCommit, 
//    SSUri                 user,
//    SSUri                 disc)throws Exception;
//
//  public void saveUEAddTag(
//    boolean               shouldCommit, 
//    SSUri                 user,
//    SSUri                 resource,
//    SSTagString           tagString,
//    SSSpaceEnum           space)throws Exception;
//  
//  public void saveUEAddTag(
//    boolean               shouldCommit, 
//    SSUri                 user,
//    SSUri                 resource,
//    List<SSTagString>     tagStrings,
//    SSSpaceEnum                space)throws Exception;
//  
//  public void saveUERemoveTag(
//    boolean     shouldCommit, 
//    SSUri       user,
//    SSUri       resource,
//    SSSpaceEnum      space,
//    SSTagString tagString)throws Exception;
//
//  public void saveUERemoveCollectionItem(
//    boolean shouldCommit, 
//    SSUri   user,
//    SSUri   coll,
//    SSUri   resource,
//    SSSpaceEnum  space)throws Exception;
//  
//  public void saveUERemoveCollection(
//    boolean   shouldCommit, 
//    SSUri     user,
//    SSUri     coll,
//    SSSpaceEnum    space)throws Exception;
//  
//  public void saveUERenameDiscussion(
//    boolean  shouldCommit, 
//    SSUri    user,
//    SSUri    disc)throws Exception;
//  
//  public void saveUEAddCollectionItem(
//    boolean  shouldCommit, 
//    boolean  saveUserEvent,
//    SSUri    user,
//    SSUri    collEntry,
//    SSUri    coll,
//    SSSpaceEnum   space) throws Exception;