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
package at.kc.tugraz.ss.test.serv.entity;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.serv.datatypes.entity.conf.SSEntityConf;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;

public class SSEntityUserDirectlyAdjoinedEntitiesRemoveTest extends SSServOpTestCaseA{
  
  public SSEntityUserDirectlyAdjoinedEntitiesRemoveTest(final SSEntityConf entityConf){
    super(entityConf, SSMethU.entityUserDirectlyAdjoinedEntitiesRemove);
  }  
  
  @Override
  protected void test() throws Exception {
    
    SSLogU.info("start " + op + " Test");
    
    SSUri entityUri = SSServCaller.entityUserDirectlyAdjoinedEntitiesRemove(
      userUri, 
      SSUri.get("https://www.google.at/"), 
      true, 
      true, 
      true, 
      true, 
      true);
    
    SSLogU.info("end " + op + " Test");
  }

  @Override
  protected void testFromClient() throws Exception{

//    jsonRequ                                   = 
//      "{\"op\":\"entityDescGet\",\"user\":\"http://m.domi/user/domi\",\"entityUri\":\"http://bbb.com\",\"getTags\":true,\"getOverallRating\":true,\"getDiscUris\":true,\"key\":\"681V454J1P3H4W3B367BB79615U184N22356I3E\"}";
//    clientServPar                              = new SSServPar          (jsonRequ);
//    final SSEntityDescGetPar  serverServPar    = new SSEntityDescGetPar (clientServPar);
//    final SSEntityDescA       serverServResult = 
//      SSServCaller.entityDescGet(
//      serverServPar.user, 
//      serverServPar.entityUri,
//      true, 
//      true, 
//      true);
//    
//    createJSONClientRetStr(SSEntityDescGetRet.get(serverServResult, op));
//      
//    System.out.println (op + " test from client end");
  }
  
  @Override
  protected void setUp() throws Exception {
    userUri = SSServCaller.userLogin(SSLabel.get("dt"), true);
  }
}
