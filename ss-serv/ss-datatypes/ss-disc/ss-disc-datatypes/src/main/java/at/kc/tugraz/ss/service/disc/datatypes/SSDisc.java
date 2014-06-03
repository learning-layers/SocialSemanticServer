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
 package at.kc.tugraz.ss.service.disc.datatypes;

import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import java.util.*;

public class SSDisc extends SSEntityA {
  
  public  SSUri             id           = null;
  public  SSLabel           label        = null;
  public  SSUri             author       = null;
  public  SSUri             entity       = null;
  public  SSEntityE         type         = null;
  public  List<SSDiscEntry> entries      = new ArrayList<SSDiscEntry>();
  public  SSTextComment     explanation  = null;
  public  Long              creationTime = null;

  public static SSDisc get(
    final SSUri             uri,
    final SSLabel           label,
    final SSUri             author,
    final SSUri             target,
    final SSEntityE         discType,
    final List<SSDiscEntry> entries,
    final SSTextComment     explanation,
    final Long              creationTime) throws Exception{
    
    return new SSDisc(uri, label, author, target, discType, entries, explanation, creationTime);
  }

  private SSDisc(
    final SSUri             uri,
    final SSLabel           label,
    final SSUri             author,
    final SSUri             target,
    final SSEntityE         discType,
    final List<SSDiscEntry> entries, 
    final SSTextComment     explanation,
    final Long              creationTime) throws Exception{
    
    super(uri);
    
    this.id       = uri;
    this.label        = label;
    this.author       = author;
    this.entity   = target;
    this.type     = discType;
    this.explanation  = explanation;
    this.creationTime = creationTime;
    
    if(entries != null){
      this.entries.addAll(entries);
    }
  }
  
  @Override
  public Object jsonLDDesc() {
   
    final Map<String, Object> ld         = new HashMap<String, Object>();
    final Map<String, Object> entriesObj = new HashMap<String, Object>();
    
    ld.put(SSVarU.id, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    
    entriesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSDiscEntry.class.getName());
    entriesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entries, entriesObj);
    
    ld.put(SSVarU.author,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.entity,   SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,        SSVarU.sss + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.type,     SSVarU.sss + SSStrU.colon + SSEntityE.class.getName());
    ld.put(SSVarU.explanation,  SSVarU.sss + SSStrU.colon + SSTextComment.class.getName());
    ld.put(SSVarU.creationTime, SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    
    return ld;
  }

  /* getters to allow for jason enconding */
  public String getId() throws Exception{
    return SSUri.toStrWithoutSlash(id);
  }

  public String getLabel(){
    return SSLabel.toStr(label);
  }

  public String getAuthor() throws Exception{
    return SSUri.toStrWithoutSlash(author);
  }

  public String getEntity() throws Exception{
    return SSUri.toStrWithoutSlash(entity);
  }
  
  public String getType() throws Exception{
    return SSEntityE.toStr(type);
  }

  public List<SSDiscEntry> getEntries(){
    return entries;
  }
  
  public String getExplanation() throws Exception{
    return SSTextComment.toStr(explanation);
  }
  
  public Long getCreationTime() throws Exception{
    return creationTime;
  }
}