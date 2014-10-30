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
 package at.kc.tugraz.ss.message.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import java.util.Map;

public class SSMessage extends SSEntity{
  
  public SSUri          user     = null;
  public SSUri          forUser  = null;
  public SSTextComment  content  = null;
  
  public static SSMessage get(
    final SSUri         message,
    final SSUri         user, 
    final SSUri         forUser,
    final SSTextComment content,
    final Long          creationTime) throws Exception{
    
    return new SSMessage(message, user, forUser, content, creationTime);
  }  
	
  protected SSMessage(    
    final SSUri         message,
    final SSUri         user, 
    final SSUri         forUser,
    final SSTextComment content,
    final Long          creationTime) throws Exception{
    
    super(message, SSEntityE.message);
    
    this.user         = user;
    this.forUser      = forUser;
    this.content      = content;
    this.creationTime = creationTime;
	}

  @Override
  public Object jsonLDDesc() {
  
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarU.user,    SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.forUser, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.content, SSVarU.sss + SSStrU.colon + SSTextComment.class.getName());
    
    return ld;
  }  
  
  /* json getters */
  public String getUser(){
    return SSStrU.removeTrailingSlash(user);
  }

  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public String getContent(){
    return SSStrU.toStr(content);
  }
}