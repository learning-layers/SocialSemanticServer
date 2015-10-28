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
package at.tugraz.sss.serv;

public enum SSServOpE{

  //mail
  mailSend,
  mailsReceive,
  
  //livingDocument
  livingDocGet,
  livingDocAdd,
  livingDocsGet,
  livingDocRemove,
  livingDocUpdate,
  
  //integrationtest
  integrationTestSolrForSearch,
  
  //location
  locationGet,
  locationAdd,
  locationsGet,
  
  //image
  imageGet,
  imageAdd,
  imagesGet,
  imageProfilePictureSet,
  
  //eval
  evalLog,
  
  //like
  likesGet,
  likeSet,
  
  //video
  videoGet,
  videosGet,
  videoAdd,
  videoAnnotationAdd,
  videoAnnotationsGet,
  videoAnnotationGet,
  
  //friend
  friendGet,
  friendsGet,
  friendAdd,
  
  //appStackLayout
  appStackLayoutGet,
  appStackLayoutsGet,
  appStackLayoutCreate,
  appStackLayoutDelete,
  appStackLayoutUpdate,
  
  //app
  appGet,
  appsGet,
  appAdd,
  appsDelete,
  
  //message
  messageGet,
  messageSend,
  messagesGet,
  
  //comment
  commentsGet,
  commentEntitiesGet,
  commentsAdd,
  
  
  //system
  systemVersionGet,
  
  //flag
  flagsGet,
  flagsSet,
  flagGet,
  
  //cloud
  cloudPublishService,
  
  //test
  testServOverall                                     ,
  
  //evernote
  evernoteNoteStoreGet                                ,
  evernoteNotebookGet,
  evernoteNoteGet,
  evernoteNotebooksSharedGet                          ,
  evernoteResourceGet,
  evernoteNoteTagNamesGet,
  evernoteUserAdd,
  evernoteUsersAuthTokenGet,
  evernoteNoteAdd,
  evernoteUSNSet,
  evernoteResourceAdd,
  evernoteResourceByHashGet,
  //  evernoteNotebooksLinkedGet                          ,
  //  evernoteNotebooksGet                                ,
//  evernoteNotesGet                                    ,
  //  evernoteNotesLinkedGet                              ,
  
  //circle
  circleCreate,
  circleEntitiesAdd,
  circleUsersAdd,
  circleTypesGet,
  circleGet,
  circlesGet,
  circlePrivURIGet,
  circlePubURIGet,
//  circleCanAccess,
  circleEntitiesRemove,
  circleUsersRemove,
  circleRemove,
  circleUsersInvite,
  circleIsEntityPrivate,
  circleIsEntityShared,
  circleIsEntityPublic,
  
  //entity
  entitiesGet,
  entityAdd                                           ,
  entityGet                                           ,
  entityFromTypeAndLabelGet,
  entityUserSubEntitiesGet                            ,
  entityUserParentEntitiesGet,
  entityRemove                                        ,
  entityUpdate,
  entityCopy,
  entityShare,
  entityDownloadsGet,
  entityDownloadsAdd,
  entityEntitiesAttach,
  entityEntitiesAttachedRemove,
  entityTypesGet,
  
  //learn ep
  learnEpsGet                                         ,
  learnEpVersionsGet                                  ,
  learnEpVersionGet                                   ,
  learnEpVersionCurrentGet                            ,
  learnEpVersionCurrentSet                            ,
  learnEpVersionCreate                                ,
  learnEpVersionCircleAdd                             ,
  learnEpVersionEntityAdd                             ,
  learnEpCreate                                       ,
  learnEpVersionCircleUpdate                          ,
  learnEpVersionEntityUpdate                          ,
  learnEpVersionCircleRemove                          ,
  learnEpVersionEntityRemove                          ,
  learnEpVersionTimelineStateSet                      ,
  learnEpVersionTimelineStateGet                      ,
  learnEpLockRemove,
  learnEpLockSet,
  learnEpsLockHold,
  learnEpLockHold,
  learnEpRemove,
  learnEpGet,
  learnEpVersionCirclesWithEntriesGet,
  
  //data export
  dataExportUserRelations,
  dataExportUsersEntitiesTagsCategoriesTimestampsFile,
  dataExportUserEntityTagsCategoriesTimestampsLine,
  dataExportUsersEntitiesTagsCategoriesTimestampsFileFromCircle,
  
  //json ld
  jsonLD                                              ,
  
  //user
  userURIGet,
  userURIsGet,
  userExists                                          ,
  usersGet,
  userAdd,
  userProfilesGet,
  userEntityUsersGet,
  
  //user event
  userEventsGet                                              ,
  userEventAdd                                               ,
  userEventGet                                               ,
  userEventCountGet                                          ,
  
  //tag
  tagsAdd,                    
  tagsRemove,                 
  tagsGet,                    
  tagAdd,                     
  tagFrequsGet,
  tagEntitiesForTagsGet,
  
//  category
  categoriesPredefinedGet,
  categoriesPredefinedAdd,
  categoriesAdd,                                         
  categoriesRemove,                                         
  categoriesGet,
  categoryAdd,                                              
  categoryFrequsGet,
  categoryEntitiesForCategoriesGet,
  
  //search
  searchMIs                                           ,
  search,
  searchResultPagesCacheClean,
  
  //rating
  ratingGet                                           ,
  ratingOverallGet                                    ,
  ratingSet,
  ratingsRemove,
  ratingEntityURIsGet,
  
  //model ue
  modelUEUpdate                                       ,
  modelUETopicScores                                  ,
  modelUETopicRecent                                  ,
  modelUEEntitiesForMiGet                             ,
  modelUEResourcesContributed                         ,
  modelUEResourcesAll                                 ,
  modelUEResourceRecent                               ,
  modelUEResourceDetails                              ,
  modelUERelatedPersons                               ,
  modelUEModelRelations                               ,
  modelUEMIsForEntityGet                              ,
  modelUEEditors                                      ,
  modelUEAuthor                                       ,
  
  //lom
  lomExtractFromDir                                   ,
  
  //file
  fileUpload                                          ,
  fileDownload                                        ,
  fileIDFromURI                                       ,
  filesGet, 
  fileAdd,
  fileGet,
  
  //data import
  dataImportUserResourceTagFromWikipedia              ,
  dataImportAchso,
  dataImportSSSUsersFromCSVFile                       ,
  dataImportBitsAndPieces                             ,
  dataImportMediaWikiUser,
  
  //disc
  discRemove,
  discEntryAdd,
  discGet,
  discsGet,
  discEntryAccept,
  discTargetsAdd,
  discUpdate,
  discEntryUpdate,
  
  //broadcast
  broadcastServerTime                                 ,
  broadcastAdd                                     ,
  broadcastsGet                                       ,
  broadcastUpdate,
  
  //auth
  authUsersFromCSVFileAdd                             ,
  authRegisterUser,
  authCheckCred                                       ,
  authCheckKey                                        ,
  
  //coll
  collGet,
  collsGet,
  collsEntityIsInGet,
  collRootGet,
  collRootAdd,
  collParentGet,
  collEntryAdd,
  collEntriesAdd,
  collEntryDelete,
  collEntriesDelete,
  collHierarchyGet,
  collCumulatedTagsGet,
  
  //recomm
  recommTags,
  recommResources,
  recommUsers,
  recommLoadUserRealms,
  recommUpdate,
  recommUpdateBulkEntities,
  recommUpdateBulk,
  recommUpdateBulkUserRealmsFromConf,
  recommUpdateBulkUserRealmsFromCircles,
  
  //i5cloud
  i5CloudAuth,
  i5CloudAchsoVideoInformationGet,
  i5CloudFileUpload,
  i5CloudFileDownload,
  i5CloudAchsoSemanticAnnotationsSetGet,
  
  //activity
  activityAdd,
  activityContentAdd,
  activityContentsAdd,
  activitiesGet,
  activityTypesGet,
  
  //overlapping community detection - ocd
  ocdCreateGraph,
  ocdGetGraphs,
  ocdGetGraph,
  ocdDeleteGraph,
  ocdCreateCover,
  ocdGetAlgorithmNames,
  ocdGetCovers,
  ocdDeleteCover;
  
  public static SSServOpE get(final String value) throws Exception{
    
    try{
      return SSServOpE.valueOf(value);
    }catch(Exception error){
      throw new Exception("sss serv op '" + value + "' not defined: please add your op in SSOpE");
    }
  }
}