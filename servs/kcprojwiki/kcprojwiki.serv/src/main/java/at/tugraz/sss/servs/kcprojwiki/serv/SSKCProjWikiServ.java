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
package at.tugraz.sss.servs.kcprojwiki.serv;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSCoreConfA;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServImplA;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.kcprojwiki.api.SSKCProjWikiClientI;
import at.tugraz.sss.servs.kcprojwiki.api.SSKCProjWikiServerI;
import at.tugraz.sss.servs.kcprojwiki.conf.SSKCProjWikiConf;
import at.tugraz.sss.servs.kcprojwiki.impl.SSKCProjWikiImpl;
import java.util.List;

public class SSKCProjWikiServ extends SSServContainerI{
  
  public static final SSKCProjWikiServ inst = new SSKCProjWikiServ(SSKCProjWikiClientI.class, SSKCProjWikiServerI.class);
  
  protected SSKCProjWikiServ(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread(){
    return new SSKCProjWikiImpl(conf);
  }
  
  @Override
  public SSServContainerI regServ() throws Exception{
    
    this.conf = SSCoreConf.instGet().getKcprojwiki();
    
    SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    final SSKCProjWikiConf projWikiConf = (SSKCProjWikiConf)conf;
    
    if(!projWikiConf.use){
      return;
    }
    
    if(!projWikiConf.initAtStartUp){
      return;
    }
  }
  
  @Override
  public void schedule() throws Exception{
    
    final SSKCProjWikiConf projWikiConf = (SSKCProjWikiConf)conf;
    
    if(
      !projWikiConf.use ||
      !projWikiConf.schedule){
      return;
    }
    
    if(
      SSObjU.isNull(projWikiConf.scheduleOps, projWikiConf.scheduleIntervals) ||
      projWikiConf.scheduleOps.isEmpty()                                        ||
      projWikiConf.scheduleIntervals.isEmpty()                                  ||
      projWikiConf.scheduleOps.size() != projWikiConf.scheduleIntervals.size()){
      
      SSLogU.warn("attempt to schedule with ops/intervals wrong");
      return;
    }
    
    if(projWikiConf.executeScheduleAtStartUp){
      
      for(SSServOpE scheduleOp : projWikiConf.scheduleOps){
        
        switch(scheduleOp){
          
          case kcprojwikiImport:{
            SSDateU.scheduleNow(new SSKCProjWikiImportTask());
            break;
          }
          
          default:{
            SSLogU.warn("attempt to schedule op at startup with no schedule task defined");
          }
        }
      }
    }
  }

  @Override
  public SSCoreConfA getConfForCloudDeployment(SSCoreConfA coreConfA, List<Class> configuredServs) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
