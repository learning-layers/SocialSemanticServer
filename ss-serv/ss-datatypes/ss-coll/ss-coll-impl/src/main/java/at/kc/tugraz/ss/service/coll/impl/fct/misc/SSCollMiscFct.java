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

package at.kc.tugraz.ss.service.coll.impl.fct.misc;

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.datatypes.SSCollEntry;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;

public class SSCollMiscFct{
  
  public static SSColl getUserCollWithEntriesWithCircleTypes(
    final SSCollSQLFct sqlFct, 
    final SSUri        userUri, 
    final SSUri        collUri) throws Exception{
    
    final SSColl coll =
      sqlFct.getUserCollWithEntries(
        userUri,
        collUri,
        SSServCaller.accessRightsUserCircleTypesForEntityGet(userUri, collUri),
        true);
    
    for(SSCollEntry entry : coll.entries){
      
      if(sqlFct.isColl(entry.uri)){ //coll entry is a coll itself
        entry.circleTypes.clear();
        entry.circleTypes.addAll(SSServCaller.accessRightsUserCircleTypesForEntityGet(userUri, entry.uri));
      }
    }
    
    return coll;
  }
}