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
package at.kc.tugraz.ss.recomm.impl.fct.misc;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.util.SSServCallerU;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;

public class SSRecommFct{
        
  public static void checkPar(
    final SSUri  user,
    final SSUri  forUser,
    final String realm) throws Exception{
    
    if(user == null){
      throw new Exception("user cannot be null");
    }
    
    if(
      realm == null ||
      SSStrU.equals(realm, SSRecommUserRealmKeeper.getSssRealm())){
      
      if(
        forUser != null &&
        !SSStrU.equals(user, forUser)){
        throw new Exception("user cannot retrieve recommendations for other users");
      }
    }
  }
  
  public static void handleAccess(
    final SSUri  user, 
    final SSUri  entityID) throws Exception{
    
    try{
      
      SSServCallerU.canUserReadEntity(
        user,
        entityID);
      
    }catch(Exception error){
      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
        SSServErrReg.reset();
      }
      
      throw error;
    }
  }
  
//  private static Long getTimestampInMillisecOfAUserTagForEntity(
//    final SSUri       userUri, 
//    final String      entityUri) throws Exception{
//    
//    final List<SSTag> tags = 
//      SSServCaller.tagsUserGet(
//        userUri,
//        null,
//        SSUri.asListWithoutNullAndEmpty(SSUri.get(entityUri)),
//        new ArrayList<>(),
//        null,
//        null);
//    
//    if(tags.isEmpty()){
//      return 0L;
//    }
//    
//    return SSServCaller.entityGet(tags.get(0).id).creationTime / 1000;
//  }
}