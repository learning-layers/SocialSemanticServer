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

import at.kc.tugraz.ss.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.serv.SSDBSQL;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import java.util.List;
import sss.serv.eval.api.SSEvalClientI;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.impl.SSEvalImpl;

public class SSEvalServ extends SSServA{
  
  public static final SSServA  inst = new SSEvalServ(SSEvalClientI.class, SSEvalServerI.class);
  
  protected SSEvalServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSEvalImpl(servConf, (SSDBSQLI)SSDBSQL.inst.serv());
  }

  @Override
  public SSServA regServ(final SSConfA conf) throws Exception{
    
    super.regServ(conf);
    
    regServForManagingEntities();
    regServForGatheringUsersResources ();
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    if(!servConf.use){
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
    
//    SSMethU op;
//      
//    for(Method method : servImplClientInteraceClass.getMethods()){
//      
//      op = SSMethU.get(method.getName());
//
//      switch(op){
//        case learnEpVersionGetTimelineState: maxRequsForClientOpsPerUser.put(op, 10);
//        case learnEpVersionSetTimelineState: maxRequsForClientOpsPerUser.put(op, 10);
//      }
//    }
  }

  @Override
  public void schedule() throws Exception{
  }
}