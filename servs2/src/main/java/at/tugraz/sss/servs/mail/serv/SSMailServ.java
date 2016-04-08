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
package at.tugraz.sss.servs.mail.serv;

import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.SSObjU;
import at.tugraz.sss.serv.util.SSStrU;
import at.tugraz.sss.serv.util.SSVarNames;
import at.tugraz.sss.servs.mail.api.SSMailClientI;
import at.tugraz.sss.servs.mail.api.SSMailServerI;
import at.tugraz.sss.servs.mail.conf.SSMailConf;
import at.tugraz.sss.servs.mail.impl.SSMailImpl;
import java.util.List;

public class SSMailServ extends SSServContainerI{
  
  public static final SSMailServ inst = new SSMailServ(SSMailClientI.class, SSMailServerI.class);
  
  protected SSMailServ(
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
      
      servImpl = new SSMailImpl(conf);
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
  public void schedule() throws SSErr{
    
    final SSMailConf mailConf = (SSMailConf)conf;
    
    if(
      !mailConf.use ||
      !mailConf.schedule){
      return;
    }
    
    if(
      SSObjU.isNull(mailConf.scheduleOps, mailConf.scheduleIntervals)   ||
      mailConf.scheduleOps.isEmpty()                                        ||
      mailConf.scheduleIntervals.isEmpty()                                  ||
      mailConf.scheduleOps.size() != mailConf.scheduleIntervals.size()){
      
      SSLogU.warn(SSWarnE.scheduleConfigInvalid, null);
      return;
    }
    
    if(mailConf.executeScheduleAtStartUp){
      
      for(String scheduleOp : mailConf.scheduleOps){
        
        if(SSStrU.isEqual(scheduleOp, SSVarNames.mailSend)){
          SSServReg.regScheduler(SSDateU.scheduleNow(new SSMailSendTask()));
        }
      }
    }
  }
  @Override
  public void initServ() throws SSErr{
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA, 
    final List<Class> configuredServs) throws SSErr{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}