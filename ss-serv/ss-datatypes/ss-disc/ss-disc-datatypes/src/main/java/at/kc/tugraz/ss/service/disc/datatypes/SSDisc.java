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
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import java.util.*;

public class SSDisc extends SSEntityA {
  
  public  SSUri             uri      = null;
  public  SSLabel           label    = null;
  public  SSUri             author   = null;
  public  SSUri             target   = null;
  public  SSEntityE         discType = null;
  public  List<SSDiscEntry> entries  = new ArrayList<SSDiscEntry>();

  public static SSDisc get(
    final SSUri             uri,
    final SSLabel           label,
    final SSUri             author,
    final SSUri             target,
    final SSEntityE         discType,
    final List<SSDiscEntry> entries) throws Exception{
    
    return new SSDisc(uri, label, author, target, discType, entries);
  }

  private SSDisc(
    final SSUri             uri,
    final SSLabel           label,
    final SSUri             author,
    final SSUri             target,
    final SSEntityE         discType,
    final List<SSDiscEntry> entries)throws Exception{
    
    super(uri);
    
    this.uri      = uri;
    this.label    = label;
    this.author   = author;
    this.target   = target;
    
    if(entries != null){
      this.entries.addAll(entries);
    }
  }
  
  @Override
  public Object jsonLDDesc() {
   
    final Map<String, Object> ld         = new HashMap<String, Object>();
    final Map<String, Object> entriesObj = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    
    entriesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSDiscEntry.class.getName());
    entriesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entries, entriesObj);
    
    ld.put(SSVarU.author,   SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.target,   SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,    SSVarU.sss + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.discType, SSVarU.sss + SSStrU.colon + SSEntityE.class.getName());
    
    return ld;
  }

  /* getters to allow for jason enconding */
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }

  public String getLabel(){
    return SSLabel.toStr(label);
  }

  public String getAuthor() throws Exception{
    return SSUri.toStrWithoutSlash(author);
  }

  public String getTarget() throws Exception{
    return SSUri.toStrWithoutSlash(target);
  }
  
  public String getDiscType() throws Exception{
    return SSEntityE.toStr(discType);
  }

  public List<SSDiscEntry> getEntries(){
    return entries;
  }
}