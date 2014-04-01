/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.db.serv;

import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.impl.SSDBGraphVirtuosoImpl;
import at.kc.tugraz.ss.serv.db.conf.SSDBGraphConf;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;

public class SSDBGraph extends SSServA{
  
  public static final SSServA inst = new SSDBGraph(null, SSDBGraphI.class);
  
    protected SSDBGraph(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSDBGraphVirtuosoImpl((SSDBGraphConf) servConf);
  }

  @Override
  protected void initServSpecificStuff() throws Exception{
  }
}

// private static SSDbalConnA createVirtuosoConnector(
//          String username,
//          String password,
//          String host,
//          int port) throws Exception {
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