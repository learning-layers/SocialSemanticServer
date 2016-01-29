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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.util.*;
import java.sql.*;

public class SSLearnEpVersionCircleAddPar extends SSServPar{
  
  public SSUri      learnEpVersion    = null;
  public SSLabel    label             = null;
  public Float      xLabel            = null;
  public Float      yLabel            = null;
  public Float      xR                = null;
  public Float      yR                = null;
  public Float      xC                = null;
  public Float      yC                = null;

  public String getLearnEpVersion(){
    return SSStrU.removeTrailingSlash(learnEpVersion);
  }

  public void setLearnEpVersion(String learnEpVersion) throws SSErr{
    this.learnEpVersion = SSUri.get(learnEpVersion);
  }

  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public SSLearnEpVersionCircleAddPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSLearnEpVersionCircleAddPar(
    final SSServPar servPar,
    final SSUri      user,
    final SSUri      learnEpVersion,
    final SSLabel    label,
    final Float      xLabel,
    final Float      yLabel,
    final Float      xR,
    final Float      yR,
    final Float      xC,
    final Float      yC,
    final boolean    withUserRestriction,
    final boolean    shouldCommit){
    
    super(SSVarNames.learnEpVersionCircleAdd, null, user, servPar.sqlCon);
    
    this.learnEpVersion      = learnEpVersion;
    this.label               = label;
    this.xLabel              = xLabel;
    this.yLabel              = yLabel;
    this.xR                  = xR;
    this.yR                  = yR;
    this.xC                  = xC;
    this.yC                  = yC;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}