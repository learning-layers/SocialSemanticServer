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
package at.kc.tugraz.ss.serv.voc.serv;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSCoreConfA;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.serv.voc.impl.SSVocImpl;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServImplA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.api.SSVocI;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServContainerI;
import java.util.List;

public class SSVoc extends SSServContainerI{
  
  public static final SSVoc inst                           = new SSVoc(null, SSVocI.class);

  protected SSVoc(
    final Class servImplClientInteraceClass, 
    final Class servServerI){
    
    super(servImplClientInteraceClass, servServerI);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws SSErr{
    return new SSVocImpl((SSVocConf) conf);
  }
  
  @Override
  public SSServContainerI regServ() throws Exception{

    this.conf = SSCoreConf.instGet().getVoc();
    
    SSServReg.inst.regServ(this);
    
    SSVocConf.systemUserUri  = SSServCaller.vocURICreateFromId(SSVocConf.systemUserLabel);
    
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

  @Override
  public void schedule() throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}