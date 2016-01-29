/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.serv.datatype;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.enums.SSImageE;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSImage extends SSEntity{
  
  @ApiModelProperty
  public SSImageE imageType     = null;

  public void setImageType(final String imageType) throws SSErr {
    this.imageType = SSImageE.get(imageType);
  }
  
  public String getImageType() {
    return SSStrU.toStr(imageType);
  }

  @ApiModelProperty
  public SSUri    link          = null;

  public void setLink(final String link) throws SSErr {
    this.link = SSUri.get(link);
  }
  
  public String getLink() {
    return SSStrU.removeTrailingSlash(link);
  }
  
  public static SSImage get(
    final SSImage         image,
    final SSEntity        entity) throws SSErr{
    
    return new SSImage(image, entity);
  }
  
  public SSImage(){}
  
  protected SSImage(
    final SSImage         image,
    final SSEntity        entity) throws SSErr{
    
    super(image, entity);
    
    if(image.imageType != null){
      this.imageType = image.imageType;
    }else{
      
      if(entity instanceof SSImage){
        this.imageType = ((SSImage)entity).imageType;
      }
    }
    
    if(image.link != null){
      this.link = image.link;
    }else{
      
      if(entity instanceof SSImage){
        this.link = ((SSImage)entity).link;
      }
    }
  }
  
  public static SSImage get(
    final SSUri           id, 
    final SSImageE        imageType,
    final SSUri           link) throws SSErr{
    
    return new SSImage(id, imageType, link);
  }
  
  protected SSImage(
    final SSUri           id, 
    final SSImageE        imageType,
    final SSUri           link) throws SSErr{
    
    super(id, SSEntityE.image);
    
    this.imageType = imageType;
    this.link      = link;
  }
  
  public static void addDistinctWithoutNull(
    final List<SSImage>     entities,
    final SSImage           entity){
    
    if(
      SSObjU.isNull  (entities, entity) ||
      SSStrU.contains(entities, entity)){
      return;
    }
    
    entities.add(entity);
  }
  
  public static void addDistinctWithoutNull(
    final List<SSImage>  entities,
    final List<SSImage>  toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSImage entity : toAddEntities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(entities, entity)){
        entities.add(entity);
      }
    }
  }
}
