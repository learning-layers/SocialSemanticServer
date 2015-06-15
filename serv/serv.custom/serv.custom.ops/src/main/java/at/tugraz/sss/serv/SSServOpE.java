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
  
  //deprecated
  fileExtGet,
  
  //eval
  evalLog,
  
  //like
  likesUserGet,
  likesGet,
  likeUserSet,
  likeSet,
  
  //video
  videoUserGet,
  videosGet,
  videosUserGet,
  videoAdd,
  videoUserAdd,
  videoAnnotationAdd,
  videoUserAnnotationAdd,
  
  //friend
  friendsGet,
  friendsUserGet,
  friendAdd,
  friendUserAdd,
  
  //appStackLayout
  appStackLayoutsGet,
  appStackLayoutCreate,
  appStackLayoutDelete,
  appStackLayoutUpdate,
  
  //app
  appsGet,
  appAdd,
  
  //message
  messageSend,
  messagesGet,
  
  //comment
  commentsGet,
  commentsUserGet,
  commentEntitiesCommentedGet,
  
  //system
  systemVersionGet,
  
  //flag
  flagsGet,
  flagsUserGet,
  flagsSet,
  flagsUserSet,
  
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
  circleEntitiesGet,
  circleEntitiesAdd,
  circleUsersAdd,
  circleMostOpenCircleTypeGet,
  circleTypesGet,
  circleGet,
  circlesGet,
  circleEntityUsersGet,
  circlePrivURIGet,
  circlePubURIGet,
  circleUserCan,
  circleEntityShare,
  circlePrivEntityAdd,
  circlePubEntityAdd,
  circleEntityPublicSet,
  circleEntitiesRemove,
  
  //entity
  entitiesForLabelsAndDescriptionsGet,
  entitiesForLabelsGet,
  entitiesForDescriptionsGet,
  entityDescGet                                       ,
  entityDescsGet,
  entityAdd                                           ,
  entityGet                                           ,
  entityExists,
  entityUserSubEntitiesGet                            ,
  entityUserParentEntitiesGet,
  entityUserDirectlyAdjoinedEntitiesRemove            ,
  entityRemove                                        ,
  entityUserUpdate                                    ,
  entityThumbAdd,
  entityThumbsGet,
  entityFilesGet,
  entityFileAdd,
  entityUpdate,
  entityUserGet,
  entityUserEntitiesAttach,
  entityCopy,
  entityUserCopy,
  entityDirectlyAdjoinedEntitiesRemove,
  entityEntitiesAttachedGet,
  entityUserCommentsGet,
  entityReadGet,
  entityScreenShotsGet,
  entityDownloadURIsGet,
  entityLocationsAdd,
  entityLocationsGet,
  entityUserAdd,
  
  //learn ep
  learnEpsGet                                         ,
  learnEpVersionsGet                                  ,
  learnEpVersionGet                                   ,
  learnEpVersionCurrentGet                            ,
  learnEpVersionCurrentSet                            ,
  learnEpVersionCreate                                ,
  learnEpVersionAddCircle                             ,
  learnEpVersionAddEntity                             ,
  learnEpUserCopyForUser                              ,
  learnEpCreate                                       ,
  learnEpVersionUpdateCircle                          ,
  learnEpVersionUpdateEntity                          ,
  learnEpVersionRemoveCircle                          ,
  learnEpVersionRemoveEntity                          ,
  learnEpVersionSetTimelineState                      ,
  learnEpVersionGetTimelineState                      ,
  learnEpLockRemove,
  learnEpLockSet,
  learnEpLockHold,
  learnEpsLockHold,
  learnEpRemove,
  
  //data export
  dataExportUserRelations,
  dataExportUserEntityTagCategoryTimestamps           ,
  dataExportAddTagsCategoriesTimestampsForUserEntity,
  
  //json ld
  jsonLD                                              ,
  
  //user
  userURIGet,
  userAll                                             ,
  userExists                                          ,
  usersGet,
  userAdd,
  
  //user event
  uEsGet                                              ,
  uEAdd                                               ,
  uEsRemove,
  uEAddAtCreationTime                                 ,
  uEGet                                               ,
  uECountGet                                          ,
  
  //tag
  tagsAdd,                    
  tagsRemove,                 
  tagsGet,                    
  tagAdd,                     
  tagFrequsGet,
  tagEntitiesForTagsGet,
  tagEdit,
  
//  category
  categoriesPredefinedGet,
  categoriesPredefinedAdd,
  categoriesAdd,                                         
  categoriesRemove,                                         
  categoriesGet,
  categoryAdd,                                              
  categoryFrequsGet,
  categoryEntitiesForCategoriesGet,
  categoryEdit,
  
  //solr
  solrAddDoc                                          ,
  solrSearch                                          ,
  solrRemoveDoc                                       ,
  solrRemoveDocsAll                                   ,
  
  //search
  searchTags                                          ,
  searchSolr                                          ,
  searchMIs                                           ,
  searchTagsWithinEntity                              ,
  search,
  searchResultPagesCacheClean,
  
  //rating
  ratingUserSet                                       ,
  ratingUserGet                                       ,
  ratingOverallGet                                    ,
  ratingsUserRemove                                   ,
  ratingSet,
  
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
  fileUserFileWrites                                  ,
  fileUpload                                          ,
  fileUpdateWritingMinutes                            ,
  fileSetReaderOrWriter                               ,
  fileReplace                                         ,
  fileRemoveReaderOrWriter                            ,
  fileWritingMinutesLeft                              ,
  fileDownload                                        ,
  fileCanWrite                                        ,
  fileIDFromURI                                       ,
  fileThumbBase64Get,
  
  //data import
  dataImportUserResourceTagFromWikipedia              ,
  dataImportAchso,
  dataImportSSSUsersFromCSVFile                       ,
  dataImportEvernote                                  ,
  dataImportMediaWikiUser,
  
  //disc
  discURIsForTargetGet,
  discEntryURIsGet,
  discRemove,
  discEntryAdd,
  discWithEntriesGet,
  discsWithEntriesGet,
  discsAllGet,
  
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
  collUserRootGet                                     ,
  collUserParentGet                                   ,
  collUserEntryAdd                                    ,
  collUserWithEntries                                 ,
  collUserRootAdd                                     ,
  collUserEntryDelete                                 ,
  collUserEntryChangePos                              ,
  collUserEntriesAdd                                  ,
  collUserEntriesDelete                               ,
  collsUserWithEntries                                ,
  collsUserEntityIsInGet                              ,
  collUserHierarchyGet                                ,
  collUserCumulatedTagsGet                            ,
  collsUserCouldSubscribeGet                          ,
  collsEntityIsInGet,
  collsCouldSubscribeGet,
  collRootGet,
  collParentGet,
  collEntryAdd,
  collEntriesAdd,
  collEntryChangePos,
  collEntryDelete,
  collEntriesDelete,
  collWithEntries,
  collsWithEntries,
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
  activitiesUserGet,
  activityTypesGet,
  activityGet,
  
  //overlapping community detection - ocd
  ocdCreateGraph,
  ocdGetGraphs,
  ocdDeleteGraph,
  ocdCreateCover,
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