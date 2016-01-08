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
package at.kc.tugraz.ss.service.disc.datatypes.pars;

import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import java.util.ArrayList;
import java.util.List;

public class SSDiscEntryAddFromClientPar extends SSDiscEntryAddPar{
  
  public List<SSUri>         users          = new ArrayList<>();
  public List<SSUri>         circles        = new ArrayList<>();
  
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users); 
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public void setCircles(final List<String> circles) throws Exception{
    this.circles = SSUri.get(circles); 
  }
  
  public List<String> getCircles() throws Exception{
    return SSStrU.removeTrailingSlash(circles);
  }
  
  public SSDiscEntryAddFromClientPar(){}
    
  public SSDiscEntryAddFromClientPar(
    final SSUri         disc,
    final List<SSUri>   targets, 
    final SSTextComment entry, 
    final boolean       addNewDisc,
    final SSEntityE     type, 
    final SSLabel       label, 
    final SSTextComment description, 
    final List<SSUri>   users, 
    final List<SSUri>   circles, 
    final List<SSUri>   entities, 
    final List<SSLabel> entityLabels){
    
    super(
      null, 
      disc, 
      targets, 
      entry, 
      addNewDisc, 
      type, 
      label, 
      description, 
      entities, 
      entityLabels, 
      true); //shouldCommit
    
    SSUri.addDistinctWithoutNull(this.users,   users);
    SSUri.addDistinctWithoutNull(this.circles, circles);
  }
}
