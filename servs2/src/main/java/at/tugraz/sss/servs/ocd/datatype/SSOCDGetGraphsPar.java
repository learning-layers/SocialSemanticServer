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

public class SSOCDGetGraphsPar extends SSServPar{
  
  private String firstIndex = null;
  private String length = null;
  private boolean includeMeta = false;
  private String executionStatuses = null;
  
  public SSOCDGetGraphsPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSOCDGetGraphsPar (
    final SSServPar servPar,
    final SSUri user,
    final String firstIndex,
    final String length,
    final boolean includeMeta,
    final String executionStatuses) {
    
       super(SSVarNames.ocdGetGraphs, null, user, servPar.sqlCon);
  }
  public String getFirstIndex() {
    return firstIndex;
  }

  public void setFirstIndex(String firstIndex) {
    this.firstIndex = firstIndex;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }

  public boolean getIncludeMeta() {
    return includeMeta;
  }

  public void setIncludeMeta(boolean includeMeta) {
    this.includeMeta = includeMeta;
  }

  public String getExecutionStatuses() {
    return executionStatuses;
  }

  public void setExecutionStatuses(String executionStatuses) {
    this.executionStatuses = executionStatuses;
  }
}