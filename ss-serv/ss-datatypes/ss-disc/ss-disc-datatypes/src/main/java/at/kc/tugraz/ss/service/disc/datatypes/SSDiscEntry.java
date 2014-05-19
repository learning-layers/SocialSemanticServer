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

import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import java.util.HashMap;
import java.util.Map;

public class SSDiscEntry extends SSEntityA{
  
  public  SSUri               uri;
  public  Integer             pos;
  public  SSTextComment       content;
  public  SSUri               author;
  public  Long                timestamp; 
  public  SSEntityE           discEntryType;

  public static SSDiscEntry get(
    final SSUri                 uri,
    final SSEntityE             discEntryType,
    final int                   pos,
    final SSTextComment         content,
    final SSUri                 author,
    final Long                  timestamp) throws Exception{
    
    return new SSDiscEntry(uri, discEntryType, pos, content, author, timestamp);
  }
  
  private SSDiscEntry(
    final SSUri                 uri,
    final SSEntityE             discEntryType,
    final int                   pos,
    final SSTextComment         content,
    final SSUri                 author,
    final Long                  timestamp) throws Exception{
    
    super(uri);
    
    this.uri           = uri;
    this.discEntryType = discEntryType;
    this.pos           = pos;
    this.content       = content;
    this.author        = author;
    this.timestamp     = timestamp;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld         = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri,           SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.discEntryType, SSVarU.sss + SSStrU.colon + SSEntityE.class.getName());
    ld.put(SSVarU.pos,           SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    ld.put(SSVarU.content,       SSVarU.sss + SSStrU.colon + SSTextComment.class.getName());
    ld.put(SSVarU.author,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.timestamp,     SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    
    return ld;
  }

  /* getters to allow for json enconding */
  
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }
  
  public String getDiscEntryType() throws Exception{
    return SSEntityE.toStr(discEntryType);
  }

  public int getPos(){
    return pos;
  }

  public String getContent(){
    return SSTextComment.toStr(content);
  }

  public String getAuthor() throws Exception{
    return SSUri.toStrWithoutSlash(author);
  }

  public Long getTimestamp(){
    return timestamp;
  }
}
