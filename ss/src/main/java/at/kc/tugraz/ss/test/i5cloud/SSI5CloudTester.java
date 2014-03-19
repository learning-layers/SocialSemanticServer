/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.test.i5cloud;

import at.kc.tugraz.ss.serv.job.i5cloud.conf.SSI5CloudConf;
import at.kc.tugraz.ss.serv.job.i5cloud.serv.SSI5CloudServ;

public class SSI5CloudTester extends Thread{
  
  @Override
  public void run(){
    
    final SSI5CloudConf  i5CloudConf   = (SSI5CloudConf)   SSI5CloudServ.inst.servConf;
    
    if(!i5CloudConf.executeOpAtStartUp){
      return;
    }
    
    switch(i5CloudConf.op){
      case i5CloudAuth:                new Thread(new SSI5CloudAuthTest(i5CloudConf)).start();                break;
    }
  }
}
