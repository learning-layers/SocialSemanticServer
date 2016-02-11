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
package sss.serv.eval.datatypes.par;

import at.tugraz.sss.serv.util.*;
import sss.serv.eval.datatypes.SSEvalLogE;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;

import java.util.ArrayList;
import java.util.List;

public class SSEvalLogPar extends SSServPar{

  public SSToolContextE   toolContext  = null;
  public SSEvalLogE       type         = null;
  public SSUri            entity       = null;
  public String           content      = null;
  public List<SSUri>      entities     = new ArrayList<>();
  public List<SSUri>      users        = new ArrayList<>();
  public Long             creationTime = null;
  
  public String           query        = null;
  public String           result       = null;
  
  public void setToolContext(final String toolContext) throws SSErr {
    this.toolContext = SSToolContextE.get(toolContext);
  }

  public void setType(final String type) throws SSErr{
    this.type = SSEvalLogE.get(type);
  }
  
  public void setEntity(final String entity) throws SSErr{
   this.entity = SSUri.get(entity); 
  }

  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities); 
  }
  
  public void setUsers(final List<String> users)throws SSErr{
    this.users = SSUri.get(users);
  }
  
  public String getToolContext(){
    return SSStrU.toStr(toolContext);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getEntities() throws SSErr{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getUsers() throws SSErr{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public SSEvalLogPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSEvalLogPar(
    final SSServPar servPar,
    final SSUri          user,
    final SSToolContextE toolContext,
    final SSEvalLogE     type, 
    final SSUri          entity, 
    final String         content, 
    final List<SSUri>    entities,
    final List<SSUri>    users,
    final Long           creationTime,
    final boolean        shouldCommit){
    
    super(SSVarNames.evalLog, null, user, servPar.sqlCon);
    
    this.toolContext  = toolContext;
    this.type         = type;
    this.entity       = entity;
    this.content      = content;
    
    SSUri.addDistinctWithoutNull(this.entities, entities);
    SSUri.addDistinctWithoutNull(this.users,    users);
    
    this.creationTime = creationTime;
    this.shouldCommit = shouldCommit;
  }
}