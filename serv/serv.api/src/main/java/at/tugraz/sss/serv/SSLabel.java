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
package at.tugraz.sss.serv;

import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSVarU;
import java.util.ArrayList;
import java.util.List;

public class SSLabel extends SSEntityA{

  public static SSLabel get(final String str) throws Exception{
    return new SSLabel(str);
  }
  
  public static SSLabel get(final SSEntityA entity) throws Exception{
    return new SSLabel(SSStrU.toStr(entity));
  }
  
  public static List<SSLabel> get(final List<String> strings) throws Exception{
    
    final List<SSLabel> result = new ArrayList<>();
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static Boolean isLabel(final String string) throws Exception{
    
    if(string == null){
      return false;
    }
    
    return true;
  }
    
   
  protected SSLabel(final String string) throws Exception{
    
    super(string);
    
    if(!isLabel(string)){
      throw new Exception("invalid label " + string);
    }
  }
  
  @Override
  public Object jsonLDDesc() {
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
}