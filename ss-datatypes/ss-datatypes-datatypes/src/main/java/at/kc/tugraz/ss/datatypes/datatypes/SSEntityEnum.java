/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
 package at.kc.tugraz.ss.datatypes.datatypes;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.List;

public enum SSEntityEnum implements SSJSONLDPropI{
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
    final List<SSEntityEnum> types, 
    final SSEntityEnum       certainType){

    if(SSObjU.isNull(certainType)){
      return false;
    }
    
    for(SSEntityEnum type : types){
    
      if(SSStrU.equals(toStr(type), toStr(certainType))){
        return true;
      }
    }
    
    return false;
  }
  
  public static String toStr(final SSEntityEnum entityType){
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
  
  public static Boolean equals(final SSEntityEnum type1, final SSEntityEnum type2) {

    if(SSObjU.isNull(type1, type2)){
      return false;
    }

    return type1.toString().equals(type2.toString());
  }

  public static boolean isColl(final SSEntityEnum resourceType){
   
    if(resourceType == null){
      return false;
    }
    
    if(SSStrU.equals(resourceType.toString(), SSEntityEnum.coll.toString())){
      return true;
    }
    return false;
  }

  public static boolean isUser(final SSEntityEnum resourceType){
    
    if(resourceType == null){
      return false;
    }
    
    if(SSStrU.equals(SSEntityEnum.user.toString(), resourceType.toString())){
      return true;
    }
    return false;
  }

  public static boolean isResourceOrFile(final SSEntityEnum resourceType){
    
    if(SSObjU.isNull(resourceType)){
      return false;
    }
    
    if(
      SSStrU.equals (SSEntityEnum.file.toString(),   resourceType.toString()) || 
      SSStrU.equals (SSEntityEnum.entity.toString(), resourceType.toString())){
      return true;
    }
    return false;
  }

  public static boolean isDisc(final SSEntityEnum resourceType){
   
    if(SSObjU.isNull(resourceType)){
      return false;
    }
    
    if(SSStrU.equals(SSEntityEnum.disc.toString(), resourceType.toString())){
      return true;
    }
    return false;
  }
}