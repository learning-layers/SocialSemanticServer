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

public enum SSErrE{

  //service
  servNotRunning,
  servImplCreationFailed,
  
  //sql
  sqlNoResultFound,
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
  entityCreationFailedOnNull,
  spaceInvalid,
  entityTypeInvalid,
  
  //activity
  activityContentTypeInvalid,
  
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
  queryPageUnavailable,
  queryResultOutDated,
  
  servAlreadyRegistered,
  
  entityTypeNotSupported,
  entityCouldntBeQueried,
  learnEpCurrentVersionNotSet,
  deployingServiceOnNodeFailed,           //could not deploy requested service on new node
  noClientServiceForOpAvailableOnNodes,   //no service found on nodes to handle client op
  servServerOpNotAvailable,         //no service found on machine to handle server op
  noClientServiceForOpAvailableOnMachine, //no service found on machine to handle client op
  userNotAllowedToAccessEntity,           //user is not allowed to access requested entity
  userNotAllowedToRetrieveForOtherUser,   //user is not allowed to access entity for other user
  maxNumDBConsReached,                    //number of possible database connections nearly reached; please try again later
  maxNumClientConsForOpReached,           //"number of possible client side connections for op :" + op + "reached; please try again later"
  codeUnreachable;                        //code not reachable reached
}