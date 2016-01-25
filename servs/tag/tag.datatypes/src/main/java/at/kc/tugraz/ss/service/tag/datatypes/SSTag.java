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
package at.kc.tugraz.ss.service.tag.datatypes;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.enums.SSSpaceE;
import io.swagger.annotations.*;
import java.util.*;

@ApiModel
public class SSTag extends SSEntity{

  @ApiModelProperty
  public  SSTagLabel          tagLabel     = null;
  
  public void setTagLabel(final String tagLabel) throws SSErr{
    this.tagLabel = SSTagLabel.get(tagLabel);
  }
  
  public String getTagLabel(){
    return SSStrU.toStr(tagLabel);
  }
  
  @ApiModelProperty
  public  SSUri               entity       = null;
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  @ApiModelProperty
  public  SSUri               user         = null;
  
  public void setUser(final String user) throws SSErr{
    this.user = SSUri.get(user);
  }
  
  public String getUser(){
    return SSStrU.removeTrailingSlash(user);
  }
  
  @ApiModelProperty
  public  SSSpaceE            space        = null;
  
  public void setSpace(final String space) throws SSErr{
    this.space = SSSpaceE.get(space);
  }
  
  public String getSpace(){
    return SSStrU.toStr(space);
  }
   
  @ApiModelProperty
  public  SSUri               circle       = null;

  public void setCircle(final String circle) throws SSErr{
    this.circle = SSUri.get(circle);
  }
  
  public String getCircle(){
    return SSStrU.removeTrailingSlash(circle);
  }

  @Override
  public String getLabel(){
    return SSStrU.toStr(tagLabel);
  }
  
  public static SSTag get(
    final SSUri       id,
    final SSUri       entity,
    final SSUri       user,
    final SSSpaceE    space,
    final SSTagLabel  tagLabel,
    final SSUri       circle, 
    final Long        creationTime) throws Exception{
    
    return new SSTag(id, entity, user, space, tagLabel, circle, creationTime);
  }
  
  public SSTag(){}
  
  protected SSTag(
    final SSUri       id,
    final SSUri       entity,
    final SSUri       user,
    final SSSpaceE    space,
    final SSTagLabel  tagLabel,
    final SSUri       circle, 
    final Long        creationTime) throws Exception{
    
    super(id, SSEntityE.tag, SSLabel.get(SSStrU.toStr(tagLabel)));
    
    this.val          = SSStrU.toStr(tagLabel);
    this.entity       = entity;
    this.user         = user;
    this.space        = space;
    this.tagLabel     = tagLabel;
    this.circle       = circle;
    this.creationTime = creationTime;
  }
  
  public static Map<String, List<String>> getTagLabelsPerEntities(final List<SSEntity> tags) throws Exception{
    
    final Map<String, List<String>>     tagsPerEntity = new HashMap<>();
    List<String>                        tagLabels;
    String                              entity;
    
    for(SSEntity tagEntity : tags){
      
      entity = SSStrU.toStr(((SSTag)tagEntity).entity);
      
      if(tagsPerEntity.containsKey(entity)){
        
        tagLabels = tagsPerEntity.get(entity);
        
        if(SSStrU.contains(tagLabels, ((SSTag)tagEntity).label)){
          continue;
        }
        
        tagLabels.add(((SSTag)tagEntity).label.toString());
      }else{
        
        tagLabels = new ArrayList<>();
        
        tagLabels.add(SSStrU.toStr(((SSTag)tagEntity).label));
        
        tagsPerEntity.put(entity, tagLabels);
      }
    }
    
    return tagsPerEntity;
  }
}

//  public static List<SSUri> getDistinctResources(
//    final List<SSTag> tags) throws Exception{
//    
//    final List<SSEntityA> result = new ArrayList<>();
//		
//		for(SSTag tag : tags){
//      SSUri.addDistinct(result, tag.entity);
//		}
//
//    
//  }
//  