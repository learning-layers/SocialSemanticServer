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

package at.tugraz.sss.servs.user.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 

public class SSUserAddPar extends SSServPar{
  
  public SSLabel    label        = null;
  public String     email        = null;
  public String     oidcSub      = null;
  public boolean    isSystemUser = false;

  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public SSUserAddPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSUserAddPar(
    final SSServPar servPar,
    final SSUri     user,
    final boolean   shouldCommit,
    final SSLabel   label,
    final String    email, 
    final String    oidcSub,
    final boolean   isSystemUser,
    final boolean   withUserRestriction){
    
    super(SSVarNames.userAdd, null, user, servPar.sqlCon);
    
    this.shouldCommit        = shouldCommit;
    this.label               = label;
    this.email               = email;
    this.oidcSub             = oidcSub;
    this.isSystemUser        = isSystemUser;
    this.withUserRestriction = withUserRestriction;
  }
}