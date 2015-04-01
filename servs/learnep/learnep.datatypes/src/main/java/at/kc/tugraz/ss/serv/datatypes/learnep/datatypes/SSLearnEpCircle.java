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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import java.util.Map;

public class SSLearnEpCircle extends SSEntity{
  
  public Float         xLabel           = null;
  public Float         yLabel           = null;
  public Float         xR               = null;
  public Float         yR               = null;
  public Float         xC               = null;
  public Float         yC               = null;
  
  public static SSLearnEpCircle get(
    final SSUri   id, 
    final SSLabel label, 
    final Float   xLabel, 
    final Float   yLabel, 
    final Float   xR, 
    final Float   yR, 
    final Float   xC, 
    final Float   yC) throws Exception{
    
    return new SSLearnEpCircle(id, label, xLabel, yLabel, xR, yR, xC, yC);
  }
  
  protected SSLearnEpCircle(
    final SSUri   id, 
    final SSLabel label, 
    final Float   xLabel, 
    final Float   yLabel, 
    final Float   xR, 
    final Float   yR, 
    final Float   xC, 
    final Float   yC)throws Exception{
    
    super(id, SSEntityE.learnEpCircle, label);
    
    this.xLabel              = xLabel;
    this.yLabel              = yLabel;
    this.xR                  = xR;
    this.yR                  = yR;
    this.xC                  = xC;
    this.yC                  = yC;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarU.xLabel,           SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.yLabel,           SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.xR,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.yR,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.xC,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.yC,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    
    return ld;
  }
}
