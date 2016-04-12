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
package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSCircleUsersInvitePar extends SSServPar{
  
  public SSUri                 circle               = null;
  public List<String>          emails               = new ArrayList<>();

  public String getCircle() throws SSErr{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public void setCircle(final String circle) throws SSErr{
    this.circle = SSUri.get(circle);
  }
  
  public SSCircleUsersInvitePar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSCircleUsersInvitePar(
    final SSServPar servPar,
    final SSUri          user,
    final SSUri          circle,
    final List<String>   emails,
    final boolean        withUserRestriction,
    final boolean        shouldCommit) throws SSErr{
    
    super(SSVarNames.circleUsersInvite, null, user, servPar.sqlCon);
    
    this.circle  = circle;
    
    if(emails != null){
      this.emails.addAll(emails);
    }
    
    this.withUserRestriction     = withUserRestriction;
    this.shouldCommit            = shouldCommit;
  }
}
