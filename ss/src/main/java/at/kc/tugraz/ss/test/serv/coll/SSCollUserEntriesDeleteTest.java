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
package at.kc.tugraz.ss.test.serv.coll;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.coll.conf.SSCollConf;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.datatypes.SSCollEntry;
import java.util.ArrayList;
import java.util.List;

public class SSCollUserEntriesDeleteTest extends SSServOpTestCaseA{
  
  public SSCollUserEntriesDeleteTest(final SSCollConf collConf) throws Exception {
    super(collConf, SSMethU.collUserEntriesDelete);
  }
  
  @Override
  protected void test() throws Exception {
    
    System.out.println (op + " test start");
    final SSColl           rootColl      = SSServCaller.collUserRootGet          (userUri);
    final List<SSUri>      collEntryUris = new ArrayList<SSUri>();
    final SSColl           rootCollAfterEntriesDelete;
    
    for(SSCollEntry collEntry : rootColl.entries){
      collEntryUris.add(collEntry.uri);
    }
    
    SSServCaller.collUserEntriesDelete(userUri, rootColl.uri, collEntryUris, true);
    
    rootCollAfterEntriesDelete = SSServCaller.collUserRootGet(userUri);
      
    System.out.println (op + " test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }
  
  @Override
  protected void setUp() throws Exception {
    userUri = SSServCaller.logUserIn(SSLabelStr.get("dt"), true);
  }
}