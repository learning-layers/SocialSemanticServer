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
 package at.kc.tugraz.ss.service.filerepo.service;

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSCoreConfA;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServA;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import at.kc.tugraz.ss.service.filerepo.impl.SSFilerepoImpl;
import at.tugraz.sss.serv.SSServImplA;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoClientI;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.kc.tugraz.ss.service.filerepo.datatypes.SSFileRepoFileAccessProperty;
import at.kc.tugraz.ss.service.filerepo.service.task.SSFileRepoWritingMinutesUpdateTask;
import at.tugraz.sss.serv.SSServContainerI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSFilerepoServ extends SSServContainerI{
  
  public  static final SSFilerepoServ                            inst            = new SSFilerepoServ(SSFileRepoClientI.class, SSFileRepoServerI.class);
  private static final Map<String, SSFileRepoFileAccessProperty> fileAccessProps = new HashMap<>();
  
  protected SSFilerepoServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSFilerepoImpl((SSFileRepoConf)conf, fileAccessProps);
  }
  
  @Override
  public void schedule() throws Exception{
    
    if(conf.use){
      
      SSDateU.scheduleAtFixedRate(
        new SSFileRepoWritingMinutesUpdateTask(),
        SSDateU.getDateForNextMinute(),
        SSDateU.minuteInMilliSeconds);
    }
  }
  
  @Override
  public SSServContainerI regServ(final SSConfA conf) throws Exception{
    
    super.regServ(conf); 
    
    SSServA.inst.regServ(this);
    
    SSServA.inst.regServForManagingEntities   (this);
    SSServA.inst.regServForDescribingEntities (this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA,
    final List<Class> configuredServs) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}