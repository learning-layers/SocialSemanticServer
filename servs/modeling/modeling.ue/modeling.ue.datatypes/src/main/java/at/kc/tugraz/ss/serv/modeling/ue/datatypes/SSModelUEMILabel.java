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
package at.kc.tugraz.ss.serv.modeling.ue.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SSModelUEMILabel extends SSEntityA{

  public static SSModelUEMILabel get(final String string) throws Exception{
    return new SSModelUEMILabel(string);
  }
  
  public static List<SSModelUEMILabel> get(final List<String> strings) throws Exception{
    
    final List<SSModelUEMILabel> result = new ArrayList<>();
    
    for(String string: strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static Boolean isMILabel(
    final String miLabel) throws Exception {

    try{
      
      if(SSStrU.isEmpty(miLabel)){
        return false;
      }

      final String tmpMILabel = miLabel.replaceAll("[/\\*\\?<>]", SSStrU.empty);

      if(
        SSStrU.isEmpty  (tmpMILabel) ||
        !Pattern.matches("^[a-zA-Z0-9_-]*$", tmpMILabel)){
        return false;
      }
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSModelUEMILabel(final String label) throws Exception{
    
    super(label);
    
    if(!isMILabel(label)){
     throw new Exception("invald mi label " + label);
    }
  }
  
  @Override
  public Object jsonLDDesc() {
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
}
