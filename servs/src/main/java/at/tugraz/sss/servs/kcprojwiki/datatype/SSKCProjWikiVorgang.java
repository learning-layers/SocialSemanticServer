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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SSKCProjWikiVorgang extends SSKCProjWikiPage{
  
  public String                                           vorgangName       = null;
  public String                                           vorgangNumber     = null;
  public String                                           vorgangStart      = null;
  public String                                           vorgangEnd        = null;
  public Float                                            totalResources    = null;
  public Float                                            usedResources     = null;
  public Map<String, SSKCProjWikiVorgangEmployeeResource> employeeResources = new HashMap<>();
  public String                                           exportDate        = null;
  public Float                                            progress          = null;
  
  public SSKCProjWikiVorgang(
    final String projectName, 
    final String projectNumber,
    final String projectAcronym,
    final String vorgangName,
    final String vorgangNumber){
    
    super(projectName, projectNumber, projectAcronym);
    
    this.vorgangName   = vorgangName;
    this.vorgangNumber = vorgangNumber;
  }
}