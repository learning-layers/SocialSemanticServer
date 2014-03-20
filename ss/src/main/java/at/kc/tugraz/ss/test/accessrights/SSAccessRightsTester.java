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
package at.kc.tugraz.ss.test.accessrights;

import at.kc.tugraz.ss.serv.job.accessrights.conf.SSAccessRightsConf;
import at.kc.tugraz.ss.serv.job.accessrights.serv.SSAccessRightsServ;

public class SSAccessRightsTester extends Thread{
  
  @Override
  public void run(){
    
    final SSAccessRightsConf  accessRightsConf   = (SSAccessRightsConf)   SSAccessRightsServ.inst.servConf;
    
    if(!accessRightsConf.executeOpAtStartUp){
      return;
    }
    
    switch(accessRightsConf.op){
      case accessRightsUserCircleCreate:            new Thread(new SSAccessRightsUserCircleCreateTest          (accessRightsConf)).start();                break;
      case accessRightsUserEntitiesToCircleAdd:     new Thread(new SSAccessRightsUserEntitiesToCircleAddTest   (accessRightsConf)).start();                break;
      case accessRightsUserUsersToCircleAdd:        new Thread(new SSAccessRightsUserUsersToCircleAddTest      (accessRightsConf)).start();                break;
    }
  }
}