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
package at.kc.tugraz.ss.test.activity;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.activity.conf.SSActivityConf;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import java.util.ArrayList;
import java.util.List;

public class SSActivitiesUserGetTest extends SSServOpTestCaseA{
  
  public SSActivitiesUserGetTest(final SSActivityConf activityConf) {
    super(activityConf, null, SSMethU.activitiesUserGet);
  }
  
  @Override
  protected void test() throws Exception {
    
    System.out.println (op + " test start");
    
//    SSServCaller.activityAdd(
//      SSVoc.systemUserUri,
//      SSActivityE.addCategory,
//      new ArrayList<>(),
//      new ArrayList<>(),
//      new ArrayList<>(),
//      null,
//      true);
//    
//    SSServCaller.activityAdd(
//      SSVoc.systemUserUri,
//      SSActivityE.tagEntity,
//      new ArrayList<>(),
//      new ArrayList<>(),
//      new ArrayList<>(),
//      null,
//      true);
    
    final List<SSActivityE> types = new ArrayList<>();
    
    types.add(SSActivityE.addCategory);
      
    System.out.println(
      SSServCaller.activitiesUserGet(
        SSVoc.systemUserUri,
        types, //types
        new ArrayList<>(), //users,
        new ArrayList<>(), //entities,
        new ArrayList<>(), //circles
        null, //startTime
        null, //endTime
        false)); //includeOnlyLastActivities
    
    types.add(SSActivityE.tagEntity);
    
    System.out.println(
      SSServCaller.activitiesUserGet(
        SSVoc.systemUserUri,
        types, //types
        new ArrayList<>(), //users,
        new ArrayList<>(), //entities,
        new ArrayList<>(), //circles
        null, //startTime
        null, //endTime
        false)); //includeOnlyLastActivities
    
    System.out.println (op + " test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }
  
  @Override
  protected void setUp() throws Exception {
  }
}