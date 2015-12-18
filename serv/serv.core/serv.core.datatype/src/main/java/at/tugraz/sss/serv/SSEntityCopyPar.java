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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.List;

public class SSEntityCopyPar extends SSServPar{
  
  public SSUri         entity                                        = null;
  public SSUri         targetEntity                                  = null;
  public List<SSUri>   forUsers                                      = new ArrayList<>();
  public SSLabel       label                                         = null;
  public Boolean       appendUserNameToLabel                         = false;
  public Boolean       includeUsers                                  = false;
  public Boolean       includeEntities                               = false;
  public Boolean       includeMetadataSpecificToEntityAndItsEntities = false;
  public Boolean       includeOriginUser                             = false;
  public List<SSUri>   entitiesToExclude                             = new ArrayList<>();
  public SSTextComment comment                                       = null;
    
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  public void setTargetEntity(final String targetEntity) throws Exception{
    this.targetEntity = SSUri.get(targetEntity);
  }
  
  public void setForUsers(final List<String> forUsers) throws Exception{
    this.forUsers = SSUri.get(forUsers);
  }
  
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
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
  
  public String getTargetEntity(){
    return SSStrU.removeTrailingSlash(targetEntity);
  }
  
  public List<String> getForUsers() throws Exception{
    return SSStrU.removeTrailingSlash(forUsers);
  }
  
  public String getLabel() throws Exception{
    return SSStrU.toStr(label);
  }
  
  public List<String> getEntitiesToExclude() throws Exception{
    return SSStrU.removeTrailingSlash(entitiesToExclude);
  }
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }
  
  public SSEntityCopyPar(){}
    
  public SSEntityCopyPar(
    final SSUri         user,
    final SSUri         entity, 
    final SSUri         targetEntity,
    final List<SSUri>   forUsers, 
    final SSLabel       label,
    final Boolean       includeUsers,
    final Boolean       includeEntities,
    final Boolean       includeMetadataSpecificToEntityAndItsEntities,
    final Boolean       includeOriginUser,
    final List<SSUri>   entitiesToExclude, 
    final SSTextComment comment, 
    final Boolean       withUserRestriction,
    final Boolean       shouldCommit){
    
    super(SSVarNames.entityCopy, null, user);
    
    this.entity       = entity;
    this.targetEntity = targetEntity;
    
    SSUri.addDistinctWithoutNull(this.forUsers,          forUsers);
    
    this.label                                           = label;
    this.includeUsers                                    = includeUsers;
    this.includeEntities                                 = includeEntities;
    this.includeMetadataSpecificToEntityAndItsEntities   = includeMetadataSpecificToEntityAndItsEntities;
    this.includeOriginUser                               = includeOriginUser;
    
    SSUri.addDistinctWithoutNull(this.entitiesToExclude, entitiesToExclude);    
    
    this.comment             = comment;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}