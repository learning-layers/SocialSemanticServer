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
package at.tugraz.sss.servs.auth.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 

public class SSAuthCheckCredPar extends SSServPar{

  public SSLabel label    = null;
  public String  password = null;
  
  public void setLabel(String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public SSAuthCheckCredPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSAuthCheckCredPar(
    final SSServPar servPar,
    final SSLabel   label, 
    final String    password){
    
    super(SSVarNames.authCheckCred, null, null, servPar.sqlCon);
    
    this.label    = label;
    this.password = password;
  }
  
  public SSAuthCheckCredPar(
    final SSServPar servPar,
    final String   key){
    
    super(SSVarNames.authCheckCred, key, null, servPar.sqlCon);
  }
}