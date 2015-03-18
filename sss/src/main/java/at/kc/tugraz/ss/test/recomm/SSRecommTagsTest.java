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
package at.kc.tugraz.ss.test.recomm;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import java.util.ArrayList;
import java.util.Map;

public class SSRecommTagsTest extends SSServOpTestCaseA{
  
  public SSRecommTagsTest(final SSRecommConf recommConf) {
    super(recommConf, null, SSMethU.recommTags);
  }
  
  @Override
  protected void test() throws Exception {
    
    System.out.println (op + " test start");
    
    final Map<String, Double> tags = 
      SSServCaller.recommTags(
        SSVoc.systemUserUri, 
        null,
        null,
        new ArrayList<>(),
        10,
        true);
    
    System.out.println (tags);
    
    System.out.println (op + " test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
//    jsonRequ            = 
//      "{\"op\":\"learnEpVersionSetTimelineState\",\"user\":\"http://eval.bp/user/dt\",\"startTime\":1398782731238,\"endTime\":1398872731238,\"key\":\"681V454J1P3H4W3B367BB79615U184N22356I3E\"}";
//    
//    clientServPar                                               = new SSServPar                            (jsonRequ);
//    final SSLearnEpVersionSetTimelineStatePar  serverServPar    = new SSLearnEpVersionSetTimelineStatePar  (clientServPar);
//    final SSUri                                serverServResult =
//      SSServCaller.learnEpVersionSetTimelineState(
//        serverServPar.user,
//        serverServPar.learnEpVersion,
//        serverServPar.startTime,
//        serverServPar.endTime,
//        true);
//    
//    createJSONClientRetStr(SSLearnEpVersionSetTimelineStateRet.get(serverServResult, op));
//      
//    System.out.println (op + " test from client end");
  }
  
  @Override
  protected void setUp() throws Exception {
  }
}