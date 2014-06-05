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

package at.kc.tugraz.ss.activity.datatypes.enums;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSActivity extends SSEntityA{
  
  public SSUri               id           = null;
  public SSActivityE         type         = null;
  public Long                creationTime = null;
  public SSUri               author       = null;
  public List<SSUri>         users        = new ArrayList<SSUri>();
  public List<SSUri>         entities     = new ArrayList<SSUri>();
  public List<SSTextComment> comments     = new ArrayList<SSTextComment>();

  public static SSActivity get(
    final SSUri               id,
    final SSActivityE         type, 
    final Long                creationTime, 
    final SSUri               author, 
    final List<SSUri>         users,
    final List<SSUri>         entities,
    final List<SSTextComment> comments) throws Exception{
    
    return new SSActivity(id, type, creationTime, author, users, entities, comments);    
  }
  
  private SSActivity(
    final SSUri               id,
    final SSActivityE         type, 
    final Long                creationTime, 
    final SSUri               author, 
    final List<SSUri>         users,
    final List<SSUri>         entities,
    final List<SSTextComment> comments) throws Exception{
    
    super(id);
    
    this.id   = id;
    this.type = type;
    this.creationTime = creationTime;
    this.author   = author;
    
    if(users != null){
      this.users.addAll(users);
    }
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    if(comments != null){
      this.comments.addAll(comments);
    }
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld              = new HashMap<String, Object>();
    final Map<String, Object> entitiesObj     = new HashMap<String, Object>();
    final Map<String, Object> usersObj        = new HashMap<String, Object>();
    final Map<String, Object> commentsObj     = new HashMap<String, Object>();
    
    ld.put(SSVarU.id,           SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.type,         SSVarU.sss + SSStrU.colon + SSActivityE.class.getName());
    ld.put(SSVarU.creationTime, SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.author,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    
    usersObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    usersObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.users, usersObj);
    
    entitiesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    entitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entities, entitiesObj);
    
    commentsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSTextComment.class.getName());
    commentsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.users, commentsObj);
    
    return ld;
  }
  
  
  /* json getters */

  public String getId(){
    return SSUri.toStrWithoutSlash(id);
  }

  public String getType(){
    return SSActivityE.toStr(type);
  }

  public Long getCreationTime(){
    return creationTime;
  }

  public String getAuthor(){
    return SSUri.toStrWithoutSlash(author);
  }

  public List<String> getUsers(){
    return SSUri.toStrWithoutSlash(users);
  }

  public List<String> getEntities(){
    return SSUri.toStrWithoutSlash(entities);
  }

  public List<String> getComments(){
    return SSTextComment.toStr(comments);
  }
}