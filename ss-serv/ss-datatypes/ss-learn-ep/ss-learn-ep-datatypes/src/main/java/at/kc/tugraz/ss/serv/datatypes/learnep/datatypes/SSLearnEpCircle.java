/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.HashMap;
import java.util.Map;

public class SSLearnEpCircle extends SSEntityA {
  
  public SSUri         learnEpCircleUri = null;
  public SSLabelStr    label            = null;
  public Float         xLabel           = null;
  public Float         yLabel           = null;
  public Float         xR               = null;
  public Float         yR               = null;
  public Float         xC               = null;
  public Float         yC               = null;
  
  public static SSLearnEpCircle get(SSUri learnEpCircleUri, SSLabelStr label, Float xLabel, Float yLabel, Float xR, Float yR, Float xC, Float yC){
    return new SSLearnEpCircle(learnEpCircleUri, label, xLabel, yLabel, xR, yR, xC, yC);
  }
  
  private SSLearnEpCircle(SSUri learnEpCircleUri, SSLabelStr label, Float xLabel, Float yLabel, Float xR, Float yR, Float xC, Float yC){
    super(learnEpCircleUri);
    
    this.learnEpCircleUri    = learnEpCircleUri;
    this.label               = label;
    this.xLabel              = xLabel;
    this.yLabel              = yLabel;
    this.xR                  = xR;
    this.yR                  = yR;
    this.xC                  = xC;
    this.yC                  = yC;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld         = new HashMap<String, Object>();
    
    ld.put(SSVarU.learnEpCircleUri, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,            SSVarU.sss + SSStrU.colon + SSLabelStr.class.getName());
    ld.put(SSVarU.xLabel,           SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.yLabel,           SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.xR,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.yR,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.xC,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.yC,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public String getLearnEpCircleUri() throws Exception {
    return SSUri.toStrWithoutSlash(learnEpCircleUri);
  }

  public String getLabel() {
    return SSLabelStr.toStr(label);
  }

  public Float getxLabel() {
    return xLabel;
  }

  public Float getyLabel() {
    return yLabel;
  }

  public Float getxR() {
    return xR;
  }

  public Float getyR() {
    return yR;
  }

  public Float getxC() {
    return xC;
  }

  public Float getyC() {
    return yC;
  }
}
