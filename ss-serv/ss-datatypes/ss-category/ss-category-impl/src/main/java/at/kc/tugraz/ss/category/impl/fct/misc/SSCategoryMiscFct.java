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
package at.kc.tugraz.ss.category.impl.fct.misc;

import at.kc.tugraz.ss.category.datatypes.par.SSCategory;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryFrequ;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryLabel;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCategoryMiscFct{
  
   public static List<SSCategoryFrequ> getCategoryFrequsFromCategorys(
    final List<SSCategory> categorys, 
    final SSSpaceE    space) throws Exception{
    
    final Map<String, Integer> counterPerCategorys = new HashMap<String, Integer>();
    
    String categoryLabel;

    for (SSCategory category : categorys) {

      categoryLabel = SSCategoryLabel.toStr(category.label);
        
      if(counterPerCategorys.containsKey(categoryLabel)){
        counterPerCategorys.put(categoryLabel, counterPerCategorys.get(categoryLabel) + 1);
      } else {
        counterPerCategorys.put(categoryLabel, 1);
      }
    }

    final List<SSCategoryFrequ> outList = new ArrayList<SSCategoryFrequ>(counterPerCategorys.size());

    for(String key : counterPerCategorys.keySet()){
      
      outList.add(
        SSCategoryFrequ.get(
          SSCategoryLabel.get(key),
          space,
          counterPerCategorys.get(key)));
    }
    
    return outList;
  }
}
