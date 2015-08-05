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

import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import java.util.Map;

public class SSDiscEntry extends SSEntity{
  
  public  Integer             pos;
  public  SSTextComment       content;
  
  public String getContent(){
    return SSStrU.toStr(content);
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarNames.pos,           SSVarNames.xsd + SSStrU.colon + SSStrU.valueInteger);
    ld.put(SSVarNames.content,       SSVarNames.sss + SSStrU.colon + SSTextComment.class.getName());
    
    return ld;
  }
  
  public static SSDiscEntry get(
    final SSUri                  id,
    final SSEntityE              type,
    final int                    pos,
    final SSTextComment          content) throws Exception{
    
    return new SSDiscEntry(
      id,
      type,
      pos,
      content);
  }
  
  public static SSDiscEntry get(
    final SSDiscEntry            entry,
    final SSEntity               entity) throws Exception{
    
    return new SSDiscEntry(
      entry, 
      entity);
  }
  
  protected SSDiscEntry(
    final SSDiscEntry            discEntry,
    final SSEntity               entity) throws Exception{
    
    super(discEntry, entity);
    
    if(discEntry.pos != null){
      this.pos = discEntry.pos;
    }else{
     
      if(entity instanceof SSDiscEntry){
        this.pos = ((SSDiscEntry) discEntry).pos;
      }
    }
    
    if(discEntry.pos != null){
      this.content = discEntry.content;
    }else{
     
      if(entity instanceof SSDiscEntry){
        this.content = ((SSDiscEntry) discEntry).content;
      }
    }
  }
  
  protected SSDiscEntry(
    final SSUri                  id,
    final SSEntityE              type,
    final int                    pos,
    final SSTextComment          content) throws Exception{
    
    super(id, type);
    
    this.pos     = pos;
    this.content = content;
  }
}