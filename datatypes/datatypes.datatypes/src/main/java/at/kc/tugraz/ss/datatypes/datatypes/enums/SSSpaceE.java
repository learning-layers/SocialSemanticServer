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
package at.kc.tugraz.ss.datatypes.datatypes.enums;

import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.ArrayList;
import java.util.List;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public enum SSSpaceE implements SSJSONLDPropI{
  
  followSpace,
  sharedSpace,
  privateSpace;
  
  public static SSSpaceE get(final String space) throws Exception{
    
    try{
      
      if(space == null){
        return null;
      }
      
      return SSSpaceE.valueOf(space);
    }catch(Exception error){
      SSServErrReg.regErrThrow(new SSErr(SSErrE.spaceNotAvailable));
      return null;
    }
  }
  
  public static List<SSSpaceE> get(final List<String> strings) throws Exception{

    final List<SSSpaceE> result = new ArrayList<>();
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static Boolean isShared(
    final SSSpaceE space){
    
    return equals(space, SSSpaceE.sharedSpace);
  }
  
  public static Boolean equals(
    final SSSpaceE space1, 
    final SSSpaceE space2){
    
    if(SSObjU.isNull(space1, space2)){
      return false;
    }
    
    return space1.toString().equals(space2.toString());
  }
  
  public static Boolean isPrivate(
    final SSSpaceE space) {
    
    return equals(space, SSSpaceE.privateSpace);
  }
  
  public static Boolean isPrivateOrShared(
    final SSSpaceE space){
    
    return isPrivate(space) || isShared(space);
  }
  
  public static Boolean isFollow(
    final SSSpaceE space){
    
    return equals(space, SSSpaceE.followSpace);
  }
  
  public static Boolean isSharedOrFollow(
    final SSSpaceE space){
    
    return isShared(space) || isFollow(space);
  }

  @Override
  public Object jsonLDDesc() {
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
}