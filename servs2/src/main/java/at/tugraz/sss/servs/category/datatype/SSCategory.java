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
package at.tugraz.sss.servs.category.datatype;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.enums.SSSpaceE;
import io.swagger.annotations.*;
import java.util.*;

@ApiModel
public class SSCategory extends SSEntity{

  @ApiModelProperty
  public  SSCategoryLabel     categoryLabel  = null;
  
  public void setCategoryLabel(final String categoryLabel) throws SSErr{
    this.categoryLabel = SSCategoryLabel.get(categoryLabel);
  }
  
  public String getCategoryLabel(){
    return SSStrU.toStr(categoryLabel);
  }
   
  @ApiModelProperty
  public  SSUri               entity         = null;
  
  public void setEntity(final String entity) throws SSErr {
    this.entity = SSUri.get(entity);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  @ApiModelProperty
  public  SSUri               user           = null;
  
  public void setUser(final String user) throws SSErr {
    this.user = SSUri.get(user);
  }
  
  public String getUser() {
    return SSStrU.removeTrailingSlash(user);
  }
  
  @ApiModelProperty
  public  SSSpaceE            space          = null;
  
  public void setSpace(final String space) throws SSErr{
    this.space = SSSpaceE.get(space);
  }
  
  public String getSpace(){
    return SSStrU.toStr(space);
  }
  
  @ApiModelProperty
  public  SSUri               circle         = null;
  
  public void setCircle(final String circle) throws SSErr{
    this.circle = SSUri.get(circle);
  }
  
  public String getCircle() throws SSErr{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  @Override
  public String getLabel(){
    return SSStrU.toStr(categoryLabel);
  }
  
  public static SSCategory get(
    final SSCategory            category,
    final SSEntity              entity) throws SSErr{
    
    return new SSCategory(category, entity);
  }
   
  public static SSCategory get(
    final SSUri            id       ,
    final SSUri            entity   ,
    final SSUri            user     ,
    final SSSpaceE         space    ,
    final SSCategoryLabel  categoryLabel,
    final SSUri            circle, 
    final Long             creationTime) throws SSErr{
    
    return new SSCategory(id, entity, user, space, categoryLabel, circle, creationTime);
  }
  
  public SSCategory(){/* Do nothing because of only JSON Jackson needs this */ }
    
  protected SSCategory(
    final SSUri             id,
    final SSUri             entity,
    final SSUri             user,
    final SSSpaceE          space,
    final SSCategoryLabel   categoryLabel,
    final SSUri             circle, 
    final Long              creationTime) throws SSErr{
    
    super(id, SSEntityE.category, SSLabel.get(SSStrU.toStr(categoryLabel)));
    
    this.entity        = entity;
    this.user          = user;
    this.space         = space;
    this.categoryLabel = categoryLabel;
    this.circle        = circle;
    this.creationTime  = creationTime;
  }
  
   protected SSCategory(
    final SSCategory            category,
    final SSEntity              entity) throws SSErr{
    
    super(category, entity);
    
    this.entity        = category.entity;
    this.user          = category.user;
    this.space         = category.space;
    this.categoryLabel = category.categoryLabel;
  }
  
  public static Map<String, List<String>> getCategoryLabelsPerEntities(final List<SSEntity> categories) throws SSErr{
    
    final Map<String, List<String>>     categorysPerEntity = new HashMap<>();
    List<String>                        categoryLabels;
    String                              entity;
    
    for(SSEntity categoryEntity : categories){
      
      entity = SSStrU.toStr(((SSCategory)categoryEntity).entity);
      
      if(categorysPerEntity.containsKey(entity)){
        
        categoryLabels = categorysPerEntity.get(entity);
        
        if(SSStrU.contains(categoryLabels, ((SSCategory)categoryEntity).label)){
          continue;
        }
        
        categoryLabels.add(((SSCategory)categoryEntity).label.toString());
      }else{
        
        categoryLabels = new ArrayList<>();
        
        categoryLabels.add(SSStrU.toStr(((SSCategory)categoryEntity).label));
        
        categorysPerEntity.put(entity, categoryLabels);
      }
    }
    
    return categorysPerEntity;
  }
}


//  public static List<SSUri> getDistinctResources(final List<SSCategory> categories) throws SSErr{
//    
//    final List<SSUri> result = new ArrayList<>();
//		
//		for(SSCategory category : categories){
//      SSUri.addDistinctWithoutNull(result, category.entity);
//		}
//		
//		return result;
//  }