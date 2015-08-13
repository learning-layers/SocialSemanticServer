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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;

public class SSEntityFromTypeAndLabelGetPar extends SSServPar{
  
  public SSLabel              label                = null;
  public SSEntityE            type                 = null;
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }
  
  public void setType(final String type) throws Exception{
    this.type = SSEntityE.get(type);
  }
  
  public SSEntityFromTypeAndLabelGetPar(){}
  
  public SSEntityFromTypeAndLabelGetPar(
    final SSUri                user,
    final SSLabel              label,
    final SSEntityE            type,
    final Boolean              withUserRestriction){
    
    super(SSServOpE.entityFromTypeAndLabelGet, null, user);
    
    this.label               = label;
    this.type                = type;
    this.withUserRestriction = withUserRestriction;
  }
}
