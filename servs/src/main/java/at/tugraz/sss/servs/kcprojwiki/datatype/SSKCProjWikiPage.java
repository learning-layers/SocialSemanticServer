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

package at.tugraz.sss.servs.kcprojwiki.datatype;

public class SSKCProjWikiPage {
  
  public String  projectName    = null;
  public String  projectNumber  = null;
  public String  projectAcronym = null;
  
  public SSKCProjWikiPage(
    final String projectName,
    final String projectNumber,
    final String projectAcronym){
    
    this.projectName    = projectName;
    this.projectNumber  = projectNumber;
    this.projectAcronym = projectAcronym;
  }
}