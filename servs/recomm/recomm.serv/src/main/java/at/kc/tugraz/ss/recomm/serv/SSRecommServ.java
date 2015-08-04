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
package at.kc.tugraz.ss.recomm.serv;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSCoreConfA;
import at.kc.tugraz.ss.recomm.api.SSRecommClientI;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.impl.SSRecommImpl;
import at.kc.tugraz.ss.recomm.serv.task.SSRecommUpdateBulkTask;
import at.kc.tugraz.ss.recomm.serv.task.SSRecommUpdateBulkUserRealmsFromCirclesTask;
import at.kc.tugraz.ss.recomm.serv.task.SSRecommUpdateBulkUserRealmsFromConfTask;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSServImplA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServContainerI;
import java.util.List;

public class SSRecommServ extends SSServContainerI{
  
  public static final SSRecommServ inst = new SSRecommServ(SSRecommClientI.class, SSRecommServerI.class);
  
  protected SSRecommServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSRecommImpl(conf);
  }
  
    @Override
  public SSServContainerI regServ() throws Exception{
    
    this.conf = SSCoreConf.instGet().getRecomm();
    
      SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    final SSRecommConf recommConf = (SSRecommConf)conf;
    
    if(!recommConf.use){
      return;
    }
    
    SSServCaller.recommLoadUserRealms(SSVocConf.systemUserUri);
    
    if(!recommConf.initAtStartUp){
      return;
    }
    
    if(
      recommConf.initAtStartUpOps == null ||
      recommConf.initAtStartUpOps.isEmpty()){
      
      SSLogU.warn("attempt to init at startup | ops empty");
      return;
    }
    
    for(SSServOpE initAtStartUpOp : recommConf.initAtStartUpOps){
      
      switch(initAtStartUpOp){
        
        case recommUpdate:{
          SSDateU.scheduleNow(new SSRecommUpdateBulkTask                     (recommConf));
          SSDateU.scheduleNow(new SSRecommUpdateBulkUserRealmsFromConfTask   (recommConf));
          SSDateU.scheduleNow(new SSRecommUpdateBulkUserRealmsFromCirclesTask(recommConf));
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
    
    final SSRecommConf recommConf = (SSRecommConf)conf;
    
    if(
      !recommConf.use ||
      !recommConf.schedule){
      return;
    }
    
    if(
      SSObjU.isNull(recommConf.scheduleOps, recommConf.scheduleIntervals) ||
      recommConf.scheduleOps.isEmpty()                                    ||
      recommConf.scheduleIntervals.isEmpty()                              ||
      recommConf.scheduleOps.size() != recommConf.scheduleIntervals.size()){
      
      SSLogU.warn("attempt to schedule with ops/intervals wrong");
      return;
    }
    
    if(recommConf.executeScheduleAtStartUp){
      
      for(SSServOpE scheduleOp : recommConf.scheduleOps){
        
        switch(scheduleOp){
          
          case recommUpdate:{
            SSDateU.scheduleNow(new SSRecommUpdateBulkTask                     (recommConf));
            SSDateU.scheduleNow(new SSRecommUpdateBulkUserRealmsFromConfTask   (recommConf));
            SSDateU.scheduleNow(new SSRecommUpdateBulkUserRealmsFromCirclesTask(recommConf));
            break;
          }
          
          default:{
            SSLogU.warn("attempt to schedule op at startup with no schedule task defined");
          }
        }
      }
    }
    
    for(int counter = 0; counter < recommConf.scheduleOps.size(); counter++){
      
      switch(recommConf.scheduleOps.get(counter)){
        
        case recommUpdate:{
          
          SSDateU.scheduleAtFixedRate(new SSRecommUpdateBulkTask(recommConf),
            SSDateU.getDatePlusMinutes(recommConf.scheduleIntervals.get(counter)),
            recommConf.scheduleIntervals.get(counter) * SSDateU.minuteInMilliSeconds);
          
          SSDateU.scheduleAtFixedRate(new SSRecommUpdateBulkUserRealmsFromConfTask(recommConf),
            SSDateU.getDatePlusMinutes(recommConf.scheduleIntervals.get(counter)),
            recommConf.scheduleIntervals.get(counter) * SSDateU.minuteInMilliSeconds);
          
          SSDateU.scheduleAtFixedRate(new SSRecommUpdateBulkUserRealmsFromCirclesTask(recommConf),
            SSDateU.getDatePlusMinutes(recommConf.scheduleIntervals.get(counter)),
            recommConf.scheduleIntervals.get(counter) * SSDateU.minuteInMilliSeconds);
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
}
