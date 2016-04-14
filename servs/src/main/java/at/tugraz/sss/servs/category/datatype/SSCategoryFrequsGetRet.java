/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.category.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.category.datatype.SSCategoryFrequ;

import at.tugraz.sss.servs.util.SSJSONLDU;
import at.tugraz.sss.servs.entity.datatype.SSServRetI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCategoryFrequsGetRet extends SSServRetI{
  
  public List<SSCategoryFrequ> categoryFrequs = new ArrayList<>();
  
  public static SSCategoryFrequsGetRet get(
    final List<SSCategoryFrequ> categoryFrequs){
    
    return new SSCategoryFrequsGetRet(categoryFrequs);
  }
  
  private SSCategoryFrequsGetRet(
    final List<SSCategoryFrequ> categoryFrequs){
    
    super(SSVarNames.categoryFrequsGet);
    
    SSCategoryFrequ.addDistinctWithoutNull(this.categoryFrequs, categoryFrequs);
  }
}