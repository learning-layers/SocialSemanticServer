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
package at.kc.tugraz.ss.category.datatypes;

import at.tugraz.sss.serv.*;
import at.tugraz.sss.serv.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSCategoryLabel extends SSEntityA{

  public static SSCategoryLabel get(
    final String string) throws Exception{
    
    return new SSCategoryLabel(string);
  }
  
  public static List<SSCategoryLabel> get(
    final List<String> strings) throws Exception{

    final List<SSCategoryLabel> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static List<SSCategoryLabel> asListWithoutNullAndEmpty(final SSCategoryLabel... categoryLabels){
   
    final List<SSCategoryLabel> result = new ArrayList<>();
    
    if(categoryLabels == null){
      return result;
    }
    
    for(SSCategoryLabel categoryLabel : categoryLabels){
      
      if(SSStrU.isEmpty(categoryLabel)){
        continue;
      }
      
      result.add(categoryLabel);
    }
    
    return result;
  }
  
  public static List<SSCategoryLabel> asListWithoutNullAndEmpty(final List<SSCategoryLabel> categoryLabels){
   
    final List<SSCategoryLabel> result = new ArrayList<>();
    
    if(categoryLabels == null){
      return result;
    }
    
    for(SSCategoryLabel categoryLabel : categoryLabels){
      
      if(SSStrU.isEmpty(categoryLabel)){
        continue;
      }
      
      result.add(categoryLabel);
    }
    
    return result;
  }
  
  protected SSCategoryLabel(final String label) throws Exception{
    super(getCategoryLabel(label));
  }
  
  private static String getCategoryLabel(final String label) throws Exception{
    
    try{
      
//previously replaced blanks with underlines automatically SSStrU.replaceAll(label, SSStrU.blank, SSStrU.underline);
      
//previously accepted only latin letters, numbers and underline return tmpLabel.replaceAll("[^a-zA-Z0-9_]+", SSStrU.empty);
      
      //accept unicode letters, blank, numbers, underline, hyphen
      return label.replaceAll("[^\\p{L}\\p{Zs}0-9_-]+", SSStrU.empty);
      
    }catch(Exception error){
      throw new Exception("category: " + label + "is not valid");
    }
  }
  
  @Override
  public Object jsonLDDesc() {
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
}