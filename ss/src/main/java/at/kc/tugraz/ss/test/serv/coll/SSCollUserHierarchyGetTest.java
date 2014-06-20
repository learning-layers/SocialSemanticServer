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
package at.kc.tugraz.ss.test.serv.coll;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.coll.conf.SSCollConf;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;

import java.util.List;

public class SSCollUserHierarchyGetTest extends SSServOpTestCaseA{
  
  public SSCollUserHierarchyGetTest(SSCollConf collConf) throws Exception {
    super(collConf, SSMethU.collUserHierarchyGet);
  }
  
  @Override
  protected void test() throws Exception {
    
    SSColl       rootColl      = SSServCaller.collUserRootGet       (SSVoc.systemUserUri);
    SSUri        collFirstUri  = SSServCaller.collUserEntryAdd      (SSVoc.systemUserUri, rootColl.id, null, SSLabel.get("firstColl"),  true, false, true);
    SSUri        collSecondUri = SSServCaller.collUserEntryAdd      (SSVoc.systemUserUri, collFirstUri, null, SSLabel.get("secondColl"), true, false, true);
    List<SSColl> collHierarchy = SSServCaller.collUserHierarchyGet  (SSVoc.systemUserUri, collSecondUri);
    
    System.out.println (op + " Test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }
  
  @Override
  protected void setUp() throws Exception {
  }
}
