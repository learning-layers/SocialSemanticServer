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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.List;

public enum SSAccessRightsRightTypeE implements SSJSONLDPropI{

  all,
  read,
  edit,
  addMetadata,
  addUserToCircle,
  addEntityToCircle,
  removeEntity,
  removeUser;
  
  public static SSAccessRightsRightTypeE get(final String value) throws Exception{
    return SSAccessRightsRightTypeE.valueOf(value);
  }
  
  public static String toStr(final SSAccessRightsRightTypeE accessRight){
    return SSStrU.toString(accessRight);
  }

  public static Boolean contains(
    final List<SSAccessRightsRightTypeE> rights, 
    final SSAccessRightsRightTypeE       certainRight){
    
    if(SSObjU.isNull(rights)){
      return false;
    }
    
    for(SSAccessRightsRightTypeE right : rights){
      
      if(SSStrU.equals(toStr(right), toStr(certainRight))){
        return true;
      }
    } 
    
    return false;
  }
  
  private SSAccessRightsRightTypeE(){}
  
  @Override
  public Object jsonLDDesc(){
    return SSStrU.valueString;
  }
}
