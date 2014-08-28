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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntity extends SSEntityA{

  public SSUri               id               = null; //entity
  public SSLabel             label            = null;
  public Long                creationTime     = null;
  public SSEntityE           type             = null;
  public SSUri               author           = null;
  public SSTextComment       description      = null;
  public List<SSCircleE>     circleTypes      = new ArrayList<>();
  public List<Object>        entries          = new ArrayList<>();
  public List<SSEntity>      attachedEntities = new ArrayList<>();
  public List<SSTextComment> comments         = new ArrayList<>();
  public SSEntityA           overallRating    = null; //new
  public List<String>        tags             = new ArrayList<>(); //new
  public List<SSEntityA>     discs            = new ArrayList<>(); //new
  public List<SSEntityA>     uEs              = new ArrayList<>(); //new
  public String              thumb            = null; //new
  public SSUri               file             = null; //new
  public List<SSEntityA>     flags            = new ArrayList<>(); //new
  
  protected SSEntity(
    final SSEntity entity) throws Exception{
    
    super(entity.id);
    
    this.id               = entity.id; //entity
    this.label            = entity.label;
    this.creationTime     = entity.creationTime;
    this.type             = entity.type;
    this.author           = entity.author;
    this.description      = entity.description;
    this.overallRating    = entity.overallRating; //new
    this.tags             = entity.tags; //new
    this.discs            = entity.discs; //new
    this.uEs              = entity.uEs; //new
    this.thumb            = entity.thumb; //new
    this.file             = entity.file; //new
    this.flags            = entity.flags; //new
    this.circleTypes      = entity.circleTypes;
    this.entries          = entity.entries;
    this.attachedEntities = entity.attachedEntities;
    this.comments         = entity.comments;
  }
  
  protected SSEntity(
    final SSUri                  id,
    final SSLabel                label,
    final Long                   creationTime,
    final SSEntityE              type,
    final SSUri                  author,
    final SSTextComment          description,
    final List<SSCircleE>        circleTypes,
    final List<? extends Object> entries,
    final List<SSEntity>         attachedEntities,
    final List<SSTextComment>    comments,
    final SSEntityA              overallRating, //new
    final List<String>           tags, //new
    final List<SSEntityA>        discs,//new
    final List<SSEntityA>        uEs,  //new
    final String                 thumb,//new
    final SSUri                  file, //new
    final List<SSEntityA>        flags) throws Exception{ //new
    
    super(id);
    
    this.id            = id;
    this.label         = label;
    this.creationTime  = creationTime;
    this.type          = type;
    this.author        = author;
    this.description   = description;
    this.overallRating = overallRating;
    this.thumb         = thumb;
    this.file          = file;
    
    if(circleTypes != null){
      this.circleTypes.addAll(circleTypes);
    }
    
    if(entries != null){
      this.entries.addAll(entries);
    }
    
    if(attachedEntities != null){
      this.attachedEntities.addAll(attachedEntities);
    }
    
    if(comments != null){
      this.comments.addAll(comments);
    }
    
    if(tags != null){
      this.tags.addAll(tags);
    }
    
    if(discs != null){
      this.discs.addAll(discs);
    }
    
    if(uEs != null){
      this.uEs.addAll(uEs);
    }
    
    if(flags != null){
      this.flags.addAll(flags);
    }
  }
  
  public static SSEntity get(
    final SSUri                      id,
    final SSLabel                    label, 
    final Long                       creationTime,
    final SSEntityE                  type,
    final SSUri                      author,
    final SSTextComment              description,
    final List<SSCircleE>            circleTypes,
    final List<? extends Object>     entries,
    final List<SSEntity>             attachedEntities,
    final List<SSTextComment>        comments,
    final SSEntityA                  overallRating, //new
    final List<String>               tags, //new
    final List<SSEntityA>            discs,//new
    final List<SSEntityA>            uEs,  //new
    final String                     thumb,//new
    final SSUri                      file, //new
    final List<SSEntityA>            flags) throws Exception{
    
    return new SSEntity(
      id, 
      label, 
      creationTime, 
      type, 
      author, 
      description,
      circleTypes,
      entries,
      attachedEntities,
      comments,
      overallRating, //new
      tags, //new
      discs,//new
      uEs,  //new
      thumb,//new
      file, //new
      flags);
  }

  @Override
  public Object jsonLDDesc() {
   
    final Map<String, Object> ld                  = new HashMap<>();
    final Map<String, Object> circleTypesObj      = new HashMap<>();
    final Map<String, Object> entriesObj          = new HashMap<>();
    final Map<String, Object> attachedEntitiesObj = new HashMap<>();
    final Map<String, Object> commentsObj         = new HashMap<>();
    final Map<String, Object> tagsObj             = new HashMap<>();
    final Map<String, Object> discsObj            = new HashMap<>();
    final Map<String, Object> uEsObj              = new HashMap<>();
    final Map<String, Object> flagsObj            = new HashMap<>();
    
    ld.put(SSVarU.id,             SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.entity,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,          SSVarU.sss + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.creationTime,   SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.type,           SSVarU.sss + SSStrU.colon + SSEntityE.class.getName());
    ld.put(SSVarU.author,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.description,    SSVarU.sss + SSStrU.colon + SSTextComment.class.getName());
    ld.put(SSVarU.overallRating,  SSVarU.sss + SSStrU.colon + SSEntityA.class.getName());
    ld.put(SSVarU.thumb,          SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.file,           SSVarU.sss + SSStrU.colon + SSUri.class.getName());
   
    circleTypesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCircleE.class.getName());
    circleTypesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circleTypes, circleTypesObj);
    
    entriesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + Object.class.getName());
    entriesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entries, entriesObj);
    
    attachedEntitiesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntity.class.getName());
    attachedEntitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.attachedEntities, attachedEntitiesObj);
    
    commentsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSTextComment.class.getName());
    commentsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.comments, commentsObj);
    
    flagsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityA.class.getName());
    flagsObj.put(SSJSONLDU.container, SSJSONLDU.set);
      
    ld.put(SSVarU.flags,      flagsObj);
      
    tagsObj.put(SSJSONLDU.id,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    tagsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.tags,      tagsObj);
    
    discsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityA.class.getName());
    discsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.discs,      discsObj);
    
    uEsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityA.class.getName());
    uEsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.uEs,      uEsObj);
    
    return ld;
  }

  /* getters for json */
  
  public String getId() throws Exception{
    return SSStrU.removeTrailingSlash(id);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public Long getCreationTime(){
    return creationTime;
  }

  public SSEntityE getType(){
    return type;
  }
  
  public String getAuthor() throws Exception{
    return SSStrU.removeTrailingSlash(author);
  }
  
  public String getDescription() throws Exception{
    return SSStrU.toStr(description);
  }
  
  public List<SSCircleE> getCircleTypes(){
    return circleTypes;
  }
  
  public List<? extends Object> getEntries() throws Exception{
    return entries;
  }
  
  public List<SSEntity> getAttachedEntities() throws Exception{
    return attachedEntities;
  }
  
  public List<String> getComments() throws Exception{
    return SSStrU.toStr(comments);
  }
  
  public List<String> getTags(){
    return tags;
  }

  public SSEntityA getOverallRating(){
    return overallRating;
  }

  public List<String> getDiscs() throws Exception{
    return SSStrU.removeTrailingSlash(discs);
  }
  
  public List<SSEntityA> getuEs() throws Exception{
    return uEs;
  }
  
  public String getThumb(){
    return thumb;
  }
  
  public String getFile(){
    return SSStrU.removeTrailingSlash(file);
  }
  
  public List<SSEntityA> getFlags() throws Exception{
    return flags;
  }
}