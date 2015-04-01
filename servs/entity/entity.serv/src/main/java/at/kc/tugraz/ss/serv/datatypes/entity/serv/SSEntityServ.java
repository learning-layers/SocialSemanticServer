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
package at.kc.tugraz.ss.serv.datatypes.entity.serv;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.SSEntityImpl;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSCoreConfA;
import at.tugraz.sss.serv.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.serv.SSDBSQL;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServA;
import at.tugraz.sss.serv.SSServImplA;
import at.tugraz.sss.serv.caller.SSServCaller;
import java.lang.reflect.Method;
import java.util.List;

public class SSEntityServ extends SSServA{
  
  public static final SSServA  inst = new SSEntityServ(SSEntityClientI.class, SSEntityServerI.class);
  
  protected SSEntityServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSEntityImpl(servConf, (SSDBSQLI)SSDBSQL.inst.serv());
  }
  
  @Override
  public SSServA regServ(final SSConfA conf) throws Exception{
    
    super.regServ(conf);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    if(!servConf.use){
      return;
    }
    
    SSServCaller.circlePubURIGet(true);    
    
    regServForGatheringUserRelations();
    regServForGatheringUsersResources();
    
    setMaxRequsForClientOps();
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA,
    final List<Class> configuredServs) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private void setMaxRequsForClientOps() throws Exception{
    
    SSServOpE op;
    
    for(Method method : servImplClientInteraceClass.getMethods()){
      
      op = SSServOpE.get(method.getName());
      
      switch(op){
        case entityDescsGet: maxRequsForClientOpsPerUser.put(op, 15);
        case entityDescGet:  maxRequsForClientOpsPerUser.put(op, 20);
      }
    }
  }

  @Override
  public void schedule() throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}