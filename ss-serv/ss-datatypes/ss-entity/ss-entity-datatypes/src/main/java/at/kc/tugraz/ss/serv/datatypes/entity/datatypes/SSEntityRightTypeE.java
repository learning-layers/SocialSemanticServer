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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.List;

public enum SSEntityRightTypeE implements SSJSONLDPropI{

  all,
  read,
  edit;
  
  public static SSEntityRightTypeE get(final String value) throws Exception{
    return SSEntityRightTypeE.valueOf(value);
  }
  
  public static String toStr(final SSEntityRightTypeE accessRight){
    return SSStrU.toString(accessRight);
  }
  
  public static Boolean equals(
    final SSEntityRightTypeE right1,
    final SSEntityRightTypeE right2){
    
    if(SSObjU.isNull(right1, right2)){
      return false;
    }
    
    return right1.toString().equals(right2.toString());
  }
  
  public static Boolean contains(
    final List<SSEntityRightTypeE> rights, 
    final SSEntityRightTypeE       certainRight){
    
    if(SSObjU.isNull(rights)){
      return false;
    }
    
    for(SSEntityRightTypeE right : rights){
      
      if(equals(right, certainRight)){
        return true;
      }
    } 
    
    return false;
  }
  
  private SSEntityRightTypeE(){}
  
  @Override
  public Object jsonLDDesc(){
    return SSStrU.valueString;
  }
}
