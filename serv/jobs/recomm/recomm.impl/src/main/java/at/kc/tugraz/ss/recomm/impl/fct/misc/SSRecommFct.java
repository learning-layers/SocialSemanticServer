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
package at.kc.tugraz.ss.recomm.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSRecommFct{

  public static Boolean exportEntityTagCombinationsForAllUsers(
    final String fileName,
    final Boolean usePrivateTagsToo) throws Exception {
    
    Boolean                   somethingExported = false;
    Map<String, List<String>> tagsPerEntities;
    
    for(SSUser user : SSServCaller.userAll()){
      
      tagsPerEntities = getTagsOfUserPerEntities(user.id, usePrivateTagsToo);
      
      if(tagsPerEntities.isEmpty()){
        continue;
      }
      
      SSServCaller.dataExportUserEntityTags(
        user.id,
        tagsPerEntities,
        fileName,
        false);
      
      somethingExported = true;
    }
    
    if(!somethingExported){
      return false;
    }
    
    SSServCaller.dataExportUserEntityTags(
      null,
      new HashMap<>(),
      fileName,
      true);
    
    return true;
  }   
  
  public static Boolean exportEntityTagTimestampCombinationsForAllUsers(
    final String  fileName,
    final Boolean usePrivateTagsToo) throws Exception {
    
    Boolean                   somethingExported = false;
    Map<String, List<String>> tagsPerEntities;
    
    for(SSUser user : SSServCaller.userAll()){
      
      tagsPerEntities = getTagsOfUserPerEntities(user.id, usePrivateTagsToo);
      
      if(tagsPerEntities.isEmpty()){
        continue;
      }
      
      //TODO dtheiler: provide various sortings by timestamps
      //TODO dtheiler: allow more than one file per algo
      //TODO dtheiler: for categories try out all tags of a resource as long as we have no category information
      SSServCaller.dataExportUserEntityTagTimestamps(
        user.id,
        tagsPerEntities,
        getTimestampInMillisecOfAUserTagForEntity (user.id, tagsPerEntities.entrySet().iterator().next().getKey(), SSSpaceE.sharedSpace),
        fileName,
        false);
      
      somethingExported = true;
    }
    
    if(!somethingExported){
      return false;
    }
    
    SSServCaller.dataExportUserEntityTagTimestamps(
      null,
      new HashMap<>(),
      null,
      fileName,
      true);
    
    return true;
  }
  
  public static Boolean exportEntityTagCategoryCombinationsForAllUsers(
    final String  fileName,
    final Boolean usePrivateTagsToo) throws Exception {
    
    Boolean                   somethingExported = false;
    Map<String, List<String>> tagsPerEntities;
    
    for(SSUser user : SSServCaller.userAll()){
      
      tagsPerEntities = getTagsOfUserPerEntities(user.id, usePrivateTagsToo);
      
      if(tagsPerEntities.isEmpty()){
        continue;
      }
      
      //TODO dtheiler: provide various sortings by timestamps
      //TODO dtheiler: allow more than one file per algo
      //TODO dtheiler: for categories try out all tags of a resource as long as we have no category information
      SSServCaller.dataExportUserEntityTagCategories(
        user.id,
        tagsPerEntities,
        getCategoriesPerEntities                  (tagsPerEntities.size()),
        fileName,
        false);
      
      somethingExported = true;
    }
    
    if(!somethingExported){
      return false;
    }
    
    SSServCaller.dataExportUserEntityTagCategories(
      null,
      new HashMap<>(),
      new HashMap<>(),
      fileName,
      true);
    
    return true;
  }
  
  public static Boolean exportEntityTagCategoryTimestampCombinationsForAllUsers(
    final String  fileName,
    final Boolean usePrivateTagsToo) throws Exception {
    
    Boolean                   somethingExported = false;
    Map<String, List<String>> tagsPerEntities;
    
    for(SSUser user : SSServCaller.userAll()){
      
      tagsPerEntities = getTagsOfUserPerEntities(user.id, usePrivateTagsToo);
      
      if(tagsPerEntities.isEmpty()){
        continue;
      }
      
      //TODO dtheiler: provide various sortings by timestamps
      //TODO dtheiler: allow more than one file per algo
      //TODO dtheiler: for categories try out all tags of a resource as long as we have no category information
      SSServCaller.dataExportUserEntityTagCategoryTimestamps(
        user.id,
        tagsPerEntities,
        getCategoriesPerEntities                  (tagsPerEntities.size()),
        getTimestampInMillisecOfAUserTagForEntity (user.id, tagsPerEntities.entrySet().iterator().next().getKey(), SSSpaceE.sharedSpace),
        fileName,
        false);
      
      somethingExported = true;
    }
    
    if(!somethingExported){
      return false;
    }
    
    SSServCaller.dataExportUserEntityTagCategoryTimestamps(
      null,
      new HashMap<>(),
      new HashMap<>(),
      null,
      fileName,
      true);
    
    return true;
  }
  
  private static Map<String, List<String>> getTagsOfUserPerEntities(
    final SSUri       userUri,
    final Boolean     usePrivateTagsToo) throws Exception{
    
    if(usePrivateTagsToo){
      
      return SSTag.getTagLabelsPerEntities(
        SSServCaller.tagsUserGet(
          userUri,
          SSUri.asListWithoutNullAndEmpty(),
          new ArrayList<>(),
          null,
          null));
      
    }else{
      return SSTag.getTagLabelsPerEntities(
        SSServCaller.tagsUserGet(
          userUri,
          SSUri.asListWithoutNullAndEmpty(),
          new ArrayList<>(),
          SSSpaceE.sharedSpace,
          null));
    }
  }

  private static Map<String, List<String>> getCategoriesPerEntities(
    final Integer numberOfEntities){
    
    final Map<String, List<String>> categoriesPerEntities = new HashMap<>();
    
    for(Integer counter = 0; counter < numberOfEntities; counter++){
      categoriesPerEntities.put(SSVoc.sssUri + SSStrU.underline + counter.toString(), new ArrayList<>());
    }
    
    return categoriesPerEntities;
  }

  private static Long getTimestampInMillisecOfAUserTagForEntity(
    final SSUri       userUri, 
    final String      entityUri, 
    final SSSpaceE    space) throws Exception{
    
    final List<SSTag> tags = 
      SSServCaller.tagsUserGet(
        userUri,
        SSUri.asListWithoutNullAndEmpty(SSUri.get(entityUri)),
        new ArrayList<>(),
        space,
        null);
    
    if(tags.isEmpty()){
      return 0L;
    }
    
    return SSServCaller.entityGet(tags.get(0).id).creationTime / 1000;
  }
}
