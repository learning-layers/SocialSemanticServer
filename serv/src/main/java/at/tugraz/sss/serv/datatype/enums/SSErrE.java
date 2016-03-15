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
package at.tugraz.sss.serv.datatype.enums;

public enum SSErrE{

  //mediaWiki
  mediaWikiUserLoginSecondTimeFailed, 
  
  //recomm
  recommSystemRealmNotLoaded,
  
  //solr, 
  solrKeywordInvald,
  
  //file
  fileExtInvalid,
  fileExtInvalidForMimeType,
  
  mimeTypeInvalid,
  mimeTypeInvalidForFileExt,
  
  //service
  servNotRunning,
  servImplCreationFailed,
  servInvalid,
  servContainerHasNoServerInterface,
  maxNumClientConsForOpReached,       
    
  //sql
  sqlDeadLock,
  sqlDefaultErr,
  mySQLConnectionFailed,
  mySQLGetConnectionFromPoolFailed,
  
  //error
  clientErrorCreationFailed,

  //default
  defaultErr,
  
  //tag
  tagLabelInvalid,
  
  //category
  categoryLabelInvalid, 
  
  //entity
  uriInvalid,
  spaceInvalid,
  entityTypeInvalid,
  entityDoesNotExist,
  
  //activity
  activityContentTypeInvalid,
  activityContentInvalid,
  
  //collection
  cannotSetSpecialCollectionPublic,
  cannotShareSpecialCollection,
  userDoesntOwnColl,
  
  //circle
  cannotShareWithPublicCircle,
  notAllowedToCreateCircle,
  userDoesntHaveRightInAnyCircleOfEntity,
  circleDoesntHaveQueriedRight,
  userCannotShareWithHimself,
  
  //user //auth
  userAlreadyExists,
  userNotRegistered,
  userKeyWrong,
  userNeedsLockOnEntity,
  userIsNotInCircle,
  authCouldntConnectToOIDC,
  authCouldntParseOIDCUserInfoResponse,
  authOIDCUserInfoRequestFailed,
  authNoUserForKey,
  userNotAllowToAccessSystemCircle,
  userCannotAddUser,
  
  parameterMissing,
  restAdapterInternalError,
  
  realmIncorrectForUser,
  
  sssConnectionFailed,
  sssJsonRequestEncodingFailed,
  sssWriteFailed,
  sssReadFailed,
  sssResponseParsingFailed,
  sssResponseFailed,
  queryPageInvalid,
  queryResultOutDated,
  
  servAlreadyRegistered,
  
  entityTypeNotSupported,
  entityCouldntBeQueried,
  learnEpCurrentVersionNotSet,
  userNotAllowedToAccessEntity,           //user is not allowed to access requested entity
  userNotAllowedToRetrieveForOtherUser,   //user is not allowed to access entity for other user
  maxNumDBConsReached,                    //number of possible database connections nearly reached; please try again later
  codeUnreachable;                        //code not reachable reached
}