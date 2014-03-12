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
package at.kc.tugraz.ss.test.serv.dataimport;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;

public class SSDataImportMediaWikiUserTest extends SSServOpTestCaseA{
  
  public SSDataImportMediaWikiUserTest(SSDataImportConf dataImportConf){
    super(dataImportConf, SSMethU.dataImportMediaWikiUser);
  }  
  
  @Override
  protected void test() throws Exception {
    
    SSLogU.info("start " + op + "Test");
    
    SSServCaller.dataImportMediaWikiUser(userUri, true);
    
    SSLogU.info("end " + op + "Test");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }

  @Override
  protected void setUp() throws Exception {
  }
}
