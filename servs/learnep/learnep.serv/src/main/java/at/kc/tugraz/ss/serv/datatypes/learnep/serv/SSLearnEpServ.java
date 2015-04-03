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
package at.kc.tugraz.ss.serv.datatypes.learnep.serv;

import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpClientI;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.SSLearnEpImpl;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.access.SSLearnEpRemainingTimeTask;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSCoreConfA;
import at.tugraz.sss.serv.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.serv.SSDBSQL;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServImplA;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SSLearnEpServ extends SSServContainerI{
  
  public static final SSLearnEpServ inst = new SSLearnEpServ(SSLearnEpClientI.class, SSLearnEpServerI.class);
  
  protected SSLearnEpServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSLearnEpImpl(conf, (SSDBSQLI)SSDBSQL.inst.serv());
  }

  @Override
  public SSServContainerI regServ(final SSConfA conf) throws Exception{
    
    this.conf = conf;
    
    SSServReg.inst.regServ(this);
    
    SSServReg.inst.regServForManagingEntities        (this);
    SSServReg.inst.regServForGatheringUsersResources (this);
    
    final Map<SSServOpE, Integer> maxRequestsForOps = new EnumMap<>(SSServOpE.class);
    
    maxRequestsForOps.put(SSServOpE.learnEpVersionGetTimelineState, 10);
    maxRequestsForOps.put(SSServOpE.learnEpVersionSetTimelineState, 10);
    
    SSServReg.inst.regClientRequestLimit(servImplClientInteraceClass, maxRequestsForOps);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    if(!conf.use){
      return;
    }
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA, 
    final List<Class> configuredServs) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public void schedule() throws Exception{
    
    if(conf.use){
      
      if(((SSLearnEpConf)conf).useEpisodeLocking){
        
        SSDateU.scheduleAtFixedRate(
          new SSLearnEpRemainingTimeTask(),
          SSDateU.getDateForNextHalfMinute(),
          SSDateU.minuteInMilliSeconds / 2);
      }
    }
  }
}