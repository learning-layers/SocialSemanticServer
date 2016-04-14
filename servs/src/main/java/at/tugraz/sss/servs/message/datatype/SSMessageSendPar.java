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

package at.tugraz.sss.servs.message.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import java.sql.*;

public class SSMessageSendPar extends SSServPar{
  
  public SSUri          forUser    = null;
  public SSTextComment  message    = null;
  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser);
  }
  
  public void setMessage(final String message) throws SSErr{
    this.message = SSTextComment.get(message);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public String getMessage() throws SSErr{
    return SSStrU.toStr(message);
  }
  
  public SSMessageSendPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSMessageSendPar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         forUser, 
    final SSTextComment message, 
    final boolean       shouldCommit){
    
    super(SSVarNames.messageSend, null, user, servPar.sqlCon);
    
    this.forUser      = forUser;
    this.message      = message;
    this.shouldCommit = shouldCommit;
  }
}
