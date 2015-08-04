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
import java.util.List;

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
  public List<SSEntity>       entries          = new ArrayList<>();
  
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
  public List<SSEntity>        tags             = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false,
    value = "categories assigned")
  public List<SSEntity>        categories     = new ArrayList<>();
  
  @ApiModelProperty(
    required = false,
    value = "discussions about")
  public List<SSEntityA>     discs            = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false,
    value = "user events")
  public List<SSEntity>     userEvents              = new ArrayList<>(); //new
  
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
  public  SSEntityA       likes = null;
  
  public String getId() throws Exception{
    return SSStrU.removeTrailingSlash(id);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public String getType(){
    return SSStrU.toStr(type);
  }
  
  public String getDescription() throws Exception{
    return SSStrU.toStr(description);
  }
  
  public List<String> getCircleTypes(){
    return SSStrU.toStr(circleTypes);
  }
  
  public List<String> getComments() throws Exception{
    return SSStrU.toStr(comments);
  }
  
  public List<String> getDiscs() throws Exception{
    return SSStrU.removeTrailingSlash(discs);
  }
  
  public String getFile(){
    return SSStrU.removeTrailingSlash(file);
  }
  
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
    
    this.id            = entity.id;
    this.label         = entity.label;
    this.creationTime  = entity.creationTime;
    this.type          = entity.type;
    this.author        = entity.author;
    this.description   = entity.description;
    this.overallRating = entity.overallRating;
    
    addEntitiesDistinctWithoutNull(this.tags,       entity.tags);
    addEntitiesDistinctWithoutNull(this.categories, entity.categories);
    
    if(
      entity.discs != null &&
      !entity.discs.isEmpty()){
      
      this.discs.addAll(entity.discs);
    }
    
    addEntitiesDistinctWithoutNull(this.userEvents, entity.userEvents);
    
    this.thumb = entity.thumb;
    this.file  = entity.file;
    
    if(
      entity.flags != null &&
      !entity.flags.isEmpty()){
      
      this.flags.addAll(entity.flags);
    }
    
    if(
      entity.circleTypes != null &&
      !entity.circleTypes.isEmpty()){
      
      this.circleTypes.addAll(entity.circleTypes);
    }
    
    addEntitiesDistinctWithoutNull(this.entries,          entity.entries);
    addEntitiesDistinctWithoutNull(this.attachedEntities, entity.attachedEntities);
    addEntitiesDistinctWithoutNull(this.attachedEntities, entity.attachedEntities);
    
    if(
      entity.comments != null &&
      !entity.comments.isEmpty()){
      
      this.comments.addAll(entity.comments);
    }
    
    addEntitiesDistinctWithoutNull(this.users,    entity.users);
    addEntitiesDistinctWithoutNull(this.entities, entity.entities);
    
    this.read = entity.read;
    
    addEntitiesDistinctWithoutNull(this.circles,  entity.circles);
    addEntitiesDistinctWithoutNull(this.locations, entity.locations);
    
    this.likes = entity.likes;
  }
  
  protected SSEntity(
    final SSEntity specificEntity, 
    final SSEntity entity) throws Exception{
    
    super(specificEntity.id);
    
    this.id               = specificEntity.id;
    
    if(specificEntity.label != null){
      this.label = specificEntity.label;
    }else{
      this.label = entity.label;
    }
    
    if(specificEntity.creationTime != null){
      this.creationTime = specificEntity.creationTime;
    }else{
      this.creationTime = entity.creationTime;
    }
    
    if(specificEntity.type != null){
      this.type = specificEntity.type;
    }else{
      this.type = entity.type;
    }
    
    if(specificEntity.author != null){
      this.author = specificEntity.author;
    }else{
      this.author = entity.author;
    }
    
    if(specificEntity.description != null){
      this.description = specificEntity.description;
    }else{
      this.description = entity.description;
    }
    
    if(specificEntity.overallRating != null){
      this.overallRating = specificEntity.overallRating;
    }else{
      this.overallRating = entity.overallRating;
    }
    
    addEntitiesDistinctWithoutNull(this.tags, specificEntity.tags);
    addEntitiesDistinctWithoutNull(this.tags, entity.tags);
    
    addEntitiesDistinctWithoutNull(this.categories, specificEntity.categories);
    addEntitiesDistinctWithoutNull(this.categories, entity.categories);
    
    if(
      specificEntity.discs != null &&
      !specificEntity.discs.isEmpty()){
    
      this.discs.addAll(specificEntity.discs);
    }else{
      
      if(
        entity.discs != null &&
        !entity.discs.isEmpty()){
        
        this.discs.addAll(entity.discs);
      }
    }
    
    addEntitiesDistinctWithoutNull(this.userEvents, specificEntity.userEvents);
    addEntitiesDistinctWithoutNull(this.userEvents, entity.userEvents);
    
    if(specificEntity.thumb != null){
      this.thumb = specificEntity.thumb;
    }else{
      this.thumb = entity.thumb;
    }
    
    if(specificEntity.file != null){
      this.file = specificEntity.file;
    }else{
      this.file = entity.file;
    }

    if(
      specificEntity.flags != null &&
      !specificEntity.flags.isEmpty()){
    
      this.flags.addAll(specificEntity.flags);
    }else{
      
      if(
        entity.flags != null &&
        !entity.flags.isEmpty()){
        
        this.flags.addAll(entity.flags);
      }
    }
    
    if(
      specificEntity.circleTypes != null &&
      !specificEntity.circleTypes.isEmpty()){
    
      this.circleTypes.addAll(specificEntity.circleTypes);
    }else{
      
      if(
        entity.circleTypes != null &&
        !entity.circleTypes.isEmpty()){
        
        this.circleTypes.addAll(entity.circleTypes);
      }
    }
    
    addEntitiesDistinctWithoutNull(this.entries, specificEntity.entries);
    addEntitiesDistinctWithoutNull(this.entries, entity.entries);
    
    addEntitiesDistinctWithoutNull(this.attachedEntities, specificEntity.attachedEntities);
    addEntitiesDistinctWithoutNull(this.attachedEntities, entity.attachedEntities);
    
    addEntitiesDistinctWithoutNull(this.attachedEntities, specificEntity.attachedEntities);
    addEntitiesDistinctWithoutNull(this.attachedEntities, entity.attachedEntities);
    
    if(
      specificEntity.comments != null &&
      !specificEntity.comments.isEmpty()){
    
      this.comments.addAll(specificEntity.comments);
    }else{
      
      if(
        entity.comments != null &&
        !entity.comments.isEmpty()){
        
        this.comments.addAll(entity.comments);
      }
    }
    
    addEntitiesDistinctWithoutNull(this.users, specificEntity.users);
    addEntitiesDistinctWithoutNull(this.users, entity.users);
    
    addEntitiesDistinctWithoutNull(this.entities, specificEntity.entities);
    addEntitiesDistinctWithoutNull(this.entities, entity.entities);
    
    if(specificEntity.read != null){
      this.read = specificEntity.read;
    }else{
      this.read = entity.read;
    }
    
    addEntitiesDistinctWithoutNull(this.circles, specificEntity.circles);
    addEntitiesDistinctWithoutNull(this.circles, entity.circles);
    
    addEntitiesDistinctWithoutNull(this.locations, specificEntity.locations);
    addEntitiesDistinctWithoutNull(this.locations, entity.locations);
    
    if(specificEntity.likes != null){
      this.likes = specificEntity.likes;
    }else{
      this.likes = entity.likes;
    }
  }
  
  @Override 
  public Object jsonLDDesc() {
    throw new UnsupportedOperationException();
  }

  public static void addEntitiesDistinctWithoutNull(
    final List<SSEntity>   entities,
    final SSEntity         entity){
    
    if(
      SSObjU.isNull  (entities, entity) ||
      SSStrU.contains(entities, entity)){
      return;
    }
    
    entities.add(entity);
  }
  
  public static void addEntitiesDistinctWithoutNull(
    final List<SSEntity>  entities,
    final List<SSEntity>  toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    toAddEntities.stream().filter((entity)-> ! (entity == null)).filter((entity)->(!SSStrU.contains(entities, entity))).forEach((entity)->{
      entities.add(entity);
    });
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