/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSObjU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityA;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSEntity extends SSEntityA{
  
  @ApiModelProperty(
    required = false)
  public SSUri               id               = null; //entity
  
  public void setId(final String id) throws SSErr{
    this.id = SSUri.get(id);
  }
  
  public String getId() {
    return SSStrU.removeTrailingSlash(id);
  }
  
  @ApiModelProperty(
    required = false)
  public SSLabel             label            = null;
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  @ApiModelProperty(
    required = false)
  public Long                creationTime     = null;
  
  @ApiModelProperty(
    required = false)
  public SSEntityE           type             = null;
  
  public void setType(final String type) throws SSErr{
    this.type = SSEntityE.get(type);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }
  
  @ApiModelProperty(
    required = false)
  public SSEntity               author           = null;
  
  @ApiModelProperty(
    required = false)
  public SSTextComment       description      = null;

  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }
  
  public String getDescription() {
    return SSStrU.toStr(description);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSCircleE>     circleTypes      = new ArrayList<>();
  
  public void setCircleTypes(final List<String> circleTypes) throws SSErr{
    this.circleTypes = SSCircleE.get(circleTypes);
  }
  
  public List<String> getCircleTypes(){
    return SSStrU.toStr(circleTypes);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>       entries          = new ArrayList<>();
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>      attachedEntities = new ArrayList<>();
  
  @ApiModelProperty(
    required = false)
  public List<SSTextComment> comments         = new ArrayList<>();
  
  public void setComments(final List<String> comments) throws SSErr{
    this.comments = SSTextComment.get(comments);
  }
  
  public List<String> getComments() {
    return SSStrU.toStr(comments);
  }
  
  @ApiModelProperty(
    required = false)
  public SSEntityA           overallRating    = null; //new
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>        tags             = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>        categories     = new ArrayList<>();
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>     discs            = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>     userEvents              = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false)
  public SSEntity              thumb            = null; //new
  
  @ApiModelProperty(
    required = false)
  public SSEntity               file             = null; //new
  
  @ApiModelProperty(
    required = false)
  public List<SSEntityA>     flags            = new ArrayList<>(); //new
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>         users        = new ArrayList<>();
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>         entities     = new ArrayList<>();
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>       circles      = new ArrayList<>();
  
  @ApiModelProperty(
    required = false)
  public List<SSEntity>       locations    = new ArrayList<>();
  
  @ApiModelProperty(
    required = false)
  public  Boolean       read = false;
  
  @ApiModelProperty(
    required = false)
  public  SSEntityA       likes = null;
  
  @ApiModelProperty(
    required = false)
  public SSEntity       profilePicture = null;
  
  @ApiModelProperty(
    required = false)
  public SSQueryResultPage activityPage = null;
  
  @ApiModelProperty(
    required = false)
  public SSQueryResultPage messagesPage = null;
  
  @ApiModelProperty(
    required = false)
  public SSQueryResultPage collsPage = null;
  
  public static SSEntity get(
    final SSUri     id,
    final SSEntityE type) throws SSErr {
    
    return new SSEntity(id, type);
  }
  
  public static List<SSEntity> get(
    final List<SSUri> ids,
    final SSEntityE   type) throws SSErr {
    
    final List<SSEntity> entities = new ArrayList<>();
    
    for(SSUri id : ids){
      entities.add(new SSEntity(id, type));
    }
    
    return entities;
  }
  
  public static SSEntity get(
    final SSUri     id,
    final SSEntityE type,
    final SSLabel   label) throws SSErr {
    
    return new SSEntity(id, type, label);
  }
  
  public static SSEntity get(
    final SSUri         id,
    final SSEntityE     type,
    final SSLabel       label,
    final SSTextComment description,
    final Long          creationTime,
    final SSEntity      author) throws SSErr {
    
    return new SSEntity(id, type, label, description, creationTime, author);
  }
  
  public SSEntity(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSEntity(
    final SSUri         id,
    final SSEntityE     type,
    final SSLabel       label,
    final SSTextComment description,
    final Long          creationTime,
    final SSEntity      author) throws SSErr {
    
    super(id);
    
    this.id           = id;
    this.type         = type;
    this.label        = label;
    this.description  = description;
    this.creationTime = creationTime;
    this.author       = author;
  }
  
  protected SSEntity(
    final SSUri     id,
    final SSEntityE type) throws SSErr{
    
    super(id);
    
    this.id    = id;
    this.type  = type;
  }
  
  protected SSEntity(
    final SSUri     id,
    final SSEntityE type,
    final SSLabel   label) throws SSErr {
    
    super(id);
    
    this.id    = id;
    this.type  = type;
    this.label = label;
  }
  
  protected SSEntity(
    final SSEntity entity) throws SSErr {
    
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
    
    addEntitiesDistinctWithoutNull(this.entries,           entity.entries);
    addEntitiesDistinctWithoutNull(this.attachedEntities,  entity.attachedEntities);
    
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
    
    this.likes          = entity.likes;
    this.profilePicture = entity.profilePicture;

    this.activityPage = entity.activityPage;
    this.messagesPage = entity.messagesPage;
    this.collsPage    = entity.collsPage;
  }
  
  protected SSEntity(
    final SSEntity specificEntity, 
    final SSEntity entity) throws SSErr {
    
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
    
    if(specificEntity.read){
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
    
    if(specificEntity.profilePicture != null){
      this.profilePicture = specificEntity.profilePicture;
    }else{
      this.profilePicture = entity.profilePicture;
    }
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
//    final List<SSEntityA>            flags) {
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
//    final List<SSEntityA>        flags) { //new
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