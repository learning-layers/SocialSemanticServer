/**
 * Code contributed to the Learning Layers project http://www.learning-layers.eu Development is partly funded by the FP7 Programme of the European Commission under Grant Agreement FP7-ICT-318209. Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute). For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
 * 
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
* http://www.apache.org/licenses/LICENSE-2.0
 * 
* Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package at.tugraz.sss.servs.ocd.datatypes.pars;


import at.tugraz.sss.serv.datatype.par.SSServPar; import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.servs.ocd.datatypes.SSOCDCreationTypeE;
import at.tugraz.sss.servs.ocd.datatypes.SSOCDGraphInputE;

public class SSOCDCreateGraphPar extends SSServPar {

  public SSOCDCreateGraphPar() {
  }

  private String graphName = null;
  private SSOCDCreationTypeE creationType = null;
  private SSOCDGraphInputE graphInputFormat = null;
  private Boolean makeUndirected = null;
  private String content = null;

  public SSOCDCreateGraphPar(
    final SSUri user,
    final String graphName,
    final SSOCDCreationTypeE creationType,
    final SSOCDGraphInputE graphInputFormat,
    final Boolean makeUndirected,
    final String content,
    final Boolean shouldCommit) {

      super(SSVarNames.ocdCreateGraph, null, user);

  }

  public String getGraphName() {
    return graphName;
  }

  public void setGraphName(String graphName) {
    this.graphName = graphName;
  }

  public SSOCDCreationTypeE getCreationType() {
    return creationType;
  }

  public void setCreationType(SSOCDCreationTypeE creationType) {
    this.creationType = creationType;
  }

  public SSOCDGraphInputE getGraphInputFormat() {
    return graphInputFormat;
  }

  public void setGraphInputFormat(SSOCDGraphInputE graphInputFormat) {
    this.graphInputFormat = graphInputFormat;
  }

  public Boolean getMakeUndirected() {
    return makeUndirected;
  }

  public void setMakeUndirected(Boolean makeUndirected) {
    this.makeUndirected = makeUndirected;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  
}
