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

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import java.sql.*;

public class SSLearnEpVersionCircleUpdatePar extends SSServPar{
  
  public SSUri         learnEpCircle     = null;
  public SSLabel       label             = null;
  public SSTextComment description       = null;
  public Float         xLabel            = null;
  public Float         yLabel            = null;
  public Float         xR                = null;
  public Float         yR                = null;
  public Float         xC                = null;
  public Float         yC                = null;

  public String getLearnEpCircle(){
    return SSStrU.removeTrailingSlash(learnEpCircle);
  }

  public void setLearnEpCircle(final String learnEpCircle) throws SSErr{
    this.learnEpCircle = SSUri.get(learnEpCircle);
  }

  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public String getDescription(){
    return SSStrU.toStr(description);
  }

  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }
  
  public SSLearnEpVersionCircleUpdatePar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSLearnEpVersionCircleUpdatePar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         learnEpCircle,
    final SSLabel       label,
    final SSTextComment description, 
    final Float         xLabel,
    final Float         yLabel,
    final Float         xR,
    final Float         yR,
    final Float         xC,
    final Float         yC,
    final boolean       withUserRestriction,
    final boolean       shouldCommit){
    
    super(SSVarNames.learnEpVersionCircleUpdate, null, user, servPar.sqlCon);
    
    this.learnEpCircle       = learnEpCircle;
    this.label               = label;
    this.description         = description;
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