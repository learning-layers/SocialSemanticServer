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
package at.tugraz.sss.servs.common.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import java.util.ArrayList;
import java.util.List;

public class SSEntityCopyPar extends SSServPar{
  
  public SSUri         entity                                        = null;
  public SSUri         targetEntity                                  = null;
  public List<SSUri>   forUsers                                      = new ArrayList<>();
  public SSLabel       label                                         = null;
  public boolean       appendUserNameToLabel                         = false;
  public boolean       includeUsers                                  = false;
  public boolean       includeEntities                               = false;
  public boolean       includeMetadataSpecificToEntityAndItsEntities = false;
  public boolean       includeOriginUser                             = false;
  public List<SSUri>   entitiesToExclude                             = new ArrayList<>();
  public SSTextComment comment                                       = null;
    
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity);
  }
  
  public void setTargetEntity(final String targetEntity) throws SSErr{
    this.targetEntity = SSUri.get(targetEntity);
  }
  
  public void setForUsers(final List<String> forUsers) throws SSErr{
    this.forUsers = SSUri.get(forUsers);
  }
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public void setEntitiesToExclude(final List<String> entitiesToExclude) throws SSErr{
    this.entitiesToExclude = SSUri.get(entitiesToExclude);
  }
  
  public void setComment(final String comment) throws SSErr{
    this.comment = SSTextComment.get(comment);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getTargetEntity(){
    return SSStrU.removeTrailingSlash(targetEntity);
  }
  
  public List<String> getForUsers() throws SSErr{
    return SSStrU.removeTrailingSlash(forUsers);
  }
  
  public String getLabel() throws SSErr{
    return SSStrU.toStr(label);
  }
  
  public List<String> getEntitiesToExclude() throws SSErr{
    return SSStrU.removeTrailingSlash(entitiesToExclude);
  }
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }
  
  public SSEntityCopyPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSEntityCopyPar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         entity, 
    final SSUri         targetEntity,
    final List<SSUri>   forUsers, 
    final SSLabel       label,
    final boolean       includeUsers,
    final boolean       includeEntities,
    final boolean       includeMetadataSpecificToEntityAndItsEntities,
    final boolean       includeOriginUser,
    final List<SSUri>   entitiesToExclude, 
    final SSTextComment comment, 
    final boolean       withUserRestriction,
    final boolean       shouldCommit){
    
    super(SSVarNames.entityCopy, null, user, servPar.sqlCon);
    
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