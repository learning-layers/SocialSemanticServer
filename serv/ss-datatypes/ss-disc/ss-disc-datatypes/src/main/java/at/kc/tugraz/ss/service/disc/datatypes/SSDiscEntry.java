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
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSDiscEntry extends SSEntity{
  
  public  Integer             pos;
  public  SSTextComment       content;

  public static SSDiscEntry get(
    final SSUri                  id,
    final SSEntityE              type,
    final int                    pos,
    final SSTextComment          content,
    final Long                   creationTime,
    final List<SSEntity>         attachedEntities,
    final SSUri                  author, 
    final List<SSTextComment>    comments,
    final SSEntityA              overallRating, //new
    final List<String>           tags, //new
    final List<SSEntityA>        discs,//new
    final List<SSEntityA>        uEs,  //new
    final String                 thumb,//new
    final SSUri                  file, //new
    final List<SSEntityA>        flags) throws Exception{
    
    return new SSDiscEntry(
      id, 
      type, 
      pos, 
      content, 
      creationTime, 
      attachedEntities, 
      author, 
      comments, 
      overallRating, 
      tags, 
      discs, 
      uEs, 
      thumb, 
      file, 
      flags);
  }
  
  private SSDiscEntry(
    final SSUri                  id,
    final SSEntityE              type,
    final int                    pos,
    final SSTextComment          content,
    final Long                   creationTime,
    final List<SSEntity>         attachedEntities,
    final SSUri                  author,
    final List<SSTextComment>    comments,
    final SSEntityA              overallRating, //new
    final List<String>           tags, //new
    final List<SSEntityA>        discs,//new
    final List<SSEntityA>        uEs,  //new
    final String                 thumb,//new
    final SSUri                  file, //new
    final List<SSEntityA>        flags) throws Exception{
    
    super(
      id,
      null,
      creationTime,
      type,
      author,
      null,
      null,
      null,
      attachedEntities, 
      comments, 
      overallRating, 
      tags, 
      discs, 
      uEs, 
      thumb, 
      file, 
      flags);
    
    this.pos          = pos;
    this.content      = content;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld         = new HashMap<>();
    
    ld.put(SSVarU.pos,           SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    ld.put(SSVarU.content,       SSVarU.sss + SSStrU.colon + SSTextComment.class.getName());
    
    return ld;
  }

  /* json getters */
  
  public String getContent(){
    return SSStrU.toStr(content);
  }
}
