/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.learnep.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.sql.*;

public class SSLearnEpLockHoldPar extends SSServPar{
  
  public SSUri  learnEp = null;

  public String getLearnEp(){
    return SSStrU.removeTrailingSlash(learnEp);
  }

  public void setLearnEp(final String learnEp) throws SSErr{
    this.learnEp = SSUri.get(learnEp);
  }
  
  public SSLearnEpLockHoldPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSLearnEpLockHoldPar(
    final SSServPar servPar,
    final SSUri       user,
    final SSUri       learnEp, 
    final boolean     withUserRestriction){
      
    super(SSVarNames.learnEpLockHold, null, user, servPar.sqlCon);
    
    this.learnEp             = learnEp;
    this.withUserRestriction = withUserRestriction;
  }
}