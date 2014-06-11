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
 package at.kc.tugraz.ss.service.search.datatypes;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import java.util.*;

public class SSSearchResult extends SSEntityA{

  public  SSUri                   entity                          = null;
  public  SSSpaceE                space                           = null;
  public  String                  label                           = null;
  public  SSEntityE               type                            = null;

  public static List<SSSearchResult> get(
    final List<SSEntity> entities) throws Exception{
    
    final List<SSSearchResult> results = new ArrayList<SSSearchResult>();
    
    for(SSEntity entity : entities){
      results.add(get(entity));
    }
    
    return results;
  }
  
  public static SSSearchResult get(
    final SSEntity entity) throws Exception{
    
    return new SSSearchResult(entity);
  }
   
  public static SSSearchResult get(
    final SSUri       entity,
    final SSSpaceE    space) throws Exception{
    
    return new SSSearchResult(entity, space);
  }
 
  private SSSearchResult(
    final SSEntity entity) throws Exception{
    
    super(entity);
    
    this.entity    = entity.id;
    this.label     = SSLabel.toStr(entity.label);
    this.type      = entity.type;
    this.space     = SSSpaceE.sharedSpace;
  }
  
  private SSSearchResult(
    SSUri       entity,
    SSSpaceE    space) throws Exception{
    
    super(entity);
    
    this.entity    = entity;
    this.space     = space;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld = new HashMap<String, Object>();
    
    ld.put(SSVarU.entity,        SSVarU.sss + SSStrU.colon  + SSUri.class.getName());
    ld.put(SSVarU.space,         SSVarU.sss + SSStrU.colon  + SSSpaceE.class.getName());
    ld.put(SSVarU.label,         SSVarU.xsd + SSStrU.colon  + SSStrU.valueString);
    ld.put(SSVarU.type,          SSVarU.sss + SSStrU.colon  + SSEntityE.class.getName());
    
    return ld;
  }
  
  public static List<SSSearchResult> distinct(
    final List<SSSearchResult> searchResults) throws Exception{
    
    final List<SSSearchResult> result = new ArrayList<SSSearchResult>();
    
    if(searchResults == null){
      return result;
    }
    
    final List<String> foundEntities = new ArrayList<String>();
    
    for(SSSearchResult searchResult : searchResults){
      
      if(
        searchResult == null ||
        foundEntities.contains(searchResult.toString())) {
        continue;
      }
      
      result.add        (searchResult);
      foundEntities.add (searchResult.toString());
    }
    
    return result;
  }

  public static void addDistinct(
    final List<SSSearchResult> entities,
    final List<SSSearchResult> toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSSearchResult entity : toAddEntities){

      if(!contains(entities, entity)){
        entities.add(entity);
      }
    }
  }
  
  /* getters to allow for json enconding */
  public String getEntity() throws Exception{
    return SSUri.toStrWithoutSlash(entity);
  }

  public String getSpace(){
    return SSSpaceE.toStr(space);
  }

  public String getLabel(){
    return label;
  }

  public String getType(){
    return SSEntityE.toStr(type);
  }
}