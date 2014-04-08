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
package at.kc.tugraz.ss.datatypes.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import java.util.ArrayList;
import java.util.List;

public class SSLabelStr extends SSEntityA{

  public static String toStr(final SSLabelStr label){
    return SSStrU.toString(label);
  }
  
  public static SSLabelStr get(final String str) throws Exception{
    return new SSLabelStr(str);
  }
  
  public static List<SSLabelStr> get(final List<String> strings) throws Exception{

    final List<SSLabelStr> result = new ArrayList<SSLabelStr>();
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
   
  public static List<SSLabelStr> getDistinct(final List<String> strings) throws Exception{

    final List<SSLabelStr> result = new ArrayList<SSLabelStr>();
    
    for(String string : SSStrU.distinct(strings)){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static Boolean isEmtpy(final SSLabelStr labelStr){
    
    if(labelStr == null){
      return true;
    }
    
    return SSStrU.isEmpty(labelStr.toString());
  }
  
  private SSLabelStr(final String str) throws Exception{
    super(str);
  }
  
  @Override
  public Object jsonLDDesc() {
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
}


//tag.replaceAll("[/\\*\\?<>]", "");