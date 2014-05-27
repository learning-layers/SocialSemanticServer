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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import java.util.HashMap;
import java.util.Map;

public class SSLearnEpCircle extends SSEntityA {
  
  public SSUri         id               = null;
  public SSLabel       label            = null;
  public Float         xLabel           = null;
  public Float         yLabel           = null;
  public Float         xR               = null;
  public Float         yR               = null;
  public Float         xC               = null;
  public Float         yC               = null;
  
  public static SSLearnEpCircle get(SSUri id, SSLabel label, Float xLabel, Float yLabel, Float xR, Float yR, Float xC, Float yC) throws Exception{
    return new SSLearnEpCircle(id, label, xLabel, yLabel, xR, yR, xC, yC);
  }
  
  private SSLearnEpCircle(SSUri id, SSLabel label, Float xLabel, Float yLabel, Float xR, Float yR, Float xC, Float yC)throws Exception{
    
    super(id);
    
    this.id                  = id;
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
    
    ld.put(SSVarU.id,               SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,            SSVarU.sss + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.xLabel,           SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.yLabel,           SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.xR,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.yR,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.xC,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    ld.put(SSVarU.yC,               SSVarU.xsd + SSStrU.colon + SSStrU.valueFloat);
    
    return ld;
  }
  
  /* getters to allow for json enconding */
  public String getId() throws Exception {
    return SSUri.toStrWithoutSlash(id);
  }

  public String getLabel() {
    return SSLabel.toStr(label);
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
