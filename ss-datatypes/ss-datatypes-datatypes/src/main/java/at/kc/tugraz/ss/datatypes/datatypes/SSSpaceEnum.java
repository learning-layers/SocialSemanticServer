/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.ArrayList;
import java.util.List;

public enum SSSpaceEnum implements SSJSONLDPropI{
  
  followSpace,
  sharedSpace,
  privateSpace;
  
  public static String toStr(final SSSpaceEnum space){
    return SSStrU.toString(space);
  }
  
  public static SSSpaceEnum get(final String space) throws Exception{
    return SSSpaceEnum.valueOf(space);
  }
  
  public static List<SSSpaceEnum> get(final List<String> strings) throws Exception{

    final List<SSSpaceEnum> result = new ArrayList<SSSpaceEnum>();
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static List<SSSpaceEnum> getDistinct(final List<String> strings) throws Exception{

    final List<SSSpaceEnum> result = new ArrayList<SSSpaceEnum>();
    
    for(String string : SSStrU.distinct(strings)){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static Boolean isShared(
    final SSSpaceEnum space){
    
    return equals(space, SSSpaceEnum.sharedSpace);
  }
  
  public static Boolean equals(
    final SSSpaceEnum space1, 
    final SSSpaceEnum space2){
    
    if(SSObjU.isNull(space1, space2)){
      return false;
    }
    
    return space1.toString().equals(space2.toString());
  }
  
  public static Boolean isPrivate(
    final SSSpaceEnum space) {
    
    return equals(space, SSSpaceEnum.privateSpace);
  }
  
  public static Boolean isPrivateOrShared(
    final SSSpaceEnum space){
    
    return isPrivate(space) || isShared(space);
  }
  
  public static Boolean isFollow(
    final SSSpaceEnum space){
    
    return equals(space, SSSpaceEnum.followSpace);
  }
  
  public static Boolean isSharedOrFollow(
    final SSSpaceEnum space){
    
    return isShared(space) || isFollow(space);
  }

  @Override
  public Object jsonLDDesc() {
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
}