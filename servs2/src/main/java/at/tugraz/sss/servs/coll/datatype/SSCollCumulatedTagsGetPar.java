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
package at.tugraz.sss.servs.coll.datatype;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import java.sql.*;

public class SSCollCumulatedTagsGetPar extends SSServPar{
  
  public SSUri coll = null;
      
  public void setColl(final String coll) throws SSErr{
    this.coll = SSUri.get(coll);
  }
  
  public String getColl(){
    return SSStrU.removeTrailingSlash(coll);
  }
  
  public SSCollCumulatedTagsGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSCollCumulatedTagsGetPar(
    final SSServPar servPar,
    final SSUri              user,
    final SSUri              coll, 
    final boolean            withUserRestriction){
    
    super(SSVarNames.collCumulatedTagsGet, null, user, servPar.sqlCon);
    
    this.coll                = coll;
    this.withUserRestriction = withUserRestriction;
  }
}