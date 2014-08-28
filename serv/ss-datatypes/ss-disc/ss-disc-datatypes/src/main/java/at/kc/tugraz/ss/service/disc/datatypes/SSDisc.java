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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import java.util.*;

public class SSDisc extends SSEntity {
  
  public  SSUri  entity = null;

  public static SSDisc get(
    final SSUri                  id,
    final SSLabel                label,
    final SSUri                  author,
    final SSUri                  entity,
    final SSEntityE              type,
    final List<SSDiscEntry>      entries,
    final SSTextComment          explanation,
    final Long                   creationTime,
    final List<SSCircleE>        circleTypes,
    final List<SSEntity>         attachedEntities,
    final List<SSTextComment>    comments,
    final SSEntityA              overallRating, //new
    final List<String>           tags, //new
    final List<SSEntityA>        discs,//new
    final List<SSEntityA>        uEs,  //new
    final String                 thumb,//new
    final SSUri                  file, //new
    final List<SSEntityA>        flags) throws Exception{
    
    return new SSDisc(
      id, 
      label, 
      author, 
      entity, 
      type, 
      entries, 
      explanation, 
      creationTime, 
      circleTypes, 
      attachedEntities, 
      comments, 
      overallRating, 
      tags, 
      discs, 
      uEs, 
      thumb, 
      file, 
      flags);
  }

  private SSDisc(
    final SSUri                  id,
    final SSLabel                label,
    final SSUri                  author,
    final SSUri                  entity,
    final SSEntityE              type,
    final List<SSDiscEntry>      entries, 
    final SSTextComment          description,
    final Long                   creationTime,
    final List<SSCircleE>        circleTypes,
    final List<SSEntity>         attachedEntities,
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
      label,
      creationTime,
      type,
      author,
      description,
      circleTypes,
      entries,
      attachedEntities,
      comments, 
      overallRating, 
      tags, 
      discs, 
      uEs, 
      thumb, 
      file, 
      flags);
    
    this.entity = entity;
  }
  
  @Override
  public Object jsonLDDesc() {
   
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarU.entity, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    
    return ld;
  }

  /* json getters */
  
  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }
}