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
package at.kc.tugraz.ss.test.accessrights;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.job.accessrights.conf.SSAccessRightsConf;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.SSCircle;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import java.util.ArrayList;
import java.util.List;

public class SSAccessRightsUserUsersToCircleAddTest extends SSServOpTestCaseA{
  
  public SSAccessRightsUserUsersToCircleAddTest(SSAccessRightsConf accessRightsConf){
    super(accessRightsConf, SSMethU.accessRightsUserUsersToCircleAdd);
  }
  
  @Override
  protected void test() throws Exception {
    
    SSLogU.info("start " + op + "Test");
    
    final List<SSUri> userUris = new ArrayList<SSUri>();
    
    userUris.add   (SSUri.get("http://test.dt/user/dk/"));
    
    final List<SSCircle> userCircles = SSServCaller.accessRightsUserCirclesGet(userUri);
    final Boolean        result      = SSServCaller.accessRightsUserUsersToCircleAdd(userUri, userCircles.get(0).circleUri, userUris, true);
    
    SSLogU.info("end " + op + "Test");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }
  
  @Override
  protected void setUp() throws Exception {
    userUri = SSServCaller.logUserIn(SSLabelStr.get("dt"), true);
  }
}