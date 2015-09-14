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

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSCoreConfA;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportClientI;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.dataimport.impl.SSDataImportImpl;
import at.kc.tugraz.ss.serv.dataimport.serv.task.SSDataImportBitsAndPiecesTask;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServImplA;
import java.util.List;

public class SSDataImportServ extends SSServContainerI{
  
  public static final SSDataImportServ inst = new SSDataImportServ(SSDataImportClientI.class, SSDataImportServerI.class);
  
  protected SSDataImportServ(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSDataImportImpl(conf);
  }
  
  @Override
  public SSServContainerI regServ() throws Exception{
    
    this.conf = SSCoreConf.instGet().getDataImport();
    
    SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    final SSDataImportConf dataImportConf = (SSDataImportConf)conf;
    
    if(!dataImportConf.use){
      return;
    }
    
    if(!dataImportConf.initAtStartUp){
      return;
    }
  }
  
  @Override
  public void schedule() throws Exception{
    
    final SSDataImportConf dataImportConf = (SSDataImportConf)conf;
    
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
      
      for(SSServOpE scheduleOp : dataImportConf.scheduleOps){
        
        switch(scheduleOp){
          
          case dataImportBitsAndPieces:{
            SSDateU.scheduleNow(new SSDataImportBitsAndPiecesTask());
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
        
        case dataImportBitsAndPieces:{
          
          SSDateU.scheduleAtFixedRate(new SSDataImportBitsAndPiecesTask(),
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
}