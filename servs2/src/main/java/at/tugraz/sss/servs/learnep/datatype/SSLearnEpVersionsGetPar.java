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

import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSLearnEpVersionsGetPar extends SSServPar{
  
  public SSUri        learnEp              = null;
  public List<SSUri>  learnEpVersions      = new ArrayList<>();
  
  public String getLearnEp(){
    return SSStrU.removeTrailingSlash(learnEp);
  }

  public void setLearnEp(final String learnEp) throws SSErr{
    this.learnEp = SSUri.get(learnEp);
  }

  public List<String> getLearnEpVersions() {
    return SSStrU.removeTrailingSlash(learnEpVersions);
  }

  public void setLearnEpVersions(final List<String> learnEpVersions) throws SSErr {
    this.learnEpVersions = SSUri.get(learnEpVersions);
  }
  
  public SSLearnEpVersionsGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSLearnEpVersionsGetPar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         learnEp,
    final List<SSUri>   learnEpVersions,
    final boolean       withUserRestriction, 
    final boolean       invokeEntityHandlers){
      
    super(SSVarNames.learnEpVersionsGet, null, user, servPar.sqlCon);
    
    this.learnEp              = learnEp;
    
    SSUri.addDistinctWithoutNull(this.learnEpVersions, learnEpVersions);
    
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}