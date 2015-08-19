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
package at.kc.tugraz.ss.circle.datatypes.par;

import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;

public class SSCircleCreateFromClientPar extends SSCircleCreatePar{
  
  public List<SSUri>     users    = new ArrayList<>();
  public List<String>    invitees = new ArrayList<>();
  public List<SSUri>     entities = new ArrayList<>();
  
  public List<String> getUsers(){
    return SSStrU.removeTrailingSlash(users);
  }
  
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
  
  public List<String> getEntities(){
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  public SSCircleCreateFromClientPar(){}
  
  public SSCircleCreateFromClientPar(
    final SSUri           user,
    final SSLabel         label,
    final SSTextComment   description,
    final List<SSUri>     users,
    final List<String>    invitees,
    final List<SSUri>     entities){
    
    super(
      user,
      SSCircleE.group,//circleType,
      label,
      description,
      false, //isSystemCircle
      true, //withUserRestriction
      true); //shouldCommit
    
    SSUri.addDistinctWithoutNull(this.users,    users);

    if(invitees != null){
      this.invitees.addAll(invitees);
    }

    SSUri.addDistinctWithoutNull(this.entities, entities);
  }
}