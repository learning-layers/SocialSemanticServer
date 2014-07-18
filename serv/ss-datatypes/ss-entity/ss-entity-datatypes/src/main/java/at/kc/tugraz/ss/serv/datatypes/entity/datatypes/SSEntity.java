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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntity extends SSEntityA{

  public SSUri            id           = null;
  public SSLabel          label        = null;
  public Long             creationTime = null;
  public SSEntityE        type         = null;
  public SSUri            author       = null;
  public SSTextComment    description  = null;
  public List<SSCircleE>  circleTypes  = new ArrayList<>();
  public List<SSEntityA>  entries      = new ArrayList<>();
  
  protected SSEntity(
    final SSUri            uri,
    final SSLabel          label, 
    final Long             creationTime,
    final SSEntityE        type,
    final SSUri            author,
    final SSTextComment    description,
    final List<SSCircleE>  circleTypes,
    final List<SSEntityA>  entries) throws Exception{
    
    super(uri);
    
    this.id          = uri;
    this.label        = label;
    this.creationTime = creationTime;
    this.type         = type;
    this.author       = author;
    this.description  = description;
    
    if(circleTypes != null){
      this.circleTypes.addAll(circleTypes);
    }
    
    if(entries != null){
      this.entries.addAll(entries);
    }
  }
  
  public static SSEntity get(
    final SSUri            uri,
    final SSLabel          label, 
    final Long             creationTime,
    final SSEntityE        type,
    final SSUri            author,
    final SSTextComment    description,
    final List<SSCircleE>  circleTypes,
    final List<SSEntityA>  entries) throws Exception{
    
    return new SSEntity(uri, label, creationTime, type, author, description, circleTypes, entries);
  }

  @Override
  public Object jsonLDDesc() {
   
    final Map<String, Object> ld             = new HashMap<>();
    final Map<String, Object> circleTypesObj = new HashMap<>();
    final Map<String, Object> entriesObj     = new HashMap<>();
    
    ld.put(SSVarU.id,             SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,          SSVarU.sss + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.creationTime,   SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.type,           SSVarU.sss + SSStrU.colon + SSEntityE.class.getName());
    ld.put(SSVarU.author,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.description,    SSVarU.sss + SSStrU.colon + SSTextComment.class.getName());
    
    circleTypesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCircleE.class.getName());
    circleTypesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circleTypes, circleTypesObj);
    
    entriesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityA.class.getName());
    entriesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entries, entriesObj);
    
    return ld;
  }

  /* getters for json */
  
  public String getId() throws Exception{
    return SSStrU.removeTrailingSlash(id);
  }

  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public Long getCreationTime(){
    return creationTime;
  }

  public SSEntityE getType(){
    return type;
  }
  
  public String getAuthor() throws Exception{
    return SSStrU.removeTrailingSlash(author);
  }
  
  public String getDescription() throws Exception{
    return SSStrU.toStr(description);
  }
  
  public List<SSCircleE> getCircleTypes(){
    return circleTypes;
  }
  
  public List<SSEntityA> getEntries(){
    return entries;
  }
}
