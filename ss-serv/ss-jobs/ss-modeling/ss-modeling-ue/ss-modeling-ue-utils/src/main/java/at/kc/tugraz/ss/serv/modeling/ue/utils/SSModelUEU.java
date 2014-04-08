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

import at.kc.tugraz.ss.datatypes.datatypes.SSUEEnum;
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
  
  public static final List<SSUEEnum> initialCollectionCollaborationEventTypes       = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> initialDiscussionCollaborationEventTypes       = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> collaborateCollectionEventTypes                = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> collaborateDiscussionEventTypes                = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> removeInitialCollectionCollaborationEventTypes = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> relateResourceEventTypes                       = new ArrayList<SSUEEnum>();
  
  public static final List<SSUEEnum> changingEventTypes                             = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> addingAndDeletingEventTypes                    = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> taggingEventTypes                              = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> sharingWithCommunityEventTypes                 = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> awarenessEventTypes                            = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> recommendEventTypes                            = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> authoringEventTypes                            = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> selectedFromOthersEventTypes                   = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> organizingInCollectionsEventTypes              = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> assessEventTypes                               = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> participationEventTypes                        = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> personAssociationEventTypes                    = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> contributeToDiscussionEventTypes               = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> exportCollectionItemEventTypes                 = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> rateEventTypes                                 = new ArrayList<SSUEEnum>();
//  public static final List<SSUEEnum> appearInSearchResultEventTypes                 = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> viewEntityEventTypes                           = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> startDicussionEventTypes                       = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> addToCollectionEventTypes                      = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> removeFromCollectionEventTypes                 = new ArrayList<SSUEEnum>();
  
  /* event type containers for user model */
  public static final List<SSUEEnum> sharedCollectionEventTypes                     = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> discussionEventTypes                           = new ArrayList<SSUEEnum>();
  public static final List<SSUEEnum> useTopicEventTypes                             = new ArrayList<SSUEEnum>();
  
  public static void init() throws Exception{
    
    //contribute to discussion event types
    contributeToDiscussionEventTypes.add(SSUEEnum.addDiscussionComment);
    
    //export collection item event types
    exportCollectionItemEventTypes.add(SSUEEnum.exportCollectionItem);
    
    //rate event types
    rateEventTypes.add(SSUEEnum.rateEntity);
    
    //appear in search result event types
//    appearInSearchResultEventTypes.add(SSUEEnum.appearsInSearchResult);
    
    //view entity event types
    viewEntityEventTypes.add(SSUEEnum.viewEntity);
    
    //start discussion event types
    startDicussionEventTypes.add(SSUEEnum.discussEntity);
    //				startDicussionEventTypes.add("startDiscussion");
    
    //add to collections event types
    addToCollectionEventTypes.add(SSUEEnum.addPrivateCollectionItem);
    addToCollectionEventTypes.add(SSUEEnum.addSharedCollectionItem);
    
    //remove from collection event types
    removeFromCollectionEventTypes.add(SSUEEnum.removeCollectionItem);
    
    //change event types
    changingEventTypes.add(SSUEEnum.addPrivateCollectionItem);
    changingEventTypes.add(SSUEEnum.addSharedCollectionItem);
    changingEventTypes.add(SSUEEnum.createPrivateCollection);
    changingEventTypes.add(SSUEEnum.createSharedCollection);
    changingEventTypes.add(SSUEEnum.renameDiscussion);
    changingEventTypes.add(SSUEEnum.addDiscussionComment);
    changingEventTypes.add(SSUEEnum.removeCollectionItem);
    changingEventTypes.add(SSUEEnum.removeCollection);
    changingEventTypes.add(SSUEEnum.renamePrivateCollection);
    changingEventTypes.add(SSUEEnum.renameSharedCollection);
    changingEventTypes.add(SSUEEnum.renamePrivateCollectionItem);
    changingEventTypes.add(SSUEEnum.renameSharedCollectionItem);
    changingEventTypes.add(SSUEEnum.changeCollectionByAddPrivateCollectionItem);
    //		changingEventTypes.add("changeCollectionAddPrivateCollectionItem");
    changingEventTypes.add(SSUEEnum.changeCollectionByAddSharedCollectionItem);
    //		changingEventTypes.add("changeCollectionAddSharedCollectionItem");
    changingEventTypes.add(SSUEEnum.changeCollectionByRemoveCollectionItem);
    //		changingEventTypes.add("changeCollectionRemovePrivateCollectionItem");
    //		changingEventTypes.add("changeCollectionRemoveSharedCollectionItem");
    changingEventTypes.add(SSUEEnum.changeCollectionByRenamePrivateCollectionItem);
    //		changingEventTypes.add("changeCollectionRenamePrivateCollectionItem");
    changingEventTypes.add(SSUEEnum.changeCollectionByRenameSharedCollectionItem);
    //		changingEventTypes.add("changeCollectionRenameSharedCollectionItem");
    changingEventTypes.add(SSUEEnum.newDiscussionByDiscussEntity);
    //		changingEventTypes.add("createDiscussion");
    
    //adding & deleting event types
    addingAndDeletingEventTypes.add(SSUEEnum.addDiscussionComment);
    addingAndDeletingEventTypes.add(SSUEEnum.addPrivateTag);
    addingAndDeletingEventTypes.add(SSUEEnum.addSharedTag);
    addingAndDeletingEventTypes.add(SSUEEnum.removePrivateTag);
    addingAndDeletingEventTypes.add(SSUEEnum.removeSharedTag);
    addingAndDeletingEventTypes.add(SSUEEnum.changeCollectionByAddPrivateCollectionItem);
    //		addingAndDeletingEventTypes.add("changeCollectionAddPrivateCollectionItem");
    addingAndDeletingEventTypes.add(SSUEEnum.changeCollectionByAddSharedCollectionItem);
    //		addingAndDeletingEventTypes.add("changeCollectionAddSharedCollectionItem");
    addingAndDeletingEventTypes.add(SSUEEnum.changeCollectionByRemoveCollectionItem);
    //		addingAndDeletingEventTypes.add("changeCollectionRemovePrivateCollectionItem");
    //		addingAndDeletingEventTypes.add("changeCollectionRemoveSharedCollectionItem");
    
    //tagging event types
    taggingEventTypes.add(SSUEEnum.addPrivateTag);
    taggingEventTypes.add(SSUEEnum.addSharedTag);
    taggingEventTypes.add(SSUEEnum.removePrivateTag);
    taggingEventTypes.add(SSUEEnum.removeSharedTag);
    
    //sharing with community event types
    sharingWithCommunityEventTypes.add(SSUEEnum.shareCollection);
    sharingWithCommunityEventTypes.add(SSUEEnum.shareCollectionItemByShareCollection);
    //			sharingWithCommunityEventTypes.add("shareCollectionItemShareCollection");
    sharingWithCommunityEventTypes.add(SSUEEnum.createSharedCollection);
    sharingWithCommunityEventTypes.add(SSUEEnum.addSharedCollectionItem);
    sharingWithCommunityEventTypes.add(SSUEEnum.addSharedTag);
    sharingWithCommunityEventTypes.add(SSUEEnum.discussEntity);
    //		sharingWithCommunityEventTypes.add("startDiscussion");
    sharingWithCommunityEventTypes.add(SSUEEnum.newDiscussionByDiscussEntity);
    //		sharingWithCommunityEventTypes.add("createDiscussion");
    
    //initial collection collaboration event types (indicating that a resource got able to be collaborated on)
    initialCollectionCollaborationEventTypes.add(SSUEEnum.subscribeCollection);
    initialCollectionCollaborationEventTypes.add(SSUEEnum.subscribeCollectionItemBySubscribeCollection);
    
    //initial discussion collaboration event types (indicating that a resource got able to be collaborated on)
    initialDiscussionCollaborationEventTypes.add(SSUEEnum.discussEntity);
    //		initialDiscussionCollaborationEventTypes.add("startDiscussion");
    initialDiscussionCollaborationEventTypes.add(SSUEEnum.newDiscussionByDiscussEntity);
    //		initialDiscussionCollaborationEventTypes.add("createDiscussion");
    
    //remove initial collection collaboration event types (indicating that a resource was removed from collaborative work)
    removeInitialCollectionCollaborationEventTypes.add(SSUEEnum.unSubscribeCollection);
    removeInitialCollectionCollaborationEventTypes.add(SSUEEnum.unSubscribeCollectionItemByUnSubscribeCollection);
    
    //collaborately collection work event types (prerequisite is that resource got able to be collaborated on)
    collaborateCollectionEventTypes.add(SSUEEnum.structureSharedCollection);
    //		collaborateCollectionEventTypes.add("structureSharedCollectionContent");
    collaborateCollectionEventTypes.add(SSUEEnum.renameSharedCollection);
    collaborateCollectionEventTypes.add(SSUEEnum.changeCollectionByAddSharedCollectionItem);
    //		collaborateCollectionEventTypes.add("changeCollectionAddSharedCollectionItem");
    collaborateCollectionEventTypes.add(SSUEEnum.changeCollectionByRemoveCollectionItem);
    //		collaborateCollectionEventTypes.add("changeCollectionRemoveSharedCollectionItem");
    collaborateCollectionEventTypes.add(SSUEEnum.changeCollectionByRenameSharedCollectionItem);
    //		collaborateCollectionEventTypes.add("changeCollectionRenameSharedCollectionItem");
    collaborateCollectionEventTypes.add(SSUEEnum.structureSharedCollectionItemByStructureSharedCollection);
    //		collaborateCollectionEventTypes.add("structureSharedCollectionItemStructureSharedCollectionContent");
    collaborateCollectionEventTypes.add(SSUEEnum.renameSharedCollectionItemByRenameSharedCollection);
    //		collaborateCollectionEventTypes.add("renameSharedCollectionItemRenameSharedCollection");
    collaborateCollectionEventTypes.add(SSUEEnum.addSharedTag);
    collaborateCollectionEventTypes.add(SSUEEnum.removeSharedTag);
    
    //collaborately discussion work event types (prerequisite is that resource got able to be collaborated on)
    collaborateDiscussionEventTypes.add(SSUEEnum.renameDiscussion);
    collaborateDiscussionEventTypes.add(SSUEEnum.addDiscussionComment);
    collaborateDiscussionEventTypes.add(SSUEEnum.addDiscussionTargetCommentByAddDiscussionComment);
    //		collaborateDiscussionEventTypes.add("addDiscussionTargetCommentAddDiscussionComment");
    collaborateDiscussionEventTypes.add(SSUEEnum.renameDiscussionTargetByRenameDiscussion);
    //		collaborateDiscussionEventTypes.add("renameDiscussionTargetRenameDiscussion");
    
    //events indicating that a resource has reached high awareness among others
    awarenessEventTypes.add(SSUEEnum.viewEntity);
    awarenessEventTypes.add(SSUEEnum.rateEntity);
    awarenessEventTypes.add(SSUEEnum.addSharedTag);
    awarenessEventTypes.add(SSUEEnum.exportCollectionItem);
    awarenessEventTypes.add(SSUEEnum.addSharedCollectionItem);
    awarenessEventTypes.add(SSUEEnum.changeCollectionByAddSharedCollectionItem);
    //		awarenessEventTypes.add("changeCollectionAddSharedCollectionItem");
    awarenessEventTypes.add(SSUEEnum.addDiscussionComment);
    awarenessEventTypes.add(SSUEEnum.addDiscussionTargetCommentByAddDiscussionComment);
    //		awarenessEventTypes.add("addDiscussionTargetCommentAddDiscussionComment");
    awarenessEventTypes.add(SSUEEnum.subscribeCollection);
    awarenessEventTypes.add(SSUEEnum.subscribeCollectionItemBySubscribeCollection);
    awarenessEventTypes.add(SSUEEnum.structureSharedCollection);
    //		awarenessEventTypes.add("structureSharedCollectionContent");
    awarenessEventTypes.add(SSUEEnum.removeSharedTag);
    awarenessEventTypes.add(SSUEEnum.structureSharedCollectionItemByStructureSharedCollection);
    //		awarenessEventTypes.add("structureSharedCollectionItemStructureSharedCollectionContent");
    
    //event types indicating that the resource was recommended
    recommendEventTypes.add(SSUEEnum.exportCollectionItem);
    recommendEventTypes.add(SSUEEnum.shareCollection);
    recommendEventTypes.add(SSUEEnum.createSharedCollection);
    recommendEventTypes.add(SSUEEnum.rateEntity);
    recommendEventTypes.add(SSUEEnum.shareCollectionItemByShareCollection);
    //		recommendEventTypes.add("shareCollectionItemShareCollection");
    recommendEventTypes.add(SSUEEnum.addSharedTag);
    recommendEventTypes.add(SSUEEnum.addSharedCollectionItem);
    
    //event types indicating that a person authored a resource
    authoringEventTypes.add(SSUEEnum.addSharedTag);
    authoringEventTypes.add(SSUEEnum.addSharedCollectionItem);
    
    //event types indicating that a resource was selected from others
    selectedFromOthersEventTypes.add(SSUEEnum.selectedFromOthers);
    
    //event types indicating that a resource was organized in collections
    organizingInCollectionsEventTypes.add(SSUEEnum.structurePrivateCollection);
    //			organizingInCollectionsEventTypes.add("structurePrivateCollectionContent");
    organizingInCollectionsEventTypes.add(SSUEEnum.structureSharedCollection);
    //		organizingInCollectionsEventTypes.add("structureSharedCollectionContent");
    organizingInCollectionsEventTypes.add(SSUEEnum.structurePrivateCollectionItemByStructurePrivateCollection);
    //			organizingInCollectionsEventTypes.add("structurePrivateCollectionItemStructurePrivateCollectionContent");
    organizingInCollectionsEventTypes.add(SSUEEnum.structureSharedCollectionItemByStructureSharedCollection);
    //			organizingInCollectionsEventTypes.add("structureSharedCollectionItemStructureSharedCollectionContent");
    
    //event types indicating that a resource was assess by a person
    assessEventTypes.add(SSUEEnum.rateEntity);
    assessEventTypes.add(SSUEEnum.addPrivateTag);
    assessEventTypes.add(SSUEEnum.addSharedTag);
    assessEventTypes.add(SSUEEnum.removePrivateTag);
    assessEventTypes.add(SSUEEnum.removeSharedTag);
    
    //event types indicating that a resource participated in discussion/collection
    participationEventTypes.add(SSUEEnum.addSharedCollectionItem);
    participationEventTypes.add(SSUEEnum.shareCollection);
    participationEventTypes.add(SSUEEnum.subscribeCollection);
    participationEventTypes.add(SSUEEnum.discussEntity);
    //			participationEventTypes.add("startDiscussion");
    participationEventTypes.add(SSUEEnum.renameDiscussion);
    participationEventTypes.add(SSUEEnum.addDiscussionComment);
    participationEventTypes.add(SSUEEnum.addSharedTag);
    participationEventTypes.add(SSUEEnum.removeSharedTag);
    participationEventTypes.add(SSUEEnum.renameSharedCollectionItem);
    participationEventTypes.add(SSUEEnum.structureSharedCollection);
    //			participationEventTypes.add("structureSharedCollectionContent");
    
    //event types indicating that a person can be associated with a resource
    personAssociationEventTypes.add(SSUEEnum.exportCollectionItem);
    personAssociationEventTypes.add(SSUEEnum.addPrivateCollectionItem);
    personAssociationEventTypes.add(SSUEEnum.addSharedCollectionItem);
    personAssociationEventTypes.add(SSUEEnum.shareCollection);
    personAssociationEventTypes.add(SSUEEnum.subscribeCollection);
    personAssociationEventTypes.add(SSUEEnum.unSubscribeCollection);
    personAssociationEventTypes.add(SSUEEnum.createPrivateCollection);
    personAssociationEventTypes.add(SSUEEnum.createSharedCollection);
    personAssociationEventTypes.add(SSUEEnum.rateEntity);
    personAssociationEventTypes.add(SSUEEnum.discussEntity);
    //			personAssociationEventTypes.add("startDiscussion");
    personAssociationEventTypes.add(SSUEEnum.renameDiscussion);
    personAssociationEventTypes.add(SSUEEnum.addDiscussionComment);
    personAssociationEventTypes.add(SSUEEnum.viewEntity);
    personAssociationEventTypes.add(SSUEEnum.addPrivateTag);
    personAssociationEventTypes.add(SSUEEnum.addSharedTag);
    personAssociationEventTypes.add(SSUEEnum.removePrivateTag);
    personAssociationEventTypes.add(SSUEEnum.removeSharedTag);
    personAssociationEventTypes.add(SSUEEnum.removeCollectionItem);
    personAssociationEventTypes.add(SSUEEnum.removeCollection);
    personAssociationEventTypes.add(SSUEEnum.renamePrivateCollection);
    personAssociationEventTypes.add(SSUEEnum.renameSharedCollection);
    personAssociationEventTypes.add(SSUEEnum.renamePrivateCollectionItem);
    personAssociationEventTypes.add(SSUEEnum.renameSharedCollectionItem);
    personAssociationEventTypes.add(SSUEEnum.structurePrivateCollection);
    //			personAssociationEventTypes.add("structurePrivateCollectionContent");
    personAssociationEventTypes.add(SSUEEnum.structureSharedCollection);
    //			personAssociationEventTypes.add("structureSharedCollectionContent");
    
    /* event type containers for user model */
    sharedCollectionEventTypes.add(SSUEEnum.addSharedCollectionItem);
    sharedCollectionEventTypes.add(SSUEEnum.changeCollectionByAddSharedCollectionItem);
    sharedCollectionEventTypes.add(SSUEEnum.subscribeCollection);
    sharedCollectionEventTypes.add(SSUEEnum.subscribeCollectionItemBySubscribeCollection);
    
    discussionEventTypes.add(SSUEEnum.renameDiscussion);
    discussionEventTypes.add(SSUEEnum.addDiscussionComment);
    discussionEventTypes.add(SSUEEnum.discussEntity);
    //			discussionEventTypes.add("startDiscussion");
    discussionEventTypes.add(SSUEEnum.addDiscussionComment);
    discussionEventTypes.add(SSUEEnum.addDiscussionComment);
    
    useTopicEventTypes.add(SSUEEnum.useTag);
    useTopicEventTypes.add(SSUEEnum.addSharedTag);
    useTopicEventTypes.add(SSUEEnum.removeSharedTag);
    
    relateResourceEventTypes.add(SSUEEnum.exportCollectionItem);
    relateResourceEventTypes.add(SSUEEnum.addPrivateCollectionItem);
    relateResourceEventTypes.add(SSUEEnum.addSharedCollectionItem);
    relateResourceEventTypes.add(SSUEEnum.shareCollection);
    relateResourceEventTypes.add(SSUEEnum.subscribeCollection);
    relateResourceEventTypes.add(SSUEEnum.unSubscribeCollection);
    relateResourceEventTypes.add(SSUEEnum.createPrivateCollection);
    relateResourceEventTypes.add(SSUEEnum.createSharedCollection);
    relateResourceEventTypes.add(SSUEEnum.rateEntity);
    relateResourceEventTypes.add(SSUEEnum.discussEntity);
    //			relateResourceEventTypes.add("startDiscussion");
    relateResourceEventTypes.add(SSUEEnum.renameDiscussion);
    relateResourceEventTypes.add(SSUEEnum.addDiscussionComment);
    relateResourceEventTypes.add(SSUEEnum.viewEntity);
    relateResourceEventTypes.add(SSUEEnum.addPrivateTag);
    relateResourceEventTypes.add(SSUEEnum.addSharedTag);
    relateResourceEventTypes.add(SSUEEnum.removePrivateTag);
    relateResourceEventTypes.add(SSUEEnum.removeSharedTag);
    relateResourceEventTypes.add(SSUEEnum.removeCollectionItem);
    relateResourceEventTypes.add(SSUEEnum.removeCollection);
    relateResourceEventTypes.add(SSUEEnum.renamePrivateCollection);
    relateResourceEventTypes.add(SSUEEnum.renameSharedCollection);
    relateResourceEventTypes.add(SSUEEnum.renamePrivateCollectionItem);
    relateResourceEventTypes.add(SSUEEnum.renameSharedCollectionItem);
    relateResourceEventTypes.add(SSUEEnum.structurePrivateCollection);
    //			relateResourceEventTypes.add("structurePrivateCollectionContent");
    relateResourceEventTypes.add(SSUEEnum.structureSharedCollection);
    //			relateResourceEventTypes.add("structureSharedCollectionContent");
  }
}
