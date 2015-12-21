/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.tugraz.sss.serv.datatype.enums;

import java.util.ArrayList;
import java.util.List;

public enum SSCircleE{
  
  priv,
  group,
  pubCircle,
  pub;
  
  public static List<SSCircleE> get(final List<String> values) throws Exception{
    
    final List<SSCircleE> result = new ArrayList<>();
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
  
  public static SSCircleE get(final String value) throws Exception{
    return SSCircleE.valueOf(value);
  }
  
  private SSCircleE(){}
}
