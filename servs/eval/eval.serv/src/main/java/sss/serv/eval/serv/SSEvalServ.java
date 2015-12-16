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
package sss.serv.eval.serv;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSCoreConfA;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServImplA;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;

import java.util.List;
import sss.serv.eval.api.SSEvalClientI;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.conf.SSEvalConf;
import sss.serv.eval.impl.SSEvalImpl;

public class SSEvalServ extends SSServContainerI{
  
  public static final SSEvalServ inst = new SSEvalServ(SSEvalClientI.class, SSEvalServerI.class);
  
  protected SSEvalServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws SSErr{
    return new SSEvalImpl(conf);
  }

  @Override
  public SSServContainerI regServ() throws Exception{
    
    this.conf = SSCoreConf.instGet().getEval();
    
    SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    if(!conf.use){
      return;
    }
    
    setMaxRequsForClientOps();
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA, 
    final List<Class> configuredServs) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private void setMaxRequsForClientOps() throws Exception{
    
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
  public void schedule() throws Exception{
    
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
      
      SSLogU.warn("attempt to schedule with ops/intervals wrong");
      return;
    }
    
    if(evalConf.executeScheduleAtStartUp){
      
      for(String scheduleOp : evalConf.scheduleOps){
        
        if(SSStrU.equals(scheduleOp, SSVarNames.evalAnalyze)){
          SSDateU.scheduleNow(new SSEvalAnalyzeTask());
          continue;
        }
        
        SSLogU.warn("attempt to schedule op at startup with no schedule task defined");
      }
    }
  }
}