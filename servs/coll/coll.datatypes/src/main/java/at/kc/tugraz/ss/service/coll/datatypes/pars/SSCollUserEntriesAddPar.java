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
 package at.kc.tugraz.ss.service.coll.datatypes.pars;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
 import at.tugraz.sss.serv.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSCollUserEntriesAddPar extends SSServPar{
  
  public SSUri             coll          = null;
  public List<SSUri>       entries       = new ArrayList<>();
  public List<SSLabel>     labels        = new ArrayList<>();
  
  public void setColl(final String coll) throws SSErr{
    this.coll = SSUri.get(coll);
  }
  
  public void setEntries(final List<String> entries) throws SSErr{
    this.entries = SSUri.get(entries);
  }

  public void setLabels(final List<String> labels) throws SSErr{
    this.labels = SSLabel.get(labels);
  }

  public String getColl(){
    return SSStrU.removeTrailingSlash(coll);
  }

  public List<String> getEntries(){
    return SSStrU.removeTrailingSlash(entries);
  }

  public List<String> getLabels(){
    return SSStrU.toStr(labels);
  }
  
  public SSCollUserEntriesAddPar(){}
  
  public SSCollUserEntriesAddPar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         coll, 
    final List<SSUri>   entries, 
    final List<SSLabel> labels,
    final boolean       withUserRestriction,
    final boolean       shouldCommit){
    
    super(SSVarNames.collEntriesAdd, null, user, servPar.sqlCon);
    
    this.coll = coll;
    
    SSUri.addDistinctWithoutNull  (this.entries, entries);
    SSLabel.addDistinctNotNull    (this.labels,  labels);
  }
}
