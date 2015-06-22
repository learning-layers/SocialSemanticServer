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

import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntity extends SSEntityA{
  
  @ApiModelProperty(
    required = false,
    value = "uri")
  public SSUri               id               = null; //entity
  
  @ApiModelProperty(
    required = false,
    value = "name")
  public SSLabel             label            = null;
  
  @ApiModelProperty(
    required = false,
    value = "creation / timestamp in milliseconds")
  public Long                creationTime     = null;
  
  @ApiModelProperty(
    required = false,
    value = "sss entity data type")
  public SSEntityE           type             = null;
  
  @ApiModelProperty(
    required = false,
    value = "creator")
  public SSAuthor               author           = null;
  
  @ApiModelProperty(
    required = false,
    value = "textual description")
  public SSTextComment       description      = null;
  
  @ApiModelProperty(
    required = false,
    value = "types of circles the entity is in")
  public List<SSCircleE>     circleTypes      = new ArrayList<>();
  
  @ApiModelProperty(
    required = false,
    value = "entities sub-entities")
  public List<Object>        entries          = new ArrayList<>();
  
  @ApiModelProperty(
    required = false,
    value = "attached entities")
  public List<SSEntity>      attachedEntities = new ArrayList<>();
  
  @ApiModelProperty(
    required = false,
    value = "comments given by users")
  public List<SSTextComment> comments         = new ArrayList<>();
  
  @ApiModelProperty(
    required = false,
    value = "overall star rating")
  public SSEntityA           overallRating    = null; //new
  
  @ApiModelProperty(
    required = false,
    value = "tags assigned")
  public List<String>        tags             = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false,
    value = "discussions about")
  public List<SSEntityA>     discs            = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false,
    value = "user events")
  public List<SSEntityA>     uEs              = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false,
    value = "thumbnail")
  public String              thumb            = null; //new
  
  @ApiModelProperty(
    required = false,
    value = "physical file")
  public SSUri               file             = null; //new
  
  @ApiModelProperty(
    required = false,
    value = "flags assigned")
  public List<SSEntityA>     flags            = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false,
    value = "users involved")
  public List<SSEntity>         users        = new ArrayList<>();
  
  @ApiModelProperty(
    required = false,
    value = "entities involved")
  public List<SSEntity>         entities     = new ArrayList<>();
  
  @ApiModelProperty(
    required = false,
    value = "circles")
  public List<SSEntity>       circles      = new ArrayList<>();
  
  @ApiModelProperty(
    required = false,
    value = "locations")
  public List<SSEntity>       locations    = new ArrayList<>();
  
  @ApiModelProperty(
    required = false,
    value = "whether user has read the entry")
  public  Boolean       read;
  
  @ApiModelProperty(
    required = false,
    value = "likes for the entity")
  public  SSEntityA       likes;
  
  public static SSEntity get(
    final SSUri     id,
    final SSEntityE type) throws Exception{
    
    return new SSEntity(id, type);
  }
  
  public static List<SSEntity> get(
    final List<SSUri> ids,
    final SSEntityE   type) throws Exception{
    
    final List<SSEntity> entities = new ArrayList<>();
    
    for(SSUri id : ids){
      entities.add(new SSEntity(id, type));
    }
    
    return entities;
  }
  
  public static SSEntity get(
    final SSUri     id,
    final SSEntityE type,
    final SSLabel   label) throws Exception{
    
    return new SSEntity(id, type, label);
  }
  
  protected SSEntity(
    final SSUri     id,
    final SSEntityE type) throws Exception{
    
    super(id);
    
    this.id    = id;
    this.type  = type;
  }
  
  protected SSEntity(
    final SSUri     id,
    final SSEntityE type,
    final SSLabel   label) throws Exception{
    
    super(id);
    
    this.id    = id;
    this.type  = type;
    this.label = label;
  }
  
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
    this.users            = entity.users;
    this.entities         = entity.entities;
    this.read             = entity.read;
    this.circles          = entity.circles;
    this.locations        = entity.locations;
    this.likes            = entity.likes;
  }
  
  @Override //TODO has to be fixed and adapted
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
    final Map<String, Object> entitiesObj         = new HashMap<>();
    final Map<String, Object> circlesObj          = new HashMap<>();
    final Map<String, Object> usersObj            = new HashMap<>();
    final Map<String, Object> locationsObj        = new HashMap<>();
    
    ld.put(SSVarNames.id,             SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.entity,         SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.label,          SSVarNames.sss + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarNames.creationTime,   SSVarNames.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarNames.type,           SSVarNames.sss + SSStrU.colon + SSEntityE.class.getName());
    ld.put(SSVarNames.author,         SSVarNames.sss + SSStrU.colon + SSAuthor.class.getName());
    ld.put(SSVarNames.description,    SSVarNames.sss + SSStrU.colon + SSTextComment.class.getName());
    ld.put(SSVarNames.overallRating,  SSVarNames.sss + SSStrU.colon + SSEntityA.class.getName());
    ld.put(SSVarNames.thumb,          SSVarNames.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarNames.file,           SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.read,           SSVarNames.xsd + SSStrU.colon + SSStrU.valueBoolean);
    ld.put(SSVarNames.likes,          SSVarNames.sss + SSStrU.colon + SSEntityA.class.getName());
   
    entriesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + Object.class.getName());
    entriesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.entries, entriesObj);
    
    attachedEntitiesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSEntity.class.getName());
    attachedEntitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.attachedEntities, attachedEntitiesObj);
    
    commentsObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSTextComment.class.getName());
    commentsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.comments, commentsObj);
    
    flagsObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSEntityA.class.getName());
    flagsObj.put(SSJSONLDU.container, SSJSONLDU.set);
      
    ld.put(SSVarNames.flags,      flagsObj);
      
    tagsObj.put(SSJSONLDU.id,        SSVarNames.xsd + SSStrU.colon + SSStrU.valueString);
    tagsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarNames.tags,      tagsObj);
    
    discsObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSEntityA.class.getName());
    discsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarNames.discs,      discsObj);
    
    uEsObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSEntityA.class.getName());
    uEsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarNames.uEs,      uEsObj);
    
    usersObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSEntity.class.getName());
    usersObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.users, usersObj);
    
    entitiesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    entitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.entities, entitiesObj);
    
    circlesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSEntity.class.getName());
    circlesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.circles, circlesObj);
    
    circleTypesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSCircleE.class.getName());
    circleTypesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.circleTypes, circleTypesObj);
    
    locationsObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSEntity.class.getName());
    locationsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.locations, locationsObj);
    
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
  
  public SSAuthor getAuthor() throws Exception{
    return author;
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
  
  public List<? extends SSEntity> getAttachedEntities() throws Exception{
    return attachedEntities;
  }
  
  public List<? extends SSEntity> getCircles() throws Exception{
    return circles;
  }
  
  public List<? extends SSEntity> getLocations() throws Exception{
    return locations;
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

  public SSEntityA getLikes(){
    return likes;
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
  
  public List<? extends SSEntity> getUsers() throws Exception{
    return users;
  }

  public List<? extends SSEntity> getEntities() throws Exception{
    return entities;
  }
}


//public static SSEntity get(
//    final SSUri                      id,
//    final SSLabel                    label, 
//    final Long                       creationTime,
//    final SSEntityE                  type,
//    final SSUri                      author,
//    final SSTextComment              description,
//    final List<SSCircleE>            circleTypes,
//    final List<? extends Object>     entries,
//    final List<SSEntity>             attachedEntities,
//    final List<SSTextComment>        comments,
//    final SSEntityA                  overallRating, //new
//    final List<String>               tags, //new
//    final List<SSEntityA>            discs,//new
//    final List<SSEntityA>            uEs,  //new
//    final String                     thumb,//new
//    final SSUri                      file, //new
//    final List<SSEntityA>            flags) throws Exception{
//    
//    return new SSEntity(
//      id, 
//      label, 
//      creationTime, 
//      type, 
//      author, 
//      description,
//      circleTypes,
//      entries,
//      attachedEntities,
//      comments,
//      overallRating, //new
//      tags, //new
//      discs,//new
//      uEs,  //new
//      thumb,//new
//      file, //new
//      flags);
//  }

//protected SSEntity(
//    final SSUri                  id,
//    final SSLabel                label,
//    final Long                   creationTime,
//    final SSEntityE              type,
//    final SSUri                  author,
//    final SSTextComment          description,
//    final List<SSCircleE>        circleTypes,
//    final List<? extends Object> entries,
//    final List<SSEntity>         attachedEntities,
//    final List<SSTextComment>    comments,
//    final SSEntityA              overallRating, //new
//    final List<String>           tags, //new
//    final List<SSEntityA>        discs,//new
//    final List<SSEntityA>        uEs,  //new
//    final String                 thumb,//new
//    final SSUri                  file, //new
//    final List<SSEntityA>        flags) throws Exception{ //new
//    
//    super(id);
//    
//    this.id            = id;
//    this.label         = label;
//    this.creationTime  = creationTime;
//    this.type          = type;
//    this.author        = author;
//    this.description   = description;
//    this.overallRating = overallRating;
//    this.thumb         = thumb;
//    this.file          = file;
//    
//    if(circleTypes != null){
//      this.circleTypes.addAll(circleTypes);
//    }
//    
//    if(entries != null){
//      this.entries.addAll(entries);
//    }
//    
//    if(attachedEntities != null){
//      this.attachedEntities.addAll(attachedEntities);
//    }
//    
//    if(comments != null){
//      this.comments.addAll(comments);
//    }
//    
//    if(tags != null){
//      this.tags.addAll(tags);
//    }
//    
//    if(discs != null){
//      this.discs.addAll(discs);
//    }
//    
//    if(uEs != null){
//      this.uEs.addAll(uEs);
//    }
//    
//    if(flags != null){
//      this.flags.addAll(flags);
//    }
//  }