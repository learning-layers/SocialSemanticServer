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

package at.tugraz.sss.servs.disc.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSDiscTargetsAddPar extends SSServPar{
  
  public SSUri        discussion           = null;
  public List<SSUri>  targets              = new ArrayList<>();

  public String getDiscussion() {
    return SSStrU.removeTrailingSlash(discussion);
  }

  public void setDiscussion(final String discussion) throws SSErr{
    this.discussion = SSUri.get(discussion);
  }

  public List<String> getTargets() {
    return SSStrU.removeTrailingSlash(targets);
  }

  public void setTargets(final List<String> targets) throws SSErr{
    this.targets = SSUri.get(targets);
  }

  public SSDiscTargetsAddPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSDiscTargetsAddPar(
    final SSServPar servPar,
    final SSUri       user,
    final SSUri       discussion,
    final List<SSUri> targets,
    final boolean     withUserRestriction,
    final boolean     shouldCommit){
    
    super(SSVarNames.discTargetsAdd, null, user, servPar.sqlCon);
    
    this.discussion = discussion;

    SSUri.addDistinctWithoutNull(this.targets,   targets);
    
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}