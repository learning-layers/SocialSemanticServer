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
package at.tugraz.sss.servs.jsonld.serv;

import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.servs.jsonld.impl.SSJSONLDImpl;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.servs.jsonld.api.*;
import at.tugraz.sss.servs.jsonld.conf.*;
import java.util.List;

public class SSJSONLD extends SSServContainerI{
  
  public static final SSJSONLD inst = new SSJSONLD(SSJSONLDClientI.class, SSJSONLDServerI.class);
  
  protected SSJSONLD(
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
      
      servImpl = new SSJSONLDImpl((SSJSONLDConf) conf);
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
  public void initServ() throws SSErr{
  }
  
  @Override
  public void schedule() throws SSErr{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}

// private static SSDbalConnA createVirtuosoConnector(
//          String username,
//          String password,
//          String host,
//          int port) throws SSErr {
//    instance = new SSDbalVirtuosoConnector(username, password, host, port);
//    return instance;
//  }

//      if (config.getProperty(SSConfig.PROP_TS_NAME).equals("anzo")) {
//        instance = createAnzoConnector(
//                config.getProperty(SSConfig.PROP_TS_USERNAME),
//                config.getProperty(SSConfig.PROP_TS_PASSWORD),
//                config.getProperty(SSConfig.PROP_TS_HOST),
//                Integer.valueOf(config.getProperty(SSConfig.PROP_TS_PORT)));
//
//      } else