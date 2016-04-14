/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.coll.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.util.ArrayList;
import java.util.List;

public class SSCollUserEntriesDeletePar extends SSServPar{
  
  public SSUri       coll     = null;
  public List<SSUri> entries  = new ArrayList<>();
  
  public void setColl(final String coll) throws SSErr{
    this.coll = SSUri.get(coll);
  }
  
  public void setEntries(final List<String> entries) throws SSErr{
    this.entries = SSUri.get(entries);
  }
  
  public String getColl(){
    return SSStrU.removeTrailingSlash(coll);
  }
  
  public List<String> getEntries() throws SSErr{
    return SSStrU.removeTrailingSlash(entries);
  }
  
  public SSCollUserEntriesDeletePar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSCollUserEntriesDeletePar(
    final SSServPar servPar, 
    final SSUri          user,
    final SSUri          coll,
    final List<SSUri>    entries,
    final boolean        withUserRestriction,
    final boolean        shouldCommit){
    
    super(SSVarNames.collEntriesDelete, null, user, servPar.sqlCon);
    
    this.coll     = coll;
    
    SSUri.addDistinctWithoutNull(this.entries, entries);
  }
}