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
package at.kc.tugraz.ss.main;

import at.kc.tugraz.ss.test.activity.SSActivityTester;
import at.kc.tugraz.ss.test.app.SSAppTester;
import at.kc.tugraz.ss.test.category.SSCategoryTester;
import at.kc.tugraz.ss.test.disc.SSDiscTester;
import at.kc.tugraz.ss.test.i5cloud.SSI5CloudTester;
import at.kc.tugraz.ss.test.like.SSLikeTester;
import at.kc.tugraz.ss.test.search.SSSearchTester;
import at.kc.tugraz.ss.test.serv.dataexport.SSDataExportTester;
import at.kc.tugraz.ss.test.serv.dataimport.SSDataImportTester;
import at.kc.tugraz.ss.test.serv.entity.SSEntityTester;
import at.kc.tugraz.ss.test.serv.flag.SSFlagTester;
import at.kc.tugraz.ss.test.serv.learnep.SSLearnEpTester;
import at.kc.tugraz.ss.test.recomm.SSRecommTester;
import at.kc.tugraz.ss.test.tag.SSTagTester;

public class SSTester extends Thread{
  
  @Override
  public void run(){
    
      new SSDataImportTester().start();
      new SSEntityTester().start();
      new SSI5CloudTester().start();
      new SSLearnEpTester().start();
      new SSDiscTester().start();
      new SSRecommTester().start();
      new SSFlagTester().start();
      new SSDataExportTester().start();
      new SSTagTester().start();
      new SSCategoryTester().start();
      new SSSearchTester().start();
      new SSAppTester().start();
      new SSActivityTester().start();
      new SSLikeTester().start();
//    new Thread(new SSRecommTester()).start();
//    SSFileSysLocalTester.run();

//    SSEvernoteTester.run();
//    SSCollTester.run();
//    SSLoadTester.run(false);
  }
}