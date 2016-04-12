/**
 * Code contributed to the Learning Layers project http://www.learning-layers.eu Development is partly funded by the FP7 Programme of the European Commission under Grant Agreement FP7-ICT-318209. Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute). For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
 * 
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
* http://www.apache.org/licenses/LICENSE-2.0
 * 
* Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package at.tugraz.sss.servs.ocd.datatype;


import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.ocd.datatype.SSOCDCreationTypeE;
import at.tugraz.sss.servs.ocd.datatype.SSOCDGraphInputE;

public class SSOCDCreateGraphPar extends SSServPar {

  public SSOCDCreateGraphPar() {
  }

  private String graphName = null;
  private SSOCDCreationTypeE creationType = null;
  private SSOCDGraphInputE graphInputFormat = null;
  private boolean makeUndirected = false;
  private String content = null;

  public SSOCDCreateGraphPar(
    final SSServPar servPar,
    final SSUri user,
    final String graphName,
    final SSOCDCreationTypeE creationType,
    final SSOCDGraphInputE graphInputFormat,
    final boolean makeUndirected,
    final String content,
    final boolean shouldCommit) {

      super(SSVarNames.ocdCreateGraph, null, user, servPar.sqlCon);

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

  public boolean getMakeUndirected() {
    return makeUndirected;
  }

  public void setMakeUndirected(boolean makeUndirected) {
    this.makeUndirected = makeUndirected;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}