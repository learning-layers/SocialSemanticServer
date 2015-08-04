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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;

public class SSMessage extends SSEntity{
  
  public SSEntity       user     = null;
  public SSEntity       forUser  = null;
  public SSTextComment  content  = null;
  
  public String getContent(){
    return SSStrU.toStr(content);
  }
  
  @Override
  public Object jsonLDDesc() {
    throw new UnsupportedOperationException();
  }
  
  public static SSMessage get(
    final SSMessage        message,
    final SSEntity         entity) throws Exception{
    
    return new SSMessage(message, entity);
  } 
  
  protected SSMessage(    
    final SSMessage        message,
    final SSEntity         entity) throws Exception{
    
    super(message, entity);
    
    this.user         = message.user;
    this.forUser      = message.forUser;
    this.content      = message.content;
	}
  
  public static SSMessage get(
    final SSUri         message,
    final SSEntity      user,
    final SSEntity      forUser,
    final SSTextComment content) throws Exception{
    
    return new SSMessage(message, user, forUser, content);
  }  
	
  protected SSMessage(    
    final SSUri         message,
    final SSEntity      user, 
    final SSEntity      forUser,
    final SSTextComment content) throws Exception{
    
    super(message, SSEntityE.message);
    
    this.user         = user;
    this.forUser      = forUser;
    this.content      = content;
	}
}