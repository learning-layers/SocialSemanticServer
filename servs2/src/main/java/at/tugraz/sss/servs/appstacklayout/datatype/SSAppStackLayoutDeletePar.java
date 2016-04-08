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
package at.tugraz.sss.servs.appstacklayout.datatype;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import java.sql.*;

public class SSAppStackLayoutDeletePar extends SSServPar{
  
  public SSUri               stack              = null;

  public void setStack(final String stack) throws SSErr{
    this.stack = SSUri.get(stack);
  }
  
  public String getStack(){
    return SSStrU.removeTrailingSlash(stack);
  }
  
  public SSAppStackLayoutDeletePar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSAppStackLayoutDeletePar(
    final SSServPar servPar,
    final SSUri            user,
    final SSUri            stack, 
    final boolean          withUserRestriction, 
    final boolean          shouldCommit){
    
    super(SSVarNames.appStackLayoutDelete, null, user, servPar.sqlCon);
    
    this.stack               = stack;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}