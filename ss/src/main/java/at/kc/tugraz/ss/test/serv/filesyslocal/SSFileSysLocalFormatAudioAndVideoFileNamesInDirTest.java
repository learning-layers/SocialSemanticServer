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
package at.kc.tugraz.ss.test.serv.filesyslocal;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.job.file.sys.local.conf.SSFileSysLocalConf;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;

public class SSFileSysLocalFormatAudioAndVideoFileNamesInDirTest extends SSServOpTestCaseA{

  public SSFileSysLocalFormatAudioAndVideoFileNamesInDirTest(SSFileSysLocalConf conf) throws Exception{
    super(conf, SSMethU.fileSysLocalFormatAudioAndVideoFileNamesInDir);
  }
  
  @Override
  protected void test() throws Exception{
    SSServCaller.formatAudioAndVideoFileNamesInDir();
  }

  @Override
  protected void testFromClient() throws Exception{
  }
  
  @Override
  protected void setUp() throws Exception {
    userUri = SSServCaller.logUserIn(SSLabelStr.get("dt"), true);
  }
}
