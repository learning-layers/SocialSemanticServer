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
package at.kc.tugraz.ss.serv.auth.serv;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSCoreConfA;
import at.tugraz.sss.serv.SSLabel;
import at.kc.tugraz.ss.serv.auth.api.SSAuthClientI;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.ss.serv.auth.impl.SSAuthImpl;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthRegisterUserPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthUsersFromCSVFileAddPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServImplA;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServContainerI;

import java.util.List;

public class SSAuthServ extends SSServContainerI{
  
  public static final SSAuthServ inst = new SSAuthServ(SSAuthClientI.class, SSAuthServerI.class);

  protected SSAuthServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws SSErr{
    return new SSAuthImpl((SSAuthConf) conf);
  }
  
  @Override
  public SSServContainerI regServ() throws Exception{
    
    this.conf = SSCoreConf.instGet().getAuth();
    
    SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    if(!conf.use){
      return;
    }
    
    final SSAuthImpl authServ = (SSAuthImpl) inst.serv();
    
    authServ.authRegisterUser(
      new SSAuthRegisterUserPar(
        SSVocConf.systemUserEmail,
        ((SSAuthConf)conf).systemUserPassword,
        SSLabel.get(SSVocConf.systemUserLabel),
        true,
        true,
        false,
        true));
    
    if(((SSAuthConf)conf).initAtStartUp){
      
      switch(((SSAuthConf)conf).authType){
        case csvFileAuth:{
          authServ.authUsersFromCSVFileAdd(new SSAuthUsersFromCSVFileAddPar(SSVocConf.systemUserUri));
          break;
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

  @Override
  public void schedule() throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}