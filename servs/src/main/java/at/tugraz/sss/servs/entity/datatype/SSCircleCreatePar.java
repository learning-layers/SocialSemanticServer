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

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSCircleE;

public class SSCircleCreatePar extends SSServPar{
  
  public SSLabel               label                = null;
  public SSTextComment         description          = null;
  public SSCircleE             circleType           = null;               
  public boolean               isSystemCircle       = false;

  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }

  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }
  
  public String getLabel() throws SSErr{
    return SSStrU.toStr(label);
  }
  
  public String getDescription() throws SSErr{
    return SSStrU.toStr(description);
  }

  public String getCircleType(){
    return SSStrU.toStr(circleType);
  }

  public void setCircleType(final String circleType) throws SSErr{
    this.circleType = SSCircleE.get(circleType);
  }
  
  public SSCircleCreatePar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSCircleCreatePar(
    final SSServPar servPar,
    final SSUri           user,
    final SSCircleE       circleType,
    final SSLabel         label,
    final SSTextComment   description,
    final boolean         isSystemCircle,
    final boolean         withUserRestriction, 
    final boolean         shouldCommit){
    
    super(SSVarNames.circleCreate, null, user, servPar.sqlCon);
    
    this.circleType               = circleType;
    this.label                    = label;
    this.description              = description;
    this.isSystemCircle           = isSystemCircle;
    this.withUserRestriction      = withUserRestriction;
    this.shouldCommit             = shouldCommit;
  }
}