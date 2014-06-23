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

import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.kc.tugraz.ss.serv.datatypes.learnep.serv.SSLearnEpServ;

public class SSLearnEpTester extends Thread{
  
  @Override
  public void run(){
    
    final SSLearnEpConf learnEpConf = (SSLearnEpConf) SSLearnEpServ.inst.servConf;
    
    if(!learnEpConf.executeOpAtStartUp){
      return;
    }
    
    switch(learnEpConf.op){
      case testServOverall:                new Thread(new SSLearnEpOverallTest                (learnEpConf)).start(); break;
      case learnEpCreate:                  new Thread(new SSLearnEpCreateTest                 (learnEpConf)).start(); break;
      case learnEpVersionCreate:           new Thread(new SSLearnEpVersionCreateTest          (learnEpConf)).start(); break;
      case learnEpVersionGet:              new Thread(new SSLearnEpVersionGetTest             (learnEpConf)).start(); break;
      case learnEpVersionAddCircle:        new Thread(new SSLearnEpVersionAddCircleTest       (learnEpConf)).start(); break;
      case learnEpVersionAddEntity:        new Thread(new SSLearnEpVersionAddEntityTest       (learnEpConf)).start(); break;
      case learnEpVersionsGet:             new Thread(new SSLearnEpVersionsGetTest            (learnEpConf)).start(); break;
      case learnEpsGet:                    new Thread(new SSLearnEpsGetTest                   (learnEpConf)).start(); break;
      case learnEpVersionCurrentGet:       new Thread(new SSLearnEpVersionCurrentGetTest      (learnEpConf)).start(); break;
      case learnEpVersionCurrentSet:       new Thread(new SSLearnEpVersionCurrentSetTest      (learnEpConf)).start(); break;
      case learnEpVersionSetTimelineState: new Thread(new SSLearnEpVersionSetTimelineStateTest(learnEpConf)).start(); break;
      case learnEpVersionGetTimelineState: new Thread(new SSLearnEpVersionGetTimelineStateTest(learnEpConf)).start(); break;
      case learnEpVersionRemoveCircle:     new Thread(new SSLearnEpVersionRemoveCircleTest    (learnEpConf)).start(); break;
      case learnEpVersionRemoveEntity:     new Thread(new SSLearnEpVersionRemoveEntityTest    (learnEpConf)).start(); break;
      case learnEpVersionUpdateCircle:     new Thread(new SSLearnEpVersionUpdateCircleTest    (learnEpConf)).start(); break;
      case learnEpVersionUpdateEntity:     new Thread(new SSLearnEpVersionUpdateEntityTest    (learnEpConf)).start(); break;
    }
  }
}