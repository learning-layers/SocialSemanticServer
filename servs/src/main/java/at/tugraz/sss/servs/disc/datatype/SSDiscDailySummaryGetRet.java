/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.disc.datatype;

import at.tugraz.sss.servs.disc.datatype.SSDiscDailySummary;
import at.tugraz.sss.servs.entity.datatype.SSServRetI;
import at.tugraz.sss.servs.util.SSVarNames;
import java.util.HashMap;
import java.util.Map;

public class SSDiscDailySummaryGetRet extends SSServRetI{

  public Map<String, SSDiscDailySummary> summaries = new HashMap<>();

  public SSDiscDailySummaryGetRet(){
    super(SSVarNames.discDailySummaryGet);
  }
}