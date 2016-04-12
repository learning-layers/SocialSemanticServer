/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.adapter.rest.v3;

import at.tugraz.sss.servs.common.impl.*;
import at.tugraz.sss.servs.entity.datatype.*;
import at.tugraz.sss.servs.util.*;
import at.tugraz.sss.servs.category.api.*;
import at.tugraz.sss.servs.category.datatype.*;
import at.tugraz.sss.servs.category.impl.*;
import at.tugraz.sss.servs.tag.api.*;
import at.tugraz.sss.servs.tag.datatype.*;
import at.tugraz.sss.servs.tag.impl.*;
import java.util.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class SSRESTCommons {
  
  public static void addTags(
    final SSServPar       servPar,
    final SSUri           user,
    final List<String>    tags,
    final List<SSUri>     entities,
    final SSUri           circleAsSpace) throws SSErr{
    
    try{
      
      if(
        tags == null ||
        tags.isEmpty()){
        return;
      }
      
      final SSTagClientI tagServ = new SSTagImpl();
      
      tagServ.tagsAdd(
        SSClientE.rest,
        new SSTagsAddPar(
          servPar,
          user,
          SSTagLabel.get(tags), //labels
          entities, //entities
          null, //space
          circleAsSpace, //circle
          null, //creationTime,
          true, //withUserRestriction
          true)); //shouldCommit)
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addCategories(
    final SSServPar       servPar,
    final SSUri           user,
    final List<String>    categories,
    final List<SSUri>     entities,
    final SSUri           circleAsSpace) throws SSErr{
    
    try{
      
      if(
        categories == null ||
        categories.isEmpty()){
        return;
      }
      
      final SSCategoryClientI categoryServ = new SSCategoryImpl();
      
      for(SSUri entity : entities){
        
        for(SSCategoryLabel label : SSCategoryLabel.get(categories)){
          
          categoryServ.categoryAdd(
            SSClientE.rest,
            new SSCategoryAddPar(
              servPar,
              user,
              entity,
              label,
              null, //space
              circleAsSpace, //circle
              null, //creationTime,
              true, //withUserRestriction
              true)); //shouldCommit)
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static String prepareErrorJSON(final Exception originalError){
    
    try{
      
      final Map<String, Object> jsonObj = new HashMap<>();
      
      if(originalError instanceof SSErr){
        
        jsonObj.put(SSVarNames.id,                    ((SSErr) originalError).code);
        jsonObj.put(SSVarNames.message,               ((SSErr) originalError).code);
      }else{
    
        jsonObj.put(SSVarNames.id,                      originalError.getClass());
        jsonObj.put(SSVarNames.message,                 originalError.getMessage());
      }
      
      return SSJSONU.jsonStr(jsonObj);
      
    }catch(Exception error){
      SSLogU.err(error);
      return null;
    }
  }
  
  public static Response prepareErrorResponse(final Exception originalError){
    
    //    SSServErrReg.logAndReset(true);
    try{
      
      return Response.status(500).entity(prepareErrorJSON(originalError)).build();
      
    }catch(Exception error){
      SSLogU.err(error);
      
      return Response.status(500).entity(SSErrE.defaultErr).build();
    }
  }
  
  public static String getBearer(
    final HttpHeaders headers) throws SSErr{
    
    return SSStrU.replaceAll(headers.getRequestHeader("authorization").get(0), "Bearer ", SSStrU.empty);
  }
}
