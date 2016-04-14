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
import java.util.ArrayList;
import java.util.List;

public class SSDiscsGetPar extends SSServPar{

  public boolean     setEntries           = false;
  public SSUri       forUser              = null;
  public List<SSUri> discs                = new ArrayList<>();
  public List<SSUri> targets              = new ArrayList<>();
  public boolean     setLikes             = false;
  public boolean     setCircleTypes       = false;
  public boolean     setComments          = false;
  public boolean     setTags              = false;
  public boolean     setAttachedEntities  = false;
  public boolean     setReads             = false;

  public String getForUser() {
    return SSStrU.removeTrailingSlash(forUser);
  }

  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser);
  }

  public List<String> getDiscs() {
    return SSStrU.removeTrailingSlash(discs);
  }

  public void setDiscs(List<String> discs) throws SSErr {
    this.discs = SSUri.get(discs);
  }
  
  public List<String> getTargets() {
    return SSStrU.removeTrailingSlash(targets);
  }

  public void setTargets(final List<String> targets) throws SSErr{
    this.targets.addAll(SSUri.get(targets));
  }
  
  public SSDiscsGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSDiscsGetPar(
    final SSServPar   servPar,
    final SSUri       user,
    final boolean     setEntries,
    final SSUri       forUser,
    final List<SSUri> discs,
    final List<SSUri> targets,
    final boolean     withUserRestriction,
    final boolean     invokeEntityHandlers){
    
    super(SSVarNames.discsGet, null, user, servPar.sqlCon);
    
    this.setEntries           = setEntries;
    this.forUser              = forUser;
    
    SSUri.addDistinctWithoutNull(this.discs,   discs);
    SSUri.addDistinctWithoutNull(this.targets, targets);
    
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}