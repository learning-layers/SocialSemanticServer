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
package at.kc.tugraz.ss.test.load;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServLoadTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;


public class SSLoadTest extends SSServLoadTestCaseA{
  
  public static volatile  SSUri     rootCollUri;
  public static volatile  SSUri     collUri;
  private final           Integer   maxThreads    = 100;
  private                 Integer   threadCounter = 0;
  
  public SSLoadTest() throws Exception {
    super(null);
  }
  
  @Override
  public void test() throws Exception{
    
    SSLogU.info(SSLoadTest.class.getName() + " start");
    
    rootCollUri  = SSServCaller.collUserRootGet   (SSVoc.systemUserUri).id;
    collUri      = SSServCaller.collUserEntryAdd  (SSVoc.systemUserUri, rootCollUri, null, SSLabel.get("firstColl"), true, false, true);
    
    for(threadCounter = 0; threadCounter < maxThreads; threadCounter++){
      new Thread(new SSLoadTestUEAdder     (null, SSVoc.systemUserUri)).start();
//      new Thread(new SSLoadTestCollCreater (null, userUri)).start();
//      new Thread(new SSLoadTestCollRemover(null, userUri)).start();
    }
    
    SSLogU.info(SSLoadTest.class.getName() + " end");
  }
}