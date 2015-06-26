/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServOpE;

public class SSEntityCopyPar extends SSServPar{
  
  public SSUri         entity             = null;
  public List<SSUri>   users              = new ArrayList<>();
  public List<SSUri>   entitiesToExclude  = new ArrayList<>();
  public SSTextComment comment            = null;
  
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
  
  public void setEntitiesToExclude(final List<String> entitiesToExclude) throws Exception{
    this.entitiesToExclude = SSUri.get(entitiesToExclude);
  }
  
  public void setComment(final String comment) throws Exception{
    this.comment = SSTextComment.get(comment);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getEntitiesToExclude() throws Exception{
    return SSStrU.removeTrailingSlash(entitiesToExclude);
  }
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }
  
  public SSEntityCopyPar(){}
    
  public SSEntityCopyPar(
    final SSServOpE     op,
    final String        key,
    final SSUri         user,
    final SSUri         entity, 
    final List<SSUri>   users, 
    final List<SSUri>   entitiesToExclude, 
    final SSTextComment comment, 
    final Boolean       shouldCommit){
    
    super(op, key, user);
    
    this.entity            = entity;
        
    SSUri.addDistinctWithoutNull(this.users,             users);
    SSUri.addDistinctWithoutNull(this.entitiesToExclude, entitiesToExclude);    
    
    this.comment      = comment;
    this.shouldCommit = shouldCommit;
  }
}