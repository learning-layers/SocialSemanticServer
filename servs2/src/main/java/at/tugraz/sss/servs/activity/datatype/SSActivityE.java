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
package at.tugraz.sss.servs.activity.datatype;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.reg.*;
import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.*;


@ApiModel
public enum SSActivityE{

  //old
  setEntityPublic,
  
  //friend
  friendAdd,
  
  //message
  messageSend,
  
  //category
  addCategory,
  removeCategories,
  
  //cite
  cite, 
  
//  //search
//  search,
  
  //tag
  tagEntity,
  removeTags,
  
  //rating
  rateEntity,
  
  //location
  addLocation, 
  
  //files 
  shareFileWithUsers,
  
  //collections
  removeCollEntry,
  addCollEntry,
  
  //learning episodes
  removeLearnEpVersionCircle,
  removeLearnEpVersionEntity,
  addEntityToLearnEpVersion,
  addCircleToLearnEpVersion,
  shareLearnEpWithUser,
  changeLearnEpVersionCircleLabel,
  changeLearnEpVersionCircleDescription,
  changeEntityForLearnEpVersionEntity,
  moveLearnEpVersionEntity,
  moveLearnEpVersionCircle,
  copyLearnEpForUsers,
  addEntityToLearnEpCircle,
  removeEntityFromLearnEpCircle,
  removeLearnEpVersionCircleWithEntitites,
  
  //entity circles
  createCircle,
  addUsersToCircle,
  addEntitiesToCircle,
  
  //entities
  copyEntityForUsers,
  shareEntityWithUsers,
  shareEntityWithCircles,

  //discussions
  discussEntity,
  addDiscEntry;

  public static SSActivityE get(final String value) throws SSErr{
    
    try{
      return SSActivityE.valueOf(value);
    }catch(IllegalArgumentException error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static List<SSActivityE> get(final List<String> values) throws SSErr{
    
    final List<SSActivityE> result = new ArrayList<>();
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
}

  /*
   * UE to store server side
   * *******************************************************************/
//  addPrivateCollectionItem,
//  addSharedCollectionItem,
//  changeCollectionByAddPrivateCollectionItem, //changeCollectionAddPrivateCollectionItem
//  changeCollectionByAddSharedCollectionItem, //changeCollectionAddSharedCollectionItem
//  appearsInSearchResult,
//  subscribeCollection,
//  unSubscribeCollection,
//  createPrivateCollection,
//  createSharedCollection,
//  rateEntity,
//  discussEntity, //startDiscussion
//  addDiscussionComment,
//  addPrivateTag,
//  addSharedTag,
//  removePrivateTag,
//  removeSharedTag,
//  removeCollectionItem,
//  removeCollection,
//  renameDiscussion,
//  changeCollectionByRemoveCollectionItem, //changeCollectionRemovePrivateCollectionItem //changeCollectionRemoveSharedCollectionItem
//  newDiscussionByDiscussEntity,

  /* user events caused by other user events
   * *******************************************************************/
//  changeCollectionByRenamePrivateCollectionItem, //changeCollectionRenamePrivateCollectionItem
//  changeCollectionByRenameSharedCollectionItem, //changeCollectionRenameSharedCollectionItem
//  structureSharedCollectionItemByStructureSharedCollection, //structureSharedCollectionItemStructureSharedCollectionContent
//  structurePrivateCollectionItemByStructurePrivateCollection, //structurePrivateCollectionItemStructurePrivateCollectionContent
//  shareCollectionItemByShareCollection, //shareCollectionItemShareCollection
//  renameDiscussionTargetByRenameDiscussion, //renameDiscussionTargetRenameDiscussion
//  addDiscussionTargetCommentByAddDiscussionComment, //addDiscussionTargetCommentAddDiscussionComment
//  unSubscribeCollectionItemByUnSubscribeCollection,
//  subscribeCollectionItemBySubscribeCollection,
//  renameSharedCollectionItemByRenameSharedCollection; //"renameSharedCollectionItemRenameSharedCollection