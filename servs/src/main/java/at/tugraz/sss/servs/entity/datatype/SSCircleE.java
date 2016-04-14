/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public enum SSCircleE{
  
  priv,
  group,
  pubCircle,
  pub;
  
  public static List<SSCircleE> get(final List<String> values) throws SSErr{
    
    final List<SSCircleE> result = new ArrayList<>();
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
  
  public static SSCircleE get(final String value) throws SSErr{
    
    try{
      return SSCircleE.valueOf(value);
    }catch(IllegalArgumentException error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }  
}
