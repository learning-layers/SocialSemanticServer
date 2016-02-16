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

import at.tugraz.sss.conf.SSCoreConf;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.recomm.api.SSRecommClientI;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommLoadUserRealmsPar;
import at.kc.tugraz.ss.recomm.impl.SSRecommImpl;
import at.kc.tugraz.ss.recomm.serv.task.SSRecommUpdateBulkTask;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import java.sql.*;
import java.util.List;

public class SSRecommServ extends SSServContainerI{
  
  public static final SSRecommServ inst = new SSRecommServ(SSRecommClientI.class, SSRecommServerI.class);
  
  protected SSRecommServ(
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
      
      servImpl = new SSRecommImpl(conf);
    }
    
    return servImpl;
  }  
  
  @Override
  public SSServContainerI regServ() throws SSErr{
    
    this.conf = SSCoreConf.instGet().getRecomm();
    
    SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws SSErr{
    
    final SSRecommConf recommConf = (SSRecommConf)conf;
    
    if(!recommConf.use){
      return;
    }
    
    Connection sqlCon = null;
    
    try{
      
      final SSServPar servPar = new SSServPar(null);
      
      sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      
      servPar.sqlCon = sqlCon;
      
      ((SSRecommServerI) getServImpl()).recommLoadUserRealms(
        new SSRecommLoadUserRealmsPar(
          servPar,
          SSConf.systemUserUri));
    }finally{
      
      if(sqlCon != null){
        
        try{
          sqlCon.close();
        }catch (SQLException error) {
          SSLogU.err(error);
        }
      }
    }
  }
  
  @Override
  public void schedule() throws SSErr{
    
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
      
      SSLogU.warn(SSWarnE.scheduleConfigInvalid, null);
      return;
    }
    
    java.util.Date startDate;
    
    for(int counter = 0; counter < recommConf.scheduleOps.size(); counter++){
      
      if(SSStrU.equals(recommConf.scheduleOps.get(counter), SSVarNames.recommUpdate)){
        
        if(recommConf.executeScheduleAtStartUp){
          startDate = new java.util.Date();
        }else{
          startDate = SSDateU.getDatePlusMinutes(recommConf.scheduleIntervals.get(counter));
        }
        
        SSServReg.regScheduler(
          SSDateU.scheduleWithFixedDelay(
            new SSRecommUpdateBulkTask(recommConf),
            startDate,
            recommConf.scheduleIntervals.get(counter) * SSDateU.minuteInMilliSeconds));
        
//        SSServReg.regScheduler(
//          SSDateU.scheduleWithFixedDelay(
//            new SSRecommUpdateBulkUserRealmsFromConfTask(),
//            startDate,
//            recommConf.scheduleIntervals.get(counter) * SSDateU.minuteInMilliSeconds));
//        
//        SSServReg.regScheduler(
//          SSDateU.scheduleWithFixedDelay(
//            new SSRecommUpdateBulkUserRealmsFromCirclesTask(),
//            SSDateU.getDatePlusMinutes(recommConf.scheduleIntervals.get(counter)),
//            recommConf.scheduleIntervals.get(counter) * SSDateU.minuteInMilliSeconds));
      }
    }
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA,
    final List<Class> configuredServs) throws SSErr{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
