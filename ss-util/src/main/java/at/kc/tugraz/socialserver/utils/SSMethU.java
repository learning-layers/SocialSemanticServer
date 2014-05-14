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

  //cloud
  
  cloudPublishService,
  
  //voc
  vocURIPrefixGet,
  //db sql
//  dbSQLCloseCon,
//  dbSQLSelectCertainDistinctWhere,
//  dbSQLSelectAllWhere,
//  dbSQLCloseStmt,
//  dbSQLSelectCertainWhere,
//  dbSQLInsert,
//  dbSQLDeleteFromWhere,
//  dbSQLUpdateWhere,
//  dbSQLDeleteAll,
//  dbSQLSelectAll,
//  dbSQLSelectAllWhereOrderBy,
//  dbSQLStartTrans,
  
  //test
  testServOverall                                     ,
  testDataImportEvernote                              ,
  
  //evernote
  evernoteNoteStoreGet                                ,
  evernoteNotebooksGet                                ,
  evernoteNotebooksSharedGet                          ,
  evernoteNotebooksLinkedGet                          ,
  evernoteNotesGet                                    ,
  evernoteNotesLinkedGet                              ,
  
  //entity
  entityDescGet                                       ,
  entityAdd                                           ,
  entityAddAtCreationTime                             ,
  entityGet                                           ,
  entityExists,
  entitySearchWithKeywordWithin                       ,                         
  entityUserDirectlyAdjoinedEntitiesRemove            ,
  entityRemove                                        ,
  entityLabelSet                                      ,
  entityCirclePublicAdd,
  entityCircleURIPublicGet,
  entityUserCircleDelete,
  entityCircleCreate,
  entityEntitiesToCircleAdd,
  entityUsersToCircleAdd,
  entityUserCircleCreate,
  entityUserAllowedIs,
  entityUserCirclesGet,
  entityUserEntityCirclesGet,
  entityUserUsersToCircleAdd, 
  entityUserEntitiesToCircleAdd,
  entityCirclesGet,
  entityInSharedCircleIs,
  entityMostOpenCircleTypeGet,
  entityUserEntityCircleTypesGet,
  entityUserPublicSet,
  entityUserGet,
  entityUserShare,
  entityUserEntityUsersGet,
  
  //learn ep
  learnEpsGet                                         ,
  learnEpVersionsGet                                  ,
  learnEpVersionGet                                   ,
  learnEpVersionCurrentGet                            ,
  learnEpVersionCurrentSet                            ,
  learnEpVersionCreate                                ,
  learnEpVersionAddCircle                             ,
  learnEpVersionAddEntity                             ,
  learnEpCreate                                       ,
  learnEpVersionUpdateCircle                          ,
  learnEpVersionUpdateEntity                          ,
  learnEpVersionRemoveCircle                          ,
  learnEpVersionRemoveEntity                          ,
  learnEpVersionSetTimelineState                      ,
  learnEpVersionGetTimelineState                      ,
  
  //data export
  dataExportUserEntityTags                            ,
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
  
  //user event
  uEsGet                                              ,
  uEAdd                                               ,
  uEAddAtCreationTime                                 ,
  uEGet                                               ,
  
  //tag
  tagsAdd                                             ,
  tagsAddAtCreationTime                               ,
  tagsRemove                                          , 
  tagsUserRemove                                      ,
  tagsUserGet                                         ,
  tagUserEntitiesForTagGet                            ,
  tagUserFrequsGet                                    ,
  tagAdd                                              ,
  tagAddAtCreationTime                                ,
  
  //
  categorysAdd                                             ,
  categorysAddAtCreationTime                               ,
  categorysRemove                                          , 
  categorysUserRemove                                      ,
  categorysUserGet                                         ,
  categoryUserEntitiesForCategoryGet                       ,
  categoryUserFrequsGet                                    ,
  categoryAdd                                              ,
  categoryAddAtCreationTime                                ,
  
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
  
  //rating
  ratingUserSet                                       ,
  ratingUserGet                                       ,
  ratingOverallGet                                    ,
  ratingsUserRemove                                   ,
  
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
  
  //location
  locationAdd                                         ,
  locationsGet                                        ,
  locationsUserRemove                                 ,
  
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
  fileUriFromID                                       ,
  fileCreateUri                                       ,
  fileExtGet                                          ,
  fileThumbGet                                        , 
  
  //data import
  dataImportUserResourceTagFromWikipedia              ,
  dataImportAchso,
  dataImportSSSUsersFromCSVFile                       ,
  dataImportEvernote                                  ,
  dataImportMediaWikiUser,
  
  //disc
  discsUserAllGet                                     ,
  discUrisUserForTargetGet                            ,
  discUserWithEntriesGet                              ,
  discsUserWithEntriesGet                             ,
  discUserEntryAdd                                    ,
  discUserRemove                                      ,
  
  //broadcast
  broadcastServerTime                                 ,
  broadcastUpdate                                     ,
  broadcastUpdateTimeGet                              ,
  
  //auth
  authUsersFromCSVFileAdd                             ,
  authLoadKeys,
  authRegisterUser,
  authCheckCred                                       ,
  authCheckKey                                        ,
  
  
  //coll
  collSearchWithKeywordWithin                         , 
  collToCircleAdd                                     ,
  collUserRootGet                                     ,
  collUserParentGet                                   ,
  collUserEntryAdd                                    ,
  collUserWithEntries                                 ,
  collUserShareWithUser                               ,
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
  
  //recomm
  recommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestamp,
  recommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestampUpdate,
  recommTagsLanguageModelBasedOnUserEntityTag,
  recommTagsLanguageModelBasedOnUserEntityTagUpdate,
  recommTagsThreeLayersBasedOnUserEntityTagCategory,
  recommTagsThreeLayersBasedOnUserEntityTagCategoryUpdate,
  recommTagsThreeLayersBasedOnUserEntityTagCategoryTimestamp,
  recommTagsThreeLayersBasedOnUserEntityTagCategoryTimestampUpdate,
  
  //i5cloud
  i5CloudAuth,
  i5CloudAchsoVideoInformationGet,
  i5CloudFileUpload,
  i5CloudFileDownload,
  i5CloudAchsoSemanticAnnotationsSetGet,
  
  //activity
  activityAdd;
  
  
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
  
  public static SSMethU get(String value) throws Exception{
  
    try{
      return SSMethU.valueOf(value);
    }catch(Exception error){
      SSLogU.errThrow(new Exception("sss op not available"));
      return null;
    }
  }

  public static String toStr(SSMethU op){
    return SSStrU.toString(op);
  
  }
  public static Boolean equals(SSMethU op1, SSMethU op2){
    return op1 == op2;
  }
  
  private SSMethU(){}
}
