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

import java.util.ArrayList;
import java.util.List;

public enum SSEntityE implements SSJSONLDPropI{
  entity,
  coll,
  disc,
  qa,
  chat,
  discEntry,
  qaEntry,
  chatEntry,
	user,
	uploadedFile,
  file,
  rating,
  tag,
  tagFrequ,
  category,
  userEvent,
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
  flag,
  comment,
  video,
  videoAnnotation,
  message,
  app,
  image,
  appStackLayout,
  location,
  placeholder,
  livingDoc,
  mail;

//  entityDesc,
//  collDesc,
//  discDesc,
//  discEntryDesc, 
//  fileDesc, 
//  learnEpDesc,
//  learnEpVersionDesc,
//  learnEpTimelineStateDesc,
//  learnEpEntityDesc,
//  learnEpCircleDesc,
//  ratingDesc,
//  tagDesc,
//  categoryDesc,
//  userEventDesc,
//  userDesc, 
//  locationDesc;

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
    return SSVarNames.xsd + SSStrU.colon + SSStrU.valueString;
  }
  
  public static List<SSEntityE> get(final List<String> values) throws Exception{
  
    final List<SSEntityE> result = new ArrayList<>();
    
    if(values == null){
      return result;
    }
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
  
  public static SSEntityE get(final String value) throws Exception{
    
    try{
    
      if(value == null){
        return null;
      }
      
      return SSEntityE.valueOf(value);
    }catch(Exception error){
      throw new Exception("entity type not available: " + value);
    }
  }
  
  public static void addDistinctWithoutNull(
    final List<SSEntityE>     entities,
    final SSEntityE           entity){
    
    if(
      SSObjU.isNull  (entities, entity) ||
      SSStrU.contains(entities, entity)){
      return;
    }
    
    entities.add(entity);
  }
  
  public static void addDistinctWithoutNull(
    final List<SSEntityE>  entities,
    final List<SSEntityE>  toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSEntityE entity : toAddEntities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(entities, entity)){
        entities.add(entity);
      }
    }
  }
  
  //TODO rename to asListWithoutNull
  public static List<SSEntityE> asListWithoutNullAndEmpty(final SSEntityE... types){
   
    final List<SSEntityE> result = new ArrayList<>();
    
    if(types == null){
      return result;
    }
    
    for(SSEntityE type : types){
      
      if(type == null){
        continue;
      }
      
      result.add(type);
    }
    
    return result;
  }
  
  //TODO rename to asListWithoutNull
  public static List<SSEntityE> asListWithoutNullAndEmpty(final List<SSEntityE> types){
   
    final List<SSEntityE> result = new ArrayList<>();
    
    if(types == null){
      return result;
    }
    
    for(SSEntityE type : types){
      
      if(type == null){
        continue;
      }
      
      result.add(type);
    }
    
    return result;
  }
  
  public static Boolean isColl(final SSEntityE resourceType){
   
    if(resourceType == null){
      return false;
    }
    
    return SSStrU.equals(resourceType.toString(), SSEntityE.coll.toString());
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
  
  public static Boolean isUser(final SSEntityE resourceType){
    
    if(resourceType == null){
      return false;
    }
    
    return SSStrU.equals(SSEntityE.user.toString(), resourceType.toString());
  }
}