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
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.servs.learnep.impl.*;
import java.util.List;

public class SSLearnEpServ extends SSServContainerI{
  
  public static final SSLearnEpServ inst = new SSLearnEpServ(SSLearnEpClientI.class, SSLearnEpServerI.class);
  
  protected SSLearnEpServ(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  public SSServImplA getServImpl() throws SSErr{
    
    if(!conf.use){
      throw SSErr.get(SSErrE.servNotRunning);
    }
    
    if(servImpl != null){
      return servImpl;
    }
    
    synchronized(this){
      
      servImpl = new SSLearnEpImpl(conf);
    }
    
    return servImpl;
  }
  
  @Override
  public SSServContainerI regServ(final SSConfA conf) throws SSErr{
    
    this.conf = conf;
    
    SSServReg.inst.regServ(this);
    
    SSServReg.inst.regServForHandlingDescribeEntity(this);
    SSServReg.inst.regServForHandlingCopyEntity(this);
    SSServReg.inst.regServForHandlingPushEntitiesToUsers(this);
    SSServReg.inst.regServForHandlingAddAffiliatedEntitiesToCircle(this);
    SSServReg.inst.regServForHandlingEntitiesSharedWithUsers(this);
    SSServReg.inst.regServForGatheringUsersResources (this);
    
    
//    final Map<SSServOpE, Integer> maxRequestsForOps = new EnumMap<>(SSVarNames.class);
//
//    maxRequestsForOps.put(SSVarNames.learnEpVersionTimelineStateGet, 10);
//    maxRequestsForOps.put(SSVarNames.learnEpVersionTimelineStateSet, 10);
//
//    SSServReg.inst.regClientRequestLimit(servImplClientInteraceClass, maxRequestsForOps);

return this;
  }
  
  @Override
  public void initServ() throws SSErr{
    
    if(!conf.use){
      return;
    }
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA,
    final List<Class> configuredServs) throws SSErr{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public void schedule() throws SSErr{
    
    if(!conf.use){
      return;
    }
    
    if(((SSLearnEpConf) conf).useEpisodeLocking){
      
      SSServReg.regScheduler(
        SSDateU.scheduleWithFixedDelay(
          new SSLearnEpRemainingTimeTask(),
          SSDateU.getDateForNextHalfMinute(),
          SSDateU.minuteInMilliSeconds / 2));
    }
    
    if(((SSLearnEpConf) conf).sendMailNotifications){
      
//      SSDateU.scheduleNow(new SSLearnEpMailNotificationTask((SSLearnEpConf) conf));
      
      SSDateU.scheduleWithFixedDelay(
        new SSLearnEpMailNotificationTask((SSLearnEpConf) conf),
        SSDateU.getDateForTomorrowMorning(),
        SSDateU.dayInMilliSeconds);
    }
  }
}