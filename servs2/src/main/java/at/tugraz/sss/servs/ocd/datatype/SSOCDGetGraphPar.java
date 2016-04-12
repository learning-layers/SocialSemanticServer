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
package at.tugraz.sss.servs.ocd.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.ocd.datatype.SSOCDGraphOutputE;

public class SSOCDGetGraphPar extends SSServPar{
  
  public SSOCDGetGraphPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  private String graphId = null;
  private SSOCDGraphOutputE graphOutput = null;
  
  public SSOCDGetGraphPar (
    final SSServPar servPar,
    final SSUri user,
    final String graphId,
    final SSOCDGraphOutputE graphOutput) {
    
    super(SSVarNames.ocdGetGraph, null, user, servPar.sqlCon);
  }
  
  public String getGraphId() {
    return graphId;
  }
  
  public void setGraphId(String graphId) {
    this.graphId = graphId;
  }
  
  public SSOCDGraphOutputE getGraphOutput() {
    return graphOutput;
  }
  
  public void setGraphOutput(SSOCDGraphOutputE graphOutput) {
    this.graphOutput = graphOutput;
  }
  
  
}
