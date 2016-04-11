/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.eval.serv;

import at.tugraz.sss.serv.conf.*;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import java.util.List;
import at.tugraz.sss.servs.eval.api.SSEvalClientI;
import at.tugraz.sss.servs.eval.api.SSEvalServerI;
import at.tugraz.sss.servs.eval.conf.*;
import at.tugraz.sss.servs.eval.impl.SSEvalImpl;

public class SSEvalServ extends SSServContainerI{
  
  public static final SSEvalServ inst = new SSEvalServ(SSEvalClientI.class, SSEvalServerI.class);
  
  protected SSEvalServ(
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
      
      servImpl = new SSEvalImpl(conf);
    }
    
    return servImpl;
  }

  @Override
  public SSServContainerI regServ(final SSConfA conf) throws SSErr{
    
    this.conf = conf;
    
    SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws SSErr{
    
    if(!conf.use){
      return;
    }
    
    setMaxRequsForClientOps();
  }
  
  private void setMaxRequsForClientOps() throws SSErr{
    
//    SSServOpE op;
//      
//    for(Method method : servImplClientInteraceClass.getMethods()){
//      
//      op = SSVarNames.get(method.getName());
//
//      switch(op){
//        case learnEpVersionGetTimelineState: maxRequsForClientOpsPerUser.put(op, 10);
//        case learnEpVersionSetTimelineState: maxRequsForClientOpsPerUser.put(op, 10);
//      }
//    }
  }

  @Override
  public void schedule() throws SSErr{
    
    final SSEvalConf evalConf = (SSEvalConf)conf;
    
    if(
      !evalConf.use ||
      !evalConf.schedule){
      return;
    }
    
    if(
      SSObjU.isNull(evalConf.scheduleOps, evalConf.scheduleIntervals) ||
      evalConf.scheduleOps.isEmpty()                                        ||
      evalConf.scheduleIntervals.isEmpty()                                  ||
      evalConf.scheduleOps.size() != evalConf.scheduleIntervals.size()){
      
      SSLogU.warn(SSWarnE.scheduleConfigInvalid, null);
      return;
    }
    
    if(evalConf.executeScheduleAtStartUp){
      
      for(String scheduleOp : evalConf.scheduleOps){
        
        if(SSStrU.isEqual(scheduleOp, SSVarNames.evalAnalyze)){
          
          new SSSchedules().regScheduler(
            SSDateU.scheduleNow(
              new SSEvalAnalyzeTask()));
        }
      }
    }
  }
}