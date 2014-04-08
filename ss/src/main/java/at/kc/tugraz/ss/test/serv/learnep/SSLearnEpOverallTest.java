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

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOverallTestCaseA;
import java.util.List;

public class SSLearnEpOverallTest extends SSServOverallTestCaseA{
  
  public SSLearnEpOverallTest(SSLearnEpConf conf) throws Exception {
    super(conf);
  }
  
  @Override
  public void test() throws Exception{
    
    SSUri learnEpUri = 
      SSServCaller.createLearnEp(
      userUri, 
      SSLabelStr.get("learn ep dieter 1"), 
      SSSpaceEnum.privateSpace, 
      true);
    
    SSUri learnEpVersionUri = 
      SSServCaller.createLearnEpVersion(
      userUri, 
      learnEpUri, 
      true);
    
    SSUri learnEpCircleUri =
      SSServCaller.addLearnEpVersionCircle(
      userUri,
      learnEpVersionUri,
      SSLabelStr.get("learn ep circle label"),
      Float.parseFloat("98"),
      Float.parseFloat("99"),
      Float.parseFloat("101"),
      Float.parseFloat("102"),
      Float.parseFloat("103"),
      Float.parseFloat("104"),
      true);

    SSUri learnEpEntityUri = 
      SSServCaller.addLearnEpVersionEntity(
      userUri, 
      learnEpVersionUri, 
      SSUri.get("http://google.com/"),
      Float.parseFloat("10"),
      Float.parseFloat("11"),
      true);
    
    SSServCaller.setLearnEpCurrentVersion(
      userUri, 
      learnEpVersionUri, 
      true);
    
    SSUri learnEpVersionTimelineStateUri = 
      SSServCaller.setLearnEpVersionTimelineState(
      userUri, 
      learnEpVersionUri, 
      SSDateU.dateAsLong(), 
      SSDateU.dateAsLong(), 
      true);

    SSLearnEpVersion currentLearnEpVersion = 
      SSServCaller.getCurrentLearnEpVersion (userUri);
    
    SSLearnEpVersion learnEpVersion = 
      SSServCaller.getLearnEpVersion (userUri, learnEpVersionUri);
    
    SSServCaller.updateLearnEpVersionCircle(
      userUri, 
      learnEpCircleUri, 
      SSLabelStr.get("new Label"), 
      Float.parseFloat("1004"), 
      Float.parseFloat("21"), 
      Float.parseFloat("104"), 
      Float.parseFloat("1"),
      Float.parseFloat("52"),
      Float.parseFloat("89"), 
      true);
    
    SSServCaller.updateLearnEpVersionEntity(
      userUri, 
      learnEpEntityUri, 
      SSUri.get("http://metallica.com/"), 
      Float.parseFloat("44"),
      Float.parseFloat("44"),
      true);
    
    SSServCaller.removeLearnEpVersionCircle(
      userUri,
      learnEpCircleUri,
      true);
    
    SSServCaller.removeLearnEpVersionEntity(
      userUri, 
      learnEpEntityUri, 
      true);
    
    List<SSLearnEp>        learnEps        =
      SSServCaller.getLearnEps(userUri);
    
    List<SSLearnEpVersion> learnEpVersions = 
      SSServCaller.getLearnEpVersions(
      userUri, 
      learnEpUri);
  }
}
