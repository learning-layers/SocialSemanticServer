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
package at.kc.tugraz.ss.category.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSLabel;
import java.util.*;

public class SSCategory extends SSEntity{

  public  SSCategoryLabel     categoryLabel  = null;
  public  SSUri               entity         = null;
  public  SSUri               user           = null;
  public  SSSpaceE            space          = null;
  public  SSUri               circle         = null;

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public String getUser() {
    return SSStrU.removeTrailingSlash(user);
  }

  public String getSpace(){
    return SSStrU.toStr(space);
  }
  
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  @Override
  public String getLabel(){
    return SSStrU.toStr(categoryLabel);
  }
  
  public String getCategoryLabel(){
    return SSStrU.toStr(categoryLabel);
  }
  
  @Override
  public Object jsonLDDesc() {
    throw new UnsupportedOperationException();
  } 
  
   public static SSCategory get(
    final SSCategory            category,
    final SSEntity              entity) throws Exception{
    
    return new SSCategory(category, entity);
  }
   
   protected SSCategory(
    final SSCategory            category,
    final SSEntity              entity) throws Exception{
    
    super(category, entity);
    
    this.entity        = category.entity;
    this.user          = category.user;
    this.space         = category.space;
    this.categoryLabel = category.categoryLabel;
  }
     
  public static SSCategory get(
    final SSUri            id       ,
    final SSUri            entity   ,
    final SSUri            user     ,
    final SSSpaceE         space    ,
    final SSCategoryLabel  categoryLabel,
    final SSUri            circle, 
    final Long             creationTime) throws Exception{
    
    return new SSCategory(id, entity, user, space, categoryLabel, circle, creationTime);
  }
  
  protected SSCategory(
    final SSUri             id,
    final SSUri             entity,
    final SSUri             user,
    final SSSpaceE          space,
    final SSCategoryLabel   categoryLabel,
    final SSUri             circle, 
    final Long              creationTime) throws Exception{
    
    super(id, SSEntityE.category, SSLabel.get(SSStrU.toStr(categoryLabel)));
    
    this.entity        = entity;
    this.user          = user;
    this.space         = space;
    this.categoryLabel = categoryLabel;
    this.circle        = circle;
    this.creationTime  = creationTime;
  }
  
  public static Map<String, List<String>> getCategoryLabelsPerEntities(final List<SSEntity> categories) throws Exception{
    
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


//  public static List<SSUri> getDistinctResources(final List<SSCategory> categories) throws Exception{
//    
//    final List<SSUri> result = new ArrayList<>();
//		
//		for(SSCategory category : categories){
//      SSUri.addDistinctWithoutNull(result, category.entity);
//		}
//		
//		return result;
//  }