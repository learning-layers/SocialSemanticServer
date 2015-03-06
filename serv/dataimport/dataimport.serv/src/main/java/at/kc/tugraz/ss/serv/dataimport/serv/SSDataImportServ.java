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
package at.kc.tugraz.ss.serv.dataimport.serv;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.ss.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportClientI;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.serv.SSDBGraph;
import at.kc.tugraz.ss.serv.db.serv.SSDBSQL;
import at.kc.tugraz.ss.serv.dataimport.impl.SSDataImportImpl;
import at.kc.tugraz.ss.serv.dataimport.serv.task.SSDataImportEvernoteTask;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import java.lang.reflect.Method;
import java.util.List;

public class SSDataImportServ extends SSServA{
  
  public static final SSServA  inst = new SSDataImportServ(SSDataImportClientI.class, SSDataImportServerI.class);
  
  protected SSDataImportServ(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSDataImportImpl(servConf, (SSDBGraphI) SSDBGraph.inst.serv(), (SSDBSQLI) SSDBSQL.inst.serv());
  }
  
  @Override
  public void initServ() throws Exception{
    
    final SSDataImportConf dataImportConf = (SSDataImportConf)servConf;
    
    if(!dataImportConf.use){
      return;
    }
    
    setMaxRequsForClientOps();
    
    if(!dataImportConf.initAtStartUp){
      return;
    }
    
    if(
      dataImportConf.initAtStartUpOps == null ||
      dataImportConf.initAtStartUpOps.isEmpty()){
      
      SSLogU.warn("attempt to init at startup | ops empty");
      return;
    }
    
    for(SSMethU initAtStartUpOp : dataImportConf.initAtStartUpOps){
      
      switch(initAtStartUpOp){
        
        case dataImportEvernote:{
          SSDateU.scheduleNow(new SSDataImportEvernoteTask());
          break;
        }
        
        default:{
          SSLogU.warn("attempt to init op with no init task defined");
        }
      }
    }
  }
  
  @Override
  public void schedule() throws Exception{
    
    final SSDataImportConf dataImportConf = (SSDataImportConf)servConf;
    
    if(
      !dataImportConf.use ||
      !dataImportConf.schedule){
      return;
    }
    
    if(
      SSObjU.isNull(dataImportConf.scheduleOps, dataImportConf.scheduleIntervals) ||
      dataImportConf.scheduleOps.isEmpty()                                        ||
      dataImportConf.scheduleIntervals.isEmpty()                                  ||
      dataImportConf.scheduleOps.size() != dataImportConf.scheduleIntervals.size()){
      
      SSLogU.warn("attempt to schedule with ops/intervals wrong");
      return;
    }
    
    if(dataImportConf.executeScheduleAtStartUp){
      
      for(SSMethU scheduleOp : dataImportConf.scheduleOps){
        
        switch(scheduleOp){
          
          case dataImportEvernote:{
            SSDateU.scheduleNow(new SSDataImportEvernoteTask());
            break;
          }
          
          default:{
            SSLogU.warn("attempt to schedule op at startup with no schedule task defined");
          }
        }
      }
    }
    
    for(int counter = 0; counter < dataImportConf.scheduleOps.size(); counter++){
      
      switch(dataImportConf.scheduleOps.get(counter)){
        
        case dataImportEvernote:{
          
          SSDateU.scheduleAtFixedRate(
            new SSDataImportEvernoteTask(),
            SSDateU.getDatePlusMinutes(dataImportConf.scheduleIntervals.get(counter)),
            dataImportConf.scheduleIntervals.get(counter) * SSDateU.minuteInMilliSeconds);
          break;
        }
        
        default:{
          SSLogU.warn("attempt to schedule op with no schedule task defined");
        }
      }
    }
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA,
    final List<Class> configuredServs) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private void setMaxRequsForClientOps() throws Exception{
    
    SSMethU op;
    
    for(Method method : servImplClientInteraceClass.getMethods()){
      
      op = SSMethU.get(method.getName());
      
      switch(op){
        case dataImportEvernote: maxRequsForClientOpsPerUser.put(op, 1);
      }
    }
  }
}