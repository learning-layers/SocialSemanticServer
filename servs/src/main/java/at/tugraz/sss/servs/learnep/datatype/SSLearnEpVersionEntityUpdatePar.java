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
package at.tugraz.sss.servs.learnep.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.sql.*;

public class SSLearnEpVersionEntityUpdatePar extends SSServPar{
  
  public SSUri    learnEpEntity      = null;
  public SSUri    entity             = null;
  public Float    x                  = null;
  public Float    y                  = null;
  
  public String getLearnEpEntity(){
    return SSStrU.removeTrailingSlash(learnEpEntity);
  }
  
  public void setLearnEpEntity(String learnEpEntity) throws SSErr{
    this.learnEpEntity = SSUri.get(learnEpEntity);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public void setEntity(String entity) throws SSErr{
    this.entity = SSUri.get(entity);
  }
  
  public SSLearnEpVersionEntityUpdatePar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSLearnEpVersionEntityUpdatePar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         learnEpEntity,
    final SSUri         entity,
    final Float         x,
    final Float         y,
    final boolean       withUserRestriction,
    final boolean       shouldCommit){
    
    super(SSVarNames.learnEpVersionEntityUpdate, null, user, servPar.sqlCon);
    
    this.learnEpEntity       = learnEpEntity;
    this.entity              = entity;
    this.x                   = x;
    this.y                   = y;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}