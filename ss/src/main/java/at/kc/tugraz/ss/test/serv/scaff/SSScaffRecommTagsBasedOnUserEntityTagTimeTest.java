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
package at.kc.tugraz.ss.test.serv.scaff;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.scaff.conf.SSScaffConf;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;

public class SSScaffRecommTagsBasedOnUserEntityTagTimeTest extends SSServOpTestCaseA{
  
  public SSScaffRecommTagsBasedOnUserEntityTagTimeTest(SSScaffConf scaffConf){
    super(scaffConf, SSMethU.scaffRecommTagsBasedOnUserEntityTagTime);
  }
  
  @Override
  protected void test() throws Exception {
    
    SSLogU.info(op + " Test start");
    
    SSServCaller.scaffRecommTagsBasedOnUserEntityTagTime(
      SSUri.get("http://eval.bp/user/301497/"), 
      SSUri.get("http://eval.bp/user/301497/"),
      SSUri.get("http://en.wikipedia.org/wiki/Moby_Project/"), 
      10);
    
    SSLogU.info(op + " Test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }
  
  @Override
  protected void setUp() throws Exception {
    userUri = SSServCaller.logUserIn(SSLabel.get("dt"), true);
  }
}
