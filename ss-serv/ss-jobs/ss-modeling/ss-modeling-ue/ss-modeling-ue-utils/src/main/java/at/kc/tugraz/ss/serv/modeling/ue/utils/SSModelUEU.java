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

package at.kc.tugraz.ss.serv.modeling.ue.utils;

import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import java.util.ArrayList;
import java.util.List;

public class SSModelUEU {

  private SSModelUEU(){}
  
  public  static Long             lastUpdateTime = -1L;
  
  /**
   * thresholds for MI/topic calculations
   */
  public static final double THRESHOLD_RATE_HIGH                        = 3;
  public static final double THRESHOLD_TOPIC_NORMAL_USER                = 3;
  
  /*
   * only available for one resource type
   */
  public static double THRESHOLD_COLLECTION_MADE_OUT_OF                  = 0;
  public static double THRESHOLD_PERSON_IS_EDITOR                        = 0;
  public static double THRESHOLD_PERSON_CONTRIBUTED_DISCUSSION           = 0;
  public static double THRESHOLD_PERSON_PARTICIPATED                     = 0;
  
  /*
   * available for more than one resource type
   */
  
  public static double THRESHOLD_COLLECTION_ORGANIZE_COLLECTION         = 0;
  public static double THRESHOLD_DISCUSSION_ORGANIZE_COLLECTION         = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_ORGANIZE_COLLECTION   = 0;
  public static double THRESHOLD_PERSON_ORGANIZE_COLLECTION             = 0;
  
  public static double THRESHOLD_COLLECTION_ADD_AND_DELETE              = 0;
  public static double THRESHOLD_DISCUSSION_ADD_AND_DELETE              = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_ADD_AND_DELETE        = 0;
  public static double THRESHOLD_PERSON_ADD_AND_DELETE                  = 0;
  
  public static double THRESHOLD_COLLECTION_RATE_HIGH                   = 0;
  public static double THRESHOLD_DISCUSSION_RATE_HIGH                   = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_RATE_HIGH             = 0;
  public static double THRESHOLD_PERSON_RATE_HIGH                       = 0;
  
  public static double THRESHOLD_COLLECTION_SHARE_COMMUNITY             = 0;
  public static double THRESHOLD_DISCUSSION_SHARE_COMMUNITY             = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_SHARE_COMMUNITY       = 0;
  public static double THRESHOLD_PERSON_SHARE_COMMUNITY                 = 0;
  
  public static double THRESHOLD_COLLECTION_USE_WIDE                    = 0;
  public static double THRESHOLD_DISCUSSION_USE_WIDE                    = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_USE_WIDE              = 0;
  public static double THRESHOLD_PERSON_USE_WIDE                        = 0;
  
  public static double THRESHOLD_COLLECTION_CREATE_JUST                 = 0;
  public static double THRESHOLD_DISCUSSION_CREATE_JUST                 = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_CREATE_JUST           = 0;
  
  public static double THRESHOLD_DIGITAL_RESOURCE_SELECT_FROM_OTHER     = 0;
  public static double THRESHOLD_COLLECTION_SELECT_FROM_OTHER           = 0;
  public static double THRESHOLD_DISCUSSION_SELECT_FROM_OTHER           = 0;
  public static double THRESHOLD_PERSON_SELECT_FROM_OTHER               = 0;
  
  public static double THRESHOLD_COLLECTION_CHANGE                      = 0;
  public static double THRESHOLD_DISCUSSION_CHANGE                      = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_CHANGE                = 0;
  
  public static double THRESHOLD_COLLECTION_TAG                         = 0;
  public static double THRESHOLD_DISCUSSION_TAG                         = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_TAG                   = 0;
  public static double THRESHOLD_PERSON_TAG                             = 0;
  
  public static double THRESHOLD_COLLECTION_VIEW                        = 0;
  public static double THRESHOLD_DISCUSSION_VIEW                        = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_VIEW                  = 0;
  public static double THRESHOLD_PERSON_VIEW                            = 0;
  
  public static double THRESHOLD_COLLECTION_COLLECTION_SIMILAR          = 0;
  public static double THRESHOLD_DISCUSSION_COLLECTION_SIMILAR          = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_COLLECTION_SIMILAR    = 0;
  public static double THRESHOLD_PERSON_COLLECTION_SIMILAR              = 0;
  
  public static double THRESHOLD_COLLECTION_COLLABORATE_COLLECTION       = 0;
  public static double THRESHOLD_DISCUSSION_COLLABORATE_COLLECTION       = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_COLLABORATE_COLLECTION = 0;
  
  public static double THRESHOLD_COLLECTION_COLLABORATE_DISCUSSION       = 0;
  public static double THRESHOLD_DISCUSSION_COLLABORATE_DISCUSSION       = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_COLLABORATE_DISCUSSION = 0;
  
  public static double THRESHOLD_COLLECTION_PRESENT_AUDIENCE             = 0;
  public static double THRESHOLD_DISCUSSION_PRESENT_AUDIENCE             = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_PRESENT_AUDIENCE       = 0;
  
  public static double THRESHOLD_COLLECTION_ASSESS                       = 0;
  public static double THRESHOLD_DISCUSSION_ASSESS                       = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_ASSESS                 = 0;
  public static double THRESHOLD_PERSON_ASSESS                           = 0;
  
  public static double THRESHOLD_COLLECTION_AWARE                        = 0;
  public static double THRESHOLD_DISCUSSION_AWARE                        = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_AWARE                  = 0;
  public static double THRESHOLD_PERSON_AWARE                            = 0;
  
  public static double THRESHOLD_COLLECTION_GOT_RATED                    = 0;
  public static double THRESHOLD_DISCUSSION_GOT_RATED                    = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_GOT_RATED              = 0;
  public static double THRESHOLD_PERSON_GOT_RATED                        = 0;
  
  public static double THRESHOLD_COLLECTION_REFERRED_BY                  = 0;
  public static double THRESHOLD_DISCUSSION_REFERRED_BY                  = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_REFERRED_BY            = 0;
  public static double THRESHOLD_PERSON_REFERRED_BY                      = 0;
  
  public static double THRESHOLD_COLLECTION_ACTIVE_PERIOD                = 0;
  public static double THRESHOLD_DISCUSSION_ACTIVE_PERIOD                = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_ACTIVE_PERIOD          = 0;
  public static double THRESHOLD_PERSON_ACTIVE_PERIOD                    = 0;
  
  public static double THRESHOLD_COLLECTION_COLLECTION                   = 0;
  public static double THRESHOLD_DISCUSSION_COLLECTION                   = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_COLLECTION             = 0;
  public static double THRESHOLD_PERSON_COLLECTION                       = 0;
  
  public static double THRESHOLD_COLLECTION_RECOMMEND                    = 0;
  public static double THRESHOLD_DISCUSSION_RECOMMEND                    = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_RECOMMEND              = 0;
  public static double THRESHOLD_PERSON_RECOMMEND                        = 0;
  
//  public static double THRESHOLD_COLLECTION_SEARCH_RESULT                = 0;
//  public static double THRESHOLD_DISCUSSION_SEARCH_RESULT                = 0;
//  public static double THRESHOLD_DIGITAL_RESOURCE_SEARCH_RESULT          = 0;
//  public static double THRESHOLD_PERSON_SEARCH_RESULT                    = 0;
  
  public static double THRESHOLD_COLLECTION_VIEW_PERSON                  = 0;
  public static double THRESHOLD_DISCUSSION_VIEW_PERSON                  = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_VIEW_PERSON            = 0;
  public static double THRESHOLD_PERSON_VIEW_PERSON                      = 0;
  
  public static double THRESHOLD_COLLECTION_CHANGE_PERSON                = 0;
  public static double THRESHOLD_COLLECTION_ASSOCIATE_PERSON             = 0;
  
  public static double THRESHOLD_DISCUSSION_CHANGE_PERSON                = 0;
  public static double THRESHOLD_DISCUSSION_ASSOCIATE_PERSON             = 0;
  
  public static double THRESHOLD_DIGITAL_RESOURCE_CHANGE_PERSON          = 0;
  public static double THRESHOLD_DIGITAL_RESOURCE_ASSOCIATE_PERSON       = 0;
  
  /**
   * factors
   */
  private static final double FACTOR_FAR_LESS_AVERAGE                      = 0.3;
  //	private static final double FACTOR_LESS_AVERAGE                          = 0.8;
  //	private static final double FACTOR_EQUAL_AVERAGE                         = 1;
  private static final double FACTOR_ABOVE_THRESHOLD                       = 1;
  
  /**
   * factors on thresholds for MI calculations
   */
  
  /*
   * only available for one resource type
   */
  public static final double FACTOR_COLLECTION_MADE_OUT_OF                    = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_PERSON_IS_EDITOR                          = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_CONTRIBUTED_DISCUSSION             = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_PARTICIPATED                       = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_USE_WIDE                       = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_USE_WIDE                       = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_USE_WIDE                 = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_USE_WIDE                           = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_CREATE_JUST                    = FACTOR_FAR_LESS_AVERAGE;
  public static final double FACTOR_DISCUSSION_CREATE_JUST                    = FACTOR_FAR_LESS_AVERAGE;
  public static final double FACTOR_DIGITAL_RESOURCE_CREATE_JUST              = FACTOR_FAR_LESS_AVERAGE;
  
  public static final double FACTOR_COLLECTION_COLLECTION                     = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_COLLECTION                     = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_COLLECTION               = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_COLLECTION                         = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_COLLECTION_SIMILAR             = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_COLLECTION_SIMILAR             = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_COLLECTION_SIMILAR       = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_COLLECTION_SIMILAR                 = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_CHANGE                         = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_CHANGE                         = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_CHANGE                   = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_ASSOCIATE_PERSON               = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_ASSOCIATE_PERSON               = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_ASSOCIATE_PERSON         = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_CHANGE_INTENSIVE               = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_CHANGE_INTENSIVE               = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_CHANGE_INTENSIVE         = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_CHANGE_PERSON                  = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_CHANGE_PERSON                  = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_CHANGE_PERSON            = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_TAG                            = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_TAG                            = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_TAG                      = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_TAG                                = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_VIEW_PERSON                    = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_VIEW_PERSON                    = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_VIEW_PERSON              = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_VIEW_PERSON                        = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_VIEW                           = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_VIEW                           = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_VIEW                     = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_VIEW                               = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_SEARCH_RESULT                  = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_SEARCH_RESULT                  = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_SEARCH_RESULT            = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_SEARCH_RESULT                      = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_SELECT_FROM_OTHER              = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_SELECT_FROM_OTHER              = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_SELECT_FROM_OTHER        = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_SELECT_FROM_OTHER                  = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_COLLABORATE_COLLECTION         = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_COLLABORATE_COLLECTION         = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_COLLABORATE_COLLECTION   = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_COLLABORATE_DISCUSSION         = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_COLLABORATE_DISCUSSION         = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_COLLABORATE_DISCUSSION   = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_AWARE                          = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_AWARE                          = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_AWARE                    = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_AWARE                              = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_RECOMMEND                      = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_RECOMMEND                      = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_RECOMMEND                = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_RECOMMEND                          = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_ADD_AND_DELETE                 = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_ADD_AND_DELETE                 = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_ADD_AND_DELETE           = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_ADD_AND_DELETE                     = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_RATE_HIGH                      = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_RATE_HIGH                      = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_RATE_HIGH                = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_RATE_HIGH                          = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_SHARE_COMMUNITY                = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_SHARE_COMMUNITY                = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_SHARE_COMMUNITY          = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_SHARE_COMMUNITY                    = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_PRESENT_AUDIENCE               = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_PRESENT_AUDIENCE               = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_PRESENT_AUDIENCE         = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_ASSESS                         = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_ASSESS                         = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_ASSESS                   = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_ASSESS                             = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_GOT_RATED                      = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_GOT_RATED                      = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_GOT_RATED                = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_GOT_RATED                          = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_REFERRED_BY                    = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_REFERRED_BY                    = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_REFERRED_BY              = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_REFERRED_BY                        = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_ACTIVE_PERIOD                  = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_ACTIVE_PERIOD                  = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_ACTIVE_PERIOD            = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_ACTIVE_PERIOD                      = FACTOR_ABOVE_THRESHOLD;
  
  public static final double FACTOR_COLLECTION_ORGANIZE_COLLECTION            = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DISCUSSION_ORGANIZE_COLLECTION            = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_DIGITAL_RESOURCE_ORGANIZE_COLLECTION      = FACTOR_ABOVE_THRESHOLD;
  public static final double FACTOR_PERSON_ORGANIZE_COLLECTION                = FACTOR_ABOVE_THRESHOLD;
  
  /*
   * event type containers
   ***********************************/
  
  public static final List<SSUEE> initialCollectionCollaborationEventTypes       = new ArrayList<>();
  public static final List<SSUEE> initialDiscussionCollaborationEventTypes       = new ArrayList<>();
  public static final List<SSUEE> collaborateCollectionEventTypes                = new ArrayList<>();
  public static final List<SSUEE> collaborateDiscussionEventTypes                = new ArrayList<>();
  public static final List<SSUEE> removeInitialCollectionCollaborationEventTypes = new ArrayList<>();
  public static final List<SSUEE> relateResourceEventTypes                       = new ArrayList<>();
  
  public static final List<SSUEE> changingEventTypes                             = new ArrayList<>();
  public static final List<SSUEE> addingAndDeletingEventTypes                    = new ArrayList<>();
  public static final List<SSUEE> taggingEventTypes                              = new ArrayList<>();
  public static final List<SSUEE> sharingWithCommunityEventTypes                 = new ArrayList<>();
  public static final List<SSUEE> awarenessEventTypes                            = new ArrayList<>();
  public static final List<SSUEE> recommendEventTypes                            = new ArrayList<>();
  public static final List<SSUEE> authoringEventTypes                            = new ArrayList<>();
  public static final List<SSUEE> selectedFromOthersEventTypes                   = new ArrayList<>();
  public static final List<SSUEE> organizingInCollectionsEventTypes              = new ArrayList<>();
  public static final List<SSUEE> assessEventTypes                               = new ArrayList<>();
  public static final List<SSUEE> participationEventTypes                        = new ArrayList<>();
  public static final List<SSUEE> personAssociationEventTypes                    = new ArrayList<>();
  public static final List<SSUEE> contributeToDiscussionEventTypes               = new ArrayList<>();
  public static final List<SSUEE> exportCollectionItemEventTypes                 = new ArrayList<>();
  public static final List<SSUEE> rateEventTypes                                 = new ArrayList<>();
//  public static final List<SSUEEnum> appearInSearchResultEventTypes                 = new ArrayList<SSUEEnum>();
  public static final List<SSUEE> viewEntityEventTypes                           = new ArrayList<>();
  public static final List<SSUEE> startDicussionEventTypes                       = new ArrayList<>();
  public static final List<SSUEE> addToCollectionEventTypes                      = new ArrayList<>();
  public static final List<SSUEE> removeFromCollectionEventTypes                 = new ArrayList<>();
  
  /* event type containers for user model */
  public static final List<SSUEE> sharedCollectionEventTypes                     = new ArrayList<>();
  public static final List<SSUEE> discussionEventTypes                           = new ArrayList<>();
  public static final List<SSUEE> useTopicEventTypes                             = new ArrayList<>();
  
  public static void init() throws Exception{
    
    //contribute to discussion event types
    contributeToDiscussionEventTypes.add(SSUEE.addDiscussionComment);
    
    //export collection item event types
    exportCollectionItemEventTypes.add(SSUEE.exportCollectionItem);
    
    //rate event types
    rateEventTypes.add(SSUEE.rateEntity);
    
    //appear in search result event types
//    appearInSearchResultEventTypes.add(SSUEEnum.appearsInSearchResult);
    
    //view entity event types
    viewEntityEventTypes.add(SSUEE.viewEntity);
    
    //start discussion event types
    startDicussionEventTypes.add(SSUEE.discussEntity);
    //				startDicussionEventTypes.add("startDiscussion");
    
    //add to collections event types
    addToCollectionEventTypes.add(SSUEE.addPrivateCollectionItem);
    addToCollectionEventTypes.add(SSUEE.addSharedCollectionItem);
    
    //remove from collection event types
    removeFromCollectionEventTypes.add(SSUEE.removeCollectionItem);
    
    //change event types
    changingEventTypes.add(SSUEE.addPrivateCollectionItem);
    changingEventTypes.add(SSUEE.addSharedCollectionItem);
    changingEventTypes.add(SSUEE.createPrivateCollection);
    changingEventTypes.add(SSUEE.createSharedCollection);
    changingEventTypes.add(SSUEE.renameDiscussion);
    changingEventTypes.add(SSUEE.addDiscussionComment);
    changingEventTypes.add(SSUEE.removeCollectionItem);
    changingEventTypes.add(SSUEE.removeCollection);
    changingEventTypes.add(SSUEE.renamePrivateCollection);
    changingEventTypes.add(SSUEE.renameSharedCollection);
    changingEventTypes.add(SSUEE.renamePrivateCollectionItem);
    changingEventTypes.add(SSUEE.renameSharedCollectionItem);
    changingEventTypes.add(SSUEE.changeCollectionByAddPrivateCollectionItem);
    //		changingEventTypes.add("changeCollectionAddPrivateCollectionItem");
    changingEventTypes.add(SSUEE.changeCollectionByAddSharedCollectionItem);
    //		changingEventTypes.add("changeCollectionAddSharedCollectionItem");
    changingEventTypes.add(SSUEE.changeCollectionByRemoveCollectionItem);
    //		changingEventTypes.add("changeCollectionRemovePrivateCollectionItem");
    //		changingEventTypes.add("changeCollectionRemoveSharedCollectionItem");
    changingEventTypes.add(SSUEE.changeCollectionByRenamePrivateCollectionItem);
    //		changingEventTypes.add("changeCollectionRenamePrivateCollectionItem");
    changingEventTypes.add(SSUEE.changeCollectionByRenameSharedCollectionItem);
    //		changingEventTypes.add("changeCollectionRenameSharedCollectionItem");
    changingEventTypes.add(SSUEE.newDiscussionByDiscussEntity);
    //		changingEventTypes.add("createDiscussion");
    
    //adding & deleting event types
    addingAndDeletingEventTypes.add(SSUEE.addDiscussionComment);
    addingAndDeletingEventTypes.add(SSUEE.addPrivateTag);
    addingAndDeletingEventTypes.add(SSUEE.addSharedTag);
    addingAndDeletingEventTypes.add(SSUEE.removePrivateTag);
    addingAndDeletingEventTypes.add(SSUEE.removeSharedTag);
    addingAndDeletingEventTypes.add(SSUEE.changeCollectionByAddPrivateCollectionItem);
    //		addingAndDeletingEventTypes.add("changeCollectionAddPrivateCollectionItem");
    addingAndDeletingEventTypes.add(SSUEE.changeCollectionByAddSharedCollectionItem);
    //		addingAndDeletingEventTypes.add("changeCollectionAddSharedCollectionItem");
    addingAndDeletingEventTypes.add(SSUEE.changeCollectionByRemoveCollectionItem);
    //		addingAndDeletingEventTypes.add("changeCollectionRemovePrivateCollectionItem");
    //		addingAndDeletingEventTypes.add("changeCollectionRemoveSharedCollectionItem");
    
    //tagging event types
    taggingEventTypes.add(SSUEE.addPrivateTag);
    taggingEventTypes.add(SSUEE.addSharedTag);
    taggingEventTypes.add(SSUEE.removePrivateTag);
    taggingEventTypes.add(SSUEE.removeSharedTag);
    
    //sharing with community event types
    sharingWithCommunityEventTypes.add(SSUEE.shareCollection);
    sharingWithCommunityEventTypes.add(SSUEE.shareCollectionItemByShareCollection);
    //			sharingWithCommunityEventTypes.add("shareCollectionItemShareCollection");
    sharingWithCommunityEventTypes.add(SSUEE.createSharedCollection);
    sharingWithCommunityEventTypes.add(SSUEE.addSharedCollectionItem);
    sharingWithCommunityEventTypes.add(SSUEE.addSharedTag);
    sharingWithCommunityEventTypes.add(SSUEE.discussEntity);
    //		sharingWithCommunityEventTypes.add("startDiscussion");
    sharingWithCommunityEventTypes.add(SSUEE.newDiscussionByDiscussEntity);
    //		sharingWithCommunityEventTypes.add("createDiscussion");
    
    //initial collection collaboration event types (indicating that a resource got able to be collaborated on)
    initialCollectionCollaborationEventTypes.add(SSUEE.subscribeCollection);
    initialCollectionCollaborationEventTypes.add(SSUEE.subscribeCollectionItemBySubscribeCollection);
    
    //initial discussion collaboration event types (indicating that a resource got able to be collaborated on)
    initialDiscussionCollaborationEventTypes.add(SSUEE.discussEntity);
    //		initialDiscussionCollaborationEventTypes.add("startDiscussion");
    initialDiscussionCollaborationEventTypes.add(SSUEE.newDiscussionByDiscussEntity);
    //		initialDiscussionCollaborationEventTypes.add("createDiscussion");
    
    //remove initial collection collaboration event types (indicating that a resource was removed from collaborative work)
    removeInitialCollectionCollaborationEventTypes.add(SSUEE.unSubscribeCollection);
    removeInitialCollectionCollaborationEventTypes.add(SSUEE.unSubscribeCollectionItemByUnSubscribeCollection);
    
    //collaborately collection work event types (prerequisite is that resource got able to be collaborated on)
    collaborateCollectionEventTypes.add(SSUEE.structureSharedCollection);
    //		collaborateCollectionEventTypes.add("structureSharedCollectionContent");
    collaborateCollectionEventTypes.add(SSUEE.renameSharedCollection);
    collaborateCollectionEventTypes.add(SSUEE.changeCollectionByAddSharedCollectionItem);
    //		collaborateCollectionEventTypes.add("changeCollectionAddSharedCollectionItem");
    collaborateCollectionEventTypes.add(SSUEE.changeCollectionByRemoveCollectionItem);
    //		collaborateCollectionEventTypes.add("changeCollectionRemoveSharedCollectionItem");
    collaborateCollectionEventTypes.add(SSUEE.changeCollectionByRenameSharedCollectionItem);
    //		collaborateCollectionEventTypes.add("changeCollectionRenameSharedCollectionItem");
    collaborateCollectionEventTypes.add(SSUEE.structureSharedCollectionItemByStructureSharedCollection);
    //		collaborateCollectionEventTypes.add("structureSharedCollectionItemStructureSharedCollectionContent");
    collaborateCollectionEventTypes.add(SSUEE.renameSharedCollectionItemByRenameSharedCollection);
    //		collaborateCollectionEventTypes.add("renameSharedCollectionItemRenameSharedCollection");
    collaborateCollectionEventTypes.add(SSUEE.addSharedTag);
    collaborateCollectionEventTypes.add(SSUEE.removeSharedTag);
    
    //collaborately discussion work event types (prerequisite is that resource got able to be collaborated on)
    collaborateDiscussionEventTypes.add(SSUEE.renameDiscussion);
    collaborateDiscussionEventTypes.add(SSUEE.addDiscussionComment);
    collaborateDiscussionEventTypes.add(SSUEE.addDiscussionTargetCommentByAddDiscussionComment);
    //		collaborateDiscussionEventTypes.add("addDiscussionTargetCommentAddDiscussionComment");
    collaborateDiscussionEventTypes.add(SSUEE.renameDiscussionTargetByRenameDiscussion);
    //		collaborateDiscussionEventTypes.add("renameDiscussionTargetRenameDiscussion");
    
    //events indicating that a resource has reached high awareness among others
    awarenessEventTypes.add(SSUEE.viewEntity);
    awarenessEventTypes.add(SSUEE.rateEntity);
    awarenessEventTypes.add(SSUEE.addSharedTag);
    awarenessEventTypes.add(SSUEE.exportCollectionItem);
    awarenessEventTypes.add(SSUEE.addSharedCollectionItem);
    awarenessEventTypes.add(SSUEE.changeCollectionByAddSharedCollectionItem);
    //		awarenessEventTypes.add("changeCollectionAddSharedCollectionItem");
    awarenessEventTypes.add(SSUEE.addDiscussionComment);
    awarenessEventTypes.add(SSUEE.addDiscussionTargetCommentByAddDiscussionComment);
    //		awarenessEventTypes.add("addDiscussionTargetCommentAddDiscussionComment");
    awarenessEventTypes.add(SSUEE.subscribeCollection);
    awarenessEventTypes.add(SSUEE.subscribeCollectionItemBySubscribeCollection);
    awarenessEventTypes.add(SSUEE.structureSharedCollection);
    //		awarenessEventTypes.add("structureSharedCollectionContent");
    awarenessEventTypes.add(SSUEE.removeSharedTag);
    awarenessEventTypes.add(SSUEE.structureSharedCollectionItemByStructureSharedCollection);
    //		awarenessEventTypes.add("structureSharedCollectionItemStructureSharedCollectionContent");
    
    //event types indicating that the resource was recommended
    recommendEventTypes.add(SSUEE.exportCollectionItem);
    recommendEventTypes.add(SSUEE.shareCollection);
    recommendEventTypes.add(SSUEE.createSharedCollection);
    recommendEventTypes.add(SSUEE.rateEntity);
    recommendEventTypes.add(SSUEE.shareCollectionItemByShareCollection);
    //		recommendEventTypes.add("shareCollectionItemShareCollection");
    recommendEventTypes.add(SSUEE.addSharedTag);
    recommendEventTypes.add(SSUEE.addSharedCollectionItem);
    
    //event types indicating that a person authored a resource
    authoringEventTypes.add(SSUEE.addSharedTag);
    authoringEventTypes.add(SSUEE.addSharedCollectionItem);
    
    //event types indicating that a resource was selected from others
    selectedFromOthersEventTypes.add(SSUEE.selectedFromOthers);
    
    //event types indicating that a resource was organized in collections
    organizingInCollectionsEventTypes.add(SSUEE.structurePrivateCollection);
    //			organizingInCollectionsEventTypes.add("structurePrivateCollectionContent");
    organizingInCollectionsEventTypes.add(SSUEE.structureSharedCollection);
    //		organizingInCollectionsEventTypes.add("structureSharedCollectionContent");
    organizingInCollectionsEventTypes.add(SSUEE.structurePrivateCollectionItemByStructurePrivateCollection);
    //			organizingInCollectionsEventTypes.add("structurePrivateCollectionItemStructurePrivateCollectionContent");
    organizingInCollectionsEventTypes.add(SSUEE.structureSharedCollectionItemByStructureSharedCollection);
    //			organizingInCollectionsEventTypes.add("structureSharedCollectionItemStructureSharedCollectionContent");
    
    //event types indicating that a resource was assess by a person
    assessEventTypes.add(SSUEE.rateEntity);
    assessEventTypes.add(SSUEE.addPrivateTag);
    assessEventTypes.add(SSUEE.addSharedTag);
    assessEventTypes.add(SSUEE.removePrivateTag);
    assessEventTypes.add(SSUEE.removeSharedTag);
    
    //event types indicating that a resource participated in discussion/collection
    participationEventTypes.add(SSUEE.addSharedCollectionItem);
    participationEventTypes.add(SSUEE.shareCollection);
    participationEventTypes.add(SSUEE.subscribeCollection);
    participationEventTypes.add(SSUEE.discussEntity);
    //			participationEventTypes.add("startDiscussion");
    participationEventTypes.add(SSUEE.renameDiscussion);
    participationEventTypes.add(SSUEE.addDiscussionComment);
    participationEventTypes.add(SSUEE.addSharedTag);
    participationEventTypes.add(SSUEE.removeSharedTag);
    participationEventTypes.add(SSUEE.renameSharedCollectionItem);
    participationEventTypes.add(SSUEE.structureSharedCollection);
    //			participationEventTypes.add("structureSharedCollectionContent");
    
    //event types indicating that a person can be associated with a resource
    personAssociationEventTypes.add(SSUEE.exportCollectionItem);
    personAssociationEventTypes.add(SSUEE.addPrivateCollectionItem);
    personAssociationEventTypes.add(SSUEE.addSharedCollectionItem);
    personAssociationEventTypes.add(SSUEE.shareCollection);
    personAssociationEventTypes.add(SSUEE.subscribeCollection);
    personAssociationEventTypes.add(SSUEE.unSubscribeCollection);
    personAssociationEventTypes.add(SSUEE.createPrivateCollection);
    personAssociationEventTypes.add(SSUEE.createSharedCollection);
    personAssociationEventTypes.add(SSUEE.rateEntity);
    personAssociationEventTypes.add(SSUEE.discussEntity);
    //			personAssociationEventTypes.add("startDiscussion");
    personAssociationEventTypes.add(SSUEE.renameDiscussion);
    personAssociationEventTypes.add(SSUEE.addDiscussionComment);
    personAssociationEventTypes.add(SSUEE.viewEntity);
    personAssociationEventTypes.add(SSUEE.addPrivateTag);
    personAssociationEventTypes.add(SSUEE.addSharedTag);
    personAssociationEventTypes.add(SSUEE.removePrivateTag);
    personAssociationEventTypes.add(SSUEE.removeSharedTag);
    personAssociationEventTypes.add(SSUEE.removeCollectionItem);
    personAssociationEventTypes.add(SSUEE.removeCollection);
    personAssociationEventTypes.add(SSUEE.renamePrivateCollection);
    personAssociationEventTypes.add(SSUEE.renameSharedCollection);
    personAssociationEventTypes.add(SSUEE.renamePrivateCollectionItem);
    personAssociationEventTypes.add(SSUEE.renameSharedCollectionItem);
    personAssociationEventTypes.add(SSUEE.structurePrivateCollection);
    //			personAssociationEventTypes.add("structurePrivateCollectionContent");
    personAssociationEventTypes.add(SSUEE.structureSharedCollection);
    //			personAssociationEventTypes.add("structureSharedCollectionContent");
    
    /* event type containers for user model */
    sharedCollectionEventTypes.add(SSUEE.addSharedCollectionItem);
    sharedCollectionEventTypes.add(SSUEE.changeCollectionByAddSharedCollectionItem);
    sharedCollectionEventTypes.add(SSUEE.subscribeCollection);
    sharedCollectionEventTypes.add(SSUEE.subscribeCollectionItemBySubscribeCollection);
    
    discussionEventTypes.add(SSUEE.renameDiscussion);
    discussionEventTypes.add(SSUEE.addDiscussionComment);
    discussionEventTypes.add(SSUEE.discussEntity);
    //			discussionEventTypes.add("startDiscussion");
    discussionEventTypes.add(SSUEE.addDiscussionComment);
    discussionEventTypes.add(SSUEE.addDiscussionComment);
    
    useTopicEventTypes.add(SSUEE.useTag);
    useTopicEventTypes.add(SSUEE.addSharedTag);
    useTopicEventTypes.add(SSUEE.removeSharedTag);
    
    relateResourceEventTypes.add(SSUEE.exportCollectionItem);
    relateResourceEventTypes.add(SSUEE.addPrivateCollectionItem);
    relateResourceEventTypes.add(SSUEE.addSharedCollectionItem);
    relateResourceEventTypes.add(SSUEE.shareCollection);
    relateResourceEventTypes.add(SSUEE.subscribeCollection);
    relateResourceEventTypes.add(SSUEE.unSubscribeCollection);
    relateResourceEventTypes.add(SSUEE.createPrivateCollection);
    relateResourceEventTypes.add(SSUEE.createSharedCollection);
    relateResourceEventTypes.add(SSUEE.rateEntity);
    relateResourceEventTypes.add(SSUEE.discussEntity);
    //			relateResourceEventTypes.add("startDiscussion");
    relateResourceEventTypes.add(SSUEE.renameDiscussion);
    relateResourceEventTypes.add(SSUEE.addDiscussionComment);
    relateResourceEventTypes.add(SSUEE.viewEntity);
    relateResourceEventTypes.add(SSUEE.addPrivateTag);
    relateResourceEventTypes.add(SSUEE.addSharedTag);
    relateResourceEventTypes.add(SSUEE.removePrivateTag);
    relateResourceEventTypes.add(SSUEE.removeSharedTag);
    relateResourceEventTypes.add(SSUEE.removeCollectionItem);
    relateResourceEventTypes.add(SSUEE.removeCollection);
    relateResourceEventTypes.add(SSUEE.renamePrivateCollection);
    relateResourceEventTypes.add(SSUEE.renameSharedCollection);
    relateResourceEventTypes.add(SSUEE.renamePrivateCollectionItem);
    relateResourceEventTypes.add(SSUEE.renameSharedCollectionItem);
    relateResourceEventTypes.add(SSUEE.structurePrivateCollection);
    //			relateResourceEventTypes.add("structurePrivateCollectionContent");
    relateResourceEventTypes.add(SSUEE.structureSharedCollection);
    //			relateResourceEventTypes.add("structureSharedCollectionContent");
  }
}
