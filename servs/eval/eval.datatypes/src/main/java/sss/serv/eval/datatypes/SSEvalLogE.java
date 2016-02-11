/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package sss.serv.eval.datatypes;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import java.util.ArrayList;
import java.util.List;

public enum SSEvalLogE{
  
  //old
  tagAdd,
  shareLearnEpWithUser,

  search,
  changeDiscContent,
  changeDiscEntryContent,
  read,
  recommTags,
  recommResources,
  addCategory,
  removeCategory,
  createVideo,
  removeVideo,
  createVideoAnnotation,
  removeVideoAnnotation,
  createLivingDoc,
  removeLivingDoc,
  createApp,
  removeApp,
  createAppStackLayout,
  removeAppStackLayout,
  updateAppStackLayoutApp,
  uploadFile,
  createCircle,
  addCircleEntities,
  addCircleUsers,
  removeCircle,
  removeCircleUsers,
  removeCircleEntities,
  shareEntityWithUsers,
  shareEntityWithCircles,
  addNotebook, 
  addNote, 
  addResource, 
  copyEntity,
  copyLearnEpForUser, 
  removeLearnEpVersionCircle, 
  removeLearnEpVersionEntity, 
  addEntityToLearnEpVersion, 
  addCircleToLearnEpVersion, 
  addEntityToLearnEpCircle, 
  removeEntityFromLearnEpCircle, 
  removeLearnEpVersionCircleWithEntitites, 
  addDiscEntry, 
  createDisc,
  addDiscTargets,
  attachEntities,
  removeEntities, 
  createLearnEp,
  removeLearnEp, 
  downloadEntity, 
  createPlaceholder, 
  addTag, 
  removeTag, 
  changeLabel,   
  changeDescription,
  clickBit,  
  likeEntity,
  dislikeEntity,
  clickTag,  
  clickLabelRecommendation,   
  clickTagRecommendation, 
  clickJumpToDateButton,   
  clickAffectButton, 
  clickHelpButton,  
  readMessage, 
  sendMessage, 
  setImportance, 
  setFilter, 
  removeFilter, 
  executeJumpToDateButton, 
  requestEditButton,  
  releaseEditButton, 
  openDiscussionTool, 
  openBitsAndPieces, 
  openLivingDocuments, 
  closeDiscussionTool, 
  startDiscussionTool, 
  startBitsAndPieces, 
  worksInBitsAndPieces, 
  worksInDiscussionTool, 
  addLearnEpVersionEntityFromRecommendedEntities, 
  ;
  
  public static SSEvalLogE get(final String space) throws SSErr{
    
    try{
      
      if(space == null){
        return null;
      }
      
      return SSEvalLogE.valueOf(space);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static List<SSEvalLogE> get(final List<String> strings) throws SSErr{

    final List<SSEvalLogE> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static void addDistinctNotNull(
    final List<SSEvalLogE>  logTypes,
    final SSEvalLogE        logType){
    
    if(SSObjU.isNull(logTypes, logType)){
      return;
    }
    
    if(!SSStrU.contains(logTypes, logType)){
      logTypes.add(logType);
    }
  }
}