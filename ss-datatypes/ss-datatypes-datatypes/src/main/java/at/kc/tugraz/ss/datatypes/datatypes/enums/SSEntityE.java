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
 package at.kc.tugraz.ss.datatypes.datatypes.enums;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.List;

public enum SSEntityE implements SSJSONLDPropI{
  entity,
  coll,
  disc,
  discEntry,
	user,
	file,
  rating,
  tag,
  userEvent,
  location,
  learnEp,
  learnEpTimelineState,
  learnEpVersion,
  learnEpCircle,
  learnEpEntity,
  evernoteNotebook,
  evernoteNote,
  evernoteResource,
  circle,
  activity,

  entityDesc,
  collDesc,
  discDesc,
  discEntryDesc, 
  fileDesc, 
  learnEpDesc,
  learnEpVersionDesc,
  learnEpTimelineStateDesc,
  learnEpEntityDesc,
  learnEpCircleDesc,
  ratingDesc,
  tagDesc,
  userEventDesc,
  userDesc, 
  locationDesc;

  public static Boolean contains(
    final List<SSEntityE> types, 
    final SSEntityE       certainType){

    if(SSObjU.isNull(certainType)){
      return false;
    }
    
    for(SSEntityE type : types){
    
      if(SSStrU.equals(toStr(type), toStr(certainType))){
        return true;
      }
    }
    
    return false;
  }
  
  public static String toStr(final SSEntityE entityType){
    return SSStrU.toString(entityType);
  }
  
//    socialServer("at.tug.kc.socialServer"),
//  tagSet("tagSet"),
//  position("pos"),
  
  //RESOURCE("resource")
  //TYPE_CONTEXT("context"),
  //HISTORY   ("_history"),
  //PROFILE   ("_profile"),
  //FAVORITES ("_favorites"),
  //EVENTS    ("_events"),
  
  @Override
  public Object jsonLDDesc(){
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
  
  public static Boolean equals(final SSEntityE type1, final SSEntityE type2) {

    if(SSObjU.isNull(type1, type2)){
      return false;
    }

    return type1.toString().equals(type2.toString());
  }

  public static Boolean isColl(final SSEntityE resourceType){
   
    if(resourceType == null){
      return false;
    }
    
    return SSStrU.equals(resourceType.toString(), SSEntityE.coll.toString());
  }

  public static Boolean isUser(final SSEntityE resourceType){
    
    if(resourceType == null){
      return false;
    }
    
    return SSStrU.equals(SSEntityE.user.toString(), resourceType.toString());
  }

  public static Boolean isResourceOrFile(final SSEntityE resourceType){
    
    if(resourceType == null){
      return false;
    }
    
    return 
      SSStrU.equals (SSEntityE.file.toString(),   resourceType.toString()) || 
      SSStrU.equals (SSEntityE.entity.toString(), resourceType.toString());
  }

  public static Boolean isDisc(final SSEntityE resourceType){
   
    if(resourceType == null){
      return false;
    }
    
    return SSStrU.equals(SSEntityE.disc.toString(), resourceType.toString());
  }
}