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
package at.tugraz.sss.servs.activity.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SSActivityAddPar extends SSServPar{

  public SSActivityE            type             = null;
  public SSUri                  entity           = null;
  public List<SSUri>            users            = new ArrayList<>();
  public List<SSUri>            entities         = new ArrayList<>();
  public List<SSTextComment>    comments         = new ArrayList<>();
  public Long                   creationTime     = null;
  
  public void setType(final String type) throws SSErr{
    this.type = SSActivityE.get(type);
  }

  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity);
  }
  
  public void setComments(final List<String> comments) throws SSErr{
    this.comments = SSTextComment.get(comments);
  }
  
  public void setUsers(final List<String> users) throws SSErr{
    this.users = SSUri.get(users);
  }
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities);
  }

  public String getType() throws SSErr{
    return SSStrU.toStr(type);
  }
  
  public String getEntity() throws SSErr{
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getUsers() throws SSErr{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getEntities() throws SSErr{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getComments() throws SSErr{
    return SSStrU.removeTrailingSlash(comments);
  }
  
  public SSActivityAddPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSActivityAddPar(
    final SSServPar servPar,
    final SSUri                 user, 
    final SSActivityE           type, 
    final SSUri                 entity, 
    final List<SSUri>           users, 
    final List<SSUri>           entities, 
    final List<SSTextComment>   comments, 
    final Long                  creationTime, 
    final boolean               shouldCommit){
    
    super(SSVarNames.activityAdd, null, user, servPar.sqlCon);
    
    this.type          = type;
    this.entity        = entity;
    
    SSUri.addDistinctWithoutNull         (this.users,    users);
    SSUri.addDistinctWithoutNull         (this.entities, entities);
    SSTextComment.addDistinctWithoutNull (this.comments, comments);
    
    this.creationTime  = creationTime;
    this.shouldCommit  = shouldCommit;
  }
}