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

import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.*;

public class SSTag extends SSEntityA {

  public  SSUri               uri          = null;
  public  SSUri               resource     = null;
  public  SSUri               user         = null;
  public  SSSpaceEnum         space        = null;
  public  SSTagLabel         label        = null;

  @Override
  public Object jsonLDDesc() {
  
    Map<String, Object> ld = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.resource,   SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.user,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());    
    ld.put(SSVarU.space,      SSVarU.sss + SSStrU.colon + SSSpaceEnum.class.getName());    
    ld.put(SSVarU.label,      SSVarU.sss + SSStrU.colon + SSTagLabel.class.getName());    
    
    return ld;
  } 
    
  public static List<SSTag> getDistinct(List<String> tags) throws Exception {
    
    final List<SSTag> result = new ArrayList<SSTag>();
    
    for(String tag : SSStrU.getDistinct(tags)){
      
      result.add(
        get(
          null,
          null,
          null,
          null,
          SSTagLabel.get(tag)));
    }
    
    return result;
  }
  
  public static SSTag get(
    SSUri       uri      ,
    SSUri       resource ,
    SSUri       user     ,
    SSSpaceEnum space    ,
    SSTagLabel  label){
    
    return new SSTag(uri, resource, user, space, label);
  }
  
  public static List<SSUri> getDistinctResources(final List<SSTag> tags){
    
    final List<SSUri> result = new ArrayList<SSUri>();
		
		for(SSTag tag : tags){
			
			if(SSUri.containsNot(result, tag.resource)){
				result.add(tag.resource);
			}
		}
		
		return result;
  }
  
  public static Map<String, List<String>> getTagLabelsPerEntities(final List<SSTag> tags){
    
    final Map<String, List<String>>     tagsPerEntity = new HashMap<String,  List<String>>();
    List<String>                        tagLabels;
    String                              entity;
    
    for(SSTag userTag : tags){
      
      entity = SSUri.toStr(userTag.resource);
      
      if(tagsPerEntity.containsKey(entity)){
        
        tagLabels = tagsPerEntity.get(entity);
        
        if(tagLabels.contains(SSTagLabel.toStr(userTag.label))){
          continue;
        }
        
        tagLabels.add(userTag.label.toString());
      }else{
        
        tagLabels = new ArrayList<String>();
        
        tagLabels.add(SSTagLabel.toStr(userTag.label));
        
        tagsPerEntity.put(entity, tagLabels);
      }
    }
    
    return tagsPerEntity;
  }
  
  private SSTag(
    SSUri        uri,
    SSUri        resource,
    SSUri        user,
    SSSpaceEnum  space,
    SSTagLabel  label){
    
    super(label);
    
    this.uri         = uri;
    this.resource    = resource;
    this.user        = user;
    this.space       = space;
    this.label       = label;
  }
  
  /* getters to allow for json enconding */
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }

  public String getResource() throws Exception{
    return SSUri.toStrWithoutSlash(resource);
  }

  public String getUser() throws Exception{
    return SSUri.toStrWithoutSlash(user);
  }

  public String getSpace(){
    return SSSpaceEnum.toStr(space);
  }

  public String getLabel(){
    return SSTagLabel.toStr(label);
  }
}
