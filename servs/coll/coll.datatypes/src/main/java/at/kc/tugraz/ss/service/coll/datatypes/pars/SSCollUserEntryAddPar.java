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
 package at.kc.tugraz.ss.service.coll.datatypes.pars;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
 import at.tugraz.sss.serv.util.*;
import java.sql.*;

public class SSCollUserEntryAddPar extends SSServPar{
  
  public SSUri        coll        = null;
  public SSLabel      label       = null;
  public boolean      addNewColl  = false;
  public SSUri        entry       = null;
  
  public void setColl(final String coll) throws SSErr{
    this.coll = SSUri.get(coll);
  }
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public void setEntry(final String entry) throws SSErr{
    this.entry = SSUri.get(entry);
  }
  
  public String getColl(){
    return SSStrU.removeTrailingSlash(coll);
  }
  
  public String getEntry(){
    return SSStrU.removeTrailingSlash(entry);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public SSCollUserEntryAddPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSCollUserEntryAddPar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         coll,
    final SSUri         entry,
    final SSLabel       label,
    final boolean       addNewColl, 
    final boolean       withUserRestriction, 
    final boolean       shouldCommit){
  
    super(SSVarNames.collEntryAdd, null, user, servPar.sqlCon);
    
    this.coll                = coll;
    this.entry               = entry;
    this.label               = label;
    this.addNewColl          = addNewColl;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
