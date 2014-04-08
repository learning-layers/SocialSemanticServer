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
package at.kc.tugraz.ss.datatypes.datatypes;

import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.*;

public enum SSUEEnum implements SSJSONLDPropI{
  
  evernoteNotebookCreate,
  evernoteNotebookUpdate,
  evernoteNotebookShare,
  evernoteNotebookFollow,
  evernoteNoteCreate,
  evernoteNoteUpdate,
  evernoteNoteDelete,
  evernoteNoteShare,
  evernoteNoteFollow,
  evernoteReminderDone,
  evernoteReminderCreate,
  evernoteResourceAdd,
  evernoteResourceFollow,
  evernoteResourceShare,
  
  /*
   * UE to store server side
   * *******************************************************************/
  addPrivateCollectionItem,
  addSharedCollectionItem,
  changeCollectionByAddPrivateCollectionItem, //changeCollectionAddPrivateCollectionItem
  changeCollectionByAddSharedCollectionItem, //changeCollectionAddSharedCollectionItem
//  appearsInSearchResult,
  subscribeCollection,
  unSubscribeCollection,
  createPrivateCollection,
  createSharedCollection,
  rateEntity,
  discussEntity, //startDiscussion
  addDiscussionComment,
  addPrivateTag,
  addSharedTag,
  removePrivateTag,
  removeSharedTag,
  removeCollectionItem,
  removeCollection,
  renameDiscussion,
  changeCollectionByRemoveCollectionItem, //changeCollectionRemovePrivateCollectionItem //changeCollectionRemoveSharedCollectionItem
  newDiscussionByDiscussEntity,
  
  /*
   * UE to store client side
   * *******************************************************************/
  timelineChangeTimelineRange,
  timelineViewEntityDetails,
  
  learnEpViewEntityDetails,
  learnEpOpenEpisodesDialog,
  learnEpSwitchEpisode,
  learnEpSwitchVersion,
  learnEpRenameEpisode,
  learnEpCreateNewEpisodeFromScratch,
  learnEpCreateNewEpisodeFromVersion,
  learnEpCreateNewVersion,
  learnEpDropOrganizeEntity,
  learnEpMoveOrganizeEntity,
  learnEpDeleteOrganizeEntity,
  learnEpCreateOrganizeCircle,
  learnEpChangeOrganizeCircle,
  learnEpRenameOrganizeCircle,
  learnEpDeleteOrganizeCircle,
  
  selectedFromOthers,
  viewEntity,
  exportCollectionItem,
  useTag,
  renamePrivateCollection,
  renameSharedCollection,
  renamePrivateCollectionItem,
  renameSharedCollectionItem,
  structurePrivateCollection, //structurePrivateCollectionContent
  structureSharedCollection, //structureSharedCollectionContent
  shareCollection,
  /*
   * UE caused by other UE
   * *******************************************************************/
  changeCollectionByRenamePrivateCollectionItem, //changeCollectionRenamePrivateCollectionItem
  changeCollectionByRenameSharedCollectionItem, //changeCollectionRenameSharedCollectionItem
  structureSharedCollectionItemByStructureSharedCollection, //structureSharedCollectionItemStructureSharedCollectionContent
  structurePrivateCollectionItemByStructurePrivateCollection, //structurePrivateCollectionItemStructurePrivateCollectionContent
  shareCollectionItemByShareCollection, //shareCollectionItemShareCollection
  renameDiscussionTargetByRenameDiscussion, //renameDiscussionTargetRenameDiscussion
  addDiscussionTargetCommentByAddDiscussionComment, //addDiscussionTargetCommentAddDiscussionComment
  unSubscribeCollectionItemByUnSubscribeCollection,
  subscribeCollectionItemBySubscribeCollection,
  renameSharedCollectionItemByRenameSharedCollection; //"renameSharedCollectionItemRenameSharedCollection

  public static String typeToStr(SSUEEnum eventType){
    return SSStrU.toString(eventType);
  }
  
  @Override
  public Object jsonLDDesc(){
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
  
  public static boolean isSame(
    SSUEEnum event1,
    SSUEEnum event2){
    
    if(
      SSObjU.isNull(event1) ||
      SSObjU.isNull(event2)){
      return false;
    }
    
    return event1.toString().equals(event2.toString());
  }
  
  public static SSUEEnum get(
    String event){
    
    if(SSStrU.isEmpty(event)){
      return null;
    }
    
    return SSUEEnum.valueOf(event);
  }
  
  public static List<SSUEEnum> get(
    List<String> events){
    
    List<SSUEEnum> result = new ArrayList<SSUEEnum>();
    
    for (String event : events){
      result.add(get(event));
    }
    
    return result;
  }
  
  public static boolean contains(
    List<SSUEEnum> eventTypes,
    SSUEEnum       actionType){
    
    for(SSUEEnum event : eventTypes){
      
      if(isSame(event, actionType)){
        return true;
      }
    }
    
    return false;
  }
}