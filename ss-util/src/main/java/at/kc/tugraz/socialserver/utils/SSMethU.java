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
package at.kc.tugraz.socialserver.utils;

public enum SSMethU{

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
  appStackLayoutTileAdd,
  
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
  
  //voc
  vocURIPrefixGet,
  
  //test
  testServOverall                                     ,
  
  //evernote
  evernoteNoteStoreGet                                ,
  evernoteNotebooksGet                                ,
  evernoteNotebooksSharedGet                          ,
  evernoteNotebooksLinkedGet                          ,
  evernoteNotesGet                                    ,
  evernoteNotesLinkedGet                              ,
  
  //entity
  entitiesUserGetNew,
  entityUserGetNew,
  entityEntityCirclesGet,
  entityUserCircleGet,
  entityCircleGet,
  entitiesForLabelsAndDescriptionsGet,
  entitiesForLabelsGet,
  entitiesForDescriptionsGet,
  entityPublicSet,
  entityDescGet                                       ,
  entityDescsGet ,
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
  entityUpdate ,
  entityCircleURIPrivGet,
  entityCircleURIPubGet,
  entityUserCircleDelete,
  entityCircleCreate,
  entityEntitiesToCircleAdd,
  entityUsersToCircleAdd,
  entityUserCircleCreate,
  entityUserCan,
  entityUserCirclesGet,
  entityEntityUsersGet,
  entityUserEntityCirclesGet,
  entityUserUsersToCircleAdd, 
  entityUserEntitiesToCircleAdd,
  entityCirclesGet,
  entityInSharedCircleIs,
  entityMostOpenCircleTypeGet,
  entityUserEntityCircleTypesGet,
  entityUserEntityMostOpenCircleTypeGet,
  entityUserPublicSet,
  entityUserGet,
  entityUserShare,
  entityUserCopy,
  entityUserEntitiesAttach,
  entityEntityToPrivCircleAdd,
  entityEntityToPubCircleAdd,
  entityCopy, 
  entityUserEntityUsersGet,
  entityDirectlyAdjoinedEntitiesRemove,
  entityShare,
  entityEntitiesAttachedGet,  
  entityUserCommentsGet,
  entityReadGet,
  entityScreenShotsGet,
  entityDownloadURIsGet,
  entityLocationsAdd,
  entityLocationsGet,
  
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
  learnEpUserShareWithUser                            ,
  learnEpCreate                                       ,
  learnEpVersionUpdateCircle                          ,
  learnEpVersionUpdateEntity                          ,
  learnEpVersionRemoveCircle                          ,
  learnEpVersionRemoveEntity                          ,
  learnEpVersionSetTimelineState                      ,
  learnEpVersionGetTimelineState                      ,
  
  //data export
  dataExportUserEntityTags                            ,
  dataExportUserRelations,
  dataExportUserEntityTagTimestamps                   ,
  dataExportUserEntityTagCategories                   ,
  dataExportUserEntityTagCategoryTimestamps           ,
  
  //file sys
  fileSysLocalFormatAudioAndVideoFileNamesInDir       ,
  fileSysLocalAddTextToFilesNamesAtBeginInDir         ,
  
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
  tagsAdd                                            ,
  tagsRemove                                          , 
  tagsUserRemove                                      ,
  tagsUserGet                                         ,
  tagUserFrequsGet                                    ,
  tagsGet,
  tagAdd                                              ,
  tagFrequsGet,
  tagUserEntitiesForTagsGet,
  tagEntitiesForTagsGet,
  tagEdit,
  tagUserEdit,
  
//  category
  categoriesPredefinedGet,
  categoriesPredefinedAdd,
  categoriesAdd                                            ,
  categoriesRemove                                          ,
  categoriesUserRemove                                      ,
  categoriesUserGet                                         ,
  categoryUserFrequsGet                                    ,
  categoriesGet,
  categoryAdd                                              ,
  categoryFrequsGet,
  categoryUserEntitiesForCategoriesGet,
  categoryEntitiesForCategoriesGet,
  categoryEdit,
  categoryUserEdit,
  
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
  
  //scaff
  scaffRecommTagsBasedOnUserEntityTag                 , //cikm | 3Layers mit den Kategorien 
  scaffRecommTagsBasedOnUserEntityTagTime             , //umap | 3LT (3Layers mit Zeit)
  scaffRecommTagsBasedOnUserEntityTagCategory         , //wbsc | BaseLevelLearning und MostPopular/LanguageModel
  scaffRecommTagsBasedOnUserEntityTagCategoryTime     ,
  
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
  fileExtGet                                          ,
  
  //data import
  dataImportUserResourceTagFromWikipedia              ,
  dataImportAchso,
  dataImportSSSUsersFromCSVFile                       ,
  dataImportEvernote                                  ,
  dataImportMediaWikiUser,
  
  //disc
  discsUserAllGet                                     ,
  discURIsForTargetGet,
  discUserDiscURIsForTargetGet                        ,
  discEntryURIsGet,
  discUserWithEntriesGet                              ,
  discsUserWithEntriesGet                             ,
  discUserEntryAdd                                    ,
  discUserRemove                                      ,
  discRemove,
  discEntryAdd,
  discWithEntriesGet,
  discsAllGet,
  
  //broadcast
  broadcastServerTime                                 ,
  broadcastUpdate                                     ,
  broadcastUpdateTimeGet                              ,
  
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
  collUserSetPublic                                   ,
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
  recommTagsUpdate,
  recommResourcesUpdate,
  
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
  activityTypesGet;
  
  
//  recommTagsCollaborativeFilteringOnUserSimilarity,
//  recommTagsCollaborativeFilteringOnEntitySimilarity,
//  recommTagsAdaptedPageRank,
//  recommTagsThreeLayersWithTime,
//  recommTagsTemporalUsagePatterns,
//  recommTagsFolkRank,
//  recommTagsLDA;
//  recommTags                                          ,
//  recommCreateLDASamples                              ,
  //  recommWriteMetricsMulan                        ,
  //  recommCalcMulan                                ,
  //  recommCreateLanguageModelSamples               ,
  //  recommCalcLanguageModel                        ,
  //  recommCalcLDAModel                             ,
  //  recommCalcAct                                  ,
  //  recommCalcFolkRank                             ,
  //  recommCalcCFTAG                                ,
//  recommCalcLDA                                       ,
//  recommCalcThreeLayers                               ,
//  recommSplitSample                                   ,
//  recommWriteMetrics                                  ,
//  recommTrainTestSize                                 ,
//  recommBetaValues                                    
  
  public static SSMethU get(final String value) throws Exception{
  
    try{
      return SSMethU.valueOf(value);
    }catch(Exception error){
      SSLogU.errThrow(new Exception("sss op " + value + " not available"));
      return null;
    }
  }

  public static String toStr(SSMethU op){
    return SSStrU.toStr(op);
  
  }
  public static Boolean equals(SSMethU op1, SSMethU op2){
    return op1 == op2;
  }
  
  private SSMethU(){}
}
