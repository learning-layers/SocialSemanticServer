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
package at.kc.tugraz.ss.test.serv.learnep;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;

public class SSLearnEpVersionAddEntityTest extends SSServOpTestCaseA{
  
  public SSLearnEpVersionAddEntityTest(final SSLearnEpConf learnEpConf) {
    super(learnEpConf, SSMethU.learnEpVersionAddEntity);
  }
  
  @Override
  protected void test() throws Exception {
    
    System.out.println (op + " test start");
    
    final SSUri learnEp = 
      SSServCaller.learnEpCreate(
        SSVoc.systemUserUri, 
        SSLabel.get("my test learn ep"), 
        SSTextComment.get("super"),
        true);
    
    final SSUri learnEpVersion = 
      SSServCaller.learnEpVersionCreate(
        SSVoc.systemUserUri,
        learnEp,
        true);
    
    final SSUri learnEpEntity = 
      SSServCaller.learnEpVersionAddEntity(
        SSVoc.systemUserUri,
        learnEpVersion,
        SSUri.get("http://www.google.com"), 
        1F, 
        1F,
        true);
    
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