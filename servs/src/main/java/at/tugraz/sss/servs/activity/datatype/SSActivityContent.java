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

package at.tugraz.sss.servs.activity.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSActivityContent extends SSEntityA{
  
  public static SSActivityContent get(final String str) throws SSErr{
    return new SSActivityContent(SSStrU.trim(str, 199));
  }
  
  public static SSActivityContent get(final SSEntityA entity) throws SSErr{
    return get(SSStrU.toStr(entity));
  }
  
  public static List<SSActivityContent> get(final List<String> strings) throws SSErr{
    
    final List<SSActivityContent> result = new ArrayList<>();
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static boolean isActivityContent(final String string){
    
    if(string == null){
      return false;
    }
    
    return true;
  }
   
  protected SSActivityContent(final String string) throws SSErr{
    
    super(string);
    
    if(!isActivityContent(string)){
      throw SSErr.get(SSErrE.activityContentInvalid);
    }
  }
}